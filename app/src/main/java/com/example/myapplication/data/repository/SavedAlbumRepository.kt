package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.SavedAlbumDao
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.SavedAlbum
import kotlinx.coroutines.flow.Flow

class SavedAlbumRepository(private val dao: SavedAlbumDao) {
    
    // Check if album is saved
    suspend fun isSaved(userId: String, albumId: Int): Boolean = 
        dao.isSaved(userId, albumId)
    
    fun isSavedFlow(userId: String, albumId: Int): Flow<Boolean> = 
        dao.isSavedFlow(userId, albumId)
    
    // Save/Unsave
    suspend fun save(userId: String, albumId: Int) {
        dao.save(SavedAlbum(userId, albumId))
    }
    
    suspend fun unsave(userId: String, albumId: Int) {
        dao.unsave(userId, albumId)
    }
    
    suspend fun toggleSave(userId: String, albumId: Int): Boolean {
        return dao.toggleSave(userId, albumId)
    }
    
    // Get saved albums
    fun getSavedAlbums(userId: String): Flow<List<SavedAlbum>> = 
        dao.getSavedAlbums(userId)
    
    fun getSavedAlbumsWithDetails(userId: String): Flow<List<Album>> = 
        dao.getSavedAlbumsWithDetails(userId)
    
    fun getSavedCount(userId: String): Flow<Int> = 
        dao.getSavedCount(userId)
}
