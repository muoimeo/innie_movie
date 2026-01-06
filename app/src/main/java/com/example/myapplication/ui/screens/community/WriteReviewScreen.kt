package com.example.myapplication.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.InnieGreen
import java.text.SimpleDateFormat
import java.util.*

/**
 * Write Review Screen - Based on Figma design
 */
@Composable
fun WriteReviewScreen(
    movieId: Int,
    navController: NavController,
    viewModel: MoviePageViewModel = viewModel()
) {
    val movie by viewModel.movie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Load movie data
    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }
    
    // State
    var watchedDate by remember { mutableStateOf(Date()) }
    var rating by remember { mutableIntStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        if (isLoading || movie == null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = InnieGreen
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .statusBarsPadding()
            ) {
                // Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1A202C),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Write Your Review",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A202C)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Movie title and poster row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Left side - Title and form
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Title with year - use Column to handle long titles
                        Column {
                            Text(
                                text = movie!!.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A202C)
                            )
                            Text(
                                text = movie!!.year?.toString() ?: "",
                                fontSize = 10.sp,
                                color = Color(0xFF1A202C)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Date label
                        Text(
                            text = "Specify the date you watched it",
                            fontSize = 9.sp,
                            color = Color(0xFF1A202C)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Date picker field
                        Row(
                            modifier = Modifier
                                .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFB3B3B3), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickable { showDatePicker = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF1A202C),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = formatWatchDate(watchedDate),
                                fontSize = 9.sp,
                                color = Color(0xFF1A202C)
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            Text(
                                text = "Change",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InnieGreen
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Rating label
                        Text(
                            text = "Give your rating",
                            fontSize = 9.sp,
                            color = Color(0xFF1A202C)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Star rating row - 5 stars with half-star support
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Stars container
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 5 stars, each clickable for half or full
                                repeat(5) { starIndex ->
                                    Box(
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        // Left half (odd numbers: 1, 3, 5, 7, 9)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .width(14.dp)
                                                .align(Alignment.CenterStart)
                                                .clickable { rating = starIndex * 2 + 1 }
                                        )
                                        // Right half (even numbers: 2, 4, 6, 8, 10)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .width(14.dp)
                                                .align(Alignment.CenterEnd)
                                                .clickable { rating = starIndex * 2 + 2 }
                                        )
                                        
                                        // Star icon display
                                        val halfPoint = starIndex * 2 + 1
                                        val fullPoint = starIndex * 2 + 2
                                        
                                        when {
                                            rating >= fullPoint -> {
                                                // Full star
                                                Icon(
                                                    imageVector = Icons.Filled.Star,
                                                    contentDescription = null,
                                                    tint = InnieGreen,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                            rating >= halfPoint -> {
                                                // Half star - show outline with half filled
                                                Box {
                                                    Icon(
                                                        imageVector = Icons.Outlined.StarOutline,
                                                        contentDescription = null,
                                                        tint = Color(0xFFB3B3B3),
                                                        modifier = Modifier.size(28.dp)
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Filled.Star,
                                                        contentDescription = null,
                                                        tint = InnieGreen,
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .graphicsLayer {
                                                                clip = true
                                                                shape = HalfStarShape()
                                                            }
                                                    )
                                                }
                                            }
                                            else -> {
                                                // Empty star
                                                Icon(
                                                    imageVector = Icons.Outlined.StarOutline,
                                                    contentDescription = null,
                                                    tint = Color(0xFFB3B3B3),
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                        }
                                    }
                                    
                                    if (starIndex < 4) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                }
                            }
                            
                            // Heart button - aligned with right edge of date picker
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) Color.Red else Color(0xFFB3B3B3),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { isLiked = !isLiked }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Movie poster
                    AsyncImage(
                        model = movie!!.posterUrl,
                        contentDescription = movie!!.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(116.dp)
                            .height(182.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFFC4C4C4))
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Review text area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(410.dp)
                        .border(1.dp, Color(0xFFB3B3B3), RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    if (reviewText.isEmpty()) {
                        Text(
                            text = "Write down your review...",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB3B3B3).copy(alpha = 0.5f)
                        )
                    }
                    BasicTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            color = Color(0xFF1A202C)
                        ),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Publish button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            // TODO: Save review to database
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .width(104.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InnieGreen)
                    ) {
                        Text(
                            text = "Publish",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF8F9FA)
                        )
                    }
                }
            }
        }
        
        // Date picker dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                onDateSelected = { date ->
                    watchedDate = date
                    showDatePicker = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(Date(millis))
                    }
                }
            ) {
                Text("OK", color = InnieGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel", color = Color.Gray)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

private fun formatWatchDate(date: Date): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(date)
}

/**
 * Shape that clips to left half for half-star display
 */
class HalfStarShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(0f, 0f, size.width / 2f, size.height)
        )
    }
}
