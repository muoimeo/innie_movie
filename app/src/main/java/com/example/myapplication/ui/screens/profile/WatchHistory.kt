package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.UserActivity
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Review
import com.example.myapplication.data.repository.UserActivityRepository
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.data.repository.NewsRepository
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen

/**
 * Watch History data class
 */
sealed class WatchedContent {
    abstract val id: Int
    abstract val title: String
    abstract val imageUrl: String
    abstract val type: String
    abstract val watchedAt: Long
    
    data class WatchedMovie(val movie: Movie, override val watchedAt: Long) : WatchedContent() {
        override val id = movie.id
        override val title = movie.title
        override val imageUrl = movie.posterUrl ?: ""
        override val type = "Film"
    }
    
    data class WatchedNews(val news: News, override val watchedAt: Long) : WatchedContent() {
        override val id = news.id
        override val title = news.title
        override val imageUrl = news.imageUrl ?: ""
        override val type = "News"
    }
    
    data class WatchedAlbum(val album: Album, override val watchedAt: Long) : WatchedContent() {
        override val id = album.id
        override val title = album.title
        override val imageUrl = album.coverUrl ?: ""
        override val type = "Album"
    }
    
    data class WatchedReview(
        val review: Review,
        val movieTitle: String,
        val authorName: String,
        override val watchedAt: Long
    ) : WatchedContent() {
        override val id = review.id
        override val title = "Review: $movieTitle"
        override val imageUrl = ""
        override val type = "Review"
        val reviewBody: String = review.body
        val rating: Float? = review.rating
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WatchHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val userActivityRepository = remember { UserActivityRepository(db.userActivityDao()) }
    val movieRepository = remember { MovieRepository(db.movieDao()) }
    val newsRepository = remember { NewsRepository(db.newsDao()) }
    val albumRepository = remember { AlbumRepository(db.albumDao()) }
    val reviewRepository = remember { ReviewRepository(db.reviewDao()) }
    val userId = UserSessionManager.getUserId()
    
    var selectedFilter by remember { mutableStateOf("Films") }
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Watched content
    var watchedFilms by remember { mutableStateOf<List<WatchedContent.WatchedMovie>>(emptyList()) }
    var watchedNews by remember { mutableStateOf<List<WatchedContent.WatchedNews>>(emptyList()) }
    var watchedAlbums by remember { mutableStateOf<List<WatchedContent.WatchedAlbum>>(emptyList()) }
    var watchedReviews by remember { mutableStateOf<List<WatchedContent.WatchedReview>>(emptyList()) }
    
    val filters = listOf("Films", "News", "Reviews", "Albums")
    
    // Load watch history from database
    LaunchedEffect(userId) {
        userActivityRepository.getViewHistory(userId).collect { activities ->
            val films = mutableListOf<WatchedContent.WatchedMovie>()
            val news = mutableListOf<WatchedContent.WatchedNews>()
            val albums = mutableListOf<WatchedContent.WatchedAlbum>()
            val reviews = mutableListOf<WatchedContent.WatchedReview>()
            
            // Group by targetId to avoid duplicates
            val uniqueActivities = activities
                .groupBy { "${it.targetType}_${it.targetId}" }
                .mapValues { it.value.maxByOrNull { a -> a.createdAt }!! }
                .values
                .sortedByDescending { it.createdAt }
            
            for (activity in uniqueActivities) {
                when (activity.targetType) {
                    "movie" -> {
                        movieRepository.getMovieById(activity.targetId)?.let { movie ->
                            films.add(WatchedContent.WatchedMovie(movie, activity.createdAt))
                        }
                    }
                    "news" -> {
                        newsRepository.getNewsById(activity.targetId)?.let { n ->
                            news.add(WatchedContent.WatchedNews(n, activity.createdAt))
                        }
                    }
                    "album" -> {
                        albumRepository.getAlbumById(activity.targetId)?.let { a ->
                            albums.add(WatchedContent.WatchedAlbum(a, activity.createdAt))
                        }
                    }
                    "review" -> {
                        reviewRepository.getById(activity.targetId)?.let { r ->
                            val movie = movieRepository.getMovieById(r.movieId)
                            reviews.add(WatchedContent.WatchedReview(
                                r,
                                movie?.title ?: "Unknown",
                                r.authorId.replace("user_", "").replace("guest_", ""),
                                activity.createdAt
                            ))
                        }
                    }
                }
            }
            
            watchedFilms = films
            watchedNews = news
            watchedAlbums = albums
            watchedReviews = reviews
            isLoading = false
        }
    }
    
    // Get current display items based on filter and search
    val displayItems: List<WatchedContent> = remember(selectedFilter, searchQuery, watchedFilms, watchedNews, watchedAlbums, watchedReviews) {
        val items = when (selectedFilter) {
            "Films" -> watchedFilms
            "News" -> watchedNews
            "Albums" -> watchedAlbums
            "Reviews" -> watchedReviews
            else -> watchedFilms
        }
        
        if (searchQuery.isEmpty()) items else {
            items.filter { it.title.lowercase().contains(searchQuery.lowercase()) }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Watch History", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // Filter chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = InnieGreen,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty() && !isFocused) {
                            Text(
                                text = "Search in $selectedFilter",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    isFocused = focusState.isFocused
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title with count
            Text(
                text = "${displayItems.size} ${selectedFilter.lowercase()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = InnieGreen)
                }
            } else if (displayItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No $selectedFilter watched yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Text(
                            text = getEmptyStateHint(selectedFilter),
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (selectedFilter == "Reviews") {
                // Reviews list - vertical cards
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    displayItems.filterIsInstance<WatchedContent.WatchedReview>().forEach { review ->
                        WatchedReviewCard(
                            review = review,
                            onClick = {
                                navController.navigate(Screen.ReviewDetail.createRoute(review.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            } else {
                // Grid for Films, News, Albums
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayItems) { item ->
                            WatchedItemCard(
                                item = item,
                                onClick = {
                                    when (item) {
                                        is WatchedContent.WatchedMovie -> 
                                            navController.navigate(Screen.MoviePage.createRoute(item.id))
                                        is WatchedContent.WatchedNews -> 
                                            navController.navigate(Screen.NewsDetail.createRoute(item.id))
                                        is WatchedContent.WatchedAlbum -> 
                                            navController.navigate(Screen.AlbumDetail.createRoute(item.id))
                                        else -> {}
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WatchedItemCard(
    item: WatchedContent,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun WatchedReviewCard(
    review: WatchedContent.WatchedReview,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Review icon
            Icon(
                imageVector = Icons.Default.RateReview,
                contentDescription = null,
                tint = InnieGreen,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                // Movie title
                Text(
                    text = review.movieTitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Author
                Text(
                    text = "by ${review.authorName}",
                    fontSize = 11.sp,
                    color = InnieGreen
                )
                
                // Review snippet
                Text(
                    text = review.reviewBody,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                // Watched time
                Text(
                    text = "Watched ${formatWatchedTime(review.watchedAt)}",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Rating if exists
            review.rating?.let { rating ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = String.format("%.1f", rating),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = InnieGreen
                    )
                    Text("★", color = InnieGreen, fontSize = 12.sp)
                }
            }
        }
    }
}

private fun getEmptyStateHint(filter: String): String {
    return when (filter) {
        "Films" -> "Write a review to mark a film as watched"
        "News" -> "Open news articles to add them here"
        "Albums" -> "View photo albums to add them here"
        "Reviews" -> "Open reviews to add them here"
        else -> "Watch content to see it here"
    }
}

private fun formatWatchedTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val hours = diff / (1000 * 60 * 60)
    val days = hours / 24
    
    return when {
        hours < 1 -> "just now"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        else -> {
            val dateFormat = java.text.SimpleDateFormat("d MMM", java.util.Locale.getDefault())
            dateFormat.format(java.util.Date(timestamp))
        }
    }
}