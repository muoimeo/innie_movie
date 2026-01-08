package com.example.myapplication.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
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
import com.example.myapplication.data.local.entities.Review
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myapplication.ui.components.CommentBottomSheet

/**
 * ReviewDetailScreen - Shows full review content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    reviewId: Int,
    navController: NavController? = null
) {
    val context = LocalContext.current
    var review by remember { mutableStateOf<Review?>(null) }
    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(0) }
    var reviewerLikedMovie by remember { mutableStateOf(false) }
    
    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Comment bottom sheet state
    var showComments by remember { mutableStateOf(false) }
    var commentCount by remember { mutableIntStateOf(0) }
    
    // Database
    val db = remember { DatabaseProvider.getDatabase(context) }
    val reviewDao = remember { db.reviewDao() }
    val movieDao = remember { db.movieDao() }
    val likeRepository = remember { LikeRepository(db.likeDao()) }
    val userActivityRepository = remember { com.example.myapplication.data.repository.UserActivityRepository(db.userActivityDao()) }
    val userId = UserSessionManager.getUserId()
    
    // Load review and movie from database + log view for watch history
    LaunchedEffect(reviewId) {
        withContext(Dispatchers.IO) {
            review = reviewDao.getById(reviewId)
            review?.let { r ->
                movie = movieDao.getMovieById(r.movieId)
                isLiked = likeRepository.isLiked(userId, "review", reviewId)
                
                // Check if the REVIEWER liked the movie (not current user)
                reviewerLikedMovie = likeRepository.isLiked(r.authorId, "movie", r.movieId)
                
                // Log view for watch history
                userActivityRepository.logView(userId, "review", reviewId)
                
                // Load comment count
                commentCount = db.commentDao().countCommentsForContent("review", reviewId)
            }
        }
    }
    
    val scrollState = rememberScrollState()
    
    if (review == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = InnieGreen)
        }
        return
    }
    
    val reviewData = review!!
    val movieData = movie
    
    // Get display name for header
    val authorDisplayName = reviewData.authorId
        .replace("user_", "")
        .replace("guest_", "")
        .replaceFirstChar { it.uppercase() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(scrollState)
            ) {
                // === HERO BACKDROP with blur at bottom ===
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    // Movie backdrop
                    AsyncImage(
                        model = movieData?.backdropUrl ?: movieData?.posterUrl,
                        contentDescription = movieData?.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Blur overlay at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .blur(radius = 10.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.9f)
                                    )
                                )
                            )
                    )
                    
                    // Gradient overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )
                    
                    // Top bar - back button left, centered title
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Back button (just arrow, no "Reviews" text)
                        IconButton(
                            onClick = { navController?.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.4f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        // Centered title: "Username's Review"
                        Text(
                            text = "$authorDisplayName's Review",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                
                // === REVIEW CONTENT ===
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Author row - clickable to view profile
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            navController?.navigate(Screen.UserProfile.createRoute(reviewData.authorId))
                        }
                    ) {
                        // Author avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4A5568)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = reviewData.authorId.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // Author name (clickable)
                        Text(
                            text = authorDisplayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = InnieGreen  // Green to indicate clickable
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Movie title + year row with poster
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Movie title + year - flexbox layout to prevent overflow
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = movieData?.title ?: "Movie",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1A1A),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = movieData?.year?.toString() ?: "",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Green stars with half-star support + reviewer's heart
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                reviewData.rating?.let { rating ->
                                    // Convert float rating to half-star steps (0-10)
                                    val ratingSteps = (rating * 2).toInt()
                                    
                                    repeat(5) { starIndex ->
                                        val halfPoint = starIndex * 2 + 1
                                        val fullPoint = starIndex * 2 + 2
                                        
                                        Box(modifier = Modifier.size(18.dp)) {
                                            when {
                                                ratingSteps >= fullPoint -> {
                                                    // Full star - green
                                                    Icon(
                                                        imageVector = Icons.Filled.Star,
                                                        contentDescription = null,
                                                        tint = InnieGreen,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                                ratingSteps >= halfPoint -> {
                                                    // Half star - green left half
                                                    Box {
                                                        Icon(
                                                            imageVector = Icons.Outlined.StarOutline,
                                                            contentDescription = null,
                                                            tint = Color(0xFFB3B3B3),
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Icon(
                                                            imageVector = Icons.Filled.Star,
                                                            contentDescription = null,
                                                            tint = InnieGreen,
                                                            modifier = Modifier
                                                                .size(18.dp)
                                                                .graphicsLayer {
                                                                    clip = true
                                                                    shape = ReviewHalfStarShape()
                                                                }
                                                        )
                                                    }
                                                }
                                                else -> {
                                                    // Empty star
                                                    Icon(
                                                        imageVector = Icons.Outlined.StarOutline,
                                                        contentDescription = null,
                                                        tint = Color(0xFFB3B3B3),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                
                                // Heart icon - shows reviewer's like (NOT clickable)
                                Icon(
                                    imageVector = if (reviewerLikedMovie) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Reviewer liked this",
                                    tint = if (reviewerLikedMovie) Color.Red else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Review date
                            Text(
                                text = formatReviewTime(reviewData.createdAt),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        
                        // Movie poster - click to go to MoviePage
                        if (movieData != null) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(120.dp)
                                    .clickable {
                                        navController?.navigate(Screen.MoviePage.createRoute(movieData.id))
                                    },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                AsyncImage(
                                    model = movieData.posterUrl,
                                    contentDescription = movieData.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Review title (if exists)
                    reviewData.title?.let { title ->
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    // Review body
                    Text(
                        text = reviewData.body,
                        fontSize = 15.sp,
                        color = Color(0xFF333333),
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Justify
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Like section (user can like this review) - no snackbar per user request
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    val newState = likeRepository.toggleLike(userId, "review", reviewId)
                                    isLiked = newState
                                    if (newState) likeCount++ else likeCount--
                                }
                                // No snackbar for review likes per user request
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIKE?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${likeCount} likes",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Tab chips: Comments and Film only (no Activity) - comments chip NOT clickable now
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Comments chip - just shows count, not clickable
                        Box(
                            modifier = Modifier
                                .border(1.dp, InnieGreen, RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "$commentCount comments",
                                fontSize = 13.sp,
                                color = InnieGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Film chip - navigates to MoviePage
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    movieData?.let {
                                        navController?.navigate(Screen.MoviePage.createRoute(it.id))
                                    }
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Film",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Comments section header
                    Text(
                        text = "Comments",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Inline comments list - loaded from database
                    var comments by remember { mutableStateOf<List<com.example.myapplication.data.local.entities.Comment>>(emptyList()) }
                    var newCommentText by remember { mutableStateOf("") }
                    
                    // Load comments
                    LaunchedEffect(reviewId, commentCount) {
                        withContext(Dispatchers.IO) {
                            db.commentDao().getComments("review", reviewId).collect { list ->
                                withContext(Dispatchers.Main) {
                                    comments = list
                                }
                            }
                        }
                    }
                    
                    if (comments.isEmpty()) {
                        // Empty state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No comments yet",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Be the first to comment!",
                                    fontSize = 12.sp,
                                    color = Color.Gray.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        // State for showing all or limited comments
                        var showAllComments by remember { mutableStateOf(false) }
                        val displayedComments = if (showAllComments) comments else comments.take(5)
                        
                        // Display comments inline with improved spacing
                        displayedComments.forEach { comment ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                ReviewCommentItem(
                                    comment = comment,
                                    db = db
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        
                        // "See more comments" button if there are more than 5
                        if (comments.size > 5 && !showAllComments) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "See more comments (${comments.size - 5} more)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InnieGreen,
                                    modifier = Modifier.clickable { showAllComments = true }
                                )
                            }
                        } else if (showAllComments && comments.size > 5) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Show less",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray,
                                    modifier = Modifier.clickable { showAllComments = false }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Add comment input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newCommentText,
                            onValueChange = { newCommentText = it },
                            placeholder = { Text("Add a comment...", fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = InnieGreen,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        IconButton(
                            onClick = {
                                if (newCommentText.isNotBlank()) {
                                    scope.launch {
                                        withContext(Dispatchers.IO) {
                                            db.commentDao().insert(
                                                com.example.myapplication.data.local.entities.Comment(
                                                    userId = userId,
                                                    targetType = "review",
                                                    targetId = reviewId,
                                                    content = newCommentText.trim(),
                                                    createdAt = System.currentTimeMillis()
                                                )
                                            )
                                            // Refresh count
                                            commentCount = db.commentDao().countCommentsForContent("review", reviewId)
                                        }
                                        newCommentText = ""
                                    }
                                }
                            },
                            enabled = newCommentText.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = if (newCommentText.isNotBlank()) InnieGreen else Color.Gray
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

// Inline comment item for ReviewDetailScreen
@Composable
fun ReviewCommentItem(
    comment: com.example.myapplication.data.local.entities.Comment,
    db: com.example.myapplication.data.local.db.AppDatabase
) {
    var authorName by remember { mutableStateOf("") }
    var authorAvatar by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(comment.userId) {
        kotlinx.coroutines.withContext(Dispatchers.IO) {
            val user = db.userDao().getUserById(comment.userId)
            authorName = user?.displayName ?: user?.username ?: comment.userId.replace("user_", "").replaceFirstChar { it.uppercase() }
            authorAvatar = user?.avatarUrl
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar - load from URL or show initial
        if (authorAvatar != null) {
            coil.compose.AsyncImage(
                model = authorAvatar,
                contentDescription = authorName,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4A5568)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4A5568)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (authorName.firstOrNull() ?: comment.userId.first()).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(10.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = authorName.ifBlank { "User" },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatReviewTime(comment.createdAt).replace("Reviewed ", ""),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.content,
                fontSize = 13.sp,
                color = Color(0xFF333333),
                lineHeight = 18.sp
            )
        }
    }
}

// Half star shape for clipping
class ReviewHalfStarShape : Shape {
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

// Helper function for review time formatting
fun formatReviewTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val hours = diff / (1000 * 60 * 60)
    val days = hours / 24
    
    return when {
        hours < 1 -> "Just now"
        hours < 24 -> "Reviewed $hours hours ago"
        days < 7 -> "Reviewed $days days ago"
        days < 30 -> "Reviewed ${days / 7} weeks ago"
        else -> {
            val dateFormat = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.getDefault())
            "Reviewed ${dateFormat.format(java.util.Date(timestamp))}"
        }
    }
}
