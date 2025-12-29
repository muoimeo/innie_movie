package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.myapplication.data.local.entities.UserProfile
import com.example.myapplication.data.sampleProfile
import kotlinx.coroutines.launch

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
                ProfileSideMenuContent(user)
            }
        }
    ) {
        Scaffold(
            containerColor = Color.White
        ) { paddingValues ->
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
                    showSeeAll = true
                )

                Spacer(modifier = Modifier.height(30.dp))
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
        SocialItem("500", "Followers")
        SocialItem("67", "Friends")
        SocialItem("420", "Followings")
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
fun HorizontalFilmSection(title: String, posters: List<Int>, showStars: Boolean = false, showSeeAll: Boolean = false) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (showSeeAll) Text(text = "See All", color = Color(0xFF00C02B), fontSize = 12.sp)
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
                        modifier = Modifier.width(85.dp).height(125.dp).clip(RoundedCornerShape(8.dp)),
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
fun ProfileSideMenuContent(user: UserProfile) {
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
            "Profile" to Icons.Default.Person,
            "Watched" to Icons.Default.Visibility,
            "Watchlist" to Icons.Default.List,
            "Albums" to Icons.Default.Collections,
            "Reviews" to Icons.Default.RateReview,
            "Likes" to Icons.Default.FavoriteBorder,
            "Settings" to Icons.Default.Settings
        )
        menuItems.forEach { (title, icon) ->
            NavigationDrawerItem(
                label = { Text(title, fontSize = 16.sp) },
                selected = title == "Profile",
                onClick = { },
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