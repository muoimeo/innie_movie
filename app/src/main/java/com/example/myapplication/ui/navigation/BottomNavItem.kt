package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    
    data object Community : BottomNavItem(
        route = "community_tab",
        label = "Community",
        selectedIcon = Icons.Filled.Groups, 
        unselectedIcon = Icons.Outlined.Groups
    )

    
    data object Search : BottomNavItem(
        route = "search_tab",
        label = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )
    
    data object Notifications : BottomNavItem(
        route = "notifications_tab",
        label = "Notifications",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications
    )
    
    data object Profile : BottomNavItem(
        route = "profile_tab",
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
    
    companion object {
        val items = listOf(Home, Community, Search, Notifications, Profile)
    }

    object MoviePage : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: Int) = "movie_detail/$movieId"
    }


}

sealed class Profile(val route: String) {
    data object WatchHistory : Profile("WatchHistory")
    data object Likes : Profile("likes")
    data object Watchlist : Profile("watchlist")
    data object Reviews : Profile("reviews")
    data object Albums : Profile("albums")
    data object Settings : Profile("settings")
}


