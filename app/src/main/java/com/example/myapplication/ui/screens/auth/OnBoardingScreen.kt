package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.InnieGreen

@Composable
fun OnBoardingScreen(
    onGetStarted: () -> Unit
) {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val averiaSerifLibre = FontFamily(
        Font(
            googleFont = GoogleFont("Averia Serif Libre"),
            fontProvider = provider
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Ảnh trên cùng (Full width, no rounding)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Takes up remaining space
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient fade to white
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp) // Increased height for smoother transition
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                )
                            )
                        )
                )
            }

            // Content Section (Logo, Text, Button)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.innie_movie_logo),
                    contentDescription = "Innie Movie",
                    modifier = Modifier.height(60.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Track films you've watched. Save those you want to see. Tell your friends what's good.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp,
                        fontFamily = averiaSerifLibre,
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(containerColor = InnieGreen),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f) // Reduced width to match design
                        .height(44.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

