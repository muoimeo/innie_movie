package com.example.myapplication.ui.screens.search

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration

// Data class
data class MovieItem(
    val title: String,
    val icon: ImageVector = Icons.Default.Search,
    val country: String = "USA",
    val contentType: String = "Movie",
    val genre: String = "Action",
    val version: String = "Original",
    val releaseYear: String = "2024"
)

// Sample Data
val mostSearchedMovies = listOf(
    MovieItem("Stranger Things", country = "USA", contentType = "TV Series", genre = "Sci-Fi", releaseYear = "2022"),
    MovieItem("Black Phone 2", country = "USA", contentType = "Movie", genre = "Horror", releaseYear = "2024"),
    MovieItem("Predator: Badlands", country = "USA", contentType = "Movie", genre = "Action", releaseYear = "2024"),
    MovieItem("Avatar: Fire and Ash", country = "USA", contentType = "Movie", genre = "Sci-Fi", releaseYear = "2024"),
    MovieItem("Frankenstein", country = "UK", contentType = "Movie", genre = "Horror", releaseYear = "2024"),
    MovieItem("Five Nights at Freddy's 2", country = "USA", contentType = "Movie", genre = "Horror", releaseYear = "2024"),
    MovieItem("The Conjuring: Last Rites", country = "USA", contentType = "Movie", genre = "Horror", releaseYear = "2024"),
    MovieItem("Zootopia 2", country = "USA", contentType = "Animation", genre = "Comedy", releaseYear = "2024"),
    MovieItem("Deadpool and Wolverine", country = "USA", contentType = "Movie", genre = "Action", releaseYear = "2024"),
    MovieItem("Pluribus", country = "France", contentType = "Documentary", genre = "Drama", releaseYear = "2023"),
    MovieItem("Breaking Bad", country = "USA", contentType = "TV Series", genre = "Crime", releaseYear = "2010s"),
    MovieItem("The Witcher", country = "USA", contentType = "TV Series", genre = "Fantasy", releaseYear = "2019"),
    MovieItem("Gladiator II", country = "USA", contentType = "Movie", genre = "Action", releaseYear = "2024"),
    MovieItem("Wicked", country = "USA", contentType = "Movie", genre = "Musical", releaseYear = "2024"),
    MovieItem("Moana 2", country = "USA", contentType = "Animation", genre = "Adventure", releaseYear = "2024"),
    MovieItem("Mufasa: The Lion King", country = "USA", contentType = "Animation", genre = "Adventure", releaseYear = "2024"),
    MovieItem("Sonic the Hedgehog 3", country = "USA", contentType = "Movie", genre = "Action", releaseYear = "2024"),
    MovieItem("Kraven the Hunter", country = "USA", contentType = "Movie", genre = "Action", releaseYear = "2024"),
    MovieItem("The Lord of the Rings: The War of the Rohirrim", country = "USA", contentType = "Animation", genre = "Fantasy", releaseYear = "2024"),
    MovieItem("Nosferatu", country = "USA", contentType = "Movie", genre = "Horror", releaseYear = "2024"),
    MovieItem("A Minecraft Movie", country = "USA", contentType = "Movie", genre = "Fantasy", releaseYear = "2025"),
    MovieItem("Captain America: Brave New World", country = "USA", contentType = "Movie", genre = "Action", releaseYear = "2025")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }

    // Filter movies based on search query (starts with) AND filter state
    val filteredMovies = remember(searchQuery, filterState) {
        mostSearchedMovies.filter { movie ->
            // Search Query Logic
            val matchesSearch = if (searchQuery.isEmpty()) true else movie.title.lowercase().startsWith(searchQuery.lowercase())

            // Filter Logic
            val matchesCountry = filterState.selectedCountries.isEmpty() || filterState.selectedCountries.contains(movie.country)
            val matchesType = filterState.selectedContentTypes.isEmpty() || filterState.selectedContentTypes.contains(movie.contentType)
            val matchesGenre = filterState.selectedGenres.isEmpty() || filterState.selectedGenres.contains(movie.genre)
            val matchesVersion = filterState.selectedVersions.isEmpty() || filterState.selectedVersions.contains(movie.version)
            val matchesYear = filterState.selectedYears.isEmpty() || filterState.selectedYears.contains(movie.releaseYear)

            matchesSearch && matchesCountry && matchesType && matchesGenre && matchesVersion && matchesYear
        }.let { list ->
            // Sort Logic
            when (filterState.selectedSort) {
                "A-Z" -> list.sortedBy { it.title }
                "Z-A" -> list.sortedByDescending { it.title }
                // For simplified demo, other sorts act as default order or could be implemented if data supported it
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
                // TODO: Apply filter logic to movie list
            },
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
            text = if (searchQuery.isEmpty()) "Most searched" else "Results for \"$searchQuery\"",
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

            // Movie Grid - 3 columns
            // Disable overscroll stretch effect
            @OptIn(ExperimentalFoundationApi::class)
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp), // Add padding for bottom nav
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMovies) { movie ->
                        MovieGridItem(movie = movie)
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
}

@Composable
fun MovieGridItem(movie: MovieItem) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Movie Poster
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = movie.icon,
                contentDescription = movie.title,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

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
    }
}
