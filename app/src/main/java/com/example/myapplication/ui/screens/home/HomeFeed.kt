package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.ReviewWithMovie
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.launch

@Composable
fun HomeFeed(
    username: String,
    movies: List<Movie> = emptyList(), // Movies from database
    onMovieClick: (Int) -> Unit,
    onAlbumClick: (Int) -> Unit,  // Changed to Int for album ID
    onReviewClick: (Int) -> Unit, // Changed to Int for review ID
    onProfileClick: (String) -> Unit,
    onSeeMoreReviews: () -> Unit = {},
    onSeeMoreAlbums: () -> Unit = {},  // New callback for navigating to AlbumFeed
    homeViewModel: HomeViewModel = viewModel()
) {
    val popularAlbums by homeViewModel.popularAlbums.collectAsState()
    val popularReviews by homeViewModel.popularReviews.collectAsState()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Greeting - trên 1 dòng
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Hello, ",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = username,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = InnieGreen
                        )
                    )
                    Text(
                        text = "!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
                Text(
                    text = "Review or track film you've watched...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }
        }

        // Popular Films Section
        item {
            SectionHeader(title = "Popular Films This Month")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Use database movies if available
                if (movies.isNotEmpty()) {
                    items(movies.take(10)) { movie ->
                        MoviePosterCardDb(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Popular Albums Section - from database (limited to 4, sorted by likes)
        item {
            SectionHeader(title = "Popular Albums This Month")
            if (popularAlbums.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No albums yet", color = Color.Gray)
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Limit to 4 albums
                    items(popularAlbums.take(4)) { album ->
                        AlbumCardDb(
                            album = album,
                            homeViewModel = homeViewModel,
                            onClick = { onAlbumClick(album.id) }
                        )
                    }
                    
                    // "See more in album" button
                    if (popularAlbums.size > 4) {
                        item {
                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0F0F0))
                                    .clickable { onSeeMoreAlbums() },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "See more",
                                        fontWeight = FontWeight.Bold,
                                        color = InnieGreen,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "in Album →",
                                        fontWeight = FontWeight.Bold,
                                        color = InnieGreen,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Popular Reviews Section - from database
        item {
            SectionHeader(title = "Popular Reviews")
        }

        if (popularReviews.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No reviews yet", color = Color.Gray)
                }
            }
        } else {
            // Limit to at most 4 reviews
            items(popularReviews.take(4)) { reviewWithMovie ->
                ReviewCardDb(
                    reviewWithMovie = reviewWithMovie,
                    homeViewModel = homeViewModel,
                    onClick = { onReviewClick(reviewWithMovie.review.id) },
                    onProfileClick = { onProfileClick(reviewWithMovie.review.authorId) }
                )
            }
            
            // "See more reviews" button
            if (popularReviews.size > 4) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "See more reviews >",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = InnieGreen
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = onSeeMoreReviews)
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

// Movie poster card using database Movie entity
@Composable
fun MoviePosterCardDb(
    movie: Movie,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Album card using database Album entity
@Composable
fun AlbumCardDb(
    album: Album,
    homeViewModel: HomeViewModel,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    
    // Load album data
    var ownerName by remember { mutableStateOf("") }
    var ownerAvatar by remember { mutableStateOf<String?>(null) }
    var likeCount by remember { mutableIntStateOf(0) }
    var commentCount by remember { mutableIntStateOf(0) }
    var albumMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    
    LaunchedEffect(album.id) {
        coroutineScope.launch {
            ownerName = homeViewModel.getAlbumOwnerName(album.ownerId)
            ownerAvatar = homeViewModel.getUserAvatar(album.ownerId)
            likeCount = homeViewModel.getAlbumLikeCount(album.id)
            commentCount = homeViewModel.getAlbumCommentCount(album.id)
            albumMovies = homeViewModel.getAlbumMovies(album.id)
        }
    }
    
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Album cover - show first movie poster or stacked posters
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                if (albumMovies.isNotEmpty()) {
                    // Show stacked movie posters (up to 4)
                    val posterWidth = 70.dp
                    val posterHeight = 100.dp
                    val overlapOffset = 20.dp
                    
                    // Show up to 4 movies stacked
                    albumMovies.take(4).forEachIndexed { index, movie ->
                        val reverseIndex = minOf(albumMovies.size, 4) - 1 - index
                        AsyncImage(
                            model = movie.posterUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(posterWidth)
                                .height(posterHeight)
                                .offset(x = overlapOffset * reverseIndex, y = 0.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                } else if (album.coverUrl != null) {
                    // Show album cover
                    AsyncImage(
                        model = album.coverUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    // Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No movies", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = InnieGreen,
                        lineHeight = 20.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Author + Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    ownerAvatar?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = ownerName.ifEmpty { "..." },
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color(0xFFE57373)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = likeCount.toString(),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = commentCount.toString(),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Review card using database ReviewWithMovie
@Composable
fun ReviewCardDb(
    reviewWithMovie: ReviewWithMovie,
    homeViewModel: HomeViewModel,
    onClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val review = reviewWithMovie.review
    val movie = reviewWithMovie.movie
    val coroutineScope = rememberCoroutineScope()
    var authorAvatar by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(review.authorId) {
        coroutineScope.launch {
            authorAvatar = homeViewModel.getUserAvatar(review.authorId)
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Avatar row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onProfileClick)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        authorAvatar?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${movie.title} (${movie.year ?: ""})",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Review by ",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                            Text(
                                text = review.authorId.removePrefix("user_"),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = InnieGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Rating stars
                            review.rating?.let { rating ->
                                repeat(rating.toInt()) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = InnieGreen
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = review.body,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Read more >",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = InnieGreen,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Movie poster
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
