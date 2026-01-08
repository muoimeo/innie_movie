package com.example.myapplication.data.local.db

import android.content.Context
import com.example.myapplication.data.local.entities.Comment
import com.example.myapplication.data.local.entities.Notification
import com.example.myapplication.data.local.entities.Review
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.local.entities.UserMovieStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Seeds the database with sample data for development/demo purposes.
 */
object DatabaseSeeder {
    
    private var isSeeded = false
    
    /**
     * Hash password for seed data - must match AuthRepository.hashPassword logic
     */
    private fun hashPasswordForSeed(password: String, salt: String): String {
        val saltedPassword = salt + password
        val bytes = saltedPassword.toByteArray()
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    
    /**
     * Call this once after database is created to populate sample data.
     */
    fun seedIfNeeded(context: Context, scope: CoroutineScope) {
        if (isSeeded) return
        
        scope.launch(Dispatchers.IO) {
            try {
                val database = DatabaseProvider.getDatabase(context)
                val userDao = database.userDao()
                val reviewDao = database.reviewDao()
                val commentDao = database.commentDao()
                val movieDao = database.movieDao()
                val userMovieStatsDao = database.userMovieStatsDao()
                val notificationDao = database.notificationDao()
                
                // Check if users already exist
                val existingUsers = userDao.getUserById("user_marquee")
                
                // Seed sample users first (if not exists)
                if (existingUsers == null) {
                    seedSampleUsers(userDao)
                    
                    // Seed social relationships after users created
                    seedSocialRelationships(database.socialDao())
                }
                
                // Check if reviews already exist
                val existingReviews = reviewDao.getRecentReviews(1).first()
                
                // Check if movies exist (FK constraint requires movies first)
                // Check if movies exist (FK constraint requires movies first)
                val existingMovies = movieDao.getAllMovies().first()
                
                // Seed movies if missing (e.g. after destructive migration)
                if (existingMovies.isEmpty()) {
                    com.example.myapplication.data.allMedia.forEach { movie ->
                        movieDao.insertMovie(movie)
                    }
                }
                
                // Check reviews again or reuse flow? Better to check count or just proceed
                // If we just seeded movies, we can assume they exist now.
                
                // Only seed reviews/stats if reviews are empty
                if (existingReviews.isEmpty()) {
                    // Get valid movie IDs from database (now populated)
                    val moviesNow = movieDao.getAllMovies().first()
                    val validMovieIds = moviesNow.map { it.id }.toSet()
                    
                    if (validMovieIds.isNotEmpty()) {
                        seedSampleReviews(reviewDao, validMovieIds)
                        seedSampleComments(commentDao)
                        
                        // Seed user movie stats (favorites, watched)
                        seedUserMovieStats(userMovieStatsDao, validMovieIds)
                        
                        // Seed likes per user (for movie likes/favorites display)
                        seedUserLikes(database.likeDao(), validMovieIds)
                        
                        // Seed albums per user (for album count display)
                        seedUserAlbums(database.albumDao())
                        
                        // Seed massive synthetic data for stats (tens/hundreds)
                        seedSyntheticData(
                            database, 
                            validMovieIds, 
                            listOf("user_marquee", "user_paul", "user_nolan_fan", "user_evelyn", "user_miles", "user_vengeance", "user_scientist", "user_cooper", "user_bruce", "user_bong", "user_ken", "user_maverick", "user_cobb", "user_arthur", "user_chani", "user_demo")
                        )
                        
                        // Seed demo-specific data (6 friends, 15 following, 12 followers, 8 reviews, 25+ watched)
                        seedDemoUserData(database, validMovieIds)
                        
                        // Seed sample notifications for demo user
                        seedSampleNotifications(notificationDao)
                    }
                }
                
                isSeeded = true
            } catch (e: Exception) {
                // Silently fail - seeding is not critical
                e.printStackTrace()
                isSeeded = true
            }
        }
    }
    
    /**
     * Seed sample users with unique names
     */
    private suspend fun seedSampleUsers(userDao: com.example.myapplication.data.local.dao.UserDao) {
        val sampleUsers = listOf(
            User(
                userId = "user_marquee",
                username = "@marquee",
                displayName = "Marquee",
                email = "marquee@innie.app",
                passwordHash = "hashed",
                bio = "Film enthusiast and critic. Love discovering hidden gems and discussing cinema theories.",
                avatarUrl = "https://i.pravatar.cc/150?u=marquee",
                coverUrl = "https://picsum.photos/seed/marquee/800/300"
            ),
            User(
                userId = "user_paul",
                username = "@muaddib",
                displayName = "Paul Atreides",
                email = "paul@innie.app",
                passwordHash = "hashed",
                bio = "Desert wanderer, spice lover. The sleeper has awakened.",
                avatarUrl = "https://i.pravatar.cc/150?u=paul",
                coverUrl = "https://picsum.photos/seed/dune/800/300"
            ),
            User(
                userId = "user_miles",
                username = "@spidey2099",
                displayName = "Miles Morales",
                email = "miles@innie.app",
                passwordHash = "hashed",
                bio = "Your friendly neighborhood Spider-Man. Brooklyn born and raised.",
                avatarUrl = "https://i.pravatar.cc/150?u=miles",
                coverUrl = "https://picsum.photos/seed/spiderverse/800/300"
            ),
            User(
                userId = "user_evelyn",
                username = "@evelyn",
                displayName = "Evelyn Wang",
                email = "evelyn@innie.app",
                passwordHash = "hashed",
                bio = "Laundromat owner, multiverse explorer. Everything everywhere all at once.",
                avatarUrl = "https://i.pravatar.cc/150?u=evelyn",
                coverUrl = "https://picsum.photos/seed/multiverse/800/300"
            ),
            User(
                userId = "user_nolan_fan",
                username = "@nolan_fan",
                displayName = "Christopher Fan",
                email = "nolan@innie.app",
                passwordHash = "hashed",
                bio = "IMAX enthusiast, Nolan devotee. Time is just a construct.",
                avatarUrl = "https://i.pravatar.cc/150?u=nolan",
                coverUrl = "https://picsum.photos/seed/inception/800/300"
            ),
            User(
                userId = "user_chani",
                username = "@chani",
                displayName = "Chani",
                email = "chani@innie.app",
                passwordHash = "hashed",
                bio = "Fremen warrior. Sand and stars. I ride the worms.",
                avatarUrl = "https://i.pravatar.cc/150?u=chani_fremen",
                coverUrl = "https://picsum.photos/seed/fremendesert/800/300"
            ),
            // === Additional users for reviews ===
            User(
                userId = "user_vengeance",
                username = "@vengeance",
                displayName = "Vengeance",
                email = "vengeance@innie.app",
                passwordHash = "hashed",
                bio = "I am vengeance. I am the night.",
                avatarUrl = "https://i.pravatar.cc/150?u=batman_vengeance",
                coverUrl = "https://picsum.photos/seed/gothamnight/800/300"
            ),
            User(
                userId = "user_scientist",
                username = "@scientist",
                displayName = "Dr. Scientist",
                email = "scientist@innie.app",
                passwordHash = "hashed",
                bio = "Physics enthusiast. Now I am become Death, the destroyer of worlds.",
                avatarUrl = "https://i.pravatar.cc/150?u=scientist_atom",
                coverUrl = "https://picsum.photos/seed/atomicera/800/300"
            ),
            User(
                userId = "user_cooper",
                username = "@cooper",
                displayName = "Cooper",
                email = "cooper@innie.app",
                passwordHash = "hashed",
                bio = "Pilot and farmer. We're explorers, Murph.",
                avatarUrl = "https://i.pravatar.cc/150?u=cooper_space",
                coverUrl = "https://picsum.photos/seed/wormhole/800/300"
            ),
            User(
                userId = "user_bruce",
                username = "@brucew",
                displayName = "Bruce W.",
                email = "bruce@innie.app",
                passwordHash = "hashed",
                bio = "Billionaire philanthropist. I work alone. Mostly.",
                avatarUrl = "https://i.pravatar.cc/150?u=bruce_wayne",
                coverUrl = "https://picsum.photos/seed/gotham/800/300"
            ),
            User(
                userId = "user_bong",
                username = "@bong",
                displayName = "Bong Joon-ho",
                email = "bong@innie.app",
                passwordHash = "hashed",
                bio = "Once you overcome the subtitles barrier, you will be introduced to so many more amazing films.",
                avatarUrl = "https://i.pravatar.cc/150?u=bong_director",
                coverUrl = "https://picsum.photos/seed/parasite/800/300"
            ),
            User(
                userId = "user_ken",
                username = "@ken",
                displayName = "Ken",
                email = "ken@innie.app",
                passwordHash = "hashed",
                bio = "Beach is my job. I'm just Ken.",
                avatarUrl = "https://i.pravatar.cc/150?u=ken_beach",
                coverUrl = "https://picsum.photos/seed/barbieland/800/300"
            ),
            User(
                userId = "user_maverick",
                username = "@maverick",
                displayName = "Maverick",
                email = "maverick@innie.app",
                passwordHash = "hashed",
                bio = "I feel the need... the need for speed.",
                avatarUrl = "https://i.pravatar.cc/150?u=maverick_pilot",
                coverUrl = "https://picsum.photos/seed/topgun/800/300"
            ),
            User(
                userId = "user_cobb",
                username = "@cobb",
                displayName = "Dom Cobb",
                email = "cobb@innie.app",
                passwordHash = "hashed",
                bio = "An idea is like a virus, resilient, highly contagious.",
                avatarUrl = "https://i.pravatar.cc/150?u=cobb_dream",
                coverUrl = "https://picsum.photos/seed/dreamscape/800/300"
            ),
            User(
                userId = "user_arthur",
                username = "@arthur",
                displayName = "Arthur Fleck",
                email = "arthur@innie.app",
                passwordHash = "hashed",
                bio = "I used to think my life was a tragedy, but now I realize it's a comedy.",
                avatarUrl = "https://i.pravatar.cc/150?u=arthur_joker",
                coverUrl = "https://picsum.photos/seed/joker/800/300"
            ),
            // === DEMO ACCOUNT for testing ===
            // Password: 11111111 | Salt: demo_salt_12345
            // Hash = SHA-256("demo_salt_12345" + "11111111") = 5a5b0f9a8c3d7e2f1a4b6c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f
            User(
                userId = "user_demo",
                username = "@demo",
                displayName = "Demo User",
                email = "demo@innie.app",
                passwordHash = hashPasswordForSeed("11111111", "demo_salt_12345"),
                salt = "demo_salt_12345",
                bio = "This is a demo account for testing the app. Feel free to explore!",
                avatarUrl = "https://image.tmdb.org/t/p/original/dMSAkUFvv7CgALBitTNNNM7ItfT.jpg",
                coverUrl = "https://image.tmdb.org/t/p/original/pbrkL804c8yAv3zBZR4QPEafpAR.jpg"
            ),
            User(
                userId = "guest_user",
                username = "@guest",
                displayName = "Guest User",
                email = "guest@innie.app",
                passwordHash = "hashed",
                bio = "Just browsing!",
                avatarUrl = null,
                coverUrl = null
            )
        )
        
        sampleUsers.forEach { user ->
            userDao.insertUser(user)
        }
    }
    
    /**
     * Seed user movie stats - favorites and watched per user
     */
    private suspend fun seedUserMovieStats(
        dao: com.example.myapplication.data.local.dao.UserMovieStatsDao,
        validMovieIds: Set<Int>
    ) {
        // user_marquee likes: Batman, Dark Knight, Joker
        if (1 in validMovieIds) dao.upsert(UserMovieStats("user_marquee", 1, isWatched = true, isFavorite = true, rating = 4.5f))
        if (5 in validMovieIds) dao.upsert(UserMovieStats("user_marquee", 5, isWatched = true, isFavorite = true, rating = 5.0f))
        if (12 in validMovieIds) dao.upsert(UserMovieStats("user_marquee", 12, isWatched = true, isFavorite = true, rating = 4.5f))
        
        // user_paul likes: Dune, Interstellar, Oppenheimer
        if (2 in validMovieIds) dao.upsert(UserMovieStats("user_paul", 2, isWatched = true, isFavorite = true, rating = 5.0f))
        if (4 in validMovieIds) dao.upsert(UserMovieStats("user_paul", 4, isWatched = true, isFavorite = true, rating = 5.0f))
        if (3 in validMovieIds) dao.upsert(UserMovieStats("user_paul", 3, isWatched = true, isFavorite = true, rating = 4.5f))
        
        // user_miles likes: Spider-Verse, Everything Everywhere
        if (7 in validMovieIds) dao.upsert(UserMovieStats("user_miles", 7, isWatched = true, isFavorite = true, rating = 5.0f))
        if (8 in validMovieIds) dao.upsert(UserMovieStats("user_miles", 8, isWatched = true, isFavorite = true, rating = 5.0f))
        
        // user_evelyn likes: Everything Everywhere, Parasite
        if (8 in validMovieIds) dao.upsert(UserMovieStats("user_evelyn", 8, isWatched = true, isFavorite = true, rating = 5.0f))
        if (6 in validMovieIds) dao.upsert(UserMovieStats("user_evelyn", 6, isWatched = true, isFavorite = true, rating = 5.0f))
        
        // user_nolan_fan likes: Oppenheimer, Interstellar, Inception, Dark Knight
        if (3 in validMovieIds) dao.upsert(UserMovieStats("user_nolan_fan", 3, isWatched = true, isFavorite = true, rating = 5.0f))
        if (4 in validMovieIds) dao.upsert(UserMovieStats("user_nolan_fan", 4, isWatched = true, isFavorite = true, rating = 5.0f))
        if (11 in validMovieIds) dao.upsert(UserMovieStats("user_nolan_fan", 11, isWatched = true, isFavorite = true, rating = 5.0f))
        if (5 in validMovieIds) dao.upsert(UserMovieStats("user_nolan_fan", 5, isWatched = true, isFavorite = true, rating = 5.0f))
        
        // user_chani likes: Dune Part Two, Blade Runner, Arrival, sci-fi focused
        if (2 in validMovieIds) dao.upsert(UserMovieStats("user_chani", 2, isWatched = true, isFavorite = true, rating = 5.0f))
        if (4 in validMovieIds) dao.upsert(UserMovieStats("user_chani", 4, isWatched = true, isFavorite = true, rating = 5.0f))
        if (11 in validMovieIds) dao.upsert(UserMovieStats("user_chani", 11, isWatched = true, isFavorite = true, rating = 4.5f))
        if (15 in validMovieIds) dao.upsert(UserMovieStats("user_chani", 15, isWatched = true, isFavorite = false, rating = 4.0f))
        
        // === Additional reviewer users ===
        // user_vengeance - Batman fan
        if (1 in validMovieIds) dao.upsert(UserMovieStats("user_vengeance", 1, isWatched = true, isFavorite = true, rating = 4.0f))
        if (5 in validMovieIds) dao.upsert(UserMovieStats("user_vengeance", 5, isWatched = true, isFavorite = true, rating = 5.0f))
        if (12 in validMovieIds) dao.upsert(UserMovieStats("user_vengeance", 12, isWatched = true, isFavorite = false, rating = 4.5f))
        
        // user_scientist - physics/sci-fi fan
        if (3 in validMovieIds) dao.upsert(UserMovieStats("user_scientist", 3, isWatched = true, isFavorite = true, rating = 4.0f))
        if (4 in validMovieIds) dao.upsert(UserMovieStats("user_scientist", 4, isWatched = true, isFavorite = true, rating = 5.0f))
        if (11 in validMovieIds) dao.upsert(UserMovieStats("user_scientist", 11, isWatched = true, isFavorite = false, rating = 4.5f))
        
        // user_cooper - space exploration fan
        if (4 in validMovieIds) dao.upsert(UserMovieStats("user_cooper", 4, isWatched = true, isFavorite = true, rating = 5.0f))
        if (2 in validMovieIds) dao.upsert(UserMovieStats("user_cooper", 2, isWatched = true, isFavorite = true, rating = 4.5f))
        
        // user_bruce - superhero fan
        if (5 in validMovieIds) dao.upsert(UserMovieStats("user_bruce", 5, isWatched = true, isFavorite = true, rating = 5.0f))
        if (1 in validMovieIds) dao.upsert(UserMovieStats("user_bruce", 1, isWatched = true, isFavorite = true, rating = 4.0f))
        if (12 in validMovieIds) dao.upsert(UserMovieStats("user_bruce", 12, isWatched = true, isFavorite = false, rating = 4.5f))
        
        // user_bong - international cinema fan
        if (6 in validMovieIds) dao.upsert(UserMovieStats("user_bong", 6, isWatched = true, isFavorite = true, rating = 5.0f))
        if (8 in validMovieIds) dao.upsert(UserMovieStats("user_bong", 8, isWatched = true, isFavorite = true, rating = 4.5f))
        
        // user_ken - comedy fan
        if (9 in validMovieIds) dao.upsert(UserMovieStats("user_ken", 9, isWatched = true, isFavorite = true, rating = 4.0f))
        if (14 in validMovieIds) dao.upsert(UserMovieStats("user_ken", 14, isWatched = true, isFavorite = false, rating = 4.0f))
        
        // user_maverick - action fan
        if (10 in validMovieIds) dao.upsert(UserMovieStats("user_maverick", 10, isWatched = true, isFavorite = true, rating = 5.0f))
        if (5 in validMovieIds) dao.upsert(UserMovieStats("user_maverick", 5, isWatched = true, isFavorite = false, rating = 4.5f))
        
        // user_cobb - mind-bending films
        if (11 in validMovieIds) dao.upsert(UserMovieStats("user_cobb", 11, isWatched = true, isFavorite = true, rating = 5.0f))
        if (3 in validMovieIds) dao.upsert(UserMovieStats("user_cobb", 3, isWatched = true, isFavorite = true, rating = 4.5f))
        if (8 in validMovieIds) dao.upsert(UserMovieStats("user_cobb", 8, isWatched = true, isFavorite = false, rating = 5.0f))
        
        // user_arthur - dark psychological films
        if (12 in validMovieIds) dao.upsert(UserMovieStats("user_arthur", 12, isWatched = true, isFavorite = true, rating = 4.5f))
        if (6 in validMovieIds) dao.upsert(UserMovieStats("user_arthur", 6, isWatched = true, isFavorite = true, rating = 5.0f))
        if (13 in validMovieIds) dao.upsert(UserMovieStats("user_arthur", 13, isWatched = true, isFavorite = false, rating = 4.5f))
        
        // guest_user - some watched movies
        if (1 in validMovieIds) dao.upsert(UserMovieStats("guest_user", 1, isWatched = true, rating = 4.0f))
        if (3 in validMovieIds) dao.upsert(UserMovieStats("guest_user", 3, isWatched = true, rating = 4.5f))
    }
    
    /**
     * Seed likes per user for "like" badge display on movies
     */
    private suspend fun seedUserLikes(
        dao: com.example.myapplication.data.local.dao.LikeDao,
        validMovieIds: Set<Int>
    ) {
        // user_marquee likes movies 1, 3, 5, 7, 11
        listOf(1, 3, 5, 7, 11).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_marquee", "movie", movieId))
        }
        
        // user_paul likes movies 2, 4, 5, 11
        listOf(2, 4, 5, 11).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_paul", "movie", movieId))
        }
        
        // user_miles likes movies 7, 8, 9, 12
        listOf(7, 8, 9, 12).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_miles", "movie", movieId))
        }
        
        // user_evelyn likes movies 6, 7, 8, 13
        listOf(6, 7, 8, 13).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_evelyn", "movie", movieId))
        }
        
        // user_nolan_fan likes movies 3, 4, 5, 10, 11
        listOf(3, 4, 5, 10, 11).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_nolan_fan", "movie", movieId))
        }
        
        // user_chani likes movies 2, 4, 11, 15
        listOf(2, 4, 11, 15).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_chani", "movie", movieId))
        }
        
        // === Additional reviewer users ===
        // user_vengeance likes movies 1, 5, 12
        listOf(1, 5, 12).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_vengeance", "movie", movieId))
        }
        // user_scientist likes movies 3, 4, 11
        listOf(3, 4, 11).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_scientist", "movie", movieId))
        }
        // user_cooper likes movies 4, 2
        listOf(4, 2).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_cooper", "movie", movieId))
        }
        // user_bruce likes movies 5, 1, 12
        listOf(5, 1, 12).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_bruce", "movie", movieId))
        }
        // user_bong likes movies 6, 8
        listOf(6, 8).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_bong", "movie", movieId))
        }
        // user_ken likes movies 9, 14
        listOf(9, 14).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_ken", "movie", movieId))
        }
        // user_maverick likes movies 10, 5
        listOf(10, 5).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_maverick", "movie", movieId))
        }
        // user_cobb likes movies 11, 3, 8
        listOf(11, 3, 8).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_cobb", "movie", movieId))
        }
        // user_arthur likes movies 12, 6, 13
        listOf(12, 6, 13).filter { it in validMovieIds }.forEach { movieId ->
            dao.like(com.example.myapplication.data.local.entities.Like("user_arthur", "movie", movieId))
        }
    }
    
    /**
     * Seed albums per user with actual movies inside (max 1-2 albums per user)
     */
    private suspend fun seedUserAlbums(dao: com.example.myapplication.data.local.dao.AlbumDao) {
        // Only a few users have albums, each with real movies inside
        
        // user_marquee - 2 albums (film critic)
        val marqueeAlbum1 = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_marquee", title = "Best of 2024", description = "Top films of 2024 - my personal picks",
            coverUrl = "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg", privacy = "public", movieCount = 4
        ))
        // Add movies to album: The Batman (1), Dune Part Two (2), Oppenheimer (3), Barbie (9)
        listOf(1, 2, 3, 9).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(marqueeAlbum1.toInt(), movieId, index))
        }
        
        val marqueeAlbum2 = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_marquee", title = "Hidden Gems", description = "Underrated masterpieces you might have missed",
            coverUrl = "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg", privacy = "public", movieCount = 3
        ))
        // Add movies: Parasite (6), Everything Everywhere (8), Joker (12)
        listOf(6, 8, 12).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(marqueeAlbum2.toInt(), movieId, index))
        }
        
        // user_nolan_fan - 2 albums (Nolan devotee)
        val nolanAlbum1 = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_nolan_fan", title = "Christopher Nolan Filmography", description = "Complete Nolan collection",
            coverUrl = "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg", privacy = "public", movieCount = 4
        ))
        // Interstellar (4), The Dark Knight (5), Oppenheimer (3), Inception (11)
        listOf(4, 5, 3, 11).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(nolanAlbum1.toInt(), movieId, index))
        }

        val nolanAlbum2 = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_nolan_fan", title = "Mind-Bending Films", description = "Movies that challenge your perception",
            coverUrl = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", privacy = "public", movieCount = 3
        ))
        // Inception (11), Everything Everywhere (8), Interstellar (4)
        listOf(11, 8, 4).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(nolanAlbum2.toInt(), movieId, index))
        }
        
        // user_evelyn - 1 album
        val evelynAlbum = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_evelyn", title = "Asian Cinema Favorites", description = "Best of Asian filmmaking",
            coverUrl = "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg", privacy = "public", movieCount = 3
        ))
        // Parasite (6), Everything Everywhere (8), Spider-Verse (7)
        listOf(6, 8, 7).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(evelynAlbum.toInt(), movieId, index))
        }
        
        // user_miles - 1 album
        val milesAlbum = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_miles", title = "Best Animated Films", description = "Animation masterpieces that push the boundaries",
            coverUrl = "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", privacy = "public", movieCount = 2
        ))
        // Spider-Verse (7), Everything Everywhere (8)
        listOf(7, 8).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(milesAlbum.toInt(), movieId, index))
        }
        
        // user_nolan_fan - 3rd album: Premium Collection with 9 movies!
        val nolanPremium = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_nolan_fan", title = "Premium Collection", description = "The ultimate must-watch list curated by a true cinephile. 9 essential films.",
            coverUrl = "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg", privacy = "public", movieCount = 9
        ))
        // All 9 movies: Batman (1), Dune (2), Oppenheimer (3), Interstellar (4), Dark Knight (5), Parasite (6), Spider-Verse (7), Everything Everywhere (8), Barbie (9)
        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(nolanPremium.toInt(), movieId, index))
        }

        // --- NEW ALBUMS REQUESTED BY USER ---

        // 8. Cinematic Masterpieces (Vol. 2)
        val mastersVol2 = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_marquee", title = "Cinematic Masterpieces (Vol. 2)", 
            description = "Continuing the journey through film history with these essential classics. Every single one is a 10/10.",
            coverUrl = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", privacy = "public", movieCount = 12
        ))
        listOf(21, 22, 23, 25, 27, 28, 29, 30, 31, 32, 34, 35).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(mastersVol2.toInt(), movieId, index))
        }

        // 9. The Sci-Fi Odyssey
        val sciFiOdyssey = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_nolan_fan", title = "The Sci-Fi Odyssey", 
            description = "Exploring the boundaries of space, time, and human consciousness. A curated list of the best science fiction.",
            coverUrl = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", privacy = "public", movieCount = 12
        ))
        listOf(4, 11, 24, 33, 37, 39, 40, 43, 44, 53, 69, 70).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(sciFiOdyssey.toInt(), movieId, index))
        }

        // 10. Pixar & Ghibli Magic
        val animationMagic = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_miles", title = "Pixar & Ghibli Magic", 
            description = "Masterpieces from the two greatest animation studios. Visual wonders for all ages.",
            coverUrl = "https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg", privacy = "public", movieCount = 12
        ))
        listOf(26, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(animationMagic.toInt(), movieId, index))
        }

        // 11. Modern Blockbusters
        val modernHits = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_paul", title = "Modern Blockbusters", 
            description = "The biggest and best films of the last few years. High stakes and incredible action.",
            coverUrl = "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg", privacy = "public", movieCount = 13
        ))
        listOf(2, 7, 9, 10, 36, 38, 45, 46, 47, 48, 49, 50, 51).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(modernHits.toInt(), movieId, index))
        }

        // 12. Crime & Thriller Essentials
        val crimeThrills = dao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = "user_vengeance", title = "Crime & Thriller Essentials", 
            description = "Gritty, tense, and absolutely gripping. These are the finest examples of the genre.",
            coverUrl = "https://image.tmdb.org/t/p/w500/aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg", privacy = "public", movieCount = 12
        ))
        listOf(1, 5, 6, 12, 13, 21, 22, 23, 27, 35, 54, 55).forEachIndexed { index, movieId ->
            dao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(crimeThrills.toInt(), movieId, index))
        }
    }
    
    /**
     * Seed social relationships - follows and friendships between users
     */
    private suspend fun seedSocialRelationships(dao: com.example.myapplication.data.local.dao.SocialDao) {
        // --- Follows ---
        // user_marquee follows: paul, miles, nolan_fan
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_marquee", "user_paul"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_marquee", "user_miles"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_marquee", "user_nolan_fan"))
        
        // user_paul follows: marquee, evelyn
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_paul", "user_marquee"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_paul", "user_evelyn"))
        
        // user_miles follows: marquee, paul, evelyn
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_miles", "user_marquee"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_miles", "user_paul"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_miles", "user_evelyn"))
        
        // user_evelyn follows: miles
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_evelyn", "user_miles"))
        
        // user_nolan_fan follows: marquee, paul
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_nolan_fan", "user_marquee"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_nolan_fan", "user_paul"))
        
        // guest_user follows: marquee
        dao.follow(com.example.myapplication.data.local.entities.Follow("guest_user", "user_marquee"))
        
        // --- Friendships (accepted) ---
        // marquee <-> paul are friends
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_marquee",
            userId2 = "user_paul",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
        
        // miles <-> evelyn are friends
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_miles",
            userId2 = "user_evelyn",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
        
        // --- Pending friend request ---
        // nolan_fan sent request to marquee
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_nolan_fan",
            userId2 = "user_marquee",
            status = "pending"
        ))
        
        // === Additional relationships for new reviewer users ===
        
        // user_vengeance follows: bruce, marquee
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_vengeance", "user_bruce"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_vengeance", "user_marquee"))
        
        // user_scientist follows: nolan_fan, cobb
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_scientist", "user_nolan_fan"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_scientist", "user_cobb"))
        
        // user_cooper follows: nolan_fan, scientist
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_cooper", "user_nolan_fan"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_cooper", "user_scientist"))
        
        // user_bruce follows: vengeance, arthur
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_bruce", "user_vengeance"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_bruce", "user_arthur"))
        
        // user_bong follows: evelyn, marquee
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_bong", "user_evelyn"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_bong", "user_marquee"))
        
        // user_ken follows: maverick, bong
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_ken", "user_maverick"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_ken", "user_bong"))
        
        // user_maverick follows: ken, cooper
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_maverick", "user_ken"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_maverick", "user_cooper"))
        
        // user_cobb follows: scientist, nolan_fan
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_cobb", "user_scientist"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_cobb", "user_nolan_fan"))
        
        // user_arthur follows: bong, bruce
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_arthur", "user_bong"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_arthur", "user_bruce"))
        
        // user_chani follows: paul, evelyn
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_chani", "user_paul"))
        dao.follow(com.example.myapplication.data.local.entities.Follow("user_chani", "user_evelyn"))
        
        // --- More friendships ---
        // vengeance <-> bruce are friends (Batman fans)
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_vengeance",
            userId2 = "user_bruce",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
        
        // scientist <-> cobb are friends (mind-bending films)
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_scientist",
            userId2 = "user_cobb",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
        
        // ken <-> maverick are friends
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_ken",
            userId2 = "user_maverick",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
        
        // bong <-> evelyn are friends (Oscar winners)
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_bong",
            userId2 = "user_evelyn",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
        
        // chani <-> paul are friends (Dune universe)
        dao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
            userId1 = "user_chani",
            userId2 = "user_paul",
            status = "accepted",
            acceptedAt = System.currentTimeMillis()
        ))
    }
    
    private suspend fun seedSampleReviews(
        reviewDao: com.example.myapplication.data.local.dao.ReviewDao,
        validMovieIds: Set<Int>
    ) {
        val sampleReviews = listOf(
            // === Reviews for The Batman (id=1) ===
            Review(
                authorId = "user_marquee",
                movieId = 1,
                rating = 4.5f,
                title = "The Dark Knight Detective",
                body = "Finally, a Batman film that focuses on the detective aspect. Pattinson brings a fresh take to the character. The noir atmosphere is perfect and the Se7en vibes are immaculate.",
                createdAt = System.currentTimeMillis() - 86400000 * 5
            ),
            Review(
                authorId = "user_vengeance",
                movieId = 1,
                rating = 4.0f,
                title = "Gritty and atmospheric",
                body = "Love the noir vibes. The Riddler is genuinely terrifying. Could have been 30 minutes shorter though.",
                createdAt = System.currentTimeMillis() - 86400000 * 3
            ),
            
            // === Reviews for Dune: Part Two (id=2) ===
            Review(
                authorId = "user_paul",
                movieId = 2,
                rating = 5.0f,
                title = "A Masterpiece of Sci-Fi Cinema",
                body = "Denis Villeneuve has outdone himself. The visuals are breathtaking, the score is haunting, and Timothée Chalamet delivers a powerful performance. This is what epic filmmaking should be.",
                createdAt = System.currentTimeMillis() - 86400000 * 7
            ),
            Review(
                authorId = "user_chani",
                movieId = 2,
                rating = 4.5f,
                title = "Epic in every sense",
                body = "The sandworm sequences alone are worth the price of admission. Some pacing issues in the second act but overall a triumph.",
                createdAt = System.currentTimeMillis() - 86400000 * 4
            ),
            
            // === Reviews for Oppenheimer (id=3) ===
            Review(
                authorId = "user_nolan_fan",
                movieId = 3,
                rating = 5.0f,
                title = "Nolan's Magnum Opus",
                body = "Three hours flew by. The ensemble cast is incredible, and Cillian Murphy's performance is Oscar-worthy. The IMAX sequences are jaw-dropping. The sound design alone is worth the price of admission.",
                createdAt = System.currentTimeMillis() - 86400000 * 10
            ),
            Review(
                authorId = "user_scientist",
                movieId = 3,
                rating = 4.0f,
                title = "Brilliant but demanding",
                body = "Not an easy watch but an important one. The non-linear storytelling can be confusing at times but rewards patience.",
                createdAt = System.currentTimeMillis() - 86400000 * 6
            ),
            
            // === Reviews for Interstellar (id=4) ===
            Review(
                authorId = "user_cooper",
                movieId = 4,
                rating = 5.0f,
                title = "Mind-blowing emotional journey",
                body = "Cried like a baby. The physics and the father-daughter bond are both mind-blowing. Hans Zimmer's score is phenomenal.",
                createdAt = System.currentTimeMillis() - 86400000 * 15
            ),
            
            // === Reviews for The Dark Knight (id=5) ===
            Review(
                authorId = "user_bruce",
                movieId = 5,
                rating = 5.0f,
                title = "The gold standard",
                body = "Still the gold standard for superhero movies. Ledger's Joker is legendary. No superhero movie has come close to this.",
                createdAt = System.currentTimeMillis() - 86400000 * 20
            ),
            
            // === Reviews for Parasite (id=6) ===
            Review(
                authorId = "user_bong",
                movieId = 6,
                rating = 5.0f,
                title = "Truly unforgettable",
                body = "A perfect mix of dark comedy, thriller, and social commentary. The basement scene is cinema at its finest.",
                createdAt = System.currentTimeMillis() - 86400000 * 12
            ),
            
            // === Reviews for Spider-Man: Across the Spider-Verse (id=7) ===
            Review(
                authorId = "user_miles",
                movieId = 7,
                rating = 5.0f,
                title = "Every frame is art",
                body = "Every frame is a piece of art. The animation is beyond anything I've seen before. Can't wait for Beyond the Spider-Verse.",
                createdAt = System.currentTimeMillis() - 86400000 * 8
            ),
            
            // === Reviews for Everything Everywhere All At Once (id=8) ===
            Review(
                authorId = "user_evelyn",
                movieId = 8,
                rating = 5.0f,
                title = "A chaotic masterpiece",
                body = "A chaotic masterpiece that somehow makes perfect sense by the end. Michelle Yeoh deserved every award.",
                createdAt = System.currentTimeMillis() - 86400000 * 14
            ),
            
            // === Reviews for Barbie (id=9) ===
            Review(
                authorId = "user_ken",
                movieId = 9,
                rating = 4.0f,
                title = "Surprisingly deep",
                body = "Bright, pink, and fun, but also surprisingly deep. Ryan Gosling stole the show with his Ken performance.",
                createdAt = System.currentTimeMillis() - 86400000 * 9
            ),
            
            // === Reviews for Top Gun: Maverick (id=10) ===
            Review(
                authorId = "user_maverick",
                movieId = 10,
                rating = 5.0f,
                title = "Pure adrenaline",
                body = "The pure adrenaline of cinema. A movie that was made for the big screen. Tom Cruise proved sequels can surpass originals.",
                createdAt = System.currentTimeMillis() - 86400000 * 11
            ),
            
            // === Reviews for Inception (id=11) ===
            Review(
                authorId = "user_cobb",
                movieId = 11,
                rating = 5.0f,
                title = "Mind-bending masterpiece",
                body = "Still rewatching and finding new details. The ending still sparks debates. Nolan at his most ambitious.",
                createdAt = System.currentTimeMillis() - 86400000 * 18
            ),
            
            // === Reviews for Joker (id=12) ===
            Review(
                authorId = "user_arthur",
                movieId = 12,
                rating = 4.5f,
                title = "Hauntingly beautiful",
                body = "Joaquin Phoenix delivers a performance that stays with you long after. A character study like no other.",
                createdAt = System.currentTimeMillis() - 86400000 * 16
            ),
            
            // Guest user reviews
            Review(
                authorId = "guest_user",
                movieId = 1,
                rating = 4.0f,
                title = "Great detective story",
                body = "Finally we get to see Batman being a detective! The three-hour runtime flew by.",
                createdAt = System.currentTimeMillis() - 86400000 * 2
            ),
            Review(
                authorId = "guest_user",
                movieId = 3,
                rating = 4.5f,
                title = "Important filmmaking",
                body = "A movie everyone should see. The weight of history has never felt heavier on screen.",
                createdAt = System.currentTimeMillis() - 86400000 * 1
            )
        )
        
        // Only insert reviews for movies that exist in database
        sampleReviews
            .filter { it.movieId in validMovieIds }
            .forEach { review ->
                reviewDao.insert(review)
            }
    }
    
    private suspend fun seedSampleComments(commentDao: com.example.myapplication.data.local.dao.CommentDao) {
        // Use sample comments from sample_comments.kt
        com.example.myapplication.data.sampleComments.forEach { comment ->
            commentDao.insert(comment)
        }
    }
    
    /**
     * Seed synthetic data to boost stats (Followers, Friends, etc.) to 10s/100s
     */
    private suspend fun seedSyntheticData(
        database: AppDatabase,
        validMovieIds: Set<Int>,
        sampleUserIds: List<String>
    ) {
        val followDao = database.socialDao()
        val friendDao = database.socialDao()
        val statsDao = database.userMovieStatsDao()
        val likeDao = database.likeDao()
        val albumDao = database.albumDao()
        val reviewDao = database.reviewDao()
        val commentDao = database.commentDao()
        val random = java.util.Random()

        sampleUserIds.forEach { userId ->
            // 1. Synthetic Followers (50-200)
            val followerCount = 50 + random.nextInt(151)
            repeat(followerCount) { i ->
                val fakeFollowerId = "user_synth_follower_${userId}_$i"
                followDao.follow(com.example.myapplication.data.local.entities.Follow(fakeFollowerId, userId))
            }

            // 2. Synthetic Following (20-80)
            val followingCount = 20 + random.nextInt(61)
            repeat(followingCount) { i ->
                val fakeFollowingId = "user_synth_following_${userId}_$i"
                followDao.follow(com.example.myapplication.data.local.entities.Follow(userId, fakeFollowingId))
            }

            // 3. Synthetic Friends (10-40)
            val friendCount = 10 + random.nextInt(31)
            repeat(friendCount) { i ->
                val fakeFriendId = "user_synth_friend_${userId}_$i"
                friendDao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
                    userId1 = userId,
                    userId2 = fakeFriendId,
                    status = "accepted",
                    acceptedAt = System.currentTimeMillis() - random.nextInt(10000000)
                ))
            }

            // 4. Synthetic Watched Movies (5-15) - Reduced to ensure variety with 20 total movies
            // Shuffle valid movie IDs and pick first N
            val shuffledMovies = validMovieIds.toList().shuffled()
            val watchedCount = (5 + random.nextInt(11)).coerceAtMost(shuffledMovies.size)
            shuffledMovies.take(watchedCount).forEach { movieId ->
                statsDao.upsert(com.example.myapplication.data.local.entities.UserMovieStats(
                    userId = userId,
                    movieId = movieId,
                    isWatched = true,
                    rating = (random.nextInt(6) + random.nextFloat() * 5).coerceIn(1f, 5f) // Random rating 1-5
                ))
            }

            // 5. Synthetic Likes (3-12)
            val likeCount = (3 + random.nextInt(10)).coerceAtMost(watchedCount) // Can't like more than watched
            val likedMovies = shuffledMovies.take(watchedCount).shuffled().take(likeCount)
            likedMovies.forEach { movieId ->
                 likeDao.like(com.example.myapplication.data.local.entities.Like(userId, "movie", movieId))
            }

            // 6. Synthetic Album Likes with FIXED high counts per album
            // Using direct insertion loops to guarantee hundreds of likes per album
            val albumLikeCounts = mapOf(
                1 to 30,    // Each user adds 30 likes to Album 1 → 15 users × 30 = 450
                2 to 25,    // Album 2: 375 likes
                3 to 20,    // Album 3: 300 likes  
                4 to 15,    // Album 4: 225 likes
                5 to 12,    // Album 5: 180 likes
                6 to 10,    // Album 6: 150 likes
                7 to 8,     // Album 7: 120 likes
                8 to 35,    // Album 8: 525 likes (New)
                9 to 40,    // Album 9: 600 likes (New)
                10 to 45,   // Album 10: 675 likes (New)
                11 to 50,   // Album 11: 750 likes (New)
                12 to 55    // Album 12: 825 likes (New)
            )
            albumLikeCounts.forEach { (albumId, count) ->
                repeat(count) { i ->
                    val fakeUserId = "user_synth_album${albumId}_liker_${userId}_$i"
                    likeDao.like(com.example.myapplication.data.local.entities.Like(fakeUserId, "album", albumId))
                }
            }
            
            // 6b. Synthetic Album Views (100-500 views per album)
            val activityDao = database.userActivityDao()
            val albumViewCounts = mapOf(
                1 to 50,    // Each user adds views → 15 users × 50 = 750
                2 to 40,
                3 to 30,
                4 to 25,
                5 to 20,
                6 to 18,
                7 to 15,
                8 to 60,    // New albums get more views
                9 to 70,
                10 to 80,
                11 to 90,
                12 to 100
            )
            albumViewCounts.forEach { (albumId, count) ->
                repeat(count) { i ->
                    val fakeUserId = "user_synth_album${albumId}_viewer_${userId}_$i"
                    activityDao.log(com.example.myapplication.data.local.entities.UserActivity(
                        userId = fakeUserId,
                        actionType = "view",
                        targetType = "album",
                        targetId = albumId,
                        createdAt = System.currentTimeMillis() - random.nextInt(100000000)
                    ))
                }
            }
            
            // 7. Synthetic Album Comments (5-10 per user per popular album)
            val commentTemplates = listOf(
                "Love this collection! 🎬",
                "Great picks!",
                "Exactly my taste",
                "Can't wait to watch these",
                "Perfect list 👌",
                "Amazing curation!",
                "Adding to my watchlist",
                "Finally someone gets it!",
                "These are all classics",
                "Best albums on the app"
            )
            (1..12).forEach { albumId ->
                if (random.nextFloat() < 0.6f) { // 60% chance to comment
                    commentDao.insert(com.example.myapplication.data.local.entities.Comment(
                        userId = userId,
                        targetType = "album",
                        targetId = albumId,
                        content = commentTemplates.random(),
                        createdAt = System.currentTimeMillis() - random.nextInt(100000000)
                    ))
                }
            }
            
            // 8. Synthetic Reviews FIRST (1-4) - must be created before comments
            val reviewCount = 1 + random.nextInt(4)
            val reviewMovies = likedMovies.take(reviewCount) // Review movies they liked
            val createdReviewIds = mutableListOf<Long>()
            reviewMovies.forEach { movieId ->
                val reviewId = reviewDao.insert(com.example.myapplication.data.local.entities.Review(
                    authorId = userId,
                    movieId = movieId,
                    rating = (3 + random.nextInt(3)).toFloat(), // 3-5 stars
                    title = listOf("Great movie!", "Worth watching", "Enjoyed it", "A classic", "Must watch").random(),
                    body = "This was a really interesting film. The cinematography was great and the acting was top notch. Highly recommended!",
                    createdAt = System.currentTimeMillis() - random.nextInt(100000000)
                ))
                createdReviewIds.add(reviewId)
            }
            
            // 9. Synthetic Review Comments for ALL reviews that exist (using actual review IDs)
            val reviewCommentTemplates = listOf(
                "Great review! 👏",
                "Totally agree with this",
                "Well written!",
                "This helped me decide to watch it",
                "Exactly what I was thinking",
                "Nice perspective!",
                "I disagree but respect your opinion",
                "You should check out the sequel too",
                "The director's cut is even better!",
                "Thanks for this review!"
            )
            // Comment on reviews from OTHER users (not our own)
            val existingReviews = reviewDao.getReviewIdsSync()
            existingReviews.forEach { reviewId ->
                // High chance for high engagement on specific reviews to test sorting
                val commentChance = if (reviewId % 3 == 0) 0.9f else 0.4f
                val commentsPerUser = if (reviewId % 3 == 0) 5 else 1
                
                repeat(commentsPerUser) {
                    if (random.nextFloat() < commentChance) {
                        commentDao.insert(com.example.myapplication.data.local.entities.Comment(
                            userId = userId,
                            targetType = "review",
                            targetId = reviewId,
                            content = reviewCommentTemplates.random(),
                            createdAt = System.currentTimeMillis() - random.nextInt(100000000)
                        ))
                    }
                }

                // Also boost likes for these reviews
                if (reviewId % 3 == 0) {
                    repeat(10) { i ->
                        val fakeUserId = "user_synth_review${reviewId}_liker_${userId}_$i"
                        likeDao.like(com.example.myapplication.data.local.entities.Like(fakeUserId, "review", reviewId))
                    }
                }
            }
        }
    }
    
    /**
     * Seed sample notifications for the demo user.
     */
    private suspend fun seedSampleNotifications(
        notificationDao: com.example.myapplication.data.local.dao.NotificationDao
    ) {
        val currentUserId = "user_demo" 
        val currentTime = System.currentTimeMillis()
        
        val sampleNotifications = listOf(
            // NEWS notifications
            Notification(
                userId = currentUserId,
                type = "NEWS",
                title = "Dune: Part Three Officially Greenlit",
                description = "Warner Bros. has officially greenlit Dune: Part Three. Denis Villeneuve's epic sci-fi saga continues!",
                imageUrl = "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
                relatedType = "news",
                relatedId = 2,
                isRead = false,
                createdAt = currentTime - 2 * 3600000 // 2 hours ago
            ),
            Notification(
                userId = currentUserId,
                type = "NEWS",
                title = "Marvel Studios Announces Phase 7 Slate",
                description = "Kevin Feige unveils Marvel's new direction with fewer films and higher stakes.",
                imageUrl = "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
                relatedType = "news",
                relatedId = 6,
                isRead = false,
                createdAt = currentTime - 5 * 3600000 // 5 hours ago
            ),
            Notification(
                userId = currentUserId,
                type = "NEWS",
                title = "The Batman 2: Villain Details Revealed",
                description = "Matt Reeves shares exclusive details about The Batman sequel, teasing the Court of Owls.",
                imageUrl = "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                relatedType = "news",
                relatedId = 4,
                isRead = true,
                createdAt = currentTime - 24 * 3600000 // 1 day ago
            ),
            
            // TRAILER notifications
            Notification(
                userId = currentUserId,
                type = "TRAILER",
                title = "Oppenheimer - Now Streaming",
                description = "Christopher Nolan's Oscar-winning masterpiece is now available to stream!",
                imageUrl = "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                relatedType = "movie",
                relatedId = 3,
                isRead = false,
                createdAt = currentTime - 3 * 3600000 // 3 hours ago
            ),
            Notification(
                userId = currentUserId,
                type = "TRAILER",
                title = "Spider-Man: Across the Spider-Verse",
                description = "The critically acclaimed animated sequel is now available!",
                imageUrl = "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
                relatedType = "movie",
                relatedId = 7,
                isRead = true,
                createdAt = currentTime - 24 * 3600000 // 1 day ago
            ),
            
            // FRIEND notifications (follows, album activity)
            Notification(
                userId = currentUserId,
                type = "FRIEND",
                title = "Paul started following you",
                description = "Check out their profile!",
                imageUrl = "https://i.pravatar.cc/150?u=user_paul",
                relatedType = "user",
                relatedId = null,
                actorUserId = "user_paul",
                actorName = "Paul",
                isRead = false,
                createdAt = currentTime - 4 * 3600000 // 4 hours ago
            ),
            Notification(
                userId = currentUserId,
                type = "FRIEND",
                title = "New Album: A-MUST-WATCH OAT!!",
                description = "Check out this curated collection of masterpieces!",
                imageUrl = "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                relatedType = "album",
                relatedId = 1,
                isRead = false,
                createdAt = currentTime - 6 * 3600000 // 6 hours ago
            ),
            Notification(
                userId = currentUserId,
                type = "FRIEND",
                title = "Nolan Fan started following you",
                description = "Check out their profile!",
                imageUrl = "https://i.pravatar.cc/150?u=user_nolan_fan",
                relatedType = "user",
                relatedId = null,
                actorUserId = "user_nolan_fan",
                actorName = "Nolan Fan",
                isRead = true,
                createdAt = currentTime - 48 * 3600000 // 2 days ago
            ),
            
            // COMMENT notifications
            Notification(
                userId = currentUserId,
                type = "COMMENT",
                title = "Evelyn commented on your review",
                description = "\"The Batman\" review: Great analysis! 👏",
                imageUrl = "https://i.pravatar.cc/150?u=user_evelyn",
                relatedType = "review",
                relatedId = 1,
                actorUserId = "user_evelyn",
                actorName = "Evelyn",
                isRead = false,
                createdAt = currentTime - 1 * 3600000 // 1 hour ago
            ),
            Notification(
                userId = currentUserId,
                type = "COMMENT",
                title = "Miles replied to your comment",
                description = "On \"Dune: Part Two\" discussion",
                imageUrl = "https://i.pravatar.cc/150?u=user_miles",
                relatedType = "movie",
                relatedId = 2,
                actorUserId = "user_miles",
                actorName = "Miles",
                isRead = false,
                createdAt = currentTime - 30 * 60000 // 30 minutes ago
            ),
            Notification(
                userId = currentUserId,
                type = "COMMENT",
                title = "Chani commented on your album",
                description = "\"Best of 2023\" album: Love this collection!",
                imageUrl = "https://i.pravatar.cc/150?u=user_chani",
                relatedType = "album",
                relatedId = 3,
                actorUserId = "user_chani",
                actorName = "Chani",
                isRead = true,
                createdAt = currentTime - 72 * 3600000 // 3 days ago
            )
        )
        
        sampleNotifications.forEach { notification ->
            notificationDao.insert(notification)
        }
    }
    
    /**
     * Seed demo-specific data for user_demo:
     * - 6 friends (mutual follows)
     * - 15 following, 12 followers
     * - 1 album with 12 movies
     * - 8 reviews
     * - 25+ watched films
     */
    private suspend fun seedDemoUserData(
        database: AppDatabase,
        validMovieIds: Set<Int>
    ) {
        val demoUserId = "user_demo"
        val socialDao = database.socialDao()
        val albumDao = database.albumDao()
        val reviewDao = database.reviewDao()
        val likeDao = database.likeDao()
        val userActivityDao = database.userActivityDao()
        val random = java.util.Random(42) // Fixed seed for reproducibility
        
        // Existing users to befriend/follow
        val existingUsers = listOf(
            "user_marquee", "user_paul", "user_nolan_fan", "user_evelyn", "user_miles",
            "user_vengeance", "user_scientist", "user_cooper", "user_bruce", "user_bong",
            "user_ken", "user_maverick", "user_cobb", "user_arthur", "user_chani"
        )
        
        // 1. Create 6 friends (mutual follows)
        val friendUsers = existingUsers.take(6)
        friendUsers.forEach { friendId ->
            // Demo follows them
            socialDao.follow(com.example.myapplication.data.local.entities.Follow(demoUserId, friendId))
            // They follow demo back
            socialDao.follow(com.example.myapplication.data.local.entities.Follow(friendId, demoUserId))
            // Add friendship record (with accepted status)
            socialDao.sendFriendRequest(com.example.myapplication.data.local.entities.Friendship(
                userId1 = demoUserId,
                userId2 = friendId,
                status = "accepted",
                acceptedAt = System.currentTimeMillis()
            ))
        }
        
        // 2. Additional following (15 - 6 = 9 more)
        val additionalFollowing = existingUsers.drop(6).take(9)
        additionalFollowing.forEach { userId ->
            socialDao.follow(com.example.myapplication.data.local.entities.Follow(demoUserId, userId))
        }
        

        // 4. Create 1 album with 12 movies
        val demoAlbumId = albumDao.insertAlbum(com.example.myapplication.data.local.entities.Album(
            ownerId = demoUserId,
            title = "My Favorite Films",
            description = "A curated collection of my all-time favorite movies.",
            coverUrl = "https://image.tmdb.org/t/p/w500/5P8SmMzSNYikXpxil6BYzJ16611.jpg",
            privacy = "public",
            movieCount = 12
        ))
        val albumMovies = validMovieIds.shuffled(random).take(12)
        albumMovies.forEachIndexed { index, movieId ->
            albumDao.addMovieToAlbum(com.example.myapplication.data.local.entities.AlbumMovie(demoAlbumId.toInt(), movieId, index))
        }
        
        // 5. Create 8 reviews for different movies
        val reviewMovies = validMovieIds.shuffled(random).take(8)
        val reviewTitles = listOf(
            "Absolutely stunning!", "A masterpiece", "Highly recommend",
            "Great cinematography", "Oscar-worthy", "Mind-blowing",
            "Emotional journey", "Pure entertainment"
        )
        val reviewBodies = listOf(
            "This film exceeded all my expectations. The direction, acting, and score come together beautifully.",
            "A true work of art that stays with you long after the credits roll.",
            "If you haven't seen this yet, you're missing out on something special.",
            "Every frame is a painting. The visual storytelling is unmatched.",
            "The performances in this movie deserve all the recognition. Truly exceptional.",
            "This movie challenges your perception and keeps you thinking for days.",
            "I cried, I laughed, I felt everything. What a beautiful story.",
            "Perfect popcorn entertainment with surprisingly deep themes."
        )
        reviewMovies.forEachIndexed { index, movieId ->
            reviewDao.insert(com.example.myapplication.data.local.entities.Review(
                authorId = demoUserId,
                movieId = movieId,
                title = reviewTitles[index],
                body = reviewBodies[index],
                rating = 3.5f + (random.nextFloat() * 1.5f), // 3.5 to 5.0
                createdAt = System.currentTimeMillis() - random.nextInt(30) * 86400000L // Last 30 days
            ))
            // Like the movies we reviewed
            likeDao.like(com.example.myapplication.data.local.entities.Like(demoUserId, "movie", movieId))
        }
        
        // 6. Watch 25+ movies (log views)
        val watchedMovies = validMovieIds.shuffled(random).take(28)
        watchedMovies.forEach { movieId ->
            userActivityDao.log(com.example.myapplication.data.local.entities.UserActivity(
                userId = demoUserId,
                actionType = "view",
                targetType = "movie",
                targetId = movieId,
                createdAt = System.currentTimeMillis() - random.nextInt(60) * 86400000L // Last 60 days
            ))
        }
    }
}

