package com.example.myapplication.data

import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.R

/**
 * Sample shots for seeding database.
 * First shot uses local video (R.raw.stranger_things), others use thumbnail images.
 * 
 * Movie IDs from sample_movies:
 * 1 = The Batman
 * 2 = Dune: Part Two
 * 3 = Oppenheimer
 * 4 = Interstellar
 * 5 = The Dark Knight
 * 17 = Stranger Things
 */
val sampleShots = listOf(
    // Shot with LOCAL VIDEO - Stranger Things
    Shot(
        id = 1,
        videoUrl = "", // Empty - will use localVideoResId instead
        thumbnailUrl = "https://image.tmdb.org/t/p/w780/sqVXe7s3QAvr9WObl8Xo88GGoL0.jpg",
        caption = "Meet the Cast of Stranger Things at the Premiere Event",
        description = "A red-carpet reunion as the Stranger Things cast kicks off the promo tour for the highly anticipated Season 5.",
        relatedMovieId = 17, // Stranger Things
        likeCount = 6777,
        commentCount = 211,
        shareCount = 1776
    ),
    // Other shots - thumbnail only
    Shot(
        id = 2,
        videoUrl = "",
        thumbnailUrl = "https://image.tmdb.org/t/p/w780/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
        caption = "Behind the Scenes: Dune Part Two Desert Filming",
        description = "Discover how Denis Villeneuve recreated the iconic desert landscapes of Arrakis in this exclusive behind-the-scenes footage.",
        relatedMovieId = 2, // Dune: Part Two
        likeCount = 12340,
        commentCount = 567,
        shareCount = 3200
    ),
    Shot(
        id = 3,
        videoUrl = "",
        thumbnailUrl = "https://image.tmdb.org/t/p/w780/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg",
        caption = "Oppenheimer: The Science Behind the Film",
        description = "Christopher Nolan explains how he recreated the Trinity test without CGI, using practical effects and IMAX cameras.",
        relatedMovieId = 3, // Oppenheimer
        likeCount = 45000,
        commentCount = 2890,
        shareCount = 8900
    ),
    Shot(
        id = 4,
        videoUrl = "",
        thumbnailUrl = "https://image.tmdb.org/t/p/w780/xJHokMbljvjADYdit5fK5VQsXEG.jpg",
        caption = "Interstellar: Journey Through Real Wormhole",
        description = "The visual effects team used real physics simulations to create the most scientifically accurate wormhole ever depicted on film.",
        relatedMovieId = 4, // Interstellar
        likeCount = 34500,
        commentCount = 1560,
        shareCount = 7800
    ),
    Shot(
        id = 5,
        videoUrl = "",
        thumbnailUrl = "https://image.tmdb.org/t/p/w780/nMKdUUepR0i5zn0y1T4CsSB5chy.jpg",
        caption = "The Dark Knight: Heath Ledger's Legendary Performance",
        description = "A tribute to Heath Ledger's Oscar-winning portrayal of the Joker, featuring rare behind-the-scenes footage.",
        relatedMovieId = 5, // The Dark Knight
        likeCount = 67000,
        commentCount = 4500,
        shareCount = 12000
    )
)

// Map shot ID to local video resource
val shotLocalVideoMap = mapOf(
    1 to R.raw.stranger_things,
    2 to R.raw.dune_bts,
    3 to R.raw.oppenheimer_bts,
    4 to R.raw.interstellar_wormhold
)



