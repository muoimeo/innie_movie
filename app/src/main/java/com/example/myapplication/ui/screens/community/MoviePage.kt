package com.example.myapplication.ui.screens.community

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.ui.theme.InnieGreen

// Data classes for movie details
data class MovieDetail(
    val id: String,
    val title: String,
    val year: String,
    val director: String,
    val duration: String,
    val synopsis: String,
    val description: String,
    val posterRes: Int,
    val backdropRes: Int,
    val rating: Float,
    val ratingDistribution: List<Int>,
    val casts: List<CastMember>,
    val crews: List<CrewMember>,
    val reviews: List<MovieReview>
)

data class CastMember(
    val name: String,
    val character: String,
    val photoRes: Int
)

data class CrewMember(
    val name: String,
    val role: String,
    val photoRes: Int
)

data class MovieReview(
    val id: String,
    val authorName: String,
    val authorPhotoRes: Int,
    val rating: Float,
    val maxRating: Int,
    val reviewText: String
)

// Sample movie data
val sampleMovie = MovieDetail(
    id = "1",
    title = "The Batman",
    year = "2022",
    director = "Matt Reeves",
    duration = "176 mins",
    synopsis = "UNMASK THE TRUTH.",
    description = "In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.",
    posterRes = R.drawable.onboarding_bg,
    backdropRes = R.drawable.onboarding_bg,
    rating = 4.4f,
    ratingDistribution = listOf(9, 15, 30, 38, 46, 57),
    casts = listOf(
        CastMember("Robert Pattinson", "Bruce Wayne", R.drawable.onboarding_bg),
        CastMember("Zoë Kravitz", "Selina Kyle", R.drawable.onboarding_bg),
        CastMember("Paul Dano", "The Riddler", R.drawable.onboarding_bg),
        CastMember("Jeffrey Wright", "James Gordon", R.drawable.onboarding_bg),
        CastMember("Colin Farrell", "Oz", R.drawable.onboarding_bg)
    ),
    crews = listOf(
        CrewMember("Matt Reeves", "Director", R.drawable.onboarding_bg),
        CrewMember("Dylan Clark", "Producer", R.drawable.onboarding_bg)
    ),
    reviews = listOf(
        MovieReview(
            "1", "David", R.drawable.onboarding_bg, 3.5f, 5,
            "It was less than three years ago that Todd Phillips' mid-budget but mega-successful \"Joker\" threateningly pointed toward a future in which superhero movies of all sizes would become so endemic to modern cinema. With Matt Reeves' \"The Batman\" — a sprawling, 176-minute latex procedural that often appears to have more in common with serial killer sagas like \"Se7en\" and \"Zodiac\" than it does anything in the Snyderverse or the MCU."
        ),
        MovieReview(
            "2", "Mason", R.drawable.onboarding_bg, 4.5f, 10,
            "Believe the hype. The Batman is a gritty noir detective story, which immediately sets it apart from the previous iterations. The grime and desolation of Gotham oozes out of each scene through its immaculate cinematography and injected into your veins through Giacchino's brilliant score."
        )
    )
)

@Composable
fun MoviePage(
    movieId: Int,
    navController: NavController,
    viewModel: MoviePageViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableIntStateOf(0) }
    
    // Load movie from database
    val movieFromDb by viewModel.movie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Listen for snackbar events
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    
    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }
    
    // Show loading or empty state
    if (isLoading || movieFromDb == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = InnieGreen)
        }
        return
    }
    
    val movie = movieFromDb!!

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
            // === BACKDROP with early curve ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)  // Reduced to 2/3 of original
            ) {
                // Backdrop - use Coil AsyncImage for URL
                AsyncImage(
                    model = movie.backdropUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 0.dp,
                                bottomEnd = 80.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 0.dp,
                                bottomEnd = 80.dp
                            )
                        )
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Back button - small
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 40.dp)
                        .size(28.dp)
                        .background(Color(0xFFF8F9FA), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF1A202C)
                    )
                }
            }

            // === POSTER + MOVIE INFO ROW (aligned side by side) ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-70).dp), // Adjusted for smaller backdrop
                verticalAlignment = Alignment.Bottom
            ) {
                // Poster - use Coil AsyncImage
                Card(
                    modifier = Modifier
                        .width(116.dp)
                        .height(166.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Movie info - pushed down so title starts right below backdrop
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 70.dp) // Adjusted: 185dp backdrop - 115dp row start = 70dp
                ) {
                    // Title + Year (stacked to handle long titles)
                    Column {
                        Text(
                            text = movie.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A202C),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = movie.year?.toString() ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFF718096)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Directed by
                    Row {
                        Text(
                            text = "Directed by ",
                            fontSize = 11.sp,
                            color = Color(0xFF1A202C)
                        )
                        Text(
                            text = movie.director ?: "Unknown",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A202C)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    // Duration - formatted from runtimeMinutes
                    val hours = (movie.runtimeMinutes ?: 0) / 60
                    val mins = (movie.runtimeMinutes ?: 0) % 60
                    val durationText = if (movie.mediaType == "series") {
                        "${movie.seasonCount ?: 0} Seasons • ${movie.episodeCount ?: 0} Episodes"
                    } else {
                        "${hours}h ${mins}min"
                    }
                    Text(
                        text = durationText,
                        fontSize = 11.sp,
                        color = Color(0xFF1A202C)
                    )
                }
            }

            // === SYNOPSIS & DESCRIPTION (below poster row) ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-60).dp) // Adjusted to remove gap
            ) {
                Text(
                    text = movie.synopsis ?: "",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A202C)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = movie.overview ?: "",
                    fontSize = 12.sp,
                    color = Color(0xFF1A202C),
                    textAlign = TextAlign.Justify,
                    lineHeight = 18.sp
                )
            }

            // === ACTION BUTTONS + RATINGS ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-30).dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Action buttons
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Collect interaction states from ViewModel
                    val isInWatchlist by viewModel.isInWatchlist.collectAsState()
                    val isLiked by viewModel.isLiked.collectAsState()
                    
                    ActionButtonSmall(
                        text = "Rate or Review",
                        icon = Icons.Outlined.Edit,
                        onClick = { 
                            navController.navigate(
                                com.example.myapplication.ui.navigation.Screen.WriteReview.createRoute(movie.id)
                            )
                        }
                    )
                    ActionButtonSmall(
                        text = "Add to Albums",
                        icon = Icons.Default.List,
                        onClick = { }
                    )
                    ActionButtonSmall(
                        text = if (isInWatchlist) "In Watchlist ✓" else "Add to Watchlist",
                        icon = if (isInWatchlist) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        isActive = isInWatchlist,
                        onClick = { viewModel.toggleWatchlist() }
                    )
                }
                
                Spacer(modifier = Modifier.width(20.dp))
                
                // Right: Ratings section
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ratings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A202C),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Rating score + stars (centered)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = String.format("%.1f", movie.rating),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = InnieGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = if (index < movie.rating.toInt()) 
                                            Icons.Filled.Star 
                                        else 
                                            Icons.Outlined.StarOutline,
                                        contentDescription = null,
                                        tint = Color(0xFFEC2626),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Rating bars below
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.height(50.dp)
                    ) {
                        // Generate distribution based on rating
                        val ratingInt = (movie.rating * 2).toInt().coerceIn(1, 10)
                        val fakeDistribution = listOf(8, 15, 25, 35, 50, 45, 30, 20, 12, 6)
                            .mapIndexed { index, base ->
                                val distance = kotlin.math.abs(index - ratingInt + 1)
                                (base - distance * 5).coerceIn(5, 50)
                            }
                        fakeDistribution.forEach { height ->
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(height.dp)
                                    .background(
                                        Color(0xFFB3B3B3).copy(alpha = 0.6f),
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }

            // === CAST/CREW TABS ===
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp)
                    .offset(y = (-20).dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Casts tab
                Box(
                    modifier = Modifier
                        .background(
                            if (selectedTab == 0) InnieGreen else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedTab = 0 }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Casts",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedTab == 0) Color.White else Color(0xFF1A202C)
                    )
                }
                
                // Crews tab
                Box(
                    modifier = Modifier
                        .background(
                            if (selectedTab == 1) InnieGreen else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedTab = 1 }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Crews",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedTab == 1) Color.White else Color(0xFF1A202C)
                    )
                }
            }

            // Cast/Crew avatars - using sampleMovie for fake cast/crew data
            val casts = sampleMovie.casts
            val crews = sampleMovie.crews
            
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .offset(y = (-20).dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val currentList = if (selectedTab == 0) casts else crews
                items(currentList.size) { index ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFC4C4C4))
                        ) {
                            Image(
                                painter = painterResource(
                                    id = if (selectedTab == 0) 
                                        casts[index].photoRes 
                                    else 
                                        crews[index].photoRes
                                ),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedTab == 0) 
                                casts[index].name.split(" ").first()
                            else 
                                crews[index].name.split(" ").first(),
                            fontSize = 10.sp,
                            color = Color(0xFF1A202C),
                            maxLines = 1
                        )
                    }
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .offset(y = (-20).dp)
                    .height(1.dp)
                    .background(Color(0xFFE0E0E0))
            )

            // === POPULAR REVIEWS SECTION ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .offset(y = (-12).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Reviews",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A202C)
                )
            }

            // Reviews list - using sampleMovie for fake reviews
            Column(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                sampleMovie.reviews.forEach { review ->
                    ReviewCard(review = review)
                }
                
                // See All button centered at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "See All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InnieGreen,
                        modifier = Modifier.clickable { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ActionButtonSmall(
    text: String,
    icon: ImageVector,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val buttonColor = if (isActive) Color(0xFF2E7D32) else InnieGreen // Darker green when active
    
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun ReviewCard(review: MovieReview) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Author row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Author avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7D3131))
                ) {
                    Image(
                        painter = painterResource(id = review.authorPhotoRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Spacer(modifier = Modifier.width(10.dp))
                
                Column {
                    // Author name
                    Row {
                        Text(
                            text = "Review by ",
                            fontSize = 12.sp,
                            color = Color(0xFF888888)
                        )
                        Text(
                            text = review.authorName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = InnieGreen
                        )
                    }
                    
                    // Rating stars
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(review.rating.toInt()) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFEC2626),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        if (review.rating % 1 >= 0.5f) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFEC2626).copy(alpha = 0.5f),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "/${review.maxRating}",
                            fontSize = 11.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Review text
            Text(
                text = review.reviewText,
                fontSize = 13.sp,
                color = Color(0xFF1A202C),
                lineHeight = 20.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Read more
            Text(
                text = "Read more",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9C4A8B),
                modifier = Modifier.clickable { }
            )
        }
    }
}