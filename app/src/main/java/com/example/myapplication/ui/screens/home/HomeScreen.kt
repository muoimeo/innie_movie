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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.myapplication.ui.screens.community.CommunityScreen
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
            BottomNavItem.Community.route -> CommunityScreen()
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Logo Header - dùng image
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

        // TopTab Row (Pill Slider) - với viền xám và nhỏ hơn
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF5F5F5))
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
                            .clickable { selectedTab = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

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

@Composable
fun NewsFeed() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "News Feed\n(Coming Soon)",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun ShotsFeed() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Shots Feed\n(Coming Soon)",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}
