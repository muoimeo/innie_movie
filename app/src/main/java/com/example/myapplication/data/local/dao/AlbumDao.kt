package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.AlbumMovie
import com.example.myapplication.data.local.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    
    // === Album CRUD ===
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album): Long
    
    @Update
    suspend fun updateAlbum(album: Album)
    
    @Delete
    suspend fun deleteAlbum(album: Album)
    
    @Query("DELETE FROM albums WHERE id = :albumId")
    suspend fun deleteAlbumById(albumId: Int)
    
    // Query albums
    @Query("SELECT * FROM albums WHERE id = :albumId")
    suspend fun getAlbumById(albumId: Int): Album?
    
    @Query("SELECT * FROM albums WHERE id = :albumId")
    fun getAlbumByIdFlow(albumId: Int): Flow<Album?>
    
    @Query("SELECT * FROM albums ORDER BY createdAt DESC")
    fun getAllAlbums(): Flow<List<Album>>
    
    @Query("SELECT * FROM albums WHERE ownerId = :userId ORDER BY createdAt DESC")
    fun getAlbumsByUser(userId: String): Flow<List<Album>>
    
    @Query("SELECT * FROM albums WHERE privacy = 'public' ORDER BY createdAt DESC")
    fun getPublicAlbums(): Flow<List<Album>>
    
    // Count albums created by user
    @Query("SELECT COUNT(*) FROM albums WHERE ownerId = :userId")
    fun countAlbumsByUser(userId: String): Flow<Int>
    
    // Search albums
    @Query("SELECT * FROM albums WHERE title LIKE '%' || :query || '%' ORDER BY title")
    fun searchAlbums(query: String): Flow<List<Album>>
    
    // === AlbumMovie junction operations ===
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToAlbum(albumMovie: AlbumMovie)
    
    @Query("DELETE FROM album_movies WHERE albumId = :albumId AND movieId = :movieId")
    suspend fun removeMovieFromAlbum(albumId: Int, movieId: Int)
    
    @Query("DELETE FROM album_movies WHERE albumId = :albumId")
    suspend fun clearAlbum(albumId: Int)
    
    // Get movies in album (ordered by position)
    @Query("""
        SELECT m.* FROM movies m
        INNER JOIN album_movies am ON m.id = am.movieId
        WHERE am.albumId = :albumId
        ORDER BY am.position
    """)
    fun getMoviesInAlbum(albumId: Int): Flow<List<Movie>>
    
    @Query("""
        SELECT m.* FROM movies m
        INNER JOIN album_movies am ON m.id = am.movieId
        WHERE am.albumId = :albumId
        ORDER BY am.position
    """)
    suspend fun getMoviesInAlbumSync(albumId: Int): List<Movie>
    
    // Get albums containing a movie
    @Query("""
        SELECT a.* FROM albums a
        INNER JOIN album_movies am ON a.id = am.albumId
        WHERE am.movieId = :movieId
        ORDER BY a.title
    """)
    fun getAlbumsContainingMovie(movieId: Int): Flow<List<Album>>
    
    // Check if movie is in album
    @Query("SELECT COUNT(*) > 0 FROM album_movies WHERE albumId = :albumId AND movieId = :movieId")
    suspend fun isMovieInAlbum(albumId: Int, movieId: Int): Boolean
    
    // Get movie count in album
    @Query("SELECT COUNT(*) FROM album_movies WHERE albumId = :albumId")
    suspend fun getMovieCountInAlbum(albumId: Int): Int
    
    // Update movie count cache
    @Query("UPDATE albums SET movieCount = :count, updatedAt = :updatedAt WHERE id = :albumId")
    suspend fun updateMovieCount(albumId: Int, count: Int, updatedAt: Long = System.currentTimeMillis())
    
    // Transaction to add movie and update count
    @Transaction
    suspend fun addMovieToAlbumWithCount(albumId: Int, movieId: Int, position: Int = 0) {
        addMovieToAlbum(AlbumMovie(albumId, movieId, position))
        val count = getMovieCountInAlbum(albumId)
        updateMovieCount(albumId, count)
    }
    
    @Transaction
    suspend fun removeMovieFromAlbumWithCount(albumId: Int, movieId: Int) {
        removeMovieFromAlbum(albumId, movieId)
        val count = getMovieCountInAlbum(albumId)
        updateMovieCount(albumId, count)
    }
}
