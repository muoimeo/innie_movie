package com.example.myapplication.data

import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.local.entities.Follow
import com.example.myapplication.data.local.entities.Friendship
import com.example.myapplication.data.local.entities.UserMovieStats
import com.example.myapplication.data.local.entities.Like

/**
 * Comprehensive sample user data for database seeding.
 * Each user has unique: avatars, covers, bios, favorites, watched, follow relationships, friends.
 */

// ===== SAMPLE USERS =====
val sampleUsers = listOf(
    User(
        userId = "user_marquee",
        username = "@marquee",
        displayName = "The Marquee",
        email = "marquee@innie.app",
        passwordHash = "hashed",
        bio = "Film critic and blogger. Obsessed with Scorsese and Villeneuve. I write about cinema every week.",
        avatarUrl = "https://i.pravatar.cc/150?u=marquee_film",
        coverUrl = "https://picsum.photos/seed/filmcriticshot/800/300"
    ),
    User(
        userId = "user_paul",
        username = "@muaddib",
        displayName = "Paul Atreides",
        email = "paul@innie.app",
        passwordHash = "hashed",
        bio = "The spice must flow. Desert power enthusiast. Arrakis is my home now.",
        avatarUrl = "https://i.pravatar.cc/150?u=paul_dune",
        coverUrl = "https://picsum.photos/seed/arrakisdesert/800/300"
    ),
    User(
        userId = "user_miles",
        username = "@spidey2099",
        displayName = "Miles Morales",
        email = "miles@innie.app",
        passwordHash = "hashed",
        bio = "Brooklyn's own Spider-Man. Animation lover. Every dimension has a Spider-Man.",
        avatarUrl = "https://i.pravatar.cc/150?u=miles_spider",
        coverUrl = "https://picsum.photos/seed/spiderbrooklyn/800/300"
    ),
    User(
        userId = "user_evelyn",
        username = "@evelyn_wang",
        displayName = "Evelyn Wang",
        email = "evelyn@innie.app",
        passwordHash = "hashed",
        bio = "Laundromat owner by day, multiverse explorer by night. Everything everywhere all at once.",
        avatarUrl = "https://i.pravatar.cc/150?u=evelyn_multi",
        coverUrl = "https://picsum.photos/seed/multiversejump/800/300"
    ),
    User(
        userId = "user_nolan_fan",
        username = "@nolan_devotee",
        displayName = "Christopher Fan",
        email = "nolan@innie.app",
        passwordHash = "hashed",
        bio = "IMAX enthusiast. Time is relative. I've watched Tenet 47 times and I still don't fully get it.",
        avatarUrl = "https://i.pravatar.cc/150?u=nolan_time",
        coverUrl = "https://picsum.photos/seed/imaxcinema/800/300"
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
    )
)

// ===== PER-USER FAVORITE MOVIE IDs =====
// These are movie IDs from the sample_movies database
val userFavoriteMovies = mapOf(
    "user_marquee" to listOf(1, 3, 5, 7, 11),      // The Irishman, Inception, Oppenheimer, Parasite, Interstellar
    "user_paul" to listOf(4, 5, 11, 15, 16),       // Dune Part Two, Oppenheimer, Interstellar, Blade Runner, Arrival
    "user_miles" to listOf(9, 7, 12, 1, 20),       // Spider-Verse, Parasite, ATSV, The Irishman, Guardians
    "user_evelyn" to listOf(6, 7, 8, 13, 14),      // EEAAO, Parasite, The Matrix, Get Out, Us
    "user_nolan_fan" to listOf(3, 5, 11, 10, 2),   // Inception, Oppenheimer, Interstellar, Tenet, Dark Knight
    "user_chani" to listOf(4, 15, 16, 11, 17)      // Dune Part Two, Blade Runner, Arrival, Interstellar, Ex Machina
)

// ===== PER-USER WATCHED MOVIE IDs WITH RATINGS =====
val userWatchedMovies = mapOf(
    "user_marquee" to listOf(
        Triple(1, 5.0f, true), Triple(2, 5.0f, true), Triple(3, 5.0f, true),
        Triple(5, 5.0f, true), Triple(7, 4.5f, true), Triple(11, 4.0f, true),
        Triple(13, 4.0f, false), Triple(14, 3.5f, false)
    ),
    "user_paul" to listOf(
        Triple(4, 5.0f, true), Triple(5, 5.0f, true), Triple(11, 5.0f, true),
        Triple(15, 5.0f, true), Triple(16, 4.5f, true), Triple(17, 4.0f, false)
    ),
    "user_miles" to listOf(
        Triple(9, 5.0f, true), Triple(12, 5.0f, true), Triple(7, 4.5f, true),
        Triple(1, 4.0f, true), Triple(20, 4.0f, true), Triple(2, 4.5f, false),
        Triple(6, 5.0f, false), Triple(18, 3.5f, false), Triple(19, 4.0f, false)
    ),
    "user_evelyn" to listOf(
        Triple(6, 5.0f, true), Triple(7, 5.0f, true), Triple(8, 4.5f, true),
        Triple(13, 5.0f, true), Triple(14, 4.0f, true), Triple(3, 4.0f, false)
    ),
    "user_nolan_fan" to listOf(
        Triple(3, 5.0f, true), Triple(5, 5.0f, true), Triple(11, 5.0f, true),
        Triple(10, 5.0f, true), Triple(2, 5.0f, true), Triple(4, 4.5f, false),
        Triple(18, 4.0f, false), Triple(21, 4.5f, false), Triple(22, 4.0f, false),
        Triple(1, 4.0f, false), Triple(7, 4.5f, false)
    ),
    "user_chani" to listOf(
        Triple(4, 5.0f, true), Triple(15, 5.0f, true), Triple(16, 4.5f, true),
        Triple(11, 4.5f, true), Triple(17, 4.0f, true)
    )
)

// ===== FOLLOW RELATIONSHIPS =====
// Pair(followerId, followingId) means follower follows following
val sampleFollows = listOf(
    // user_marquee follows many critics/fans
    Follow("user_marquee", "user_paul"),
    Follow("user_marquee", "user_nolan_fan"),
    Follow("user_marquee", "user_evelyn"),
    
    // user_paul (Dune fan) follows other sci-fi fans
    Follow("user_paul", "user_chani"),
    Follow("user_paul", "user_nolan_fan"),
    
    // user_miles follows diverse users
    Follow("user_miles", "user_evelyn"),
    Follow("user_miles", "user_marquee"),
    Follow("user_miles", "user_paul"),
    
    // user_evelyn follows film critics
    Follow("user_evelyn", "user_marquee"),
    Follow("user_evelyn", "user_miles"),
    
    // user_nolan_fan follows Nolan-appreciating users
    Follow("user_nolan_fan", "user_marquee"),
    Follow("user_nolan_fan", "user_paul"),
    Follow("user_nolan_fan", "user_evelyn"),
    
    // user_chani follows other Dune fans
    Follow("user_chani", "user_paul"),
    Follow("user_chani", "user_nolan_fan")
)

// ===== FRIENDSHIPS =====
val sampleFriendships = listOf(
    // Dune fans are friends
    Friendship("user_paul", "user_chani", "accepted", System.currentTimeMillis()),
    
    // Film buffs are friends
    Friendship("user_marquee", "user_nolan_fan", "accepted", System.currentTimeMillis()),
    
    // Miles and Evelyn are friends (they appreciate unique storytelling)
    Friendship("user_miles", "user_evelyn", "accepted", System.currentTimeMillis()),
    
    // Pending friend requests
    Friendship("user_miles", "user_paul", "pending"),
    Friendship("user_evelyn", "user_nolan_fan", "pending")
)

// ===== PER-USER LIKES (for content like shots, albums) =====
val userContentLikes = mapOf(
    "user_marquee" to listOf("movie:1", "movie:3", "movie:5", "album:1", "shot:1", "shot:2"),
    "user_paul" to listOf("movie:4", "movie:5", "movie:11", "album:2", "shot:3"),
    "user_miles" to listOf("movie:9", "movie:12", "movie:7", "album:3", "shot:1", "shot:4"),
    "user_evelyn" to listOf("movie:6", "movie:7", "movie:8", "album:4", "shot:2"),
    "user_nolan_fan" to listOf("movie:3", "movie:5", "movie:11", "movie:10", "movie:2", "album:2", "shot:3"),
    "user_chani" to listOf("movie:4", "movie:15", "movie:16", "album:2")
)
