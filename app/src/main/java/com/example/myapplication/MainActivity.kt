package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.screens.auth.AuthViewModel
import com.example.myapplication.ui.screens.auth.CheckEmailScreen
import com.example.myapplication.ui.screens.auth.LoginScreen
import com.example.myapplication.ui.screens.auth.OnBoardingScreen
import com.example.myapplication.ui.screens.auth.SignUpScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.screens.community.MoviePage
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.Profile
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.profile.WatchHistoryScreen
class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {

    // === DEV FLAG: Set to true to skip login and go directly to Home ===
    private val SKIP_AUTH_FOR_DEV = true // Change to false to require login
    // ===================================================================

    private val authViewModel: AuthViewModel by viewModels {
        val db = DatabaseProvider.getDatabase(this)
        val repo = AuthRepository(db.userDao())
        AuthViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (SKIP_AUTH_FOR_DEV) Screen.Home.route else Screen.OnBoarding.route
                ) {
                    composable(Screen.OnBoarding.route) {
                        OnBoardingScreen(
                            onGetStarted = {
                                navController.navigate(Screen.Login.route)
                            }
                        )
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.OnBoarding.route) { inclusive = true }
                                }
                            },
                            onGoToSignUp = {
                                navController.navigate(Screen.SignUp.route)
                            },
                            onForgotPassword = {
                                navController.navigate(Screen.CheckEmail.route)
                            }
                        )
                    }
                    composable(Screen.SignUp.route) {
                        SignUpScreen(
                            viewModel = authViewModel,
                            onSignUpSuccess = {
                                // Handled internally by onBackToLogin
                            },
                            onBackToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Screen.CheckEmail.route) {
                        CheckEmailScreen(
                            onVerifySuccess = {
                                navController.popBackStack(
                                    Screen.Login.route,
                                    inclusive = false
                                )
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.Home.route) {
                        val state = authViewModel.state.collectAsState()
                        HomeScreen(
                            username = state.value.currentUser?.username ?: "User",
                            authViewModel = authViewModel,
                            navController = navController
                        )
                    }

                    composable(
                        route = Screen.MoviePage.route,
                        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("movieId") ?: 0
                        MoviePage(movieId = id, navController = navController)
                    }
                    
                    composable(
                        route = Screen.AlbumDetail.route,
                        arguments = listOf(navArgument("albumId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
                        com.example.myapplication.ui.screens.community.AlbumDetailScreen(
                            albumId = albumId,
                            navController = navController
                        )
                    }
                    composable(Profile.WatchHistory.route) {
                        WatchHistoryScreen(navController = navController)
                    }
                    composable(Profile.Albums.route) {
                        ProfileScreen(navController = navController)
                    }
                    composable(Profile.Reviews.route) {
                        ProfileScreen(navController = navController)
                    }
                    composable(Profile.Likes.route) {
                        ProfileScreen(navController = navController)
                    }
                    composable(Profile.Settings.route) {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}
