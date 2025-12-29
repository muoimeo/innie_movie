package com.example.myapplication.ui.navigation

sealed class Screen(val route: String) {
    data object OnBoarding : Screen("onboarding")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object CheckEmail : Screen("check_email")
    data object Home : Screen("home")

    data object MoviePage : Screen("movie_page/{movieId}") {
        fun createRoute(movieId: Int) = "movie_page/$movieId"
    }

}