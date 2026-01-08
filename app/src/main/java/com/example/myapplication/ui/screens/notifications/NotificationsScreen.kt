package com.example.myapplication.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.NotificationPreferencesManager
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Notification
import com.example.myapplication.data.session.UserSessionManager
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController? = null,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefsManager = remember { NotificationPreferencesManager(context) }
    val db = remember { DatabaseProvider.getDatabase(context) }
    val currentUserId = UserSessionManager.getUserId()
    
    // Load notifications from database
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(currentUserId) {
        withContext(Dispatchers.IO) {
            val allNotifications = db.notificationDao().getNotificationsForUserOnce(currentUserId)
            // Filter by preferences
            notifications = allNotifications.filter { notification ->
                prefsManager.shouldShowNotification(
                    when (notification.type) {
                        "NEWS" -> com.example.myapplication.data.NotificationType.NEWS
                        "COMMENT" -> com.example.myapplication.data.NotificationType.COMMENT
                        "TRAILER" -> com.example.myapplication.data.NotificationType.TRAILER
                        "FRIEND" -> com.example.myapplication.data.NotificationType.FRIEND
                        else -> com.example.myapplication.data.NotificationType.NEWS
                    }
                )
            }
        }
        isLoading = false
    }
    
    var selectedNotification by remember { mutableStateOf<Notification?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF00C02B))
            }
        } else if (notifications.isEmpty()) {
            // Empty state when all notifications are filtered out
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "No notifications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You don't have any notifications yet.\nInteract with the app to receive notifications!",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(notifications, key = { it.id }) { notification ->
                    NotificationRow(
                        notification = notification,
                        onMenuClick = { selectedNotification = notification },
                        onClick = {
                            // Mark as read
                            scope.launch(Dispatchers.IO) {
                                db.notificationDao().markAsRead(notification.id)
                                // Update local list
                                notifications = notifications.map {
                                    if (it.id == notification.id) it.copy(isRead = true) else it
                                }
                            }
                            
                            // Navigate based on notification type
                            navController?.let { nav ->
                                when (notification.type) {
                                    "NEWS" -> {
                                        notification.relatedId?.let { id ->
                                            nav.navigate(Screen.NewsDetail.createRoute(id))
                                        }
                                    }
                                    "TRAILER" -> {
                                        notification.relatedId?.let { id ->
                                            nav.navigate(Screen.MoviePage.createRoute(id))
                                        }
                                    }
                                    "FRIEND" -> {
                                        when (notification.relatedType) {
                                            "album" -> notification.relatedId?.let { id ->
                                                nav.navigate(Screen.AlbumDetail.createRoute(id))
                                            }
                                            "user" -> {
                                                // TODO: Navigate to user profile when available
                                            }
                                        }
                                    }
                                    "COMMENT" -> {
                                        when (notification.relatedType) {
                                            "review" -> notification.relatedId?.let { id ->
                                                // Navigate to movie of the review
                                                nav.navigate(Screen.MoviePage.createRoute(id))
                                            }
                                            "movie" -> notification.relatedId?.let { id ->
                                                nav.navigate(Screen.MoviePage.createRoute(id))
                                            }
                                            "album" -> notification.relatedId?.let { id ->
                                                nav.navigate(Screen.AlbumDetail.createRoute(id))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        if (selectedNotification != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedNotification = null },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(modifier = Modifier.padding(bottom = 32.dp)) {
                    Text(
                        text = "Options",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    ListItem(
                        headlineContent = { Text("Delete this notification") },
                        leadingContent = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                        modifier = Modifier.clickable {
                            val notifToDelete = selectedNotification
                            selectedNotification = null // Close sheet first
                            
                            // Delete from database and update UI
                            notifToDelete?.let { notif ->
                                scope.launch {
                                    // Delete from database on IO thread
                                    withContext(Dispatchers.IO) {
                                        db.notificationDao().deleteById(notif.id)
                                    }
                                    // Update UI on Main thread
                                    notifications = notifications.filter { it.id != notif.id }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationRow(
    notification: Notification,
    onMenuClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            // Unread = gray background, Read = white background
            .background(if (!notification.isRead) Color(0xFFEEEEEE) else Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar with Type Badge
        Box {
            AsyncImage(
                model = notification.imageUrl,
                contentDescription = "Notification Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            // Type Badge - small circle at bottom-right
            val (badgeColor, badgeIcon) = when (notification.type) {
                "NEWS" -> Color(0xFFFFC107) to Icons.Filled.Article  // Yellow
                "COMMENT" -> Color(0xFF00C02B) to Icons.Filled.ChatBubble  // Green
                "TRAILER" -> Color(0xFF2196F3) to Icons.Filled.Movie  // Blue
                "FRIEND" -> Color(0xFF9C27B0) to Icons.Filled.Person  // Purple
                else -> Color.Gray to Icons.Filled.Article
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(22.dp)
                    .background(badgeColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                fontSize = 15.sp,
                fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.description,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatNotificationTime(notification.createdAt),
                fontSize = 11.sp,
                color = Color.LightGray
            )
        }
        
        // More options button
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "More options",
                tint = Color.Gray
            )
        }
    }
}

private fun formatNotificationTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val minutes = diff / (1000 * 60)
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> "${days / 7}w ago"
    }
}
