package com.example.myapplication.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.InnieGreen

/**
 * Share Bottom Sheet - Shows sharing options with copy link functionality
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    contentType: String,  // "news", "shot", "movie", "album"
    contentId: Int,
    contentTitle: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    // Generate share link based on content type
    val shareLink = "https://inniemovie.app/$contentType/$contentId"
    val shareText = "$contentTitle\n\nCheck it out on Innie Movie:\n$shareLink"
    
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Share",
                    fontSize = 18.sp,
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
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Link preview box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Link,
                        contentDescription = null,
                        tint = InnieGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = shareLink,
                        fontSize = 13.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    
                    // Copy button
                    TextButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Share Link", shareLink)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                        },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Copy",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = InnieGreen
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Share options row
            Text(
                text = "Share to",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Copy Link
                ShareOption(
                    icon = Icons.Outlined.ContentCopy,
                    label = "Copy Link",
                    color = Color(0xFF5C6BC0),
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Share Link", shareLink)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                )
                
                // Messages
                ShareOption(
                    icon = Icons.Outlined.Message,
                    label = "Message",
                    color = Color(0xFF4CAF50),
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                        onDismiss()
                    }
                )
                
                // WhatsApp
                ShareOption(
                    icon = Icons.Default.Chat,
                    label = "WhatsApp",
                    color = Color(0xFF25D366),
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                setPackage("com.whatsapp")
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                        }
                        onDismiss()
                    }
                )
                
                // More options
                ShareOption(
                    icon = Icons.Outlined.MoreHoriz,
                    label = "More",
                    color = Color(0xFF9E9E9E),
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                        onDismiss()
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShareOption(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}
