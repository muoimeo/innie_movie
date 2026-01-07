package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.NotificationPreferencesManager

private val InnieGreen = Color(0xFF00C02B)

// Notification category data
data class NotificationCategory(
    val id: String,
    val prefKey: String,
    val title: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefsManager = remember { NotificationPreferencesManager(context) }
    
    // Categories with their preference keys
    val categories = listOf(
        NotificationCategory(
            id = "comments",
            prefKey = NotificationPreferencesManager.KEY_COMMENTS,
            title = "Comments",
            description = "Get notified when someone comments on your reviews, lists, or replies to your comments."
        ),
        NotificationCategory(
            id = "friend_updates",
            prefKey = NotificationPreferencesManager.KEY_FRIEND_UPDATES,
            title = "Friend Updates",
            description = "Stay updated when your friends add new reviews, create lists, or update their profiles."
        ),
        NotificationCategory(
            id = "friend_requests",
            prefKey = NotificationPreferencesManager.KEY_FRIEND_REQUESTS,
            title = "Friend Requests",
            description = "Receive notifications when someone sends you a friend request or accepts yours."
        ),
        NotificationCategory(
            id = "birthdays",
            prefKey = NotificationPreferencesManager.KEY_BIRTHDAYS,
            title = "Birthdays",
            description = "Get reminded about your friends' birthdays so you never miss a chance to wish them."
        ),
        NotificationCategory(
            id = "news",
            prefKey = NotificationPreferencesManager.KEY_NEWS,
            title = "News & Articles",
            description = "Stay informed about the latest movie news, trailers, and entertainment updates."
        ),
        NotificationCategory(
            id = "albums",
            prefKey = NotificationPreferencesManager.KEY_ALBUMS,
            title = "Albums & Lists",
            description = "Get notified when someone follows your albums or when featured albums are updated."
        ),
        NotificationCategory(
            id = "community",
            prefKey = NotificationPreferencesManager.KEY_COMMUNITY,
            title = "Community",
            description = "Receive updates about community discussions, polls, and trending topics."
        ),
        NotificationCategory(
            id = "memories",
            prefKey = NotificationPreferencesManager.KEY_MEMORIES,
            title = "Memories & Anniversaries",
            description = "Relive your movie moments with anniversary reminders of films you watched."
        ),
        NotificationCategory(
            id = "recommendations",
            prefKey = NotificationPreferencesManager.KEY_RECOMMENDATIONS,
            title = "Recommendations",
            description = "Get personalized movie recommendations based on your watching history and preferences."
        ),
        NotificationCategory(
            id = "releases",
            prefKey = NotificationPreferencesManager.KEY_RELEASES,
            title = "New Releases",
            description = "Be the first to know when movies on your watchlist are released or available to stream."
        ),
        NotificationCategory(
            id = "reviews",
            prefKey = NotificationPreferencesManager.KEY_REVIEWS,
            title = "Review Interactions",
            description = "Get notified when someone likes, shares, or mentions your reviews."
        ),
        NotificationCategory(
            id = "trending",
            prefKey = NotificationPreferencesManager.KEY_TRENDING,
            title = "Trending",
            description = "Stay updated with trending movies, shows, and what the community is watching."
        )
    )
    
    // State for each category toggle - load from preferences
    var toggleStates by remember {
        mutableStateOf(
            categories.associate { it.id to prefsManager.isEnabled(it.prefKey) }
        )
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Info text
            Text(
                text = "Manage how you receive notifications for different activities.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Notification categories - separate cards with equal spacing
            categories.forEach { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    NotificationItem(
                        title = category.title,
                        description = category.description,
                        enabled = toggleStates[category.id] ?: true,
                        onToggle = { enabled ->
                            // Update local state
                            toggleStates = toggleStates + (category.id to enabled)
                            // Save to preferences
                            prefsManager.setEnabled(category.prefKey, enabled)
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun NotificationItem(
    title: String,
    description: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Toggle on the right
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = InnieGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}
