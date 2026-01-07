package com.example.myapplication.data.local.db

import android.content.Context
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
            
            // Check if reviews already exist
            val existingReviews = reviewDao.getRecentReviews(1).first()
            
            // Only seed if empty
            if (existingReviews.isEmpty()) {
                seedSampleReviews(reviewDao)
            }
            
            isSeeded = true
        }
    }
    
    private suspend fun seedSampleReviews(reviewDao: com.example.myapplication.data.local.dao.ReviewDao) {
        val sampleReviews = listOf(
            // Reviews for movie ID 1 (Dune: Part Two assumed)
            Review(
                authorId = "user_1",
                movieId = 1,
                rating = 5.0f,
                title = "A Masterpiece of Sci-Fi Cinema",
                body = "Denis Villeneuve has outdone himself. The visuals are breathtaking, the score is haunting, and Timothée Chalamet delivers a powerful performance. This is what epic filmmaking should be.",
                createdAt = System.currentTimeMillis() - 86400000 * 5 // 5 days ago
            ),
            Review(
                authorId = "user_2",
                movieId = 1,
                rating = 4.5f,
                title = "Epic in every sense",
                body = "The sandworm sequences alone are worth the price of admission. Some pacing issues in the second act but overall a triumph.",
                createdAt = System.currentTimeMillis() - 86400000 * 3
            ),
            
            // Reviews for movie ID 2 (Oppenheimer assumed)
            Review(
                authorId = "user_3",
                movieId = 2,
                rating = 5.0f,
                title = "Nolan's Magnum Opus",
                body = "Three hours flew by. The ensemble cast is incredible, and Cillian Murphy's performance is Oscar-worthy. The IMAX sequences are jaw-dropping.",
                createdAt = System.currentTimeMillis() - 86400000 * 7
            ),
            Review(
                authorId = "user_1",
                movieId = 2,
                rating = 4.0f,
                title = "Brilliant but demanding",
                body = "Not an easy watch but an important one. The non-linear storytelling can be confusing at times but rewards patience.",
                createdAt = System.currentTimeMillis() - 86400000 * 4
            ),
            
            // Reviews for movie ID 3 (The Batman assumed)
            Review(
                authorId = "user_4",
                movieId = 3,
                rating = 4.5f,
                title = "The Dark Knight Detective",
                body = "Finally, a Batman film that focuses on the detective aspect. Pattinson brings a fresh take to the character. The noir atmosphere is perfect.",
                createdAt = System.currentTimeMillis() - 86400000 * 10
            ),
            Review(
                authorId = "guest_user",
                movieId = 3,
                rating = 4.0f,
                title = "Gritty and atmospheric",
                body = "Love the Se7en vibes. The Riddler is genuinely terrifying. Could have been 30 minutes shorter though.",
                createdAt = System.currentTimeMillis() - 86400000 * 2
            ),
            
            // Reviews for movie ID 4
            Review(
                authorId = "user_5",
                movieId = 4,
                rating = 3.5f,
                title = "Solid entertainment",
                body = "Not groundbreaking but enjoyable. Good performances and some genuinely funny moments.",
                createdAt = System.currentTimeMillis() - 86400000 * 8
            ),
            
            // Reviews for movie ID 5
            Review(
                authorId = "user_2",
                movieId = 5,
                rating = 5.0f,
                title = "A timeless classic",
                body = "Rewatched for the 10th time and it still holds up. Spielberg at his finest. The beach scene still gives me chills.",
                createdAt = System.currentTimeMillis() - 86400000 * 15
            )
        )
        
        sampleReviews.forEach { review ->
            reviewDao.insert(review)
        }
    }
}
