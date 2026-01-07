package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
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
import com.example.myapplication.data.local.entities.UserProfile
import com.example.myapplication.data.sampleProfile
import com.example.myapplication.data.recentReviewedJaws
import kotlinx.coroutines.launch
import com.example.myapplication.ui.navigation.Profile
import com.example.myapplication.data.local.db.DatabaseProvider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope 

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: UserProfile = sampleProfile,
    profileViewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    isOwnProfile: Boolean = true,  // true = own profile with sidebar, false = viewing other user
    targetUserId: String? = null,   // userId when viewing other user's profile
    onNavigateToSearch: () -> Unit = {},
    onRemoveFavorite: (Int) -> Unit = {}
) {
    // Determine which user to load
    val userIdToLoad = targetUserId ?: com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    // Load profile for target user when this composable is created
    LaunchedEffect(userIdToLoad) {
        profileViewModel.loadForUser(userIdToLoad)
    }
    
    // Real stats from database
    val watchedCount by profileViewModel.watchedCount.collectAsState()
    val likeCount by profileViewModel.likeCount.collectAsState()
    val reviewCount by profileViewModel.reviewCount.collectAsState()
    val likedMovies by profileViewModel.likedMovies.collectAsState()
    val recentWatchedMovies by profileViewModel.recentWatchedMovies.collectAsState()
    
    // User display info from ViewModel (now loads target user's data)
    val displayName by profileViewModel.displayName.collectAsState()
    val username by profileViewModel.username.collectAsState()
    val bio by profileViewModel.bio.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()
    val coverUrl by profileViewModel.coverUrl.collectAsState()
    val followersCount by profileViewModel.followersCount.collectAsState()
    val friendsCount by profileViewModel.friendsCount.collectAsState()
    val followingCount by profileViewModel.followingCount.collectAsState()
    
    // Use ViewModel values directly (already loaded for correct user)
    val profileDisplayName = displayName
    val profileUsername = username
    
    // Drawer state (only used for own profile)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Profile content composable (reused for both modes)
    @Composable
    fun ProfileContentBody(showMenuButton: Boolean) {
        Scaffold(
            containerColor = Color.White
        ) { paddingValues ->
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 1. Header Section - menu button only for own profile
                    if (showMenuButton) {
                        ProfileHeaderSectionReal(
                            displayName = profileDisplayName,
                            username = profileUsername,
                            onMenuClick = { scope.launch { drawerState.open() } }
                        )
                    } else {
                        // Header for other user's profile with back button
                        ProfileHeaderSectionOther(
                            displayName = profileDisplayName,
                            username = profileUsername,
                            onBackClick = { navController.popBackStack() },
                            avatarUrl = avatarUrl,
                            coverUrl = coverUrl,
                            bio = bio
                        )
                        
                        // Follow/Friend buttons for other users
                        UserActionButtons(
                            targetUserId = targetUserId ?: "",
                            navController = navController
                        )
                    }

                    // 2. Social Stats (Followers, Friends...)
                    SocialStatsSectionReal(
                        followersCount = followersCount,
                        friendsCount = friendsCount,
                        followingCount = followingCount
                    )

                    // 3. Activity Stats (Watched, Likes, Albums, Reviews)
                    ActivityStatsSectionReal(
                        watchedCount = watchedCount,
                        likesCount = likeCount,
                        reviewCount = reviewCount,
                        user = user
                    )

                    // 4. Favorite Films - Exactly 3 Slots (like profile customization)
                    FavoriteFilmSlots(
                        title = "$profileDisplayName's Favorite Films",
                        movies = likedMovies.take(3),
                        navController = navController,
                        isOwnProfile = isOwnProfile,
                        onNavigateToSearch = onNavigateToSearch,
                        onRemoveFavorite = onRemoveFavorite
                    )

                    // 5. Recent Watched from Database (max 4 movies + 'more' item)
                    if (recentWatchedMovies.isNotEmpty()) {
                        HorizontalFilmSectionFromMovies(
                            title = "Recent Watched",
                            movies = recentWatchedMovies,
                            showSeeAll = isOwnProfile,
                            onSeeAllClick = { navController.navigate(Profile.WatchHistory.route) },
                            maxItems = 4
                        )
                    } else if (isOwnProfile) {
                        // Fallback to template when no watched movies
                        HorizontalFilmSection(
                            title = "Recent Watched",
                            posters = user.favoriteFilms.reversed().take(5),
                            showStars = true,
                            showSeeAll = true,
                            onSeeAllClick = { navController.navigate(Profile.WatchHistory.route) }
                        )
                    }

                    // 6. Recent Reviewed (only for own profile)
                    if (isOwnProfile) {
                        RecentReviewedSection(user, onSeeAllClick = { navController.navigate(Profile.Reviews.route) })
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }

    // Conditionally wrap with drawer (own profile) or show directly (other user)
    if (isOwnProfile) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerContainerColor = Color.White,
                    drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                ) {
                    ProfileSideMenuContent(profileDisplayName, profileUsername, navController, drawerState, scope)
                }
            }
        ) {
            ProfileContentBody(showMenuButton = true)
        }
    } else {
        // Other user's profile - no drawer
        ProfileContentBody(showMenuButton = false)
    }
}

@Composable
fun ProfileHeaderSection(user: UserProfile, onMenuClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
        // Ảnh nền
        Image(
            painter = painterResource(id = user.backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )

        // Nút Menu 3 gạch
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.padding(top = 40.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Avatar và Tên
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = user.avatarRes),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
                    .padding(3.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = user.username, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun SocialStatsSection(user: UserProfile) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SocialItem(user.followersCount.toString(), "Followers")
        SocialItem(user.friendsCount.toString(), "Friends")
        SocialItem(user.followingCount.toString(), "Followings")
    }
}

/**
 * Profile header using ViewModel data with default placeholder avatar/background
 */
@Composable
fun ProfileHeaderSectionReal(
    displayName: String,
    username: String,
    onMenuClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
        // Default grey background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color(0xFF2D3748), Color(0xFF1A202C))
                    )
                )
        )

        // Menu button with circular background for visibility
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .padding(top = 40.dp, start = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Avatar and Name
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Default person icon as avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
                    .background(Color(0xFF4A5568)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = displayName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = username, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

/**
 * Profile header for OTHER users (with back button instead of menu)
 */
@Composable
fun ProfileHeaderSectionOther(
    displayName: String,
    username: String,
    onBackClick: () -> Unit,
    avatarUrl: String? = null,
    coverUrl: String? = null,
    bio: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            // Cover image or gradient background
            if (coverUrl != null) {
                coil.compose.AsyncImage(
                    model = coverUrl,
                    contentDescription = "Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color(0xFF2D3748), Color(0xFF1A202C))
                            )
                        )
                )
            }

            // Back button with circular background for visibility
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 40.dp, start = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Avatar and Name
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar - load from URL or show default icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .background(Color(0xFF4A5568)),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUrl != null) {
                        coil.compose.AsyncImage(
                            model = avatarUrl,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = displayName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = username, fontSize = 14.sp, color = Color.Gray)
            }
        }
        
        // Bio section
        if (!bio.isNullOrBlank()) {
            Text(
                text = bio,
                fontSize = 13.sp,
                color = Color(0xFF555555),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

/**
 * Social stats section using ViewModel values
 */
@Composable
fun SocialStatsSectionReal(
    followersCount: Int,
    friendsCount: Int,
    followingCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SocialItem(followersCount.toString(), "Followers")
        SocialItem(friendsCount.toString(), "Friends")
        SocialItem(followingCount.toString(), "Followings")
    }
}

@Composable
fun SocialItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Box(modifier = Modifier.width(40.dp).height(2.dp).background(Color(0xFF00C02B)))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

/**
 * Follow & Add Friend buttons for other users' profiles
 */
@Composable
fun UserActionButtons(
    targetUserId: String,
    navController: NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val socialRepository = remember { com.example.myapplication.data.repository.SocialRepository(database.socialDao()) }
    val currentUserId = com.example.myapplication.data.session.UserSessionManager.getUserId()
    val scope = rememberCoroutineScope()
    
    var isFollowing by remember { mutableStateOf(false) }
    var friendStatus by remember { mutableStateOf<String?>(null) } // null, pending, accepted
    
    // Load initial state
    LaunchedEffect(targetUserId) {
        isFollowing = socialRepository.isFollowing(currentUserId, targetUserId)
        friendStatus = socialRepository.getFriendshipStatus(currentUserId, targetUserId)
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Follow/Following Button
        Button(
            onClick = {
                scope.launch {
                    val newState = socialRepository.toggleFollow(currentUserId, targetUserId)
                    isFollowing = newState
                }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) Color(0xFFE0E0E0) else com.example.myapplication.ui.theme.InnieGreen
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (isFollowing) "Following" else "Follow",
                color = if (isFollowing) Color.Black else Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        // Add Friend / Friends / Pending Button
        OutlinedButton(
            onClick = {
                scope.launch {
                    when (friendStatus) {
                        null -> {
                            socialRepository.sendFriendRequest(currentUserId, targetUserId)
                            friendStatus = "pending"
                        }
                        "pending" -> {
                            // Already pending, do nothing
                        }
                        "accepted" -> {
                            socialRepository.unfriend(currentUserId, targetUserId)
                            friendStatus = null
                        }
                    }
                }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                1.dp,
                when (friendStatus) {
                    "accepted" -> com.example.myapplication.ui.theme.InnieGreen
                    "pending" -> Color.Gray
                    else -> Color.Black
                }
            )
        ) {
            Text(
                text = when (friendStatus) {
                    "accepted" -> "Friends ✓"
                    "pending" -> "Pending..."
                    else -> "Add Friend"
                },
                color = when (friendStatus) {
                    "accepted" -> com.example.myapplication.ui.theme.InnieGreen
                    "pending" -> Color.Gray
                    else -> Color.Black
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ActivityStatsSection(user: UserProfile) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActivityStatItem(user.watchedCount, "Watched", Color(0xFF00C02B))
        ActivityStatItem(user.filmsThisYear, "Film This Year", Color(0xFFB34393))
        ActivityStatItem(user.albumsCount, "Albums", Color(0xFF00C02B))
        ActivityStatItem(user.reviewsCount, "Review", Color(0xFFB34393))
    }
}

@Composable
fun ActivityStatItem(count: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

/**
 * Activity stats section using real database values for Watched and Likes
 */
@Composable
fun ActivityStatsSectionReal(
    watchedCount: Int,
    likesCount: Int,
    reviewCount: Int,
    user: UserProfile
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActivityStatItem(watchedCount, "Watched", Color(0xFF00C02B))
        ActivityStatItem(likesCount, "Likes", Color(0xFFB34393))
        ActivityStatItem(user.albumsCount, "Albums", Color(0xFF00C02B))
        ActivityStatItem(reviewCount, "Reviews", Color(0xFFB34393))
    }
}

/**
 * Horizontal film section using Movie entities from database
 * @param maxItems Max movies to show before '... and X more' item (null = show all)
 */
@Composable
fun HorizontalFilmSectionFromMovies(
    title: String,
    movies: List<com.example.myapplication.data.local.entities.Movie>,
    showSeeAll: Boolean = false,
    onSeeAllClick: () -> Unit = {},
    maxItems: Int? = null
) {
    val displayMovies = if (maxItems != null && movies.size > maxItems) movies.take(maxItems) else movies
    val remainingCount = if (maxItems != null && movies.size > maxItems) movies.size - maxItems else 0
    
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            if (showSeeAll) {
                Text(
                    text = "See All",
                    fontSize = 12.sp,
                    color = Color(0xFF00C02B),
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }
        }
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayMovies) { movie ->
                coil.compose.AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Show "and X more" item if there are more movies
            if (remainingCount > 0) {
                item {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0E0E0))
                            .clickable { onSeeAllClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+$remainingCount\nmore",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF666666),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * Favorite Films Section with exactly 3 slots
 * - Filled slots show movie posters (tap to view, long-press to remove)
 * - Empty slots show dashed border placeholder, clicking navigates to Search
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteFilmSlots(
    title: String,
    movies: List<com.example.myapplication.data.local.entities.Movie>,
    navController: NavController,
    isOwnProfile: Boolean = true,
    onNavigateToSearch: () -> Unit,
    onRemoveFavorite: (Int) -> Unit = {}
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    var movieToRemove by remember { mutableStateOf<com.example.myapplication.data.local.entities.Movie?>(null) }
    
    // Remove confirmation dialog
    if (showRemoveDialog && movieToRemove != null) {
        AlertDialog(
            onDismissRequest = { 
                showRemoveDialog = false
                movieToRemove = null
            },
            title = { Text("Remove from Favorites?") },
            text = { Text("Remove \"${movieToRemove?.title}\" from your favorites?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        movieToRemove?.let { onRemoveFavorite(it.id) }
                        showRemoveDialog = false
                        movieToRemove = null
                    }
                ) {
                    Text("Remove", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showRemoveDialog = false
                    movieToRemove = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    Column(modifier = Modifier.padding(top = 24.dp)) {
        // Title
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 3 Fixed Slots in a Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) { index ->
                val movie = movies.getOrNull(index)
                
                if (movie != null) {
                    // Filled slot - show movie poster
                    coil.compose.AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {
                                    navController.navigate(
                                        com.example.myapplication.ui.navigation.Screen.MoviePage.createRoute(movie.id)
                                    )
                                },
                                onLongClick = {
                                    if (isOwnProfile) {
                                        movieToRemove = movie
                                        showRemoveDialog = true
                                    }
                                }
                            ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Empty slot - show placeholder
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 2.dp,
                                color = Color(0xFFCCCCCC),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(Color(0xFFF5F5F5))
                            .then(
                                if (isOwnProfile) {
                                    Modifier.clickable {
                                        onNavigateToSearch()
                                    }
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isOwnProfile) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color(0xFFAAAAAA),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Add",
                                    fontSize = 10.sp,
                                    color = Color(0xFFAAAAAA)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalFilmSection(
    title: String,
    posters: List<Int>,
    showStars: Boolean = false,
    showSeeAll: Boolean = false,
    onSeeAllClick: () -> Unit = {}) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (showSeeAll) {
                Text(
                    text = "See All",
                    color = Color(0xFF00C02B),
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onSeeAllClick() }
                )

            }

        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(posters) { posterId ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = posterId),
                        contentDescription = null,
                        modifier = Modifier.width(85.dp).height(125.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    if (showStars) {
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            repeat(4) { Text("★", color = Color.Red, fontSize = 10.sp) }
                            Text("½", color = Color.Red, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}
    @Composable
    fun ProfileSideMenuContent(
        displayName: String,
        username: String,
        navController: NavController,
        drawerState: DrawerState,
        scope: CoroutineScope
    ) {
        Column(modifier = Modifier.fillMaxHeight().padding(24.dp)) {
            // User info header - using ViewModel data
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Default person icon avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4A5568)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(displayName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(username, color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            
            // Main menu items (excluding Settings)
            val mainMenuItems = listOf(
                Triple("Watch History", Icons.Default.Visibility, Profile.WatchHistory),
                Triple("Likes", Icons.Default.FavoriteBorder, Profile.Likes),
                Triple("Watchlist", Icons.Default.BookmarkBorder, Profile.Watchlist),
                Triple("Reviews", Icons.Default.RateReview, Profile.Reviews),
                Triple("Albums", Icons.Default.Collections, Profile.Albums)
            )

            mainMenuItems.forEach { (title, icon, destination) ->
                NavigationDrawerItem(
                    label = { Text(title, fontSize = 16.sp) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate(destination.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(icon, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF00C02B),
                        selectedTextColor = Color.White,
                        selectedIconColor = Color.White,
                        unselectedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
            
            // Push Settings to bottom
            Spacer(modifier = Modifier.weight(1f))
            
            // Settings item pinned at bottom
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )
            
            NavigationDrawerItem(
                label = { Text("Settings", fontSize = 16.sp) },
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(Profile.Settings.route) {
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFF00C02B),
                    selectedTextColor = Color.White,
                    selectedIconColor = Color.White,
                    unselectedContainerColor = Color.Transparent
                ),
                modifier = Modifier.padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))
        }
    }

@Composable
fun RecentReviewedSection(user: UserProfile, onSeeAllClick: () -> Unit) {
    val review = recentReviewedJaws // Using the mock data added to sample_data.kt

    Column(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${user.name}'s Recent Reviewed", // Or just "Kyran's Recent Reviewed" hardcoded if specifically requested, but dynamic is better
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See All",
                color = Color(0xFF00C02B),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Review Card content
        // The background seems to have a slight gradient or just off-white/grayish tint in the image bottom
        // Using a basic Card or Column with background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            // Left Content (Avatar, Review Info)
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = review.avatarRes),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = review.movieTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = review.year,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Review by ",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = review.reviewerName,
                                fontSize = 11.sp,
                                color = Color(0xFF00C02B), // Green color for name
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Stars
                            Row {
                                repeat(5) {
                                    Text("★", color = Color.Red, fontSize = 10.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Comments",
                                tint = Color.Gray,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = review.commentCount.toString(), // Mock comment count
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = review.reviewText,
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    lineHeight = 16.sp,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right Content (Poster)
            Image(
                painter = painterResource(id = review.posterRes),
                contentDescription = "Poster",
                modifier = Modifier
                    .width(80.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
