package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R // Đảm bảo import R của package bạn

// 1. Data Class giữ nguyên
data class ReviewItem(
    val movieTitle: String,
    val posterRes: Int,
    val rating: Int,
    val reviewTitle: String,
    val reviewContent: String
)

// 2. Sample Data: ĐÃ SỬA LẠI
// Tôi thay thế các R.drawable.poster_... bị lỗi bằng android.R.drawable.ic_menu_gallery
// Đây là icon có sẵn trong mọi máy Android để test hiển thị.
val sampleReviews = listOf(
    ReviewItem(
        movieTitle = "Stranger Things: Season 5",
        posterRes = android.R.drawable.ic_menu_gallery, // Placeholder
        rating = 5,
        reviewTitle = "One of the best supernatural world ever",
        reviewContent = "Stranger Things is absolutely as good as everyone says it is. When a show is as talked about as much as this one has been it's hard to live up to."
    ),
    ReviewItem(
        movieTitle = "Pluribus",
        posterRes = android.R.drawable.ic_menu_gallery, // Placeholder
        rating = 4,
        reviewTitle = "Empty and Overhyped",
        reviewContent = "This show just isn't good. The basic idea of virus attack or body snatching is been used so many times before. Characters behave in silly and unrealistic ways."
    ),
    ReviewItem(
        movieTitle = "The beast in me",
        posterRes = android.R.drawable.ic_menu_gallery, // Placeholder
        rating = 3,
        reviewTitle = "Don't compare it, just watch",
        reviewContent = "Matthew Rhys is absolutely menacing, just an OUTSTANDING performance on his part. Truly captivating! Danes"
    ),
    ReviewItem(
        movieTitle = "IT: Welcome to Derry",
        posterRes = android.R.drawable.ic_menu_gallery, // Placeholder
        rating = 1,
        reviewTitle = "For the younger one",
        reviewContent = "For me, this was really garbage, story is copy paste and no new ideas either, the horror is reminding of Peter Jacksons old movie braindead."
    ),
    ReviewItem(
        movieTitle = "The Boys",
        posterRes = android.R.drawable.ic_menu_gallery, // Placeholder
        rating = 5,
        reviewTitle = "It's 4 am",
        reviewContent = "Started watching this brilliant spin on a superhero show after dinner. Now it's 4 am and I have just watched the whole thing."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Reviews", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
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
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Recently viewed",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(sampleReviews) { review ->
                    ReviewListItem(review)
                }
            }
        }
    }
}

@Composable
fun ReviewListItem(review: ReviewItem) {
    Column {
        // Title
        Text(
            text = review.movieTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Stars Row
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            repeat(5) { index ->
                val starColor = if (index < review.rating) Color(0xFFFF3D3D) // Red for filled
                else Color.LightGray

                Text(
                    text = "★",
                    color = starColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content Row (Poster + Comment Box)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Quan trọng: Giúp Box bên phải cao bằng Image bên trái hoặc ngược lại
        ) {
            // Poster
            Image(
                painter = painterResource(id = review.posterRes),
                contentDescription = review.movieTitle,
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp)) // Bo góc mềm mại hơn
                    .background(Color.LightGray), // Background phòng khi ảnh lỗi
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Comment Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = review.reviewTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00C02B) // Màu xanh lá
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val annotatedString = buildAnnotatedString {
                        append(review.reviewContent)
                        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 12.sp)) {
                            append(" ...see more")
                        }
                    }

                    Text(
                        text = annotatedString,
                        fontSize = 12.sp,
                        color = Color.Black,
                        lineHeight = 16.sp,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}