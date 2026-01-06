package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.local.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface ShotDao {
    
    @Query("SELECT * FROM shots ORDER BY createdAt DESC")
    fun getAllShots(): Flow<List<Shot>>
    
    @Query("SELECT * FROM shots WHERE id = :id")
    suspend fun getShotById(id: Int): Shot?
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getRelatedMovie(movieId: Int): Movie?
    
    @Query("SELECT COUNT(*) FROM shots")
    suspend fun getCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shots: List<Shot>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shot: Shot): Long
    
    @Update
    suspend fun update(shot: Shot)
    
    @Delete
    suspend fun delete(shot: Shot)
    
    @Query("DELETE FROM shots")
    suspend fun deleteAll()
}
