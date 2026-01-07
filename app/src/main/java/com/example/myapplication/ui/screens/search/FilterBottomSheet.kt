package com.example.myapplication.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Filter Colors - using app's InnieGreen theme with LIGHT background
private val FilterBackgroundColor = Color.White
private val FilterSurfaceColor = Color(0xFFF5F5F5)
private val FilterAccentColor = Color(0xFF00C02B) // InnieGreen
private val FilterTextColor = Color(0xFF1A1A1A) // Dark gray
private val FilterSecondaryTextColor = Color(0xFF666666)

// Sample filter options (will be replaced by database later)
object FilterOptions {
    val mediaTypes = listOf("Movie", "Series")
    val genres = listOf("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance", "Thriller", "Fantasy", "Adventure", "Crime")
    val releaseYears = listOf("2024", "2023", "2022", "2021", "2020", "2019", "2010s", "2000s", "1990s", "Older")
    val ratings = listOf("4.5+", "4.0+", "3.5+", "3.0+", "Any")
    val sortOptions = listOf("Newest", "Oldest", "A-Z", "Z-A", "Popular", "Rating")
}

// Data class for filter state
data class FilterState(
    val selectedMediaTypes: Set<String> = emptySet(),
    val selectedGenres: Set<String> = emptySet(),
    val selectedYears: Set<String> = emptySet(),
    val minRating: String = "Any",
    val selectedSort: String = "Newest"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filterState: FilterState,
    onFilterChange: (FilterState) -> Unit,
    onApplyFilter: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = FilterBackgroundColor,
        contentColor = FilterTextColor,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = "Filter",
                    tint = FilterTextColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Filters",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = FilterTextColor
                )
            }

            // Filter sections
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Media Type
                ExpandableFilterSection(
                    title = "Media Type:",
                    options = FilterOptions.mediaTypes,
                    selectedOptions = filterState.selectedMediaTypes,
                    onOptionToggle = { option ->
                        val newSet = if (filterState.selectedMediaTypes.contains(option)) {
                            filterState.selectedMediaTypes - option
                        } else {
                            filterState.selectedMediaTypes + option
                        }
                        onFilterChange(filterState.copy(selectedMediaTypes = newSet))
                    }
                )

                // Genre
                ExpandableFilterSection(
                    title = "Genre:",
                    options = FilterOptions.genres,
                    selectedOptions = filterState.selectedGenres,
                    onOptionToggle = { option ->
                        val newSet = if (filterState.selectedGenres.contains(option)) {
                            filterState.selectedGenres - option
                        } else {
                            filterState.selectedGenres + option
                        }
                        onFilterChange(filterState.copy(selectedGenres = newSet))
                    }
                )

                // Release Year
                ExpandableFilterSection(
                    title = "Release Year:",
                    options = FilterOptions.releaseYears,
                    selectedOptions = filterState.selectedYears,
                    onOptionToggle = { option ->
                        val newSet = if (filterState.selectedYears.contains(option)) {
                            filterState.selectedYears - option
                        } else {
                            filterState.selectedYears + option
                        }
                        onFilterChange(filterState.copy(selectedYears = newSet))
                    }
                )

                // Rating (single selection)
                ExpandableSortSection(
                    title = "Minimum Rating:",
                    options = FilterOptions.ratings,
                    selectedOption = filterState.minRating,
                    onOptionSelect = { option ->
                        onFilterChange(filterState.copy(minRating = option))
                    }
                )

                // Sort By (single selection)
                ExpandableSortSection(
                    title = "Sort By:",
                    options = FilterOptions.sortOptions,
                    selectedOption = filterState.selectedSort,
                    onOptionSelect = { option ->
                        onFilterChange(filterState.copy(selectedSort = option))
                    }
                )
            }

            // Apply Filter Button
            Button(
                onClick = onApplyFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FilterAccentColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Filter Results",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ExpandableFilterSection(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    // val displayText = if (selectedOptions.isEmpty()) "All" else selectedOptions.joinToString(", ")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = FilterTextColor
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (selectedOptions.isEmpty()) "All" else "${selectedOptions.size} selected",
                    fontSize = 14.sp,
                    color = if (isExpanded) FilterAccentColor else FilterSecondaryTextColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = if (isExpanded) FilterAccentColor else FilterSecondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Expandable Content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { option ->
                    FilterChip(
                        text = option,
                        isSelected = selectedOptions.contains(option),
                        onClick = { onOptionToggle(option) }
                    )
                }
            }
        }

        // Divider
        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)
    }
}

@Composable
fun ExpandableSortSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelect: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = FilterTextColor
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedOption,
                    fontSize = 14.sp,
                    color = if (isExpanded) FilterAccentColor else FilterSecondaryTextColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = if (isExpanded) FilterAccentColor else FilterSecondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Expandable Content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { option ->
                    FilterChip(
                        text = option,
                        isSelected = selectedOption == option,
                        onClick = { onOptionSelect(option) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) FilterAccentColor else FilterSecondaryTextColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) FilterAccentColor.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (isSelected) FilterAccentColor else FilterSecondaryTextColor
        )
    }
}
