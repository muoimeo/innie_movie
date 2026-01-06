package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.local.entities.MovieItem
import com.example.myapplication.data.watchHistoryList
import com.example.myapplication.ui.screens.search.FilterBottomSheet
import com.example.myapplication.ui.screens.search.FilterState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WatchHistoryScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }

    // Filter movies based on search query (starts with) AND filter state
    val filteredMovies = remember(searchQuery, filterState) {
        watchHistoryList.filter { movie ->
            // Search Query Logic - starts with matching
            val matchesSearch = if (searchQuery.isEmpty()) true else movie.title.lowercase().startsWith(searchQuery.lowercase())

            // Filter Logic (using movie properties if available)
            // Note: MovieItem from watchHistoryList may have different properties
            // For now, we apply search filter only. Extend as needed.
            matchesSearch
        }.let { list ->
            // Sort Logic
            when (filterState.selectedSort) {
                "A-Z" -> list.sortedBy { it.title }
                "Z-A" -> list.sortedByDescending { it.title }
                else -> list
            }
        }
    }

    // Filter BottomSheet
    if (showFilterSheet) {
        FilterBottomSheet(
            filterState = filterState,
            onFilterChange = { filterState = it },
            onApplyFilter = {
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Watch History", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar + Filter Button Row (same style as SearchScreen)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty() && !isFocused) {
                                Text(
                                    text = "Find your movies",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { focusState ->
                                        isFocused = focusState.isFocused
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Filter Button
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.White)
                        .clickable(onClick = { showFilterSheet = true })
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "Filter",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Filter",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title - changes based on search state
            Text(
                text = if (searchQuery.isEmpty()) "All movies" else "Results for \"$searchQuery\"",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Show message if no results found
            if (filteredMovies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No movies found for \"$searchQuery\"",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                // Lưới phim 3 cột - Disable overscroll stretch effect
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 50.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredMovies) { item ->
                            MovieGridItem(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieGridItem(item: MovieItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Image(
                painter = painterResource(id = item.posterRes),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f), // Tỉ lệ poster chuẩn
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}