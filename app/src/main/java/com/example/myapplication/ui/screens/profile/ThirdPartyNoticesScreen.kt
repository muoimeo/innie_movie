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
fun ThirdPartyNoticesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Third Party Notices", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                text = "Third Party Notices",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Innie Movie uses the following open source libraries and third-party services. We are grateful to the developers and communities who make these resources available.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ThirdPartySection(
                name = "Jetpack Compose",
                license = "Apache License 2.0",
                description = "Android's modern toolkit for building native UI. Compose simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs.",
                copyright = "Copyright © Google LLC"
            )
            
            ThirdPartySection(
                name = "Kotlin",
                license = "Apache License 2.0",
                description = "A modern programming language that makes developers happier. Kotlin is concise, safe, interoperable with Java, and provides many ways to reuse code.",
                copyright = "Copyright © JetBrains s.r.o."
            )
            
            ThirdPartySection(
                name = "Room Database",
                license = "Apache License 2.0",
                description = "The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.",
                copyright = "Copyright © Google LLC"
            )
            
            ThirdPartySection(
                name = "Coil",
                license = "Apache License 2.0",
                description = "An image loading library for Android backed by Kotlin Coroutines. Coil is fast, lightweight, and easy to use.",
                copyright = "Copyright © Coil Contributors"
            )
            
            ThirdPartySection(
                name = "Material Design 3",
                license = "Apache License 2.0",
                description = "Material Design is an adaptable system of guidelines, components, and tools that support the best practices of user interface design.",
                copyright = "Copyright © Google LLC"
            )
            
            ThirdPartySection(
                name = "Navigation Compose",
                license = "Apache License 2.0",
                description = "The Navigation component provides support for Jetpack Compose applications, allowing you to navigate between composables.",
                copyright = "Copyright © Google LLC"
            )
            
            ThirdPartySection(
                name = "Kotlin Coroutines",
                license = "Apache License 2.0",
                description = "A library for writing asynchronous, non-blocking code. Coroutines simplify async programming by putting the complications into libraries.",
                copyright = "Copyright © JetBrains s.r.o."
            )
            
            ThirdPartySection(
                name = "AndroidX Libraries",
                license = "Apache License 2.0",
                description = "AndroidX is a major improvement to the original Android Support Library, providing backward-compatible versions of Android framework APIs.",
                copyright = "Copyright © Google LLC"
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Apache License text
            Text(
                text = "Apache License 2.0",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = """
Licensed under the Apache License, Version 2.0 (the "License"); you may not use these files except in compliance with the License. You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
                """.trimIndent(),
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Third-Party Data Services",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = """
Movie information, posters, cast details, and related content may be provided by third-party data services. This content is used in accordance with their respective terms of service and attribution requirements.

We acknowledge and thank all content providers for making their data available to enhance the Innie Movie experience.

Some images and movie posters displayed in the App are property of their respective copyright holders and are used for informational purposes under fair use principles.
                """.trimIndent(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Acknowledgments",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = """
We would like to thank the entire open source community for their contributions to the tools and libraries that make Innie Movie possible.

Special thanks to:
• The Android development community
• Kotlin and JetBrains teams
• Material Design team at Google
• All contributors to the open source projects we use

If you believe any content or code in our App infringes on your intellectual property rights, please contact us at legal@inniemovie.com.
                """.trimIndent(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun ThirdPartySection(
    name: String,
    license: String,
    description: String,
    copyright: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = license,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF00C02B)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = copyright,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
