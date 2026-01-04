package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.AlbumDao
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.AlbumMovie
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.sampleAlbums
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Album operations.
 */
class AlbumRepository(private val albumDao: AlbumDao) {
    
    // Get all albums
    fun getAllAlbums(): Flow<List<Album>> = albumDao.getAllAlbums()
    
    // Get public albums
    fun getPublicAlbums(): Flow<List<Album>> = albumDao.getPublicAlbums()
    
    // Get albums by user
    fun getAlbumsByUser(userId: String): Flow<List<Album>> = albumDao.getAlbumsByUser(userId)
    
    // Get album by ID
    suspend fun getAlbumById(id: Int): Album? = albumDao.getAlbumById(id)
    
    fun getAlbumByIdFlow(id: Int): Flow<Album?> = albumDao.getAlbumByIdFlow(id)
    
    // Search albums
    fun searchAlbums(query: String): Flow<List<Album>> = albumDao.searchAlbums(query)
    
    // Create album
    suspend fun createAlbum(album: Album): Long = albumDao.insertAlbum(album)
    
    // Update album
    suspend fun updateAlbum(album: Album) = albumDao.updateAlbum(album)
    
    // Delete album
    suspend fun deleteAlbum(album: Album) = albumDao.deleteAlbum(album)
    
    // Get movies in album
    fun getMoviesInAlbum(albumId: Int): Flow<List<Movie>> = albumDao.getMoviesInAlbum(albumId)
    
    suspend fun getMoviesInAlbumSync(albumId: Int): List<Movie> = albumDao.getMoviesInAlbumSync(albumId)
    
    // Add movie to album
    suspend fun addMovieToAlbum(albumId: Int, movieId: Int, position: Int = 0) {
        albumDao.addMovieToAlbumWithCount(albumId, movieId, position)
    }
    
    // Remove movie from album
    suspend fun removeMovieFromAlbum(albumId: Int, movieId: Int) {
        albumDao.removeMovieFromAlbumWithCount(albumId, movieId)
    }
    
    // Check if movie in album
    suspend fun isMovieInAlbum(albumId: Int, movieId: Int): Boolean {
        return albumDao.isMovieInAlbum(albumId, movieId)
    }
    
    // Get albums containing a movie
    fun getAlbumsContainingMovie(movieId: Int): Flow<List<Album>> {
        return albumDao.getAlbumsContainingMovie(movieId)
    }
    
    /**
     * Seeds the database with sample albums if empty.
     */
    suspend fun seedDatabaseIfEmpty() {
        // We check if we need to seed
        var needToSeed = false
        
        val firstAlbum = albumDao.getAlbumById(1)
        if (firstAlbum == null) {
            needToSeed = true
            // Insert albums if missing
            sampleAlbums.forEach { album ->
                albumDao.insertAlbum(album)
            }
        } else {
            // Album exists, but check if connections are missing (e.g. after Movie reseed)
            val count = albumDao.getMovieCountInAlbum(1)
            if (count == 0) {
                // Connections likely lost, re-seed them
                needToSeed = true
            }
        }
        
        if (needToSeed) {
            // Add movies to albums (Idempotent if using OnConflictStrategy.REPLACE or careful logic)
            // Album 1: Must-Watch - add movies 1,2,3,4,5,6,7,8,9,10
            listOf(1,2,3,4,5,6,7,8,9,10).forEachIndexed { index, movieId ->
                albumDao.addMovieToAlbum(AlbumMovie(1, movieId, index))
            }
            albumDao.updateMovieCount(1, 10)
            
            // Album 2: Nolan Collection - add movies 3,4,5,11
            listOf(3,4,5,11).forEachIndexed { index, movieId ->
                albumDao.addMovieToAlbum(AlbumMovie(2, movieId, index))
            }
            albumDao.updateMovieCount(2, 4)
            
            // Album 3: Best of 2023 - add movies 3,7,8,9
            listOf(3,7,8,9).forEachIndexed { index, movieId ->
                albumDao.addMovieToAlbum(AlbumMovie(3, movieId, index))
            }
            albumDao.updateMovieCount(3, 4)
            
            // Album 4: Superhero Movies - add 1,5,7,12
            listOf(1,5,7,12).forEachIndexed { index, movieId ->
                albumDao.addMovieToAlbum(AlbumMovie(4, movieId, index))
            }
            albumDao.updateMovieCount(4, 4)
            
            // Album 5: Binge-worthy Series - add 16,17,18,19,20
            listOf(16,17,18,19,20).forEachIndexed { index, movieId ->
                albumDao.addMovieToAlbum(AlbumMovie(5, movieId, index))
            }
            albumDao.updateMovieCount(5, 5)
        }
    }
}
