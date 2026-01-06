package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope 

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: UserProfile = sampleProfile
) {
    // Trạng thái điều khiển Drawer (danh sách đè lên)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                ProfileSideMenuContent(user, navController, drawerState, scope)
            }
        }
    ) {
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
                    // 1. Header Section với nút mở Menu
                    ProfileHeaderSection(
                        user = user,
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )

                    // 2. Social Stats (Followers, Friends...)
                    SocialStatsSection(user)

                    // 3. Activity Stats (Watched, Film this year...)
                    ActivityStatsSection(user)

                    // 4. Favorite Films
                    HorizontalFilmSection(
                        title = "${user.name}'s Favorite Films",
                        posters = user.favoriteFilms
                    )

                    // 5. Recent Watched (Lọc từ data cũ)
                    HorizontalFilmSection(
                        title = "Recent Watched",
                        posters = user.favoriteFilms.reversed().take(5),
                        showStars = true,
                        showSeeAll = true,
                        onSeeAllClick = { navController.navigate(Profile.WatchHistory.route) }
                    )

                    // 6. Recent Reviewed (New Section)
                    RecentReviewedSection(user, onSeeAllClick = { navController.navigate(Profile.Reviews.route) })

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
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

@Composable
fun SocialItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Box(modifier = Modifier.width(40.dp).height(2.dp).background(Color(0xFF00C02B)))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
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
        user: UserProfile,
        navController: NavController,
        drawerState: DrawerState,
        scope: CoroutineScope
    ) {
        Column(modifier = Modifier.fillMaxHeight().padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = user.avatarRes),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(user.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(user.username, color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            val menuItems = listOf(
                Triple("Watch History", Icons.Default.Visibility, Profile.WatchHistory),
                Triple("Albums", Icons.Default.Collections, Profile.Albums),
                Triple("Reviews", Icons.Default.RateReview, Profile.Reviews),
                Triple("Likes", Icons.Default.FavoriteBorder, Profile.Likes),
                Triple("Settings", Icons.Default.Settings, Profile.Settings)
            )

            menuItems.forEach { (title, icon, destination) ->
                NavigationDrawerItem(
                    label = { Text(title, fontSize = 16.sp) },
                    selected = title == "Profile",
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
                        selectedIconColor = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
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
