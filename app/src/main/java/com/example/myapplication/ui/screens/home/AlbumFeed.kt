package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.InnieGreen

data class FakeAlbumDetail(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val posterRes: Int,
    val movieCount: Int
)

val featuredAlbums = listOf(
    FakeAlbumDetail(
        "1", "A-MUST-WATCH OAT!!", "Adrian",
        "Masterpieces that everyone should watch at least once in their lifetime!",
        R.drawable.onboarding_bg, 67
    ),
    FakeAlbumDetail(
        "2", "Top 10 ANIME of 2025", "GOAT",
        "Top 10 must-watch anime this year according to CBR.",
        R.drawable.onboarding_bg, 10
    ),
    FakeAlbumDetail(
        "3", "A-MUST-WATCH OAT!!", "Adrian",
        "Masterpieces that everyone should watch at least once in their lifetime!",
        R.drawable.onboarding_bg, 67
    ),
    FakeAlbumDetail(
        "4", "A-MUST-WATCH OAT!!", "Adrian",
        "Masterpieces that everyone should watch at least once in their lifetime!",
        R.drawable.onboarding_bg, 67
    ),
    FakeAlbumDetail(
        "5", "A-MUST-WATCH OAT!!", "Adrian",
        "Masterpieces that everyone should watch at least once in their lifetime!",
        R.drawable.onboarding_bg, 67
    ),
    FakeAlbumDetail(
        "6", "A-MUST-WATCH OAT!!", "Adrian",
        "Masterpieces that everyone should watch at least once in their lifetime!",
        R.drawable.onboarding_bg, 67
    )
)

@Composable
fun AlbumFeed(
    onAlbumClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search bar row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Find your lists", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = InnieGreen,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { /* TODO: Filter */ },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Filter",
                    tint = Color.DarkGray
                )
            }
        }

        // Featured header
        Text(
            text = "Featured",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = InnieGreen
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // Grid of albums
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(featuredAlbums) { album ->
                AlbumGridCard(
                    album = album,
                    onClick = { onAlbumClick(album.id) }
                )
            }
        }
    }
}

@Composable
fun AlbumGridCard(
    album: FakeAlbumDetail,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Stacked movie posters (simplified)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = album.posterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Movie count badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "and ${album.movieCount} more",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = album.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Author row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "by ",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
                Text(
                    text = album.author,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = InnieGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = "Description: ${album.description}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // View list link
            Text(
                text = "View list >",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = InnieGreen,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
