package com.example.myapplication.data.local.db

import android.content.Context
import com.example.myapplication.data.local.entities.Comment
import com.example.myapplication.data.local.entities.Review
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
     * Call this once after database is created to populate sample data.
     */
    fun seedIfNeeded(context: Context, scope: CoroutineScope) {
        if (isSeeded) return
        
        scope.launch(Dispatchers.IO) {
            val database = DatabaseProvider.getDatabase(context)
            val reviewDao = database.reviewDao()
            val commentDao = database.commentDao()
            
            // Check if reviews already exist
            val existingReviews = reviewDao.getRecentReviews(1).first()
            
            // Only seed if empty
            if (existingReviews.isEmpty()) {
                seedSampleReviews(reviewDao)
                seedSampleComments(commentDao)
            }
            
            isSeeded = true
        }
    }
    
    private suspend fun seedSampleReviews(reviewDao: com.example.myapplication.data.local.dao.ReviewDao) {
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
        
        sampleReviews.forEach { review ->
            reviewDao.insert(review)
        }
    }
    
    private suspend fun seedSampleComments(commentDao: com.example.myapplication.data.local.dao.CommentDao) {
        val sampleComments = listOf(
            // Comments on review 1 (The Batman)
            Comment(
                userId = "user_fan1",
                targetType = "review",
                targetId = 1,
                content = "Totally agree! Pattinson nailed it.",
                createdAt = System.currentTimeMillis() - 86400000 * 4
            ),
            Comment(
                userId = "user_fan2",
                targetType = "review",
                targetId = 1,
                content = "The Riddler was so creepy, loved it!",
                createdAt = System.currentTimeMillis() - 86400000 * 3
            ),
            // Reply to first comment
            Comment(
                userId = "user_fan3",
                targetType = "review",
                targetId = 1,
                parentCommentId = 1,
                content = "Right? Best portrayal yet!",
                createdAt = System.currentTimeMillis() - 86400000 * 2
            ),
            
            // Comments on review 3 (Dune)
            Comment(
                userId = "user_spice",
                targetType = "review",
                targetId = 3,
                content = "The spice must flow! 🏜️",
                createdAt = System.currentTimeMillis() - 86400000 * 6
            ),
            Comment(
                userId = "user_fremen",
                targetType = "review",
                targetId = 3,
                content = "Villeneuve is a master of sci-fi.",
                createdAt = System.currentTimeMillis() - 86400000 * 5
            ),
            
            // Comments on album (for future use)
            Comment(
                userId = "user_collector",
                targetType = "album",
                targetId = 1,
                content = "Great collection! Adding to my watchlist.",
                createdAt = System.currentTimeMillis() - 86400000 * 8
            ),
            
            // Comments on news (for future use)
            Comment(
                userId = "user_newsreader",
                targetType = "news",
                targetId = 1,
                content = "Can't wait for this!",
                createdAt = System.currentTimeMillis() - 86400000 * 1
            )
        )
        
        sampleComments.forEach { comment ->
            commentDao.insert(comment)
        }
    }
}
