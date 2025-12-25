package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.components.BottomNavBar
import com.example.myapplication.ui.navigation.BottomNavItem
import com.example.myapplication.ui.screens.auth.AuthViewModel
import com.example.myapplication.ui.screens.community.CommunityContent
import com.example.myapplication.ui.screens.notifications.NotificationsScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.search.SearchScreen
import com.example.myapplication.ui.theme.InnieGreen

enum class HomeTopTab(val title: String) {
    HOME("Home"),
    ALBUM("Album"),
    NEWS("News"),
    SHOTS("Shots")
}

@Composable
fun HomeScreen(
    username: String,
    authViewModel: AuthViewModel
) {
    var currentRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemClick = { item ->
                    currentRoute = item.route
                }
            )
        }
    ) { paddingValues ->
        when (currentRoute) {
            BottomNavItem.Home.route -> {
                HomeContent(
                    username = username,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            BottomNavItem.Community.route -> CommunityContent()
            BottomNavItem.Search.route -> SearchScreen()
            BottomNavItem.Notifications.route -> NotificationsScreen()
            BottomNavItem.Profile.route -> ProfileScreen()
            else -> {
                // Fallback để tránh crash
                HomeContent(
                    username = username,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    username: String,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = HomeTopTab.entries
    val isNewsTab = tabs[selectedTab] == HomeTopTab.NEWS
    val isShotsTab = tabs[selectedTab] == HomeTopTab.SHOTS

    when {
        isNewsTab -> {
            // Special layout for News tab - hero image behind navbar
            NewsTabLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )
        }
        isShotsTab -> {
            // Special layout for Shots tab - video behind navbar
            ShotsTabLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )
        }
        else -> {
            // Normal layout for other tabs
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Logo Header - dùng image (ẩn khi ở News tab)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.innie_movie_logo),
                        contentDescription = "Innie Movie Logo",
                        modifier = Modifier.height(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // TopTab Row (Pill Slider)
                TopTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    tabs = tabs,
                    isOverlay = false
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Content based on selected TopTab
                when (tabs[selectedTab]) {
                    HomeTopTab.HOME -> HomeFeed(
                        username = username,
                        onMovieClick = { /* TODO */ },
                        onAlbumClick = { /* TODO */ },
                        onReviewClick = { /* TODO */ },
                        onProfileClick = { /* TODO */ }
                    )
                    HomeTopTab.ALBUM -> AlbumFeed(
                        onAlbumClick = { /* TODO */ }
                    )
                    HomeTopTab.NEWS -> NewsFeed()
                    HomeTopTab.SHOTS -> ShotsFeed()
                }
            }
        }
    }
}

@Composable
fun TopTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<HomeTopTab>,
    isOverlay: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = if (isOverlay) Color(0xFFB3B3B3) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isOverlay) Color.White.copy(alpha = 0.6f) 
                    else Color(0xFFF5F5F5)
                )
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) InnieGreen else Color.Transparent)
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.title,
                        color = if (isSelected) Color.White 
                               else if (isOverlay) Color(0xFF1A202C).copy(alpha = 0.6f)
                               else Color.Black,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NewsTabLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<HomeTopTab>
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // News Feed content (with hero image at top)
        NewsFeed(
            showHeroFullScreen = true
        )
        
        // Overlay: TopTab and Search bar - add top padding to match logo space from other tabs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp) // Logo height space (12dp + 40dp + 6dp)
        ) {
            // TopTab with blur background
            TopTabBar(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                tabs = tabs,
                isOverlay = true
            )
            
            // Search and Filter bar with blur
            NewsSearchBar()
        }
    }
}

@Composable
fun ShotsTabLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<HomeTopTab>
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Shots Feed content (with video at background)
        ShotsFeed()
        
        // Overlay: TopTab only (no search bar for shots)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp) // Logo height space
        ) {
            // TopTab with blur background
            TopTabBar(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                tabs = tabs,
                isOverlay = true
            )
        }
    }
}

@Composable
fun NewsSearchBar(
    isBlur: Boolean = true,
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .background(
                    if (isBlur) Color.White.copy(alpha = 0.6f) else Color.White,
                    RoundedCornerShape(8.dp)
                )
                .border(0.5.dp, Color(0xFFB3B3B3), RoundedCornerShape(8.dp))
                .clickable { onSearchClick() }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF1A202C)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Find out what's new today?",
                    color = Color(0xFF1A202C),
                    fontSize = 11.sp
                )
            }
        }
        
        // Filter Button
        Box(
            modifier = Modifier
                .height(36.dp)
                .background(
                    if (isBlur) Color.White.copy(alpha = 0.6f) else Color.White,
                    RoundedCornerShape(8.dp)
                )
                .border(0.5.dp, Color(0xFFB3B3B3), RoundedCornerShape(8.dp))
                .clickable { onFilterClick() }
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
                    modifier = Modifier.size(14.dp),
                    tint = Color(0xFF1A202C)
                )
                Text(
                    text = "Filter",
                    color = Color(0xFF1A202C),
                    fontSize = 11.sp
                )
            }
        }
    }
}

// Shared Featured Header component
@Composable
fun FeaturedHeaderWithDividers() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left divider
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.5.dp)
                .background(Color(0xFF1A202C))
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
        Box(
            modifier = Modifier
                .weight(1f)
                .height(0.5.dp)
                .background(Color(0xFF1A202C))
        )
    }
}
