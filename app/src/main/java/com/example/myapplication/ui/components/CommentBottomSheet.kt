package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import coil.compose.AsyncImage
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Comment
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.session.UserSessionManager
import com.example.myapplication.ui.theme.InnieGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * TikTok-style Comment Bottom Sheet
 * Full screen, toggle on/off (no drag)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    targetType: String,  // "album", "news", "shot"
    targetId: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { DatabaseProvider.getDatabase(context) }
    
    var comments by remember { mutableStateOf<List<Comment>>(emptyList()) }
    var users by remember { mutableStateOf<Map<String, User>>(emptyMap()) }
    var commentText by remember { mutableStateOf("") }
    var commentCount by remember { mutableIntStateOf(0) }
    
    // Track liked comments locally
    var likedComments by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var commentLikes by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    
    val currentUserId = UserSessionManager.getUserId()
    
    // Load comments and users
    LaunchedEffect(targetType, targetId) {
        withContext(Dispatchers.IO) {
            val commentsList = db.commentDao().getComments(targetType, targetId).first()
            comments = commentsList
            commentCount = commentsList.size
            
            // Initialize like counts (random for demo)
            val likesMap = mutableMapOf<Int, Int>()
            commentsList.forEach { comment ->
                likesMap[comment.id] = (0..50).random()
            }
            commentLikes = likesMap
            
            // Get user info
            val userIds = commentsList.map { it.userId }.distinct()
            val usersMap = mutableMapOf<String, User>()
            userIds.forEach { userId ->
                db.userDao().getUserById(userId)?.let { user ->
                    usersMap[userId] = user
                }
            }
            users = usersMap
        }
    }
    
    // Full screen modal - no drag, just toggle
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Always fully expanded
    )
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null // No drag handle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f) // 85% screen
        ) {
            // Header - comment count and close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$commentCount comments",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
            
            // Comments list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (comments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No comments yet. Be the first!",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(comments, key = { it.id }) { comment ->
                        val isLiked = likedComments.contains(comment.id)
                        val likeCount = commentLikes[comment.id] ?: 0
                        
                        CommentItem(
                            comment = comment,
                            user = users[comment.userId],
                            isLiked = isLiked,
                            likeCount = likeCount,
                            onLikeClick = {
                                if (isLiked) {
                                    likedComments = likedComments - comment.id
                                    commentLikes = commentLikes + (comment.id to (likeCount - 1))
                                } else {
                                    likedComments = likedComments + comment.id
                                    commentLikes = commentLikes + (comment.id to (likeCount + 1))
                                }
                            }
                        )
                    }
                }
            }
            
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
            
            // Input area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Current user avatar
                AsyncImage(
                    model = "https://i.pravatar.cc/150?u=$currentUserId",
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(10.dp))
                
                // Text input
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { 
                        Text(
                            "Add a comment...", 
                            color = Color.Gray,
                            fontSize = 14.sp
                        ) 
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Send button
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    val newComment = Comment(
                                        userId = currentUserId,
                                        targetType = targetType,
                                        targetId = targetId,
                                        content = commentText.trim()
                                    )
                                    db.commentDao().insert(newComment)
                                    
                                    // Refresh comments
                                    val updatedComments = db.commentDao()
                                        .getComments(targetType, targetId).first()
                                    comments = updatedComments
                                    commentCount = updatedComments.size
                                    
                                    // Set 0 likes for new comments (find comment by content match)
                                    val newlyAddedComment = updatedComments.find { 
                                        it.userId == currentUserId && it.content == newComment.content 
                                    }
                                    newlyAddedComment?.let { c ->
                                        commentLikes = commentLikes + (c.id to 0)
                                    }
                                    
                                    // Get user for new comment
                                    if (!users.containsKey(currentUserId)) {
                                        db.userDao().getUserById(currentUserId)?.let { user ->
                                            users = users + (currentUserId to user)
                                        }
                                    }
                                }
                                commentText = ""
                            }
                        }
                    },
                    enabled = commentText.isNotBlank(),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (commentText.isNotBlank()) InnieGreen else Color.LightGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: Comment,
    user: User?,
    isLiked: Boolean,
    likeCount: Int,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // User avatar
        AsyncImage(
            model = user?.avatarUrl ?: "https://i.pravatar.cc/150?u=${comment.userId}",
            contentDescription = user?.displayName ?: "User",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            // Username
            Text(
                text = user?.displayName ?: user?.username ?: "User",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // Comment text
            Text(
                text = comment.content,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                lineHeight = 18.sp
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // Time + Reply
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatCommentTime(comment.createdAt),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Reply",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        // Like button - clickable with count
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onLikeClick() }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) Color.Red else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = likeCount.toString(),
                fontSize = 11.sp,
                color = if (isLiked) Color.Red else Color.Gray
            )
        }
    }
}

private fun formatCommentTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val minutes = diff / (1000 * 60)
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m"
        hours < 24 -> "${hours}h"
        days < 7 -> "${days}d"
        else -> "${days / 7}w"
    }
}
