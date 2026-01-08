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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableFloatStateOf
import com.example.myapplication.R
import com.example.myapplication.ui.components.BottomNavBar
import com.example.myapplication.ui.navigation.BottomNavItem
import com.example.myapplication.ui.screens.auth.AuthViewModel
import com.example.myapplication.ui.screens.community.CommunityContent
import com.example.myapplication.ui.screens.notifications.NotificationsScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.search.SearchScreen
import com.example.myapplication.ui.theme.InnieGreen
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.onFocusChanged

enum class HomeTopTab(val title: String) {
    HOME("Home"),
    ALBUM("Album"),
    NEWS("News"),
    SHOTS("Shots")
}

@Composable
fun HomeScreen(
    username: String,
    authViewModel: AuthViewModel,
    navController: NavController,
    initialTab: Int = 0  // 0=Home, 1=Album, 2=News, 3=Shots
) {
    var currentRoute by rememberSaveable { mutableStateOf(BottomNavItem.Home.route) }
    
    // State for add-favorite mode
    var isAddingFavorite by rememberSaveable { mutableStateOf(false) }
    
    // Get database and like dao for persisting favorites
    val context = androidx.compose.ui.platform.LocalContext.current
    val database = remember { com.example.myapplication.data.local.db.DatabaseProvider.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemClick = { item ->
                    // Reset add mode when switching tabs manually
                    isAddingFavorite = false
                    currentRoute = item.route
                }
            )
        }
    ) { paddingValues ->
        when (currentRoute) {
            BottomNavItem.Home.route -> {
                HomeContent(
                    username = username,
                    navController = navController,
                    modifier = Modifier.padding(paddingValues),
                    initialTab = initialTab
                )
            }
            BottomNavItem.Community.route -> CommunityContent(navController = navController)
            BottomNavItem.Search.route -> SearchScreen(
                navController = navController,
                isAddMode = isAddingFavorite,
                onMovieSelected = { movieId ->
                    // Save this movie as a favorite (like)
                    val userId = com.example.myapplication.data.session.UserSessionManager.getUserId()
                    coroutineScope.launch {
                        database.likeDao().like(
                            com.example.myapplication.data.local.entities.Like(userId, "movie", movieId)
                        )
                    }
                    // Return to profile
                    isAddingFavorite = false
                    currentRoute = BottomNavItem.Profile.route
                }
            )
            BottomNavItem.Notifications.route -> NotificationsScreen(navController = navController, onBackClick = { currentRoute = BottomNavItem.Home.route })
            BottomNavItem.Profile.route -> ProfileScreen(
                navController = navController,
                onNavigateToSearch = { 
                    isAddingFavorite = true
                    currentRoute = BottomNavItem.Search.route 
                },
                onRemoveFavorite = { movieId ->
                    val userId = com.example.myapplication.data.session.UserSessionManager.getUserId()
                    coroutineScope.launch {
                        database.likeDao().unlike(userId, "movie", movieId)
                    }
                }
            )
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
    navController: NavController? = null,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    albumViewModel: AlbumViewModel = viewModel(),
    initialTab: Int = 0
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(initialTab) }
    val tabs = HomeTopTab.entries
    val isNewsTab = tabs[selectedTab] == HomeTopTab.NEWS
    val isShotsTab = tabs[selectedTab] == HomeTopTab.SHOTS
    
    // Collect movies from ViewModel
    val movies by homeViewModel.movies.collectAsState()
    
    // Collect albums from ViewModel
    val albums by albumViewModel.albums.collectAsState()

    when {
        isNewsTab -> {
            // Special layout for News tab - hero image behind navbar
            NewsTabLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs,
                navController = navController
            )
        }
        isShotsTab -> {
            // Special layout for Shots tab - video behind navbar
            ShotsTabLayout(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs,
                navController = navController
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
                        movies = movies,
                        onMovieClick = { movieId ->
                            navController?.navigate(
                                com.example.myapplication.ui.navigation.Screen.MoviePage.createRoute(movieId)
                            )
                        },
                        onAlbumClick = { albumId ->
                            navController?.navigate(
                                com.example.myapplication.ui.navigation.Screen.AlbumDetail.createRoute(albumId)
                            )
                        },
                        onReviewClick = { reviewId ->
                            navController?.navigate(
                                com.example.myapplication.ui.navigation.Screen.ReviewDetail.createRoute(reviewId)
                            )
                        },
                        onProfileClick = { userId ->
                            navController?.navigate(
                                com.example.myapplication.ui.navigation.Screen.UserProfile.createRoute(userId)
                            )
                        },
                        onSeeMoreReviews = {
                            // Community is a tab within this screen, cannot navigate externally
                            // Users should use the bottom nav to go to Community
                        },
                        onSeeMoreAlbums = {
                            // Switch to Album tab (index 1)
                            selectedTab = 1
                        }
                    )
                    HomeTopTab.ALBUM -> AlbumFeed(
                        albums = albums,
                        onAlbumClick = { albumId ->
                            navController?.navigate(
                                com.example.myapplication.ui.navigation.Screen.AlbumDetail.createRoute(albumId)
                            )
                        }
                    )
                    HomeTopTab.NEWS -> NewsFeed(
                        onNewsClick = { news ->
                            navController?.navigate(
                                com.example.myapplication.ui.navigation.Screen.NewsDetail.createRoute(news.id)
                            )
                        }
                    )
                    HomeTopTab.SHOTS -> ShotsFeed(navController = navController)
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
    tabs: List<HomeTopTab>,
    navController: NavController? = null
) {
    // Collapsing search bar logic
    val searchBarHeight = 52.dp
    val density = LocalDensity.current
    val searchBarHeightPx = with(density) { searchBarHeight.toPx() }
    var searchBarOffsetHeightPx by remember { mutableFloatStateOf(0f) }
    
    // Search query state for inline filtering
    var newsSearchQuery by remember { mutableStateOf("") }
    
    // Smoothly hide search bar on scroll down, show on scroll up
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = searchBarOffsetHeightPx + delta
                searchBarOffsetHeightPx = newOffset.coerceIn(-searchBarHeightPx, 0f)
                return Offset.Zero // Don't consume, let content scroll
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // News Feed content (with hero image at top)
        NewsFeed(
            searchQuery = newsSearchQuery,
            showHeroFullScreen = true,
            onNewsClick = { news ->
                navController?.navigate(
                    com.example.myapplication.ui.navigation.Screen.NewsDetail.createRoute(news.id)
                )
            }
        )
        
        // Overlay: TopTab (always visible) and Search bar (collapsible)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp) // Logo height space
        ) {
            // TopTab with blur background - always visible
            Box(modifier = Modifier.zIndex(1f)) {
                TopTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected,
                    tabs = tabs,
                    isOverlay = true
                )
            }
            
            // Search bar - collapses on scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(searchBarHeight)
                    .offset { IntOffset(0, searchBarOffsetHeightPx.toInt()) }
                    .graphicsLayer {
                        // Fade out as it collapses
                        alpha = 1f + (searchBarOffsetHeightPx / searchBarHeightPx)
                    }
                    .zIndex(0f) // Ensure it's below the tabs
            ) {
                // If collapsed, disable the search bar box so it doesn't intercept clicks for items behind it
                val isCollapsed = searchBarOffsetHeightPx <= -searchBarHeightPx
                Box(modifier = Modifier.fillMaxSize().padding(bottom = 0.dp)) {
                    if (!isCollapsed) {
                        // Pass searchQuery state to NewsSearchBar for inline filtering
                        NewsSearchBarFunctional(
                            searchQuery = newsSearchQuery,
                            onSearchChange = { newsSearchQuery = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShotsTabLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<HomeTopTab>,
    navController: NavController? = null
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Shots Feed content (with video at background)
        ShotsFeed(navController = navController)
        
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Bar - Full Width
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
    }
}

// Functional search bar with inline text input (like AlbumFeed)
@Composable
fun NewsSearchBarFunctional(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.6f),
                            Color.White.copy(alpha = 0.9f)
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(0.5.dp, Color(0xFFB3B3B3).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF1A202C)
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty() && !isFocused) {
                        Text(
                            text = "Find out what's new today?",
                            color = Color(0xFF1A202C).copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    }
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = Color(0xFF1A202C),
                            fontSize = 11.sp
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            }
                    )
                }
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
