package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.sampleProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                .verticalScroll(rememberScrollState())
        ) {
            // User Info Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = sampleProfile.avatarRes),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = sampleProfile.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "kyran@gmail.com", // Mock email as requested
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // Settings List
            SettingsItem(
                icon = Icons.Default.PersonAdd,
                title = "Account",
                description = "Security notifications, change number",
                onClick = { /* Handle click */ }
            )
            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Privacy",
                description = "Block contacts, disappearing messages",
                onClick = { /* Handle click */ }
            )
            SettingsItem(
                icon = Icons.Default.Face,
                title = "Avatar",
                description = "Create, edit, profile photo",
                onClick = { /* Handle click */ }
            )
            SettingsItem(
                icon = Icons.Outlined.ChatBubbleOutline,
                title = "Chats",
                description = "Theme, wallpapers, chat history",
                onClick = { /* Handle click */ }
            )
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                description = "Message, group & call tones",
                onClick = { /* Handle click */ }
            )
            SettingsItem(
                icon = Icons.Default.Help,
                title = "Help",
                description = "Help centre, contact us, privacy policy",
                onClick = { /* Handle click */ }
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF00C02B), // Green color as requested
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold, // Bold and larger
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
