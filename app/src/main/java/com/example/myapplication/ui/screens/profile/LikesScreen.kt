package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.InnieGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikesScreen(
    navController: NavController,
    likesViewModel: LikesViewModel = viewModel()
) {
    var selectedFilter by remember { mutableStateOf("All") }
    
    val allLikes by likesViewModel.allLikes.collectAsState()
    val isLoading by likesViewModel.isLoading.collectAsState()
    val movieCount by likesViewModel.movieCount.collectAsState()
    val albumCount by likesViewModel.albumCount.collectAsState()
    val shotCount by likesViewModel.shotCount.collectAsState()
    val newsCount by likesViewModel.newsCount.collectAsState()
    
    val filters = listOf("All", "Movies", "Albums", "Shots", "News")
    
    // Get filtered items based on selection
    val displayItems = remember(selectedFilter, allLikes) {
        likesViewModel.getFilteredLikes(selectedFilter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Likes", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Stats summary - real counts from database
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LikeStatCard(count = movieCount, label = "Movies", modifier = Modifier.weight(1f))
                LikeStatCard(count = albumCount, label = "Albums", modifier = Modifier.weight(1f))
                LikeStatCard(count = shotCount, label = "Shots", modifier = Modifier.weight(1f))
                LikeStatCard(count = newsCount, label = "News", modifier = Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = InnieGreen)
                }
            } else if (allLikes.isEmpty() && selectedFilter == "All") {
                // Empty state for All tab
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No likes yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Text(
                            text = "Like movies, albums, shots or news\nto see them here",
                            fontSize = 14.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (selectedFilter == "All") {
                // NEW: Horizontal rows for All tab
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Movies row
                    val movies = likesViewModel.getFilteredLikes("Movies")
                    if (movies.isNotEmpty()) {
                        LikesHorizontalRow(
                            title = "Movies",
                            items = movies.take(5),
                            hasMore = movies.size > 5,
                            onMoreClick = { selectedFilter = "Movies" }
                        )
                    }
                    
                    // Albums row
                    val albums = likesViewModel.getFilteredLikes("Albums")
                    if (albums.isNotEmpty()) {
                        LikesHorizontalRow(
                            title = "Albums",
                            items = albums.take(5),
                            hasMore = albums.size > 5,
                            onMoreClick = { selectedFilter = "Albums" }
                        )
                    }
                    
                    // Shots row
                    val shots = likesViewModel.getFilteredLikes("Shots")
                    if (shots.isNotEmpty()) {
                        LikesHorizontalRow(
                            title = "Shots",
                            items = shots.take(5),
                            hasMore = shots.size > 5,
                            onMoreClick = { selectedFilter = "Shots" }
                        )
                    }
                    
                    // News row
                    val news = likesViewModel.getFilteredLikes("News")
                    if (news.isNotEmpty()) {
                        LikesHorizontalRow(
                            title = "News",
                            items = news.take(5),
                            hasMore = news.size > 5,
                            onMoreClick = { selectedFilter = "News" }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(80.dp))
                }
            } else if (displayItems.isEmpty()) {
                // Empty state for specific category
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No liked $selectedFilter yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // Liked content grid for specific category tabs
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayItems) { item ->
                        LikedItemCard(
                            posterUrl = item.imageUrl,
                            title = item.title,
                            type = item.type,
                            onClick = { /* Navigate to detail */ }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Horizontal row for a category in All tab
 */
@Composable
private fun LikesHorizontalRow(
    title: String,
    items: List<LikedContent>,
    hasMore: Boolean,
    onMoreClick: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            if (hasMore) {
                Text(
                    text = "See All",
                    fontSize = 12.sp,
                    color = InnieGreen,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onMoreClick() }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Horizontal row
        androidx.compose.foundation.lazy.LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                LikedItemCompact(
                    posterUrl = item.imageUrl,
                    title = item.title,
                    onClick = { /* Navigate to detail */ }
                )
            }
            
            // More button at end
            if (hasMore) {
                item {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF0F0F0))
                            .clickable { onMoreClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "More",
                                tint = InnieGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "More",
                                fontSize = 12.sp,
                                color = InnieGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LikedItemCompact(
    posterUrl: String,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LikeStatCard(
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = InnieGreen
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun LikedItemCard(
    posterUrl: String,
    title: String,
    type: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = posterUrl,
                contentDescription = title,
                modifier = Modifier
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Like icon overlay
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Liked",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        
        Text(
            text = type,
            fontSize = 9.sp,
            color = Color.Gray
        )
    }
}
