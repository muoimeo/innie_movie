package com.example.myapplication.ui.screens.home

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

// Filter Colors
private val FilterBackgroundColor = Color(0xFF1E2235)
private val FilterSurfaceColor = Color(0xFF252A40)
private val FilterAccentColor = Color(0xFFD4A84B) // Gold/yellow color for selected
private val FilterTextColor = Color.White
private val FilterSecondaryTextColor = Color(0xFFB0B0B0)

// News-specific filter options
object NewsFilterOptions {
    val studios = listOf("Netflix", "HBO", "Warner Bros", "Disney+", "Amazon", "Apple TV+", "Paramount", "Universal", "Sony", "Lionsgate", "A24", "Other")
    val genres = listOf("Action", "Drama", "Comedy", "Horror", "Sci-Fi", "Romance", "Thriller", "Fantasy", "Animation", "Documentary")
    val timePeriods = listOf("Today", "Last 24 hours", "This week", "This month", "This year", "All time")
    val popularity = listOf("Trending", "Most viewed", "Most commented", "Most shared", "Editor's pick")
    val regions = listOf("Global", "North America", "Europe", "Asia", "Latin America", "Oceania")
    val contentTypes = listOf("News", "Announcement", "Review", "Rumor", "Interview", "Behind the Scenes")
}

// Data class for news filter state
data class NewsFilterState(
    val selectedStudios: Set<String> = emptySet(),
    val selectedGenres: Set<String> = emptySet(),
    val selectedTimePeriod: String = "All time",
    val selectedPopularity: String = "Trending",
    val selectedRegions: Set<String> = emptySet(),
    val selectedContentTypes: Set<String> = emptySet()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFilterBottomSheet(
    filterState: NewsFilterState,
    onFilterChange: (NewsFilterState) -> Unit,
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
                    text = "News Filters",
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
                // Studio
                NewsExpandableFilterSection(
                    title = "Studio:",
                    options = NewsFilterOptions.studios,
                    selectedOptions = filterState.selectedStudios,
                    onOptionToggle = { option ->
                        val newSet = if (filterState.selectedStudios.contains(option)) {
                            filterState.selectedStudios - option
                        } else {
                            filterState.selectedStudios + option
                        }
                        onFilterChange(filterState.copy(selectedStudios = newSet))
                    }
                )

                // Genre
                NewsExpandableFilterSection(
                    title = "Genre:",
                    options = NewsFilterOptions.genres,
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

                // Time Period (single selection)
                NewsExpandableSingleSection(
                    title = "Time Period:",
                    options = NewsFilterOptions.timePeriods,
                    selectedOption = filterState.selectedTimePeriod,
                    onOptionSelect = { option ->
                        onFilterChange(filterState.copy(selectedTimePeriod = option))
                    }
                )

                // Popularity (single selection)
                NewsExpandableSingleSection(
                    title = "Popularity:",
                    options = NewsFilterOptions.popularity,
                    selectedOption = filterState.selectedPopularity,
                    onOptionSelect = { option ->
                        onFilterChange(filterState.copy(selectedPopularity = option))
                    }
                )

                // Region
                NewsExpandableFilterSection(
                    title = "Region:",
                    options = NewsFilterOptions.regions,
                    selectedOptions = filterState.selectedRegions,
                    onOptionToggle = { option ->
                        val newSet = if (filterState.selectedRegions.contains(option)) {
                            filterState.selectedRegions - option
                        } else {
                            filterState.selectedRegions + option
                        }
                        onFilterChange(filterState.copy(selectedRegions = newSet))
                    }
                )

                // Content Type
                NewsExpandableFilterSection(
                    title = "Content Type:",
                    options = NewsFilterOptions.contentTypes,
                    selectedOptions = filterState.selectedContentTypes,
                    onOptionToggle = { option ->
                        val newSet = if (filterState.selectedContentTypes.contains(option)) {
                            filterState.selectedContentTypes - option
                        } else {
                            filterState.selectedContentTypes + option
                        }
                        onFilterChange(filterState.copy(selectedContentTypes = newSet))
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
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Apply Filters",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun NewsExpandableFilterSection(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit
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
                    NewsFilterChip(
                        text = option,
                        isSelected = selectedOptions.contains(option),
                        onClick = { onOptionToggle(option) }
                    )
                }
            }
        }

        // Divider
        HorizontalDivider(color = Color(0xFF3A3F55), thickness = 0.5.dp)
    }
}

@Composable
fun NewsExpandableSingleSection(
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
                    NewsFilterChip(
                        text = option,
                        isSelected = selectedOption == option,
                        onClick = { onOptionSelect(option) }
                    )
                }
            }
        }

        // Divider
        HorizontalDivider(color = Color(0xFF3A3F55), thickness = 0.5.dp)
    }
}

@Composable
fun NewsFilterChip(
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
