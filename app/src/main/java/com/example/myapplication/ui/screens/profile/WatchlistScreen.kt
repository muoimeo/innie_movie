package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.repository.WatchlistRepository
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.InnieGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(navController: NavController) {
    val context = LocalContext.current
    
    // Get database and repository
    val database = remember { DatabaseProvider.getDatabase(context) }
    val watchlistRepository = remember { WatchlistRepository(database.watchlistDao()) }
    val currentUserId = UserSessionManager.getUserId()
    
    // Load watchlist movies from database
    val watchlistMovies by watchlistRepository.getAllWatchlistMovies(currentUserId).collectAsState(initial = emptyList())
    
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    
    // Filter states
    var selectedType by remember { mutableStateOf("Any") }
    var selectedRuntime by remember { mutableStateOf("Any") }
    var selectedGenres by remember { mutableStateOf<Set<String>>(emptySet()) }
    var sortBy by remember { mutableStateOf("Date Added") }
    
    // Watched confirmation dialog
    var showWatchedDialog by remember { mutableStateOf(false) }
    var movieToMarkWatched by remember { mutableStateOf<Movie?>(null) }
    
    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Filter logic
    val filteredMovies = remember(searchQuery, selectedType, selectedRuntime, selectedGenres, watchlistMovies) {
        watchlistMovies.filter { movie ->
            // Search filter
            val matchesSearch = searchQuery.isEmpty() || 
                movie.title.contains(searchQuery, ignoreCase = true)
            
            // Type filter
            val matchesType = selectedType == "Any" || movie.mediaType == selectedType.lowercase()
            
            // Runtime filter
            val matchesRuntime = when (selectedRuntime) {
                "Any" -> true
                "<90 min" -> (movie.runtimeMinutes ?: 0) < 90
                "90-120 min" -> (movie.runtimeMinutes ?: 0) in 90..120
                ">120 min" -> (movie.runtimeMinutes ?: 0) > 120
                else -> true
            }
            
            // Genres filter
            val matchesGenres = selectedGenres.isEmpty() || 
                selectedGenres.any { genre -> movie.genres?.contains(genre, ignoreCase = true) == true }
            
            matchesSearch && matchesType && matchesRuntime && matchesGenres
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Watchlist", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar with filter button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search movies...", color = Color.Gray) },
                    leadingIcon = { 
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) 
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InnieGreen,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Filter button
                FilledTonalIconButton(
                    onClick = { showFilterSheet = true },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (hasActiveFilters(selectedType, selectedRuntime, selectedGenres)) 
                            InnieGreen.copy(alpha = 0.2f) else Color(0xFFF0F0F0)
                    )
                ) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = if (hasActiveFilters(selectedType, selectedRuntime, selectedGenres)) 
                            InnieGreen else Color.DarkGray
                    )
                }
            }
            
            // Count and sort info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredMovies.size} Titles",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Sorted by $sortBy",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            // Movie grid
            if (filteredMovies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.MovieFilter,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "No results found" else "Your watchlist is empty",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        if (watchlistMovies.isEmpty()) {
                            Text(
                                text = "Add movies from Movie pages",
                                fontSize = 14.sp,
                                color = Color.Gray.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMovies) { movie ->
                        WatchlistMovieCard(
                            movie = movie,
                            onMovieClick = {
                                navController.navigate(Screen.MoviePage.createRoute(movie.id))
                            },
                            onEyeClick = {
                                movieToMarkWatched = movie
                                showWatchedDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Watched confirmation dialog
    if (showWatchedDialog && movieToMarkWatched != null) {
        AlertDialog(
            onDismissRequest = { 
                showWatchedDialog = false
                movieToMarkWatched = null
            },
            icon = {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = null,
                    tint = InnieGreen,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { 
                Text(
                    "Have you watched this?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = { 
                Text(
                    "\"${movieToMarkWatched?.title}\"\n\nIf you've already watched this, we'll remove it from your watchlist.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val movie = movieToMarkWatched
                        if (movie != null) {
                            scope.launch {
                                // Get default category and remove from watchlist
                                val categories = watchlistRepository.getCategoriesByUser(currentUserId)
                                categories.collect { cats ->
                                    if (cats.isNotEmpty()) {
                                        watchlistRepository.removeFromWatchlist(cats.first().id, movie.id)
                                        snackbarHostState.showSnackbar("${movie.title} removed from Watchlist")
                                    }
                                    return@collect
                                }
                            }
                        }
                        showWatchedDialog = false
                        movieToMarkWatched = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InnieGreen)
                ) {
                    Text("Yes, I've watched it")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showWatchedDialog = false
                        movieToMarkWatched = null
                    }
                ) {
                    Text("No, not yet", color = Color.Gray)
                }
            }
        )
    }
    
    // Filter bottom sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = Color.White
        ) {
            FilterBottomSheetContent(
                selectedType = selectedType,
                onTypeChange = { selectedType = it },
                selectedRuntime = selectedRuntime,
                onRuntimeChange = { selectedRuntime = it },
                selectedGenres = selectedGenres,
                onGenresChange = { selectedGenres = it },
                sortBy = sortBy,
                onSortChange = { sortBy = it },
                onClear = {
                    selectedType = "Any"
                    selectedRuntime = "Any"
                    selectedGenres = emptySet()
                    sortBy = "Date Added"
                },
                onApply = { showFilterSheet = false }
            )
        }
    }
}

private fun hasActiveFilters(type: String, runtime: String, genres: Set<String>): Boolean {
    return type != "Any" || runtime != "Any" || genres.isNotEmpty()
}

@Composable
private fun WatchlistMovieCard(
    movie: Movie,
    onMovieClick: () -> Unit,
    onEyeClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onMovieClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Eye icon button (top-right corner) - rounded rectangle matching poster
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomStart = 8.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onEyeClick() }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = "Mark as watched",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = movie.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    selectedType: String,
    onTypeChange: (String) -> Unit,
    selectedRuntime: String,
    onRuntimeChange: (String) -> Unit,
    selectedGenres: Set<String>,
    onGenresChange: (Set<String>) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit,
    onClear: () -> Unit,
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onClear) {
                Text("Clear", color = InnieGreen)
            }
            Text("Filter", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(containerColor = InnieGreen)
            ) {
                Text("Apply")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sort By
        FilterSection(title = "Sort By") {
            FilterChipRow(
                options = listOf("Date Added", "Rating", "Title", "Year"),
                selected = sortBy,
                onSelect = onSortChange
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Type
        FilterSection(title = "Type") {
            FilterChipRow(
                options = listOf("Any", "Movie", "Series"),
                selected = selectedType,
                onSelect = onTypeChange
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Runtime
        FilterSection(title = "Runtime") {
            FilterChipRow(
                options = listOf("Any", "<90 min", "90-120 min", ">120 min"),
                selected = selectedRuntime,
                onSelect = onRuntimeChange
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Genres
        FilterSection(title = "Genres") {
            val genres = listOf("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Thriller", "Adventure", "Crime")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genre ->
                    FilterChip(
                        selected = genre in selectedGenres,
                        onClick = {
                            onGenresChange(
                                if (genre in selectedGenres) selectedGenres - genre
                                else selectedGenres + genre
                            )
                        },
                        label = { Text(genre, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = InnieGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterChipRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = InnieGreen,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
