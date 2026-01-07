package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
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
import coil.compose.AsyncImage
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.ui.theme.InnieGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlbumScreen(
    onCancel: () -> Unit,
    onSave: (name: String, description: String, visibility: String, movieIds: List<Int>) -> Unit,
    onAddMovies: () -> Unit = {},
    selectedMovies: List<Movie> = emptyList(),
    onRemoveMovie: (Int) -> Unit = {},
    // Initial values for form restoration
    initialName: String = "",
    initialDescription: String = "",
    initialVisibility: String = "Friends",
    initialHashtags: String = "",
    onFormChange: (name: String, description: String, visibility: String, hashtags: String) -> Unit = { _, _, _, _ -> }
) {
    var albumName by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var hashtags by remember { mutableStateOf(initialHashtags) }
    var visibleTo by remember { mutableStateOf(initialVisibility) }
    var whoCanComment by remember { mutableStateOf("Friends") }
    
    // Update form state when values change
    LaunchedEffect(albumName, description, visibleTo, hashtags) {
        onFormChange(albumName, description, visibleTo, hashtags)
    }

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
                    TextButton(
                        onClick = { 
                            onSave(albumName, description, visibleTo, selectedMovies.map { it.id })
                        },
                        enabled = albumName.isNotBlank() && selectedMovies.isNotEmpty()
                    ) {
                        Text(
                            "Save", 
                            color = if (albumName.isNotBlank() && selectedMovies.isNotEmpty()) InnieGreen else Color.Gray, 
                            fontSize = 16.sp
                        )
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
                                    color = Color.Gray
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
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        if (description.isEmpty()) {
                            Text(
                                "Add your album description",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add movies (${selectedMovies.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InnieGreen
                )
                IconButton(onClick = onAddMovies) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Movies",
                        tint = InnieGreen
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Movie Grid using database movies
            if (selectedMovies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(2.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .clickable { onAddMovies() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tap to add movies", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(selectedMovies) { index, movie ->
                        MovieSelectionItemDb(
                            movie = movie,
                            index = index + 1,
                            onRemove = { onRemoveMovie(movie.id) }
                        )
                    }
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
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
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
fun MovieSelectionItemDb(movie: Movie, index: Int, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp) // Space for badge overflow
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // Remove button (top right)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onRemove() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        // Badge at bottom center, overlapping edge
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 12.dp)
                .size(26.dp)
                .clip(CircleShape)
                .background(InnieGreen)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = index.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
