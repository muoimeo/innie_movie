package com.example.myapplication.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    navController: NavController? = null,
    isAddMode: Boolean = false,
    onMovieSelected: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }

    // Load ALL movies from database
    val allMovies by database.movieDao().getAllMovies().collectAsState(initial = emptyList())
    
    // Filter movies based on search query and filter state
    val filteredMovies = remember(searchQuery, filterState, allMovies) {
        allMovies.filter { movie ->
            // Search Query Logic - title contains query (case insensitive)
            val matchesSearch = if (searchQuery.isEmpty()) true 
                else movie.title.lowercase().contains(searchQuery.lowercase())

            // Media Type filter
            val matchesMediaType = filterState.selectedMediaTypes.isEmpty() ||
                filterState.selectedMediaTypes.any { type ->
                    movie.mediaType.equals(type, ignoreCase = true)
                }

            // Genre filter - check if movie's genres contain any selected genre
            val matchesGenre = filterState.selectedGenres.isEmpty() || 
                filterState.selectedGenres.any { genre -> 
                    movie.genres?.contains(genre, ignoreCase = true) == true 
                }
            
            // Year filter - check if movie's year matches
            val matchesYear = filterState.selectedYears.isEmpty() ||
                filterState.selectedYears.any { yearFilter ->
                    when {
                        yearFilter == "2024" -> movie.year == 2024
                        yearFilter == "2023" -> movie.year == 2023
                        yearFilter == "2022" -> movie.year == 2022
                        yearFilter == "2021" -> movie.year == 2021
                        yearFilter == "2020" -> movie.year == 2020
                        yearFilter == "2019" -> movie.year == 2019
                        yearFilter == "2010s" -> movie.year in 2010..2019
                        yearFilter == "2000s" -> movie.year in 2000..2009
                        yearFilter == "1990s" -> movie.year in 1990..1999
                        yearFilter == "Older" -> (movie.year ?: 0) < 1990
                        else -> false
                    }
                }

            // Rating filter
            val matchesRating = if (filterState.minRating == "Any") true
            else {
                val minVal = filterState.minRating.replace("+", "").toFloatOrNull() ?: 0f
                movie.rating >= minVal
            }

            matchesSearch && matchesMediaType && matchesGenre && matchesYear && matchesRating
        }.let { list ->
            // Sort Logic
            when (filterState.selectedSort) {
                "A-Z" -> list.sortedBy { it.title }
                "Z-A" -> list.sortedByDescending { it.title }
                "Newest" -> list.sortedByDescending { it.year }
                "Oldest" -> list.sortedBy { it.year }
                "Rating" -> list.sortedByDescending { it.rating }
                "Popular" -> list.sortedByDescending { it.rating } // Fallback to rating
                else -> list
            }
        }
    }

    // Filter BottomSheet
    if (showFilterSheet) {
        FilterBottomSheet(
            filterState = filterState,
            onFilterChange = { filterState = it },
            onApplyFilter = { showFilterSheet = false },
            onDismiss = { showFilterSheet = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        
        // Add Mode Header
        if (isAddMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select a movie to add to favorites",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = InnieGreen
                )
            }
        }

        // Search Bar + Filter Button Row
        SearchBarWithFilter(
            searchQuery = searchQuery,
            isFocused = isFocused,
            onSearchChange = { searchQuery = it },
            onFocusChange = { isFocused = it },
            onFilterClick = { showFilterSheet = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title - changes based on search state
        Text(
            text = if (searchQuery.isEmpty()) "All Movies" else "Results for \"$searchQuery\"",
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
                    text = if (searchQuery.isEmpty()) "No movies in database" else "No movies found for \"$searchQuery\"",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        } else {
            // Movie Grid - 3 columns using actual database movies
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMovies) { movie ->
                        MovieGridItemDb(
                            movie = movie,
                            onClick = {
                                if (isAddMode && onMovieSelected != null) {
                                    // Add mode: call callback to add to favorites
                                    onMovieSelected(movie.id)
                                } else {
                                    // Normal mode: navigate to movie page
                                    navController?.navigate(Screen.MoviePage.createRoute(movie.id))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBarWithFilter(
    searchQuery: String,
    isFocused: Boolean,
    onSearchChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onFilterClick: () -> Unit
) {
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
                            text = "Looking for something?",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                onFocusChange(focusState.isFocused)
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Filter Button with InnieGreen accent
        Box(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = InnieGreen,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.White)
                .clickable(onClick = onFilterClick)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = "Filter",
                    tint = InnieGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Filter",
                    fontSize = 14.sp,
                    color = InnieGreen,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun MovieGridItemDb(
    movie: Movie,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        // Movie Poster using Coil
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Movie Title
        Text(
            text = movie.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        // Movie Year
        movie.year?.let { year ->
            Text(
                text = year.toString(),
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}
