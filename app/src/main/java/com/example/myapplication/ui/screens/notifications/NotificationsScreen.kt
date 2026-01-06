package com.example.myapplication.ui.screens.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.NotificationItem
import com.example.myapplication.data.NotificationType
import com.example.myapplication.data.sampleNotifications
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBackClick: () -> Unit = {}) {
    var selectedNotification by remember { mutableStateOf<NotificationItem?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(sampleNotifications) { notification ->
                NotificationRow(
                    notification = notification,
                    onMenuClick = { selectedNotification = notification }
                )
            }
        }

        if (selectedNotification != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedNotification = null },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(modifier = Modifier.padding(bottom = 32.dp)) {
                    // Header or Title (optional, mimicking filter title if any, or just options)
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
                            // Handle Delete
                            selectedNotification = null
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Report to admin") },
                        leadingContent = { Icon(Icons.Outlined.Report, contentDescription = null) },
                        modifier = Modifier.clickable {
                            // Handle Report
                            selectedNotification = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationRow(
    notification: NotificationItem,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (notification.isRead) Color.White else Color(0xFFEEEEEE))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar with Type Badge
        Box {
            Image(
                painter = painterResource(id = notification.avatarRes),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            // Type Badge - small circle at bottom-right
            val (badgeColor, badgeIcon) = when (notification.type) {
                NotificationType.NEWS -> Color(0xFFFFC107) to Icons.Filled.Article  // Yellow
                NotificationType.COMMENT -> Color(0xFF00C02B) to Icons.Filled.ChatBubble  // Green
                NotificationType.TRAILER -> Color(0xFF2196F3) to Icons.Filled.Movie  // Blue
                NotificationType.FRIEND -> Color(0xFF9C27B0) to Icons.Filled.Person  // Purple
            }
            
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = "Notification Type",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Title + 3-dot Menu Row
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )

                // 3-dot Menu Icon
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Menu",
                        tint = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = notification.description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Time (below text as requested)
            Text(
                text = notification.time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
