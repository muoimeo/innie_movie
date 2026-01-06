package com.example.myapplication.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * NewsDetailScreen - BBC/Variety-style professional article layout.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsDetailScreen(
    newsId: Int,
    navController: NavController? = null
) {
    val context = LocalContext.current
    var news by remember { mutableStateOf<News?>(null) }
    var isLiked by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }
    
    // Load news from database
    LaunchedEffect(newsId) {
        withContext(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(context)
            news = db.newsDao().getNewsById(newsId)
        }
    }
    
    if (news == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", color = Color.Gray)
        }
        return
    }
    
    val article = news!!
    val scrollState = rememberScrollState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            // Hero Image with gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
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
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
                
                // Back button
                IconButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                // Category badge at top
                article.category?.let { category ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(InnieGreen, RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category.uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                
                // Image caption at bottom
                article.imageCaption?.let { caption ->
                    Text(
                        text = caption,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    )
                }
            }
            
            // Article content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                // Title
                Text(
                    text = article.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 32.sp
                )
                
                // Subtitle
                article.subtitle?.let { subtitle ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = subtitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF4A4A4A),
                        lineHeight = 26.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Author and meta row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Author avatar
                    AsyncImage(
                        model = article.authorImageUrl ?: "https://i.pravatar.cc/150",
                        contentDescription = article.authorName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, InnieGreen, CircleShape)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        // Author name
                        Text(
                            text = article.authorName ?: "Staff Writer",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1A1A)
                        )
                        
                        // Source and time
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = article.sourceName,
                                fontSize = 12.sp,
                                color = InnieGreen,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = " • ",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = formatNewsTime(article.createdAt),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    // Read time
                    article.readTimeMinutes?.let { readTime ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccessTime,
                                contentDescription = "Read time",
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$readTime min read",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(20.dp))
                
                // Article body - render HTML-like content
                ArticleBody(body = article.body ?: article.content ?: "")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Tags
                article.tags?.let { tags ->
                    val tagList = tags.split(",").map { it.trim() }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tagList.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(16.dp))
                
                // Engagement stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EngagementStat(
                        icon = Icons.Outlined.RemoveRedEye,
                        count = article.viewCount,
                        label = "views"
                    )
                    EngagementStat(
                        icon = Icons.Filled.Favorite,
                        count = article.likeCount,
                        label = "likes",
                        tint = Color(0xFFEC2626)
                    )
                    EngagementStat(
                        icon = Icons.Outlined.ChatBubbleOutline,
                        count = article.commentCount,
                        label = "comments"
                    )
                }
                
                Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav
            }
        }
        
        // Bottom action bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like button
            ActionChip(
                icon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                label = "Like",
                isActive = isLiked,
                activeColor = Color(0xFFEC2626),
                onClick = { isLiked = !isLiked }
            )
            
            // Bookmark button
            ActionChip(
                icon = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                label = "Save",
                isActive = isBookmarked,
                activeColor = InnieGreen,
                onClick = { isBookmarked = !isBookmarked }
            )
            
            // Share button
            ActionChip(
                icon = Icons.Filled.Share,
                label = "Share",
                isActive = false,
                activeColor = InnieGreen,
                onClick = { /* TODO: Share functionality */ }
            )
        }
    }
}

@Composable
fun ArticleBody(body: String) {
    // Simple HTML-like rendering - parse paragraphs
    val paragraphs = body
        .replace("<p>", "")
        .replace("<i>", "")
        .replace("</i>", "")
        .replace("<h3>", "\n### ")
        .replace("</h3>", "\n")
        .replace("<ul>", "\n")
        .replace("</ul>", "")
        .replace("<li>", "• ")
        .replace("</li>", "\n")
        .replace("<strong>", "")
        .replace("</strong>", "")
        .split("</p>")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
    
    Column {
        paragraphs.forEach { paragraph ->
            if (paragraph.startsWith("### ")) {
                // Heading
                Text(
                    text = paragraph.removePrefix("### "),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else if (paragraph.startsWith("• ")) {
                // List item
                Text(
                    text = paragraph,
                    fontSize = 16.sp,
                    color = Color(0xFF333333),
                    lineHeight = 26.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )
            } else {
                // Regular paragraph
                Text(
                    text = paragraph,
                    fontSize = 16.sp,
                    color = Color(0xFF333333),
                    lineHeight = 28.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
fun EngagementStat(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    label: String,
    tint: Color = Color.Gray
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatNewsCount(count),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isActive) activeColor.copy(alpha = 0.1f) else Color(0xFFF5F5F5))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) activeColor else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isActive) activeColor else Color.Gray
        )
    }
}

// Helper function for formatting counts
fun formatNewsCount(count: Int): String {
    return when {
        count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
        count >= 1000 -> String.format("%.1fK", count / 1000.0)
        else -> count.toString()
    }
}

// Helper function for time formatting  
fun formatNewsTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val hours = diff / (1000 * 60 * 60)
    val days = hours / 24
    
    return when {
        hours < 1 -> "Just now"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        else -> "${days / 7} weeks ago"
    }
}
