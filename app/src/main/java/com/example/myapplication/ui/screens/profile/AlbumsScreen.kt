package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.navigation.BottomNavItem
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.screens.search.SearchScreen
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    navController: NavController,
    viewModel: AlbumsViewModel = viewModel()
) {
    val context = LocalContext.current
    val database = remember { DatabaseProvider.getDatabase(context) }
    val albumRepository = remember { AlbumRepository(database.albumDao()) }
    val coroutineScope = rememberCoroutineScope()
    
    // Use rememberSaveable to preserve state across configuration changes and recompositions
    var isCreatingAlbum by rememberSaveable { mutableStateOf(false) }
    var isSearchingMovies by rememberSaveable { mutableStateOf(false) }
    
    // Store selected movie IDs (primitive list) with rememberSaveable
    var selectedMovieIds by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    
    // Load actual Movie objects from the selected IDs
    var selectedMoviesForAlbum by remember { mutableStateOf<List<Movie>>(emptyList()) }
    
    // Form data preserved with rememberSaveable
    var savedAlbumName by rememberSaveable { mutableStateOf("") }
    var savedDescription by rememberSaveable { mutableStateOf("") }
    var savedVisibility by rememberSaveable { mutableStateOf("Friends") }
    var savedHashtags by rememberSaveable { mutableStateOf("") }
    
    // Delete confirmation dialog state
    var showDeleteDialog by remember { mutableStateOf(false) }
    var albumToDelete by remember { mutableStateOf<Album?>(null) }
    
    // Delete confirmation dialog
    if (showDeleteDialog && albumToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                albumToDelete = null
            },
            title = { Text("Delete Album?") },
            text = { Text("Are you sure you want to delete \"${albumToDelete?.title}\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        albumToDelete?.let { viewModel.deleteAlbum(it.id) }
                        showDeleteDialog = false
                        albumToDelete = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteDialog = false
                    albumToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Load movies from IDs when they change
    LaunchedEffect(selectedMovieIds) {
        if (selectedMovieIds.isNotEmpty()) {
            val movies = selectedMovieIds.mapNotNull { id ->
                database.movieDao().getMovieById(id)
            }
            selectedMoviesForAlbum = movies
        } else {
            selectedMoviesForAlbum = emptyList()
        }
    }
    
    val myAlbums by viewModel.myAlbums.collectAsState()
    val savedAlbums by viewModel.savedAlbums.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // SearchScreen for adding movies to album
    if (isSearchingMovies) {
        SearchScreen(
            navController = null,
            isAddMode = true,
            addModeText = "Select a movie to add to your album",
            onMovieSelected = { movieId ->
                // Add movie ID to list if not already present
                if (!selectedMovieIds.contains(movieId)) {
                    selectedMovieIds = selectedMovieIds + movieId
                }
                isSearchingMovies = false
            }
        )
    }
    // CreateAlbumScreen
    else if (isCreatingAlbum) {
        CreateAlbumScreen(
            onCancel = { 
                isCreatingAlbum = false 
                // Clear all saved form data
                selectedMovieIds = emptyList()
                savedAlbumName = ""
                savedDescription = ""
                savedVisibility = "Friends"
                savedHashtags = ""
            },
            onSave = { name, description, visibility, movieIds ->
                coroutineScope.launch {
                    // Get cover from first movie's poster
                    val firstMoviePoster = selectedMoviesForAlbum.firstOrNull()?.posterUrl
                    
                    // Create album in database with cover from first movie
                    val album = Album(
                        title = name,
                        description = description,
                        ownerId = UserSessionManager.getUserId(),
                        privacy = visibility.lowercase(),
                        movieCount = movieIds.size,
                        coverUrl = firstMoviePoster
                    )
                    val albumId = albumRepository.createAlbum(album)
                    
                    // Add movies to album
                    movieIds.forEachIndexed { index, movieId ->
                        albumRepository.addMovieToAlbum(albumId.toInt(), movieId, index)
                    }
                }
                isCreatingAlbum = false 
                // Clear all saved form data after save
                selectedMovieIds = emptyList()
                savedAlbumName = ""
                savedDescription = ""
                savedVisibility = "Friends"
                savedHashtags = ""
            },
            onAddMovies = {
                isSearchingMovies = true
            },
            selectedMovies = selectedMoviesForAlbum,
            onRemoveMovie = { movieId ->
                selectedMovieIds = selectedMovieIds.filter { it != movieId }
            },
            initialName = savedAlbumName,
            initialDescription = savedDescription,
            initialVisibility = savedVisibility,
            initialHashtags = savedHashtags,
            onFormChange = { name, desc, vis, hashtags ->
                savedAlbumName = name
                savedDescription = desc
                savedVisibility = vis
                savedHashtags = hashtags
            }
        )
    } 
    // Main Albums List
    else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Albums", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = InnieGreen)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Description Section
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Share movie, TV & celebrity lists with everyone at Innie movie, or make it private just for you.",
                                fontSize = 13.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Create Album Button
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isCreatingAlbum = true }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = InnieGreen
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Create an album...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                    
                    // Discover Albums Button - Navigate to Home and switch to Album tab
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    // Navigate to HomeAlbum to open Album tab directly
                                    navController.navigate(Screen.HomeAlbum.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = InnieGreen
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Discover albums from community...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                    
                    // My Albums Section
                    if (myAlbums.isNotEmpty()) {
                        item {
                            Text(
                                text = "My Albums",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        
                        items(myAlbums) { album ->
                            AlbumListItem(
                                album = album,
                                onClick = {
                                    navController.navigate(Screen.AlbumDetail.createRoute(album.id))
                                },
                                onRemove = {
                                    albumToDelete = album
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                    
                    // Saved Albums Section
                    if (savedAlbums.isNotEmpty()) {
                        item {
                            Text(
                                text = "Saved Albums",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        
                        items(savedAlbums) { album ->
                            AlbumListItem(
                                album = album,
                                onClick = {
                                    navController.navigate(Screen.AlbumDetail.createRoute(album.id))
                                },
                                onRemove = {
                                    viewModel.removeSavedAlbum(album.id)
                                }
                            )
                        }
                    }
                    
                    // Empty state
                    if (myAlbums.isEmpty() && savedAlbums.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "No albums yet",
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Create your own album or save albums from the community",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumListItem(
    album: Album,
    onClick: () -> Unit,
    onRemove: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Album cover
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            album.coverUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Album info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = album.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "${album.movieCount} movies • by ${album.ownerId}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        
        // Remove button (for saved albums)
        if (onRemove != null) {
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
    
    HorizontalDivider(
        modifier = Modifier.padding(start = 76.dp),
        thickness = 0.5.dp,
        color = Color.LightGray
    )
}
