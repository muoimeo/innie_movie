package com.example.myapplication.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.screens.home.AlbumViewModel
import com.example.myapplication.ui.theme.InnieGreen
import com.example.myapplication.ui.components.CommentBottomSheet
import java.text.SimpleDateFormat
import java.util.*

/**
 * Album Detail Screen - Designed per Figma
 */
@Composable
fun AlbumDetailScreen(
    albumId: Int,
    navController: NavController,
    viewModel: AlbumViewModel = viewModel()
) {
    val album by viewModel.selectedAlbum.collectAsState()
    val movies by viewModel.albumMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLiked by viewModel.isLiked.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    
    // Collect stats from ViewModel
    val viewCount by viewModel.viewCount.collectAsState()
    val likeCount by viewModel.likeCount.collectAsState()
    val commentCount by viewModel.commentCount.collectAsState()
    
    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Comment bottom sheet state
    var showComments by remember { mutableStateOf(false) }
    
    // Load album data when screen opens
    LaunchedEffect(albumId) {
        viewModel.loadAlbumDetail(albumId)
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = InnieGreen
            )
        } else if (album != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Fixed white top bar
                AlbumTopBar(onBackClick = { navController.popBackStack() })
                
                // Scrollable content below top bar
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Album cover image with blur at bottom
                    item {
                        AlbumCoverSection(album = album!!)
                    }
                    
                    // Author & Title Section
                    item {
                        val currentUserId = com.example.myapplication.data.session.UserSessionManager.getUserId()
                        AlbumInfoSection(
                            album = album!!,
                            isSaved = isSaved,
                            isOwnAlbum = album!!.ownerId == currentUserId,
                            onSaveClick = {
                                viewModel.toggleSave()
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (!isSaved) "Added to Albums" else "Removed from Albums",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                    
                    // Description
                    item {
                        Text(
                            text = album!!.description ?: "No description available.",
                            fontSize = 12.sp,
                            color = Color(0xFF1A202C).copy(alpha = 0.79f),
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    // Stats Row (Views, Likes, Comments)
                    item {
                        AlbumStatsRow(
                            viewCount = viewCount,
                            likeCount = likeCount,
                            commentCount = commentCount,
                            isLiked = isLiked,
                            onLikeClick = {
                                viewModel.toggleLike()
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (!isLiked) "Added to Likes" else "Removed from Likes",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            onCommentClick = { showComments = true }
                        )
                    }
                    
                    // Movies Grid
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        if (movies.isEmpty()) {
                            Text(
                                text = "No films in this album yet",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        } else {
                            MoviesGrid(
                                movies = movies,
                                onMovieClick = { movie ->
                                    navController.navigate(Screen.MoviePage.createRoute(movie.id))
                                }
                            )
                        }
                    }
                    
                    // Leave a comment section
                    item {
                        LeaveCommentSection()
                    }
                    
                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        } else {
            // Album not found
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Album not found",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = InnieGreen)
                ) {
                    Text("Go Back")
                }
            }
        }
    }
    } // Close Scaffold
    
    // Comment Bottom Sheet
    if (showComments) {
        CommentBottomSheet(
            targetType = "album",
            targetId = albumId,
            onDismiss = { showComments = false }
        )
    }
}

// Fix: White top bar WITHOUT statusBarsPadding to be truly fixed
@Composable
fun AlbumTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF1A202C),
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "Albums",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A202C)
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Placeholder for settings icon
        Box(modifier = Modifier.size(32.dp))
    }
}

// Fix #5: Album cover with blur effect at bottom
@Composable
fun AlbumCoverSection(album: Album) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(204.dp)
    ) {
        // Cover image
        AsyncImage(
            model = album.coverUrl,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        )
        
        // Blur/gradient fade at bottom (like OnBoardingScreen but shorter)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFF8F9FA).copy(alpha = 0.95f)
                        )
                    )
                )
        )
    }
}

@Composable
fun AlbumInfoSection(
    album: Album,
    isSaved: Boolean = false,
    isOwnAlbum: Boolean = false,
    onSaveClick: () -> Unit = {}
) {
    // Format author name from ownerId (e.g., "user_marquee" -> "Marquee")
    val authorName = album.ownerId
        .replace("user_", "")
        .replace("guest_", "")
        .replaceFirstChar { it.uppercase() }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Author row with avatar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Author avatar
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(InnieGreen)
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            // Author display name (cleaned up)
            Text(
                text = authorName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A202C)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Published date formatted properly
            Text(
                text = formatDate(album.createdAt),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB3B3B3)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Title row with Add to Albums button (only show for other people's albums)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = album.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A202C),
                modifier = Modifier.weight(1f)
            )
            
            // Add/Remove from Albums button - hide for own albums
            if (!isOwnAlbum) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSaveClick() }
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.Add,
                        contentDescription = if (isSaved) "Remove from Albums" else "Add to Albums",
                        tint = if (isSaved) InnieGreen else Color(0xFF1E1E1E),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isSaved) "Saved" else "Add to Albums",
                        fontSize = 8.sp,
                        color = InnieGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Fix #4: Helper to format timestamp to readable date
fun formatDate(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        "Published ${sdf.format(Date(timestamp))}"
    } catch (e: Exception) {
        ""
    }
}

// Fix #6: Heart icon is now outline (FavoriteBorder) and clickable
@Composable
fun AlbumStatsRow(
    viewCount: Int = 0,
    likeCount: Int = 0,
    commentCount: Int = 0,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {}
) {
    // Format counts for display
    fun formatCount(count: Int): String {
        return when {
            count >= 1000000 -> "${count / 1000000}M"
            count >= 1000 -> "${count / 1000}k"
            else -> count.toString()
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            icon = Icons.Outlined.RemoveRedEye,
            count = formatCount(viewCount),
            tint = InnieGreen
        )
        // Clickable heart with filled icon when liked
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onLikeClick() }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = if (isLiked) Color.Red else Color(0xFF1A202C),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCount(likeCount),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A202C)
            )
        }
        // Comment icon - clickable
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onCommentClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = Color(0xFF1A202C),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCount(commentCount),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A202C)
            )
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, count: String, tint: Color = Color(0xFF1A202C)) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A202C)
        )
    }
}

@Composable
fun MoviesGrid(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    // Non-scrollable grid inside LazyColumn
    val rows = movies.chunked(3)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 11.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Extra space for rank badges
    ) {
        rows.forEachIndexed { rowIndex, rowMovies ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowMovies.forEachIndexed { colIndex, movie ->
                    val rankNumber = rowIndex * 3 + colIndex + 1
                    MoviePosterWithRank(
                        movie = movie,
                        rank = rankNumber,
                        onClick = { onMovieClick(movie) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty spaces if row has less than 3 items
                repeat(3 - rowMovies.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MoviePosterWithRank(
    movie: Movie,
    rank: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Use Column to stack poster and badge so badge isn't clipped
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.67f)
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onClick)
        ) {
            // Poster image
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }
        
        // Rank badge OUTSIDE the clipped box to prevent cutting
        Box(
            modifier = Modifier
                .offset(y = (-12).dp) // Overlap poster from below
                .size(25.dp)
                .background(InnieGreen, CircleShape)
                .border(1.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun LeaveCommentSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ChatBubbleOutline,
            contentDescription = null,
            tint = Color(0xFF1A202C),
            modifier = Modifier.size(17.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Leave a comment?",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A202C)
        )
    }
}
