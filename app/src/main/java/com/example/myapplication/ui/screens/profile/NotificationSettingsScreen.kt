package com.example.myapplication.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val InnieGreen = Color(0xFF00C02B)

// Notification category data
data class NotificationCategory(
    val id: String,
    val title: String,
    val description: String,
    var inAppEnabled: Boolean = true,
    var emailEnabled: Boolean = false,
    var smsEnabled: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(navController: NavController) {
    // Notification categories with state
    var categories by remember {
        mutableStateOf(
            listOf(
                NotificationCategory(
                    id = "comments",
                    title = "Comments",
                    description = "Get notified when someone comments on your reviews, lists, or replies to your comments."
                ),
                NotificationCategory(
                    id = "friend_updates",
                    title = "Friend Updates",
                    description = "Stay updated when your friends add new reviews, create lists, or update their profiles."
                ),
                NotificationCategory(
                    id = "friend_requests",
                    title = "Friend Requests",
                    description = "Receive notifications when someone sends you a friend request or accepts yours."
                ),
                NotificationCategory(
                    id = "birthdays",
                    title = "Birthdays",
                    description = "Get reminded about your friends' birthdays so you never miss a chance to wish them."
                ),
                NotificationCategory(
                    id = "news",
                    title = "News & Articles",
                    description = "Stay informed about the latest movie news, trailers, and entertainment updates."
                ),
                NotificationCategory(
                    id = "albums",
                    title = "Albums & Lists",
                    description = "Get notified when someone follows your albums or when featured albums are updated."
                ),
                NotificationCategory(
                    id = "community",
                    title = "Community",
                    description = "Receive updates about community discussions, polls, and trending topics."
                ),
                NotificationCategory(
                    id = "memories",
                    title = "Memories & Anniversaries",
                    description = "Relive your movie moments with anniversary reminders of films you watched."
                ),
                NotificationCategory(
                    id = "recommendations",
                    title = "Recommendations",
                    description = "Get personalized movie recommendations based on your watching history and preferences."
                ),
                NotificationCategory(
                    id = "releases",
                    title = "New Releases",
                    description = "Be the first to know when movies on your watchlist are released or available to stream."
                ),
                NotificationCategory(
                    id = "reviews",
                    title = "Review Interactions",
                    description = "Get notified when someone likes, shares, or mentions your reviews."
                ),
                NotificationCategory(
                    id = "trending",
                    title = "Trending",
                    description = "Stay updated with trending movies, shows, and what the community is watching."
                )
            )
        )
    }
    
    // Track expanded state for each category
    var expandedCategory by remember { mutableStateOf<String?>(null) }
    
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
            
            // Notification categories card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    categories.forEachIndexed { index, category ->
                        NotificationCategoryItem(
                            category = category,
                            isExpanded = expandedCategory == category.id,
                            onExpandClick = {
                                expandedCategory = if (expandedCategory == category.id) null else category.id
                            },
                            onInAppChange = { enabled ->
                                categories = categories.map { 
                                    if (it.id == category.id) it.copy(inAppEnabled = enabled) else it 
                                }
                            },
                            onEmailChange = { enabled ->
                                categories = categories.map { 
                                    if (it.id == category.id) it.copy(emailEnabled = enabled) else it 
                                }
                            },
                            onSmsChange = { enabled ->
                                categories = categories.map { 
                                    if (it.id == category.id) it.copy(smsEnabled = enabled) else it 
                                }
                            }
                        )
                        
                        // Divider between items (except last)
                        if (index < categories.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE0E0E0)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Quick actions card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Quick Actions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Enable all in-app notifications
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                categories = categories.map { it.copy(inAppEnabled = true) }
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enable all in-app notifications",
                            fontSize = 14.sp,
                            color = InnieGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Disable all notifications
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                categories = categories.map { 
                                    it.copy(inAppEnabled = false, emailEnabled = false, smsEnabled = false) 
                                }
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Disable all notifications",
                            fontSize = 14.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun NotificationCategoryItem(
    category: NotificationCategory,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onInAppChange: (Boolean) -> Unit,
    onEmailChange: (Boolean) -> Unit,
    onSmsChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandClick() }
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Expanded content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Description
                Text(
                    text = category.description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Toggle options
                NotificationToggleRow(
                    title = "Notify on Innie Movie",
                    isEnabled = category.inAppEnabled,
                    onToggle = onInAppChange
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                NotificationToggleRow(
                    title = "Email",
                    isEnabled = category.emailEnabled,
                    onToggle = onEmailChange
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                NotificationToggleRow(
                    title = "SMS",
                    isEnabled = category.smsEnabled,
                    onToggle = onSmsChange
                )
            }
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Black
        )
        
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = InnieGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            ),
            modifier = Modifier.height(24.dp)
        )
    }
}
