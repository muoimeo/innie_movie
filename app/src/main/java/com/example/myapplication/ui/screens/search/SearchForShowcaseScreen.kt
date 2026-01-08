package com.example.myapplication.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Search screen specifically for adding movies to showcase slots.
 * When a movie is selected, it's added to the specified slot and the screen navigates back.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForShowcaseScreen(
    slotPosition: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val currentUserId = UserSessionManager.getUserId()
    
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Load all movies initially
    LaunchedEffect(Unit) {
        isLoading = true
        withContext(Dispatchers.IO) {
            searchResults = database.movieDao().getAllMovies().first()
        }
        isLoading = false
    }
    
    // Search movies when query changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            isLoading = true
            withContext(Dispatchers.IO) {
                searchResults = database.movieDao().searchMovies(searchQuery).first()
            }
            isLoading = false
        } else {
            withContext(Dispatchers.IO) {
                searchResults = database.movieDao().getAllMovies().first()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add to Favorites", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search movies...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = InnieGreen,
                    cursorColor = InnieGreen
                )
            )
            
            Text(
                text = "Tap a movie to add it to your favorites",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = InnieGreen)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(searchResults) { movie ->
                        MovieSearchItem(
                            movie = movie,
                            onClick = {
                                // Add to showcase and navigate back
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        // Add to showcase
                                        database.showcaseDao().addToShowcase(
                                            com.example.myapplication.data.local.entities.ShowcaseMovie(
                                                userId = currentUserId,
                                                movieId = movie.id,
                                                slotPosition = slotPosition
                                            )
                                        )
                                        // Also like the movie
                                        database.likeDao().like(
                                            com.example.myapplication.data.local.entities.Like(
                                                userId = currentUserId,
                                                targetType = "movie",
                                                targetId = movie.id
                                            )
                                        )
                                    }
                                    // Navigate back to profile
                                    navController.popBackStack()
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
fun MovieSearchItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(Color(0xFFF5F5F5))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .width(60.dp)
                .height(90.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            movie.year?.let { year ->
                Text(
                    text = year.toString(),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            movie.genres?.let { genresString ->
                val genresList = genresString.split(",").take(2).map { it.trim() }
                Text(
                    text = genresList.joinToString(", "),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
