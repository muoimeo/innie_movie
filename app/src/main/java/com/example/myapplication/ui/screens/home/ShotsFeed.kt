package com.example.myapplication.ui.screens.home

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.shotLocalVideoMap
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen
import com.example.myapplication.ui.components.CommentBottomSheet
import com.example.myapplication.ui.components.ShareBottomSheet
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShotsFeed(
    navController: NavController? = null,
    shotsViewModel: ShotsViewModel = viewModel()
) {
    val shots by shotsViewModel.shots.collectAsState()
    val isLoading by shotsViewModel.isLoading.collectAsState()
    val relatedMovies by shotsViewModel.relatedMovies.collectAsState()
    val isRefreshing by shotsViewModel.isRefreshing.collectAsState()
    
    if (isLoading || shots.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = InnieGreen)
        }
        return
    }
    
    // Use finite pager starting at 0 (can't scroll up past first video)
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { shots.size }
    )
    
    // Collect liked shots as state for reactivity
    val likedShotsSet by shotsViewModel.likedShots.collectAsState()
    
    // Pull-to-refresh state
    val pullToRefreshState = rememberPullToRefreshState()
    
    // Handle pull-to-refresh when at top
    LaunchedEffect(pullToRefreshState.distanceFraction) {
        if (pullToRefreshState.distanceFraction > 0 && pagerState.currentPage == 0) {
            // User is pulling down on first page
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { shotsViewModel.refresh() },
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val shot = shots[page]
                val relatedMovie = shot.relatedMovieId?.let { relatedMovies[it] }
                val localVideoResId = shotLocalVideoMap[shot.id]
                
                // Use collected state for reactivity
                val isLiked = likedShotsSet.contains(shot.id)
                
                // Comment state for this shot
                var showComments by remember { mutableStateOf(false) }
                
                // Share state for this shot
                var showShare by remember { mutableStateOf(false) }
                
                ShotItem(
                    shot = shot,
                    relatedMovie = relatedMovie,
                    isCurrentPage = pagerState.currentPage == page,
                    localVideoResId = localVideoResId,
                    isLiked = isLiked,
                    onLikeClick = { shotsViewModel.toggleLike(shot.id) },
                    onCommentClick = { showComments = true },
                    onShareClick = { showShare = true },
                    onMovieClick = { movieId ->
                        navController?.navigate(Screen.MoviePage.createRoute(movieId))
                    }
                )
                
                // Comment Bottom Sheet for this shot
                if (showComments) {
                    CommentBottomSheet(
                        targetType = "shot",
                        targetId = shot.id,
                        onDismiss = { showComments = false }
                    )
                }
                
                // Share Bottom Sheet for this shot
                if (showShare) {
                    ShareBottomSheet(
                        contentType = "shot",
                        contentId = shot.id,
                        contentTitle = shot.caption,
                        onDismiss = { showShare = false }
                    )
                }
            }
        }
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ShotItem(
    shot: Shot,
    relatedMovie: Movie?,
    isCurrentPage: Boolean,
    localVideoResId: Int? = null,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    var showMoreInfo by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var videoProgress by remember { mutableFloatStateOf(0f) }
    var isVideoReady by remember { mutableStateOf(false) }
    
    // ExoPlayer for local video
    val exoPlayer = remember(localVideoResId) {
        if (localVideoResId != null) {
            ExoPlayer.Builder(context).build().apply {
                val videoUri = Uri.parse("android.resource://${context.packageName}/$localVideoResId")
                setMediaItem(MediaItem.fromUri(videoUri))
                repeatMode = Player.REPEAT_MODE_ALL
                volume = 0f
                prepare()
            }
        } else null
    }
    
    // Track video progress
    LaunchedEffect(exoPlayer, isCurrentPage, isPaused) {
        if (exoPlayer != null && isCurrentPage && !isPaused) {
            while (true) {
                val duration = exoPlayer.duration
                val position = exoPlayer.currentPosition
                if (duration > 0) {
                    videoProgress = position.toFloat() / duration.toFloat()
                }
                delay(50)
            }
        }
    }
    
    // Player state listener
    LaunchedEffect(exoPlayer) {
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                isVideoReady = state == Player.STATE_READY
            }
        })
    }
    
    // Play/pause based on page visibility and pause state
    LaunchedEffect(isCurrentPage, isPaused) {
        if (exoPlayer != null) {
            if (isCurrentPage && !isPaused) {
                exoPlayer.play()
            } else {
                exoPlayer.pause()
            }
        }
    }
    
    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPaused = !isPaused
            }
    ) {
        // Video player OR thumbnail
        if (exoPlayer != null) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            
            // Thumbnail fallback while video loading
            if (!isVideoReady) {
                AsyncImage(
                    model = shot.thumbnailUrl,
                    contentDescription = shot.caption,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // No video - just thumbnail
            AsyncImage(
                model = shot.thumbnailUrl,
                contentDescription = shot.caption,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Pause icon overlay (center)
        AnimatedVisibility(
            visible = isPaused,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        
        // Gradient overlay at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
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
            ShotActionButton(
                icon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                count = shot.likeCount,
                tint = if (isLiked) Color(0xFFEC2626) else Color.White,
                onClick = onLikeClick
            )
            
            ShotActionButton(
                icon = Icons.Outlined.ChatBubbleOutline,
                count = shot.commentCount,
                onClick = onCommentClick
            )
            
            ShotActionButton(
                icon = Icons.Default.Share,
                count = shot.shareCount,
                onClick = onShareClick
            )
        }
        
        // Bottom content - ABOVE bottom nav (padding bottom = 80dp)
        if (!showMoreInfo) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, end = 70.dp, bottom = 80.dp)
            ) {
                // Title with "... more" button
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = shot.caption,
                        color = Color.White,
                        fontSize = 16.sp,
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
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Progress bar - real progress for video, static for image
                VideoProgressBar(
                    progress = if (exoPlayer != null) videoProgress else 0.35f,
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
                shot = shot,
                relatedMovie = relatedMovie,
                onDismiss = { showMoreInfo = false },
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
fun ShotActionButton(
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
    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = modifier
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        val progressWidth = maxWidth * progress.coerceIn(0f, 1f)
        
        // Progress fill (green line)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(progressWidth)
                .background(InnieGreen)
        )
        
        // Progress dot at end of green line
        if (progress > 0.01f) {
            Box(
                modifier = Modifier
                    .offset(x = progressWidth - 5.dp) // Center the 10dp dot on end of line
                    .size(10.dp)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun MoreInfoOverlay(
    shot: Shot,
    relatedMovie: Movie?,
    onDismiss: () -> Unit,
    onMovieClick: (Int) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onDismiss() }
                )
            }
    ) {
        // Content column - clicks will be handled by children, not consumed by parent
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, end = 12.dp, bottom = 100.dp)
        ) {
            Text(
                text = shot.caption,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = shot.description ?: "",
                color = Color(0xFFB3B3B3),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
            
            // Related movie card - clickable to navigate to MoviePage
            relatedMovie?.let { movie ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.3f))
                        .padding(8.dp)
                        .pointerInput(movie.id) {
                            detectTapGestures(
                                onTap = { onMovieClick(movie.id) }
                            )
                        }
                ) {
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(59.dp)
                            .height(87.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Gray)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = movie.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row {
                            Text(
                                text = if (movie.mediaType == "series") "Series" else "Movie",
                                color = Color(0xFFB3B3B3),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = movie.year?.toString() ?: "",
                                color = Color(0xFFB3B3B3),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
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
                                    text = String.format("%.1f", movie.rating),
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

fun formatShotCount(count: Int): String {
    return when {
        count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
        count >= 1000 -> String.format("%.1fk", count / 1000.0)
        else -> count.toString()
    }
}
