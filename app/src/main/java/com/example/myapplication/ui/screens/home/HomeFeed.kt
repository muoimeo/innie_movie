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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.ui.theme.InnieGreen

// Fake data models
data class FakeMovie(
    val id: String,
    val title: String,
    val posterRes: Int
)

data class FakeAlbum(
    val id: String,
    val title: String,
    val author: String,
    val posterRes: Int,
    val likeCount: Int,
    val commentCount: Int
)

data class FakeReview(
    val id: String,
    val movieTitle: String,
    val movieYear: String,
    val authorName: String,
    val rating: Float,
    val reviewText: String,
    val posterRes: Int
)

// Fake data
val fakeMovies = listOf(
    FakeMovie("1", "Inception", R.drawable.onboarding_bg),
    FakeMovie("2", "The Dark Knight", R.drawable.onboarding_bg),
    FakeMovie("3", "Interstellar", R.drawable.onboarding_bg),
    FakeMovie("4", "The French Dispatch", R.drawable.onboarding_bg),
    FakeMovie("5", "The Power", R.drawable.onboarding_bg),
    FakeMovie("6", "Don't Look Up", R.drawable.onboarding_bg)
)

val fakeAlbums = listOf(
    FakeAlbum("1", "Life-Changing Movies", "Alejandro", R.drawable.onboarding_bg, 42, 12),
    FakeAlbum("2", "100 Best Thriller Movies", "Wendy", R.drawable.onboarding_bg, 128, 34),
    FakeAlbum("3", "Comfort Movies To Watch", "Ruth", R.drawable.onboarding_bg, 89, 21)
)

val fakeReviews = listOf(
    FakeReview(
        "1", "The Irishman", "2019", "Adrian", 4.5f,
        "not sure i've ever mentioned this before but i have a very personal fear of not... feeling... correctly. like enormously important things are happening around you in a matter-of-fact, dissociative way that you can understand the significance of but you can't shake...",
        R.drawable.onboarding_bg
    ),
    FakeReview(
        "2", "Zack Snyder's Justice League", "2021", "Audrey", 4.0f,
        "the interesting thing about snyder is that he always swings big. whether it results in a colossal whiff or a home run just depends on the particular project, amount of creative control, and audience reception...",
        R.drawable.onboarding_bg
    ),
    FakeReview(
        "3", "tick, tick...BOOM!", "2021", "Rebecca", 3.0f,
        "Andrew Garfield gives an incredible performance in this deeply personal story...",
        R.drawable.onboarding_bg
    )
)

@Composable
fun HomeFeed(
    username: String,
    movies: List<Movie> = emptyList(), // Movies from database
    onMovieClick: (Int) -> Unit,
    onAlbumClick: (String) -> Unit,
    onReviewClick: (String) -> Unit,
    onProfileClick: (String) -> Unit
) {
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
                // Use database movies if available, fallback to fakeMovies
                if (movies.isNotEmpty()) {
                    items(movies.take(10)) { movie ->
                        MoviePosterCardDb(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                } else {
                    items(fakeMovies) { movie ->
                        MoviePosterCard(
                            movie = movie,
                            onClick = { onMovieClick(movie.posterRes) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Popular Albums Section
        item {
            SectionHeader(title = "Popular Albums This Month")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(fakeAlbums) { album ->
                    AlbumCard(
                        album = album,
                        onClick = { onAlbumClick(album.id) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Recent Friends' Review Section
        item {
            SectionHeader(title = "Recent Friends' Review")
        }

        items(fakeReviews) { review ->
            ReviewCard(
                review = review,
                onClick = { onReviewClick(review.id) },
                onProfileClick = { onProfileClick(review.authorName) }
            )
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

@Composable
fun MoviePosterCard(
    movie: FakeMovie,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = movie.posterRes),
        contentDescription = movie.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(100.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    )
}

// New composable for database movies with Coil
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

@Composable
fun AlbumCard(
    album: FakeAlbum,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Stacked posters - 4 ảnh xếp ngang từ trái sang phải, cùng độ cao
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                val posterWidth = 70.dp
                val posterHeight = 100.dp
                val overlapOffset = 20.dp
                
                // Poster 4 (Dưới cùng - Bên phải)
                Image(
                    painter = painterResource(id = album.posterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(posterWidth)
                        .height(posterHeight)
                        .offset(x = overlapOffset * 3, y = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                // Poster 3
                Image(
                    painter = painterResource(id = album.posterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(posterWidth)
                        .height(posterHeight)
                        .offset(x = overlapOffset * 2, y = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                // Poster 2
                Image(
                    painter = painterResource(id = album.posterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(posterWidth)
                        .height(posterHeight)
                        .offset(x = overlapOffset * 1, y = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                // Poster 1 (Trên cùng - Bên trái)
                Image(
                    painter = painterResource(id = album.posterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(posterWidth)
                        .height(posterHeight)
                        .offset(x = 0.dp, y = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Phần 1: Title - Tăng chiều cao để đủ 2 dòng
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Tăng từ 40dp lên 48dp để chắc chắn đủ 2 dòng
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

            // Phần 2: Author + Stats - chiều cao cố định
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
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = album.author,
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
                    text = album.likeCount.toString(),
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
                    text = album.commentCount.toString(),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ReviewCard(
    review: FakeReview,
    onClick: () -> Unit,
    onProfileClick: () -> Unit
) {
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
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${review.movieTitle} ${review.movieYear}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Review by ",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                            Text(
                                text = review.authorName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = InnieGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Rating stars
                            repeat(review.rating.toInt()) {
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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = review.reviewText,
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
            Image(
                painter = painterResource(id = review.posterRes),
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
