package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.SavedAlbum
import com.example.myapplication.data.local.entities.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedAlbumDao {
    
    // Check if album is saved
    @Query("SELECT EXISTS(SELECT 1 FROM saved_albums WHERE userId = :userId AND albumId = :albumId)")
    suspend fun isSaved(userId: String, albumId: Int): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM saved_albums WHERE userId = :userId AND albumId = :albumId)")
    fun isSavedFlow(userId: String, albumId: Int): Flow<Boolean>
    
    // Save album
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(savedAlbum: SavedAlbum)
    
    // Unsave album
    @Query("DELETE FROM saved_albums WHERE userId = :userId AND albumId = :albumId")
    suspend fun unsave(userId: String, albumId: Int)
    
    // Toggle save
    @Transaction
    suspend fun toggleSave(userId: String, albumId: Int): Boolean {
        val isSaved = isSaved(userId, albumId)
        if (isSaved) {
            unsave(userId, albumId)
        } else {
            save(SavedAlbum(userId, albumId))
        }
        return !isSaved
    }
    
    // Get all saved albums for user
    @Query("SELECT * FROM saved_albums WHERE userId = :userId ORDER BY savedAt DESC")
    fun getSavedAlbums(userId: String): Flow<List<SavedAlbum>>
    
    // Get saved album IDs for user
    @Query("SELECT albumId FROM saved_albums WHERE userId = :userId")
    fun getSavedAlbumIds(userId: String): Flow<List<Int>>
    
    // Get saved albums with Album details (join)
    @Query("""
        SELECT a.* FROM albums a 
        INNER JOIN saved_albums sa ON a.id = sa.albumId 
        WHERE sa.userId = :userId 
        ORDER BY sa.savedAt DESC
    """)
    fun getSavedAlbumsWithDetails(userId: String): Flow<List<Album>>
    
    // Get saved album count for user
    @Query("SELECT COUNT(*) FROM saved_albums WHERE userId = :userId")
    fun getSavedCount(userId: String): Flow<Int>
}
