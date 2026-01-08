package com.example.myapplication.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.ui.theme.InnieGreen
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun NewsFeed(
    searchQuery: String = "",
    onNewsClick: (News) -> Unit = {},
    showHeroFullScreen: Boolean = false,
    newsViewModel: NewsViewModel = viewModel()
) {
    val news by newsViewModel.news.collectAsState()
    val isLoading by newsViewModel.isLoading.collectAsState()
    
    // React to search query changes
    LaunchedEffect(searchQuery) {
        newsViewModel.searchNews(searchQuery)
    }
    
    // Show filter bottom sheet - kept locally if needed, but UI removed for now as requested
    // var showFilterSheet by remember { mutableStateOf(false) }
    // var newsFilterState by remember { mutableStateOf(NewsFilterState()) }
    
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = InnieGreen)
        }
        return
    }
    // Check if we are in search mode
    val isSearching = searchQuery.isNotBlank()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        if (news.isEmpty() && isSearching) {
            // Empty search results
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No matching content",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Try a different search term",
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (!isSearching && news.isNotEmpty()) {
                    // Featured Hero Section - only when NOT searching
                    item {
                        FeaturedHeroSection(
                            news = news.first(),
                            isFullScreen = showHeroFullScreen,
                            onClick = { onNewsClick(news.first()) }
                        )
                    }
                    
                    // Featured Header
                    item {
                        FeaturedHeader()
                    }
                    
                    // News Articles List (skip first one since it's the hero)
                    items(news.drop(1)) { article ->
                        NewsArticleCard(
                            news = article,
                            onClick = { onNewsClick(article) }
                        )
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    // Search mode: Search results header
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 170.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(20.dp)
                                        .background(InnieGreen, RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Search Results (${news.size})",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1A1A)
                                )
                            }
                        }
                    }
                    
                    // All search results as normal article cards
                    items(news) { article ->
                        NewsArticleCard(
                            news = article,
                            onClick = { onNewsClick(article) }
                        )
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedHeroSection(
    news: News,
    isFullScreen: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )
        
        // Text Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            news.category?.let { category ->
                Text(
                    text = category.uppercase(),
                    color = InnieGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            Text(
                text = news.title,
                color = Color.White,
                fontSize = 20.sp, // Reduced from 24sp to fit better with overlay
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp, // Reduced from 32sp
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = news.sourceName,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " • ",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = formatTimeAgo(news.createdAt),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun FeaturedHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .background(InnieGreen, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Must Read",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        }
    }
}

@Composable
fun NewsArticleCard(
    news: News,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Content (Left side, weight 1)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            news.category?.let { category ->
                Text(
                    text = category.uppercase(),
                    color = InnieGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            
            Text(
                text = news.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                lineHeight = 22.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = news.sourceName,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = " • ",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = formatTimeAgo(news.createdAt),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
        
        // Thumbnail Image (Right side)
        AsyncImage(
            model = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 75.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        )
    }
}

@Composable
fun SearchAndFilterBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit = {}
) {
    // Search Box - full width (removed filter button)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color.White, RoundedCornerShape(22.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(22.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            androidx.compose.foundation.text.BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search news...",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun formatTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val hours = diff / (1000 * 60 * 60)
    val days = hours / 24
    
    return when {
        hours < 1 -> "Just now"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> "${days / 7}w ago"
    }
}
