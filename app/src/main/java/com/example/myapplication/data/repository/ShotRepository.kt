package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ShotDao
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.sampleShots
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Shot operations.
 */
class ShotRepository(private val shotDao: ShotDao) {
    
    fun getAllShots(): Flow<List<Shot>> = shotDao.getAllShots()
    
    suspend fun getShotById(id: Int): Shot? = shotDao.getShotById(id)
    
    suspend fun getRelatedMovie(movieId: Int): Movie? = shotDao.getRelatedMovie(movieId)
    
    suspend fun insert(shot: Shot): Long = shotDao.insert(shot)
    
    suspend fun update(shot: Shot) = shotDao.update(shot)
    
    suspend fun delete(shot: Shot) = shotDao.delete(shot)
    
    /**
     * Seeds the database with sample shots if empty.
     */
    suspend fun seedDatabaseIfEmpty() {
        val count = shotDao.getCount()
        if (count == 0) {
            shotDao.insertAll(sampleShots)
        }
    }
    
    /**
     * Force reseed - delete all and insert fresh data.
     * Use when data structure changes.
     */
    suspend fun forceReseed() {
        shotDao.deleteAll()
        shotDao.insertAll(sampleShots)
    }
}
