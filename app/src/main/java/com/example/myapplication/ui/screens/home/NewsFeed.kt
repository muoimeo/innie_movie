package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.InnieGreen

// Data class for News Article
data class NewsArticle(
    val id: String,
    val title: String,
    val author: String,
    val timeAgo: String,
    val imageRes: Int,
    val likeCount: Int,
    val viewCount: Int,
    val commentCount: Int,
    val isBookmarked: Boolean = false
)

// Fake news data
val fakeNewsArticles = listOf(
    NewsArticle(
        id = "1",
        title = "'Stranger Things 5' Just Did What Shows Like 'Game of Thrones' Couldn't",
        author = "Huan",
        timeAgo = "8 hours ago",
        imageRes = R.drawable.onboarding_bg,
        likeCount = 2967,
        viewCount = 20000,
        commentCount = 677,
        isBookmarked = true
    ),
    NewsArticle(
        id = "2",
        title = "Pluribus Breaks Apple TV Records: Most Viewed Drama Launch Ever",
        author = "Minh",
        timeAgo = "6 hours ago",
        imageRes = R.drawable.onboarding_bg,
        likeCount = 10000,
        viewCount = 67000,
        commentCount = 767,
        isBookmarked = false
    ),
    NewsArticle(
        id = "3",
        title = "Inside the Making of Dune: Why Villeneuve's Visuals Feel Biblical",
        author = "Anna",
        timeAgo = "2 days ago",
        imageRes = R.drawable.onboarding_bg,
        likeCount = 5400,
        viewCount = 45000,
        commentCount = 324,
        isBookmarked = false
    ),
    NewsArticle(
        id = "4",
        title = "Marvel's Secret Wars: Everything We Know So Far About the Biggest MCU Event",
        author = "David",
        timeAgo = "3 days ago",
        imageRes = R.drawable.onboarding_bg,
        likeCount = 8900,
        viewCount = 120000,
        commentCount = 1200,
        isBookmarked = true
    )
)

@Composable
fun NewsFeed(
    onNewsClick: (NewsArticle) -> Unit = {},
    showHeroFullScreen: Boolean = false
) {
    var searchQuery by remember { mutableStateOf("") }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Featured Hero Section - taller when full screen mode
        item {
            FeaturedHeroSection(
                isFullScreen = showHeroFullScreen
            )
        }
        
        // Search Bar and Filter - hidden when overlay mode (handled by HomeScreen)
        if (!showHeroFullScreen) {
            item {
                SearchAndFilterBar(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it }
                )
            }
        }
        
        // Featured Header with dividers
        item {
            FeaturedHeader()
        }
        
        // News Articles List
        items(fakeNewsArticles) { article ->
            NewsArticleCard(
                article = article,
                onClick = { onNewsClick(article) }
            )
            
            // Divider between articles
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 60.dp, vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color(0xFF1A202C)
            )
        }
    }
}

@Composable
fun FeaturedHeroSection(
    isFullScreen: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isFullScreen) 380.dp else 300.dp)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.onboarding_bg),
            contentDescription = "Featured News",
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
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 100f
                    )
                )
        )
        
        // Title at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = "Inside of making Dune:\nWhy Villeneuve's visuals feel biblical?",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 26.sp,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
fun SearchAndFilterBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(0.5.dp, Color(0xFFB3B3B3), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF1A202C)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (searchQuery.isEmpty()) "Find out what's new today?" else searchQuery,
                    color = if (searchQuery.isEmpty()) Color(0xFF1A202C) else Color(0xFF1A202C),
                    fontSize = 12.sp
                )
            }
        }
        
        // Filter Button
        Box(
            modifier = Modifier
                .height(36.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(0.5.dp, Color(0xFFB3B3B3), RoundedCornerShape(8.dp))
                .clickable { /* TODO: Open filter */ }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Filter",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF1A202C)
                )
                Text(
                    text = "Filter",
                    color = Color(0xFF1A202C),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun FeaturedHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left divider
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = Color(0xFF1A202C)
        )
        
        // "Featured" text
        Text(
            text = "Featured",
            color = InnieGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        
        // Right divider
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = Color(0xFF1A202C)
        )
    }
}

@Composable
fun NewsArticleCard(
    article: NewsArticle,
    onClick: () -> Unit
) {
    var isBookmarked by remember { mutableStateOf(article.isBookmarked) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Thumbnail Image
        Image(
            painter = painterResource(id = article.imageRes),
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(100.dp)
                .height(128.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Time ago
            Text(
                text = article.timeAgo,
                color = Color(0xFFB3B3B3),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Title
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A202C),
                    lineHeight = 18.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Author and Stats Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // By Author
                Text(
                    text = "By ",
                    color = Color(0xFFB3B3B3),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = article.author,
                    color = InnieGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(10.dp)
                        .background(Color(0xFF1A202C))
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Likes
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Likes",
                    modifier = Modifier.size(12.dp),
                    tint = Color(0xFFEC2626)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = formatCount(article.likeCount),
                    fontSize = 9.sp,
                    color = Color(0xFF1A202C),
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.width(6.dp))
                
                // Views
                Icon(
                    imageVector = Icons.Outlined.RemoveRedEye,
                    contentDescription = "Views",
                    modifier = Modifier.size(12.dp),
                    tint = InnieGreen.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = formatCount(article.viewCount),
                    fontSize = 9.sp,
                    color = Color(0xFF1A202C),
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.width(6.dp))
                
                // Comments
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comments",
                    modifier = Modifier.size(12.dp),
                    tint = Color(0xFF1A202C)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = article.commentCount.toString(),
                    fontSize = 9.sp,
                    color = Color(0xFF1A202C),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        // Bookmark Icon
        IconButton(
            onClick = { isBookmarked = !isBookmarked },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Bookmark",
                tint = if (isBookmarked) Color(0xFFFF383C) else Color(0xFFB3B3B3),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Helper function to format large numbers
fun formatCount(count: Int): String {
    return when {
        count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
        count >= 1000 -> String.format("%.0fk", count / 1000.0)
        else -> count.toString()
    }
}
