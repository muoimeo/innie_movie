package com.example.myapplication.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.InnieGreen

// Data class for Shot video
data class ShotVideo(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailRes: Int,
    val likeCount: Int,
    val commentCount: Int,
    val shareCount: Int,
    val progress: Float, // 0.0 to 1.0
    val relatedMovie: RelatedMovie? = null,
    val isLiked: Boolean = false
)

data class RelatedMovie(
    val title: String,
    val type: String,
    val year: String,
    val rating: Float,
    val posterRes: Int
)

// Fake shots data
val fakeShotsVideos = listOf(
    ShotVideo(
        id = "1",
        title = "Meet the Cast of Stranger Things at the Premiere Event",
        description = "A red-carpet reunion as the Stranger Things cast kicks off the promo tour for the highly anticipated Season 5.",
        thumbnailRes = R.drawable.onboarding_bg,
        likeCount = 6777,
        commentCount = 211,
        shareCount = 1776,
        progress = 0.31f,
        relatedMovie = RelatedMovie(
            title = "Stranger Things",
            type = "TV Series",
            year = "2016 - 2025",
            rating = 4.0f,
            posterRes = R.drawable.onboarding_bg
        )
    ),
    ShotVideo(
        id = "2",
        title = "Behind the Scenes: Dune Part Two Desert Filming",
        description = "Discover how Denis Villeneuve recreated the iconic desert landscapes of Arrakis in this exclusive behind-the-scenes footage.",
        thumbnailRes = R.drawable.onboarding_bg,
        likeCount = 12340,
        commentCount = 567,
        shareCount = 3200,
        progress = 0.65f,
        relatedMovie = RelatedMovie(
            title = "Dune: Part Two",
            type = "Movie",
            year = "2024",
            rating = 4.5f,
            posterRes = R.drawable.onboarding_bg
        )
    ),
    ShotVideo(
        id = "3",
        title = "Marvel's Secret Wars: First Look at the Multiverse",
        description = "The first glimpse of the upcoming Avengers Secret Wars movie, featuring heroes from across the multiverse.",
        thumbnailRes = R.drawable.onboarding_bg,
        likeCount = 45000,
        commentCount = 2890,
        shareCount = 8900,
        progress = 0.12f,
        relatedMovie = RelatedMovie(
            title = "Avengers: Secret Wars",
            type = "Movie",
            year = "2027",
            rating = 0f,
            posterRes = R.drawable.onboarding_bg
        )
    )
)

@Composable
fun ShotsFeed() {
    // Infinite loop: use very large page count and start in the middle
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )
    
    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        // Use modulo to cycle through the actual videos
        val actualIndex = page % fakeShotsVideos.size
        ShotVideoItem(
            video = fakeShotsVideos[actualIndex]
        )
    }
}

@Composable
fun ShotVideoItem(
    video: ShotVideo
) {
    var isLiked by remember { mutableStateOf(video.isLiked) }
    var showMoreInfo by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Video/Image (full screen)
        Image(
            painter = painterResource(id = video.thumbnailRes),
            contentDescription = video.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlay at bottom for better text visibility
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        
        // Right sidebar with actions
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like button
            ActionButton(
                icon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                count = video.likeCount,
                tint = if (isLiked) Color(0xFFEC2626) else Color.White,
                onClick = { isLiked = !isLiked }
            )
            
            // Comment button
            ActionButton(
                icon = Icons.Outlined.ChatBubbleOutline,
                count = video.commentCount,
                onClick = { /* TODO: Open comments */ }
            )
            
            // Share button
            ActionButton(
                icon = Icons.Default.Share,
                count = video.shareCount,
                onClick = { /* TODO: Share */ }
            )
        }
        
        // Bottom content: Title + Progress bar (hidden when overlay is shown)
        if (!showMoreInfo) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, end = 70.dp, bottom = 70.dp)
            ) {
                // Title with "... more" button
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = video.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "... more",
                        color = Color(0xFFB3B3B3),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showMoreInfo = true }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress bar
                VideoProgressBar(
                    progress = video.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // "More Info" overlay
        AnimatedVisibility(
            visible = showMoreInfo,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            MoreInfoOverlay(
                video = video,
                onDismiss = { showMoreInfo = false }
            )
        }
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    count: Int,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = formatShotCount(count),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VideoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(InnieGreen)
        )
        // Progress indicator circle
        Box(
            modifier = Modifier
                .size(8.dp)
                .align(Alignment.CenterStart)
                .padding(start = (progress * 100).dp.coerceAtMost(280.dp))
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Composable
fun MoreInfoOverlay(
    video: ShotVideo,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, end = 12.dp, bottom = 120.dp)
        ) {
            // Full title
            Text(
                text = video.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Description
            Text(
                text = video.description,
                color = Color(0xFFB3B3B3),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Related content header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Related content",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Related content",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Related movie card
            video.relatedMovie?.let { movie ->
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    // Movie poster
                    Image(
                        painter = painterResource(id = movie.posterRes),
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(59.dp)
                            .height(87.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        // Movie title
                        Text(
                            text = movie.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Type and year
                        Row {
                            Text(
                                text = movie.type,
                                color = Color(0xFFB3B3B3),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = movie.year,
                                color = Color(0xFFB3B3B3),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Rating stars
                        if (movie.rating > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = if (index < movie.rating.toInt()) 
                                            Icons.Filled.Star 
                                        else 
                                            Icons.Outlined.StarOutline,
                                        contentDescription = null,
                                        tint = if (index < movie.rating.toInt()) 
                                            InnieGreen 
                                        else 
                                            Color.White.copy(alpha = 0.3f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(1.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = movie.rating.toString(),
                                    color = InnieGreen,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper function to format counts
fun formatShotCount(count: Int): String {
    return when {
        count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
        count >= 1000 -> String.format("%.1fk", count / 1000.0)
        else -> count.toString()
    }
}
