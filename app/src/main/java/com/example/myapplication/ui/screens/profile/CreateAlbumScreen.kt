package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.entities.MovieItem
import com.example.myapplication.data.watchHistoryList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlbumScreen(
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    var albumName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hashtags by remember { mutableStateOf("") }
    var visibleTo by remember { mutableStateOf("Friends") }
    var whoCanComment by remember { mutableStateOf("Friends") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("New album", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = Color.Gray, fontSize = 16.sp)
                    }
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text("Save", color = Color.Gray, fontSize = 16.sp)
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
            // Album Name Input
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "ALBUM NAME",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                BasicTextField(
                    value = albumName,
                    onValueChange = { albumName = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        if (albumName.isEmpty()) {
                            Text(
                                "Your album is called ...",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Description Input
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "DESCRIPTION",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                BasicTextField(
                    value = description,
                    onValueChange = { description = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    ),
                    decorationBox = { innerTextField ->
                        if (description.isEmpty()) {
                            Text(
                                "Add your album description",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF424242) // Darker gray for placeholder
                                )
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Dropdowns
            DropdownRow(label = "VISIBLE TO?", value = visibleTo, onValueChange = { visibleTo = it })
            HorizontalDivider(color = Color(0xFFF0F0F0))
            DropdownRow(label = "WHO CAN COMMENT?", value = whoCanComment, onValueChange = { whoCanComment = it })
            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Hashtags
            Box(modifier = Modifier.padding(vertical = 12.dp)) {
                if (hashtags.isEmpty()) {
                    Text("ADD HASHTAGS ...", fontSize = 14.sp, color = Color.Gray)
                }
                BasicTextField(
                    value = hashtags,
                    onValueChange = { hashtags = it },
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Add Movies Section
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Add movies ...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00C02B) // Green color
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Movie Grid (Sample)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Mocking selection for demo
                items(watchHistoryList.take(2)) { movie -> // Taking 2 for demo as per image
                   MovieSelectionItem(movie)
                }
            }
        }
    }
}

@Composable
fun DropdownRow(label: String, value: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Friends", "Public", "Private")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                // Icon users (mock)
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces), // Placeholder icon
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color.White
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieSelectionItem(movie: MovieItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painterResource(id = movie.posterRes),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Badge (Mock number 1, 2)
        val index = if (movie.title.contains("Stranger")) "1" else "2"
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp) // Slight overlap
                .size(24.dp)
                .clip(CircleShape)
                .background(Color(0xFF00C02B))
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = index, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
