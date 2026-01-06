package com.example.myapplication.ui.navigation

sealed class Screen(val route: String) {
    data object OnBoarding : Screen("onboarding")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object CheckEmail : Screen("check_email")
    data object Home : Screen("home")

    data object WatchHistory : Screen("WatchHistory")
    data object Albums : Screen("albums")

    data object Reviews : Screen("reviews")

    data object Likes : Screen("likes")

    data object Settings : Screen("settings")

    data object MoviePage : Screen("movie_page/{movieId}") {
        fun createRoute(movieId: Int) = "movie_page/$movieId"
    }

    data object AlbumDetail : Screen("album_detail/{albumId}") {
        fun createRoute(albumId: Int) = "album_detail/$albumId"
    }

    data object WriteReview : Screen("write_review/{movieId}") {
        fun createRoute(movieId: Int) = "write_review/$movieId"
    }
    
    data object NewsDetail : Screen("news_detail/{newsId}") {
        fun createRoute(newsId: Int) = "news_detail/$newsId"
    }
}