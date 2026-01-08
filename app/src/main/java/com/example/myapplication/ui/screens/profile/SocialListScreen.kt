package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class SocialTab(val title: String) {
    FOLLOWERS("Followers"),
    FRIENDS("Friends"),
    FOLLOWING("Following")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialListScreen(
    navController: NavController,
    initialTab: Int = 0  // 0=Followers, 1=Friends, 2=Following
) {
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()
    val currentUserId = UserSessionManager.getUserId()
    
    var selectedTab by remember { mutableStateOf(initialTab) }
    val tabs = SocialTab.entries
    
    // User lists
    var followers by remember { mutableStateOf<List<User>>(emptyList()) }
    var friends by remember { mutableStateOf<List<User>>(emptyList()) }
    var following by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Load data
    LaunchedEffect(currentUserId) {
        isLoading = true
        
        // Load followers
        val followerIds = database.socialDao().getFollowers(currentUserId).first()
        followers = followerIds.mapNotNull { database.userDao().getUserById(it) }
        
        // Load friends
        val friendIds = database.socialDao().getFriends(currentUserId).first()
        friends = friendIds.mapNotNull { database.userDao().getUserById(it) }
        
        // Load following
        val followingIds = database.socialDao().getFollowing(currentUserId).first()
        following = followingIds.mapNotNull { database.userDao().getUserById(it) }
        
        isLoading = false
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Social", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = InnieGreen
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            val count = when (tab) {
                                SocialTab.FOLLOWERS -> followers.size
                                SocialTab.FRIENDS -> friends.size
                                SocialTab.FOLLOWING -> following.size
                            }
                            Text(
                                "${tab.title} ($count)",
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            
            // Content
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = InnieGreen)
                }
            } else {
                val currentList = when (tabs[selectedTab]) {
                    SocialTab.FOLLOWERS -> followers
                    SocialTab.FRIENDS -> friends
                    SocialTab.FOLLOWING -> following
                }
                
                if (currentList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                when (tabs[selectedTab]) {
                                    SocialTab.FOLLOWERS -> "No followers yet"
                                    SocialTab.FRIENDS -> "No friends yet"
                                    SocialTab.FOLLOWING -> "Not following anyone"
                                },
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(currentList) { user ->
                            UserListItem(
                                user = user,
                                onClick = {
                                    // Navigate to user's profile
                                    navController.navigate(Screen.UserProfile.createRoute(user.userId))
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
fun UserListItem(
    user: User,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
        ) {
            if (user.avatarUrl != null) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    tint = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // User info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.displayName ?: user.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "@${user.username}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}
