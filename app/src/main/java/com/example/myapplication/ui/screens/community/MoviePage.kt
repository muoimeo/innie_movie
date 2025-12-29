package com.example.myapplication.ui.screens.community

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.ui.theme.InnieGreen

@Composable
fun MoviePage(
    movieId: Int,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val mainGreen = InnieGreen

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // --- PHẦN 1: HEADER BANNER (ẢNH NỀN CONG) ---
            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                Image(
                    painter = painterResource(id = movieId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(bottomEnd = 160.dp)), // Tạo đường cong góc dưới
                    contentScale = ContentScale.Crop
                )

                // Nút Back
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.White, CircleShape)
                        .size(36.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }

            // --- PHẦN 2: THÔNG TIN CHÍNH (POSTER ĐÈ & TIÊU ĐỀ) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-80).dp), // Đẩy phần này lên trên ảnh nền
                verticalAlignment = Alignment.Bottom
            ) {
                // Poster nhỏ
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(130.dp).height(190.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = movieId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text thông tin
                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(
                        text = "The Batman",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "2022", color = Color.Gray, fontSize = 14.sp)
                        Text(text = "  •  ", color = Color.Gray)
                        Text(text = "176 mins", color = Color.Gray, fontSize = 14.sp)
                    }
                    Text(text = "Directed by Matt Reeves", fontSize = 13.sp, color = Color.Black)
                }
            }

            // --- PHẦN 3: STATS & ACTION BUTTONS ---
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-40).dp)
            ) {
                // Stats (Watch, Heart, List)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    StatItem(Icons.Default.Visibility, "40k")
                    StatItem(Icons.Default.Favorite, "30k", Color.Red)
                    StatItem(Icons.Default.List, "12k", Color(0xFF2196F3))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút bấm xanh
                ActionButton(text = "Rate or Review", icon = Icons.Default.Edit, color = mainGreen)
                ActionButton(text = "Add to Albums", icon = Icons.Default.List, color = mainGreen)
                ActionButton(text = "Add to Watchlist", icon = Icons.Default.Add, color = mainGreen)

                Spacer(modifier = Modifier.height(24.dp))

                // Synopsis
                Text(text = "UNMASK THE TRUTH.", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(
                    text = "In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family...",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Casts
                Text(text = "Casts", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(
                    modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    repeat(5) { // Mô phỏng các diễn viên
                        Box(modifier = Modifier.size(55.dp).clip(CircleShape).background(Color.LightGray))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, tint: Color = Color.Gray) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ActionButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(45.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}