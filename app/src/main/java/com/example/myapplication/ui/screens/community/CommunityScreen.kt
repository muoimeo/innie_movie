package com.example.myapplication.ui.screens.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.ReviewWithMovie
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.SocialRepository
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.flow.first

// Enum cho Tab điều hướng phía trên - 3 tabs
enum class CommunityTopTap(val title: String) {
    Following("Following"),
    For_you("For you"),
    Friends("Friends")
}

@Composable
fun CommunityContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val currentUserId = UserSessionManager.getUserId()
    
    // Get database and repositories
    val database = remember { DatabaseProvider.getDatabase(context) }
    val reviewRepository = remember { ReviewRepository(database.reviewDao()) }
    val socialRepository = remember { SocialRepository(database.socialDao()) }
    
    var selectedTab by remember { mutableIntStateOf(1) } // Default to "For you"
    val tabs = CommunityTopTap.entries
    
    // Load reviews from database for "For you" tab
    val forYouReviews by reviewRepository.getRecentReviewsWithMovies(20).collectAsState(initial = emptyList())
    
    // Following reviews - dynamically fetched based on who current user follows
    val followingUserIds by socialRepository.getFollowing(currentUserId).collectAsState(initial = emptyList())
    val followingReviews by remember(followingUserIds) {
        if (followingUserIds.isEmpty()) {
            kotlinx.coroutines.flow.flowOf(emptyList<ReviewWithMovie>())
        } else {
            database.reviewDao().getReviewsByAuthorsWithMovies(followingUserIds, 50)
        }
    }.collectAsState(initial = emptyList())
    
    // Friends reviews - dynamically fetch friends and their reviews
    val friendIds by remember { kotlinx.coroutines.flow.flowOf(emptyList<String>()) }.collectAsState(initial = emptyList()) // TODO: Add getFriends query
    val friendsReviews = emptyList<ReviewWithMovie>() // Empty for now

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Header - just the pill toggle, no back/arrow buttons
        CommunityHeader(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabs = tabs
        )

        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0E0E0))

        // 2. Content based on selected tab
        when (selectedTab) {
            0 -> { // Following
                if (followingReviews.isEmpty()) {
                    EmptyStateContent(
                        icon = Icons.Default.PersonAdd,
                        title = "You haven't followed anyone yet",
                        subtitle = "Follow people to see their reviews here"
                    )
                } else {
                    ReviewFeedListDb(reviews = followingReviews, navController = navController)
                }
            }
            1 -> { // For you
                if (forYouReviews.isEmpty()) {
                    EmptyStateContent(
                        icon = Icons.Default.GroupOff,
                        title = "No reviews yet",
                        subtitle = "Be the first to write a review!"
                    )
                } else {
                    ReviewFeedListDb(reviews = forYouReviews, navController = navController)
                }
            }
            2 -> { // Friends
                if (friendsReviews.isEmpty()) {
                    EmptyStateContent(
                        icon = Icons.Default.GroupOff,
                        title = "You haven't added any friends yet",
                        subtitle = "Add friends to see their reviews here"
                    )
                } else {
                    ReviewFeedListDb(reviews = friendsReviews, navController = navController)
                }
            }
        }
    }
}

@Composable
fun EmptyStateContent(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CommunityHeader(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<CommunityTopTap>
) {
    // Centered pill toggle only - no back button or arrow
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CommunityPillToggle(selectedTab, onTabSelected, tabs)
    }
}

@Composable
fun CommunityPillToggle(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<CommunityTopTap>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(Color(0xFFF2F2F2))
            .padding(2.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(15.dp))
                    .background(if (isSelected) Color(0xFF00C02B) else Color.Transparent)
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.title,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ReviewFeedListDb(
    reviews: List<ReviewWithMovie>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp) // Add bottom padding for scroll
    ) {
        items(reviews) { reviewWithMovie ->
            CommunityReviewItemDb(review = reviewWithMovie, navController = navController)

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color(0xFFE0E0E0)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommunityReviewItemDb(
    review: ReviewWithMovie,
    navController: NavController
) {
    val currentUserId = UserSessionManager.getUserId()
    val isOwnReview = review.authorId == currentUserId
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isOwnReview) {
                    Modifier
                        .background(Color(0xFFF0FDF4)) // Light green background
                        .padding(16.dp)
                } else {
                    Modifier.padding(16.dp)
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // CỘT 1: AVATAR - green border for own review
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isOwnReview) InnieGreen else Color(0xFF4A5568)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isOwnReview) "You" else review.authorId.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = if (isOwnReview) 10.sp else 16.sp
            )
        }

        // CỘT 2: NỘI DUNG VĂN BẢN
        Column(modifier = Modifier.weight(1f)) {
            // Movie title only - no year
            Text(
                text = review.movieTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Reviewer info and rating - no comment count
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (isOwnReview) {
                    // Own review - highlight
                    Text(
                        text = "Your Review",
                        fontSize = 11.sp,
                        color = InnieGreen,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Review by ", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = review.authorId.replace("user_", "").replace("guest_", ""),
                            fontSize = 11.sp,
                            color = Color(0xFF00C02B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Rating as "4.5★" format
                if (review.rating != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = String.format("%.1f", review.rating),
                            fontSize = 11.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Text("★", color = Color.Red, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.reviewText,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Read more >",
                color = Color(0xFF00C02B),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.ReviewDetail.createRoute(review.review.id))
                    }
                    .padding(top = 4.dp)
            )
        }

        // POSTER CỦA PHIM - using Coil for URL
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(72.dp)
                .height(108.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            AsyncImage(
                model = review.posterUrl,
                contentDescription = review.movieTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        navController.navigate(Screen.MoviePage.createRoute(review.review.movieId))
                    }
            )
        }
    }
}