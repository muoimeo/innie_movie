package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.ReviewWithMovie
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.flow.collectLatest

/**
 * ReviewsScreen - Shows user's reviews with modern UI connected to database
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val reviewRepository = remember { ReviewRepository(db.reviewDao()) }
    val userId = UserSessionManager.getUserId()
    
    var reviews by remember { mutableStateOf<List<ReviewWithMovie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Load user's reviews from database
    LaunchedEffect(userId) {
        reviewRepository.getReviewsByUserWithMovies(userId).collectLatest { list ->
            reviews = list
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Reviews", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = InnieGreen,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (reviews.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.RateReview,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No reviews yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Write a review for a movie\nto see it here",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Reviews list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Stats header
                    item {
                        ReviewsStatsHeader(reviewCount = reviews.size)
                    }
                    
                    items(reviews) { reviewWithMovie ->
                        UserReviewCard(
                            reviewWithMovie = reviewWithMovie,
                            onClick = {
                                navController.navigate(Screen.ReviewDetail.createRoute(reviewWithMovie.review.id))
                            },
                            onMovieClick = {
                                navController.navigate(Screen.MoviePage.createRoute(reviewWithMovie.movie.id))
                            }
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewsStatsHeader(reviewCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = reviewCount.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = InnieGreen
                )
                Text(
                    text = "Total Reviews",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            VerticalDivider(
                modifier = Modifier.height(50.dp),
                color = InnieGreen.copy(alpha = 0.3f)
            )
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.RateReview,
                    contentDescription = null,
                    tint = InnieGreen,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "Your Reviews",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun UserReviewCard(
    reviewWithMovie: ReviewWithMovie,
    onClick: () -> Unit,
    onMovieClick: () -> Unit
) {
    val review = reviewWithMovie.review
    val movie = reviewWithMovie.movie
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Movie poster
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(70.dp)
                    .height(100.dp)
                    .clickable { onMovieClick() },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Review content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Movie title + year
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    movie.year?.let { year ->
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = year.toString(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Rating stars
                review.rating?.let { rating ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val ratingSteps = (rating * 2).toInt()
                        repeat(5) { starIndex ->
                            val halfPoint = starIndex * 2 + 1
                            val fullPoint = starIndex * 2 + 2
                            
                            Box(modifier = Modifier.size(14.dp)) {
                                when {
                                    ratingSteps >= fullPoint -> {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = InnieGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    ratingSteps >= halfPoint -> {
                                        Box {
                                            Icon(
                                                imageVector = Icons.Outlined.StarOutline,
                                                contentDescription = null,
                                                tint = Color(0xFFB3B3B3),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = null,
                                                tint = InnieGreen,
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .graphicsLayer {
                                                        clip = true
                                                        shape = ProfileReviewHalfStarShape()
                                                    }
                                            )
                                        }
                                    }
                                    else -> {
                                        Icon(
                                            imageVector = Icons.Outlined.StarOutline,
                                            contentDescription = null,
                                            tint = Color(0xFFB3B3B3),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format("%.1f", rating),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = InnieGreen
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Review title if exists
                review.title?.let { title ->
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                
                // Review body snippet
                Text(
                    text = review.body,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Date
                Text(
                    text = formatReviewDate(review.createdAt),
                    fontSize = 10.sp,
                    color = InnieGreen
                )
            }
            
            // Chevron
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View",
                tint = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(24.dp)
            )
        }
    }
}

private fun formatReviewDate(timestamp: Long): String {
    val dateFormat = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.getDefault())
    return dateFormat.format(java.util.Date(timestamp))
}

// Half star shape for clipping
private class ProfileReviewHalfStarShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(0f, 0f, size.width / 2f, size.height)
        )
    }
}