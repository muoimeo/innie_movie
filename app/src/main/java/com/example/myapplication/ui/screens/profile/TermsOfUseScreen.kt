package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Terms of Use", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Innie Movie Terms of Use",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Last Updated: January 7, 2026",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LegalSection(
                title = "1. Acceptance of Terms",
                content = """
Welcome to Innie Movie. By accessing or using our mobile application ("App"), you agree to be bound by these Terms of Use ("Terms"). If you do not agree to these Terms, please do not use our App.

These Terms constitute a legally binding agreement between you and Innie Movie regarding your use of the App. We reserve the right to modify these Terms at any time, and such modifications will be effective immediately upon posting. Your continued use of the App following any modifications indicates your acceptance of the modified Terms.
                """.trimIndent()
            )
            
            LegalSection(
                title = "2. Description of Service",
                content = """
Innie Movie is a movie tracking and social platform that allows users to:
• Discover and browse movies and TV shows
• Create and manage personal watchlists
• Rate and review films
• Create custom movie collections and albums
• Connect with other movie enthusiasts
• Track viewing history and preferences
• Receive personalized recommendations
• Share thoughts and engage in community discussions

We strive to provide accurate information about films, but we do not guarantee the completeness or accuracy of any content displayed on the App.
                """.trimIndent()
            )
            
            LegalSection(
                title = "3. User Accounts",
                content = """
To access certain features of the App, you must create an account. When creating an account, you agree to:
• Provide accurate, current, and complete information
• Maintain and promptly update your account information
• Keep your password secure and confidential
• Accept responsibility for all activities under your account
• Notify us immediately of any unauthorized use

You must be at least 13 years old to create an account. If you are under 18, you must have parental consent. We reserve the right to suspend or terminate accounts that violate these Terms.
                """.trimIndent()
            )
            
            LegalSection(
                title = "4. User Content",
                content = """
You may submit reviews, comments, ratings, and other content ("User Content"). By submitting User Content, you:
• Grant us a non-exclusive, worldwide, royalty-free license to use, display, and distribute your content
• Represent that you own or have the necessary rights to the content
• Agree not to submit content that is illegal, offensive, defamatory, or infringes on others' rights

We reserve the right to remove any User Content that violates these Terms or is otherwise objectionable. We do not endorse any User Content and are not responsible for any content posted by users.
                """.trimIndent()
            )
            
            LegalSection(
                title = "5. Prohibited Activities",
                content = """
When using the App, you agree not to:
• Violate any applicable laws or regulations
• Infringe on the intellectual property rights of others
• Upload malicious code, viruses, or harmful content
• Attempt to gain unauthorized access to our systems
• Harass, abuse, or harm other users
• Impersonate any person or entity
• Use automated systems to access the App without permission
• Interfere with the proper functioning of the App
• Collect user information without consent
• Use the App for commercial purposes without authorization

Violation of these prohibitions may result in immediate termination of your account.
                """.trimIndent()
            )
            
            LegalSection(
                title = "6. Intellectual Property",
                content = """
The App and its original content, features, and functionality are owned by Innie Movie and are protected by international copyright, trademark, and other intellectual property laws.

Movie information, images, and related content displayed in the App may be subject to third-party copyrights. We display this content under fair use principles and proper licensing agreements. You may not reproduce, distribute, or create derivative works without proper authorization.
                """.trimIndent()
            )
            
            LegalSection(
                title = "7. Third-Party Services",
                content = """
The App may contain links to third-party websites or services that are not owned or controlled by Innie Movie. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.

We use third-party APIs and services to provide movie data and functionality. These services are governed by their own terms and conditions.
                """.trimIndent()
            )
            
            LegalSection(
                title = "8. Disclaimer of Warranties",
                content = """
THE APP IS PROVIDED "AS IS" AND "AS AVAILABLE" WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED. WE DO NOT WARRANT THAT THE APP WILL BE UNINTERRUPTED, ERROR-FREE, OR FREE OF VIRUSES OR OTHER HARMFUL COMPONENTS.

We make no warranties regarding the accuracy, reliability, or completeness of any content or information provided through the App.
                """.trimIndent()
            )
            
            LegalSection(
                title = "9. Limitation of Liability",
                content = """
TO THE MAXIMUM EXTENT PERMITTED BY LAW, INNIE MOVIE SHALL NOT BE LIABLE FOR ANY INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES ARISING OUT OF OR RELATED TO YOUR USE OF THE APP.

Our total liability for any claims arising under these Terms shall not exceed the amount you paid to us, if any, during the twelve months preceding the claim.
                """.trimIndent()
            )
            
            LegalSection(
                title = "10. Termination",
                content = """
We may terminate or suspend your account and access to the App immediately, without prior notice, for any reason, including breach of these Terms.

Upon termination, your right to use the App will cease immediately. All provisions of these Terms which by their nature should survive termination shall survive.
                """.trimIndent()
            )
            
            LegalSection(
                title = "11. Contact Us",
                content = """
If you have any questions about these Terms of Use, please contact us at:

Email: legal@inniemovie.com
Address: 123 Movie Street, Film City, FC 12345
                """.trimIndent()
            )
            
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun LegalSection(title: String, content: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    
    Spacer(modifier = Modifier.height(8.dp))
    
    Text(
        text = content,
        fontSize = 14.sp,
        color = Color.DarkGray,
        lineHeight = 22.sp
    )
    
    Spacer(modifier = Modifier.height(20.dp))
}
