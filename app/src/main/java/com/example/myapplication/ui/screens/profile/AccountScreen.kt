package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Wc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.session.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private val InnieGreen = Color(0xFF00C02B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Get current user from session
    val userId = UserSessionManager.getUserId()
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Profile state - loaded from database
    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf<Long?>(null) }
    var gender by remember { mutableStateOf("prefer_not_to_say") }
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    var coverUrl by remember { mutableStateOf<String?>(null) }
    var bio by remember { mutableStateOf("") }
    
    // Load user from database
    LaunchedEffect(userId) {
        withContext(Dispatchers.IO) {
            user = db.userDao().getUserById(userId)
            user?.let { u ->
                displayName = u.displayName ?: ""
                username = u.username
                email = u.email
                dateOfBirth = u.dateOfBirth
                gender = u.gender ?: "prefer_not_to_say"
                avatarUrl = u.avatarUrl
                coverUrl = u.coverUrl
                bio = u.bio ?: ""
            }
            isLoading = false
        }
    }
    
    // Helper function to save changes to database
    fun saveToDatabase() {
        scope.launch {
            withContext(Dispatchers.IO) {
                db.userDao().updateUserProfile(
                    userId = userId,
                    displayName = displayName.ifBlank { null },
                    username = username,
                    email = email,
                    avatarUrl = avatarUrl,
                    coverUrl = coverUrl,
                    bio = bio.ifBlank { null },
                    gender = gender,
                    dateOfBirth = dateOfBirth
                )
            }
            snackbarHostState.showSnackbar("Profile updated!")
        }
    }
    
    // Edit dialog states
    var showEditDialog by remember { mutableStateOf(false) }
    var editField by remember { mutableStateOf("") }
    var editValue by remember { mutableStateOf("") }
    var editLabel by remember { mutableStateOf("") }
    
    // Gender selection dialog
    var showGenderDialog by remember { mutableStateOf(false) }
    
    // Avatar URL dialog
    var showAvatarUrlDialog by remember { mutableStateOf(false) }
    var avatarUrlInput by remember { mutableStateOf("") }
    
    // Cover URL dialog
    var showCoverUrlDialog by remember { mutableStateOf(false) }
    var coverUrlInput by remember { mutableStateOf("") }
    
    // Edit Dialog
    if (showEditDialog) {
        EditFieldDialog(
            label = editLabel,
            value = editValue,
            onValueChange = { editValue = it },
            onDismiss = { showEditDialog = false },
            onConfirm = {
                when (editField) {
                    "name" -> displayName = editValue
                    "email" -> email = editValue
                    "username" -> username = editValue
                    "bio" -> bio = editValue
                    "dob" -> {
                        // Parse date string to timestamp
                        try {
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            dateOfBirth = sdf.parse(editValue)?.time
                        } catch (e: Exception) { }
                    }
                }
                showEditDialog = false
                saveToDatabase()
            }
        )
    }
    
    // Gender Selection Dialog
    if (showGenderDialog) {
        GenderSelectionDialog(
            currentGender = gender,
            onSelect = { 
                gender = it 
                showGenderDialog = false
                saveToDatabase()
            },
            onDismiss = { showGenderDialog = false }
        )
    }
    
    // Avatar URL Dialog
    if (showAvatarUrlDialog) {
        EditFieldDialog(
            label = "Avatar URL",
            value = avatarUrlInput,
            onValueChange = { avatarUrlInput = it },
            onDismiss = { showAvatarUrlDialog = false },
            onConfirm = {
                avatarUrl = avatarUrlInput.ifBlank { null }
                showAvatarUrlDialog = false
                saveToDatabase()
            }
        )
    }
    
    // Cover URL Dialog
    if (showCoverUrlDialog) {
        EditFieldDialog(
            label = "Cover Image URL",
            value = coverUrlInput,
            onValueChange = { coverUrlInput = it },
            onDismiss = { showCoverUrlDialog = false },
            onConfirm = {
                coverUrl = coverUrlInput.ifBlank { null }
                showCoverUrlDialog = false
                saveToDatabase()
            }
        )
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Account", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Cover and Avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Cover Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF2D3748), Color(0xFF4A5568))
                            )
                        )
                ) {
                    if (coverUrl != null) {
                        AsyncImage(
                            model = coverUrl,
                            contentDescription = "Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    // Edit Cover Button (Camera icon - top right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable { 
                                coverUrlInput = coverUrl ?: ""
                                showCoverUrlDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Cover",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                
                // Avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(Color(0xFF4A5568)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatarUrl != null) {
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Default Avatar",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                    
                    // Edit Avatar Button (Green pencil icon)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(InnieGreen)
                            .clickable { 
                                avatarUrlInput = avatarUrl ?: ""
                                showAvatarUrlDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Change Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            // Username display
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displayName.ifBlank { username },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "@$username",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Profile Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    // Section Header
                    Text(
                        text = "Profile Information",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                    )
                    
                    AccountInfoRow(
                        icon = Icons.Outlined.PersonOutline,
                        label = "Display Name",
                        value = displayName,
                        onEditClick = {
                            editField = "name"
                            editLabel = "Display Name"
                            editValue = displayName
                            showEditDialog = true
                        }
                    )
                    
                    AccountDivider()
                    
                    AccountInfoRow(
                        icon = Icons.Outlined.Email,
                        label = "Email",
                        value = email,
                        onEditClick = {
                            editField = "email"
                            editLabel = "Email"
                            editValue = email
                            showEditDialog = true
                        }
                    )
                    
                    AccountDivider()
                    
                    // Format date for display
                    val dobDisplay = dateOfBirth?.let { 
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it)) 
                    } ?: "Not set"
                    
                    AccountInfoRow(
                        icon = Icons.Outlined.Cake,
                        label = "Date of Birth",
                        value = dobDisplay,
                        onEditClick = {
                            editField = "dob"
                            editLabel = "Date of Birth (dd/MM/yyyy)"
                            editValue = dobDisplay
                            showEditDialog = true
                        }
                    )
                    
                    AccountDivider()
                    
                    // Format gender for display
                    val genderDisplay = when(gender) {
                        "male" -> "Male"
                        "female" -> "Female"
                        "other" -> "Other"
                        else -> "Prefer not to say"
                    }
                    
                    AccountInfoRow(
                        icon = Icons.Outlined.Wc,
                        label = "Gender",
                        value = genderDisplay,
                        onEditClick = { showGenderDialog = true }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Additional Profile Info Card - Bio and Username
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    Text(
                        text = "Additional Info",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                    )
                    
                    AccountInfoRow(
                        icon = Icons.Outlined.Person,
                        label = "Username",
                        value = "@$username",
                        onEditClick = {
                            editField = "username"
                            editLabel = "Username"
                            editValue = username
                            showEditDialog = true
                        }
                    )
                    
                    AccountDivider()
                    
                    AccountInfoRow(
                        icon = Icons.Default.Info,
                        label = "Bio",
                        value = bio.ifBlank { "Tell us about yourself..." },
                        onEditClick = {
                            editField = "bio"
                            editLabel = "Bio"
                            editValue = bio
                            showEditDialog = true
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Account Info Card (Read-only)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Account Info",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    user?.let { u ->
                        val joinDate = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                            .format(Date(u.joinDateMillis))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Member since", fontSize = 13.sp, color = Color.Gray)
                            Text(joinDate, fontSize = 13.sp, color = Color(0xFF1A202C))
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("User ID", fontSize = 13.sp, color = Color.Gray)
                            Text(
                                u.userId.take(12) + if (u.userId.length > 12) "..." else "",
                                fontSize = 13.sp,
                                color = Color(0xFF1A202C)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun AccountInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = InnieGreen,
            modifier = Modifier.size(22.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        
        // Pencil icon for edit
        IconButton(
            onClick = onEditClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit $label",
                tint = Color.LightGray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun AccountDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        thickness = 0.5.dp,
        color = Color(0xFFE0E0E0)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFieldDialog(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Edit $label",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(label) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InnieGreen,
                        focusedLabelColor = InnieGreen,
                        cursorColor = InnieGreen
                    )
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = InnieGreen)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun GenderSelectionDialog(
    currentGender: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Map of database value to display label
    val genderOptions = listOf(
        "male" to "Male",
        "female" to "Female",
        "other" to "Other",
        "prefer_not_to_say" to "Prefer not to say"
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Select Gender",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                genderOptions.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(value) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentGender == value,
                            onClick = { onSelect(value) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = InnieGreen
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = label,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
