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
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Privacy Policy", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                text = "Innie Movie Privacy Policy",
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
            
            PrivacySection(
                title = "1. Introduction",
                content = """
At Innie Movie, we are committed to protecting your privacy and ensuring the security of your personal information. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application.

By using Innie Movie, you consent to the data practices described in this policy. We encourage you to read this Privacy Policy carefully and contact us if you have any questions.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "2. Information We Collect",
                content = """
We collect several types of information to provide and improve our service:

Personal Information:
• Name and email address when you create an account
• Profile picture and bio (optional)
• Date of birth (optional, for age-appropriate content)
• Gender preferences (optional)

Usage Information:
• Movies and shows you rate, review, or add to lists
• Viewing history and watchlist contents
• Albums and collections you create
• Comments and interactions with other users
• Search queries and browsing patterns

Device Information:
• Device type, operating system, and version
• Unique device identifiers
• IP address and general location
• App usage statistics and crash reports
                """.trimIndent()
            )
            
            PrivacySection(
                title = "3. How We Use Your Information",
                content = """
We use the collected information for various purposes:

• To provide and maintain our service
• To personalize your experience and recommendations
• To send you notifications about new features, updates, and movies
• To respond to your inquiries and customer service requests
• To analyze usage patterns and improve our App
• To detect and prevent fraud or abuse
• To comply with legal obligations
• To create aggregated, anonymized data for analytics

We do not sell your personal information to third parties.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "4. Information Sharing",
                content = """
We may share your information in the following circumstances:

With Other Users:
• Your public profile information (username, avatar, bio)
• Your reviews, ratings, and public lists
• Your activity in community discussions

With Service Providers:
• Cloud hosting and data storage services
• Analytics and crash reporting tools
• Email and notification services
• Customer support platforms

For Legal Reasons:
• To comply with legal obligations
• To protect our rights and safety
• To investigate potential violations of our Terms

With Your Consent:
• When you explicitly agree to share information
• When you connect to third-party services
                """.trimIndent()
            )
            
            PrivacySection(
                title = "5. Data Security",
                content = """
We implement appropriate technical and organizational measures to protect your personal information:

• Encryption of data in transit using SSL/TLS
• Secure password hashing and storage
• Regular security assessments and updates
• Access controls limiting who can view your data
• Secure data centers with physical security measures

While we strive to protect your information, no method of transmission over the Internet is 100% secure. We cannot guarantee absolute security.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "6. Your Rights and Choices",
                content = """
You have certain rights regarding your personal information:

Access and Portability:
• Request a copy of your personal data
• Export your reviews, ratings, and lists

Correction:
• Update your account information at any time
• Correct inaccuracies in your profile

Deletion:
• Delete your account and associated data
• Request removal of specific content

Opt-Out:
• Disable push notifications in your device settings
• Unsubscribe from marketing emails
• Adjust privacy settings in the App

To exercise these rights, contact us at privacy@inniemovie.com.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "7. Cookies and Tracking",
                content = """
We use various technologies to collect and store information:

• Session cookies to maintain your login state
• Preference cookies to remember your settings
• Analytics tools to understand usage patterns
• Device identifiers for personalization

You can control cookie preferences through your device settings, though some features may not function properly without them.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "8. Children's Privacy",
                content = """
Innie Movie is not intended for children under 13 years of age. We do not knowingly collect personal information from children under 13.

If you are a parent or guardian and believe your child has provided us with personal information, please contact us immediately. We will take steps to delete such information from our systems.

Users between 13 and 18 should have parental consent to use the App.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "9. International Data Transfers",
                content = """
Your information may be transferred to and maintained on servers located outside your country. By using our App, you consent to this transfer.

We ensure that appropriate safeguards are in place to protect your data when it is transferred internationally, in compliance with applicable data protection laws.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "10. Changes to This Policy",
                content = """
We may update this Privacy Policy from time to time. We will notify you of any changes by posting the new policy on this page and updating the "Last Updated" date.

You are advised to review this Privacy Policy periodically for any changes. Your continued use of the App after any modifications indicates your acceptance of the updated policy.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "11. Contact Us",
                content = """
If you have questions about this Privacy Policy or our data practices, please contact us:

Email: privacy@inniemovie.com
Address: 123 Movie Street, Film City, FC 12345

Data Protection Officer: dpo@inniemovie.com
                """.trimIndent()
            )
            
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun PrivacySection(title: String, content: String) {
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
