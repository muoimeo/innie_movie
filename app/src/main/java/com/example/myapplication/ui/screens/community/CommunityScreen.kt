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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.example.myapplication.data.local.entities.MovieReview
import com.example.myapplication.data.followingReviews
import com.example.myapplication.data.forYouReviews
import com.example.myapplication.ui.theme.*
import androidx.compose.foundation.layout.FlowRow
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.Screen

// Enum cho Tab điều hướng phía trên
enum class CommunityTopTap(val title: String) {
    Following("Following"),
    For_you("For you"),
}

@Composable
fun CommunityContent(
    modifier: Modifier = Modifier,
    navController: NavController // THÊM THAM SỐ NÀY ĐỂ TRUYỀN XUỐNG DƯỚI
) {
    var selectedTab by remember { mutableIntStateOf(1) }
    val tabs = CommunityTopTap.entries

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Header
        CommunityHeader(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            tabs = tabs,
            navController = navController // TRUYỀN NAVCONTROLLER
        )

        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0E0E0))

        // 2. Danh sách Feed
        val data = if (selectedTab == 0) followingReviews else forYouReviews

        // TRUYỀN NAVCONTROLLER VÀO ĐÂY (Hết lỗi dòng 66)
        ReviewFeedList(reviews = data, navController = navController)
    }
}

@Composable
fun CommunityHeader(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<CommunityTopTap>,
    navController: NavController // THÊM THAM SỐ
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(24.dp).clickable { navController.popBackStack() }
        )

        Box(modifier = Modifier.width(200.dp)) {
            CommunityPillToggle(selectedTab, onTabSelected, tabs)
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "More",
            modifier = Modifier.size(24.dp).clickable { /* Xử lý options */ }
        )
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
fun ReviewFeedList(
    reviews: List<MovieReview>,
    navController: NavController // NHẬN NAVCONTROLLER
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(reviews) { review ->
            // TRUYỀN TIẾP XUỐNG ITEM
            CommunityReviewItem(review = review, navController = navController)

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
fun CommunityReviewItem(
    review: MovieReview,
    navController: NavController // NHẬN NAVCONTROLLER
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // CỘT 1: AVATAR
        Image(
            painter = painterResource(id = review.avatarRes),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // CỘT 2: NỘI DUNG VĂN BẢN
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = review.movieTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = review.year, color = Color.Gray, fontSize = 11.sp)
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Review by ", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = review.reviewerName,
                            fontSize = 11.sp,
                            color = Color(0xFF00C02B),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row {
                            repeat(review.rating) {
                                Text("★", color = Color.Red, fontSize = 8.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(8.dp),
                            tint = Color.Gray
                        )
                        Text(" ${review.commentCount}", fontSize = 8.sp, color = Color.Gray)
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
            }

            Text(
                text = "Read more >",
                color = Color(0xFF00C02B),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable {
                        // CŨNG CÓ THỂ ĐIỀU HƯỚNG Ở ĐÂY NẾU MUỐN
                        navController.navigate(Screen.MoviePage.createRoute(review.posterRes))
                    }
                    .padding(top = 4.dp)
            )
        }

        // POSTER CỦA PHIM (KHI CLICK SẼ CHUYỂN TRANG)
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(72.dp)
                .height(108.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Image(
                painter = painterResource(id = review.posterRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        // SỬ DỤNG NGOẶC NHỌN VÀ POSTER RES LÀM ID
                        navController.navigate(Screen.MoviePage.createRoute(review.posterRes))
                    }
            )
        }
    }
}