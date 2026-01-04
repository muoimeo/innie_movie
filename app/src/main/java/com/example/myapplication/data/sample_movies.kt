package com.example.myapplication.data

import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.Album

/**
 * Sample movies data with real TMDB information.
 * TMDB Image base URL: https://image.tmdb.org/t/p/w500/
 * Replace {poster_path} with the posterUrl value.
 */

// === SAMPLE MOVIES (15 films) ===
val sampleMovies = listOf(
    Movie(
        id = 1,
        tmdbId = "414906",
        title = "The Batman",
        year = 2022,
        director = "Matt Reeves",
        synopsis = "Unmask the truth.",
        overview = "In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.",
        runtimeMinutes = 176,
        posterUrl = "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg",
        genres = "Crime,Mystery,Thriller",
        rating = 4.4f,
        mediaType = "movie"
    ),
    Movie(
        id = 2,
        tmdbId = "693134",
        title = "Dune: Part Two",
        year = 2024,
        director = "Denis Villeneuve",
        synopsis = "Long live the fighters.",
        overview = "Follow the mythic journey of Paul Atreides as he unites with Chani and the Fremen while on a path of revenge against the conspirators who destroyed his family.",
        runtimeMinutes = 166,
        posterUrl = "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
        genres = "Science Fiction,Adventure",
        rating = 4.6f,
        mediaType = "movie"
    ),
    Movie(
        id = 3,
        tmdbId = "872585",
        title = "Oppenheimer",
        year = 2023,
        director = "Christopher Nolan",
        synopsis = "The world forever changes.",
        overview = "The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.",
        runtimeMinutes = 180,
        posterUrl = "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg",
        genres = "Drama,History",
        rating = 4.7f,
        mediaType = "movie"
    ),
    Movie(
        id = 4,
        tmdbId = "157336",
        title = "Interstellar",
        year = 2014,
        director = "Christopher Nolan",
        synopsis = "Mankind was born on Earth. It was never meant to die here.",
        overview = "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.",
        runtimeMinutes = 169,
        posterUrl = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/xJHokMbljvjADYdit5fK5VQsXEG.jpg",
        genres = "Adventure,Drama,Science Fiction",
        rating = 4.5f,
        mediaType = "movie"
    ),
    Movie(
        id = 5,
        tmdbId = "155",
        title = "The Dark Knight",
        year = 2008,
        director = "Christopher Nolan",
        synopsis = "Why so serious?",
        overview = "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets.",
        runtimeMinutes = 152,
        posterUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/nMKdUUepR0i5zn0y1T4CsSB5chy.jpg",
        genres = "Drama,Action,Crime,Thriller",
        rating = 4.8f,
        mediaType = "movie"
    ),
    Movie(
        id = 6,
        tmdbId = "496243",
        title = "Parasite",
        year = 2019,
        director = "Bong Joon-ho",
        synopsis = "Act like you own the place.",
        overview = "All unemployed, Ki-taek's family takes peculiar interest in the wealthy and glamorous Parks for their livelihood until they get entangled in an unexpected incident.",
        runtimeMinutes = 132,
        posterUrl = "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/TU9NIjwzjoKPwQHoHshkFcQUCG.jpg",
        genres = "Comedy,Thriller,Drama",
        rating = 4.6f,
        mediaType = "movie"
    ),
    Movie(
        id = 7,
        tmdbId = "569094",
        title = "Spider-Man: Across the Spider-Verse",
        year = 2023,
        director = "Joaquim Dos Santos",
        synopsis = "It's how you wear the mask that matters.",
        overview = "After reuniting with Gwen Stacy, Brooklyn's full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse.",
        runtimeMinutes = 140,
        posterUrl = "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/9xfDWXAUbFXQK585JvByT5pEAhe.jpg",
        genres = "Animation,Action,Adventure",
        rating = 4.7f,
        mediaType = "movie"
    ),
    Movie(
        id = 8,
        tmdbId = "545611",
        title = "Everything Everywhere All at Once",
        year = 2022,
        director = "Daniel Kwan, Daniel Scheinert",
        synopsis = "The universe is so much bigger than you realize.",
        overview = "An aging Chinese immigrant is swept up in an insane adventure, where she alone can save what's important to her by connecting with the lives she could have led in other universes.",
        runtimeMinutes = 139,
        posterUrl = "https://image.tmdb.org/t/p/w500/w3LxiVYdWWRvEVdn5RYq6jIqkb1.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/fIwiFha3WPu5nHkBeMQ4GzEk0Hv.jpg",
        genres = "Action,Adventure,Science Fiction",
        rating = 4.5f,
        mediaType = "movie"
    ),
    Movie(
        id = 9,
        tmdbId = "346698",
        title = "Barbie",
        year = 2023,
        director = "Greta Gerwig",
        synopsis = "She's everything. He's just Ken.",
        overview = "Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans.",
        runtimeMinutes = 114,
        posterUrl = "https://image.tmdb.org/t/p/w500/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/nHf61UzkfFno5X1ofIhugCPus2R.jpg",
        genres = "Comedy,Adventure,Fantasy",
        rating = 4.0f,
        mediaType = "movie"
    ),
    Movie(
        id = 10,
        tmdbId = "361743",
        title = "Top Gun: Maverick",
        year = 2022,
        director = "Joseph Kosinski",
        synopsis = "Feel the need... the need for speed.",
        overview = "After more than thirty years of service as one of the Navy's top aviators, and target away from promotion, Pete Mitchell is where he belongs, pushing the envelope as a courageous test pilot.",
        runtimeMinutes = 130,
        posterUrl = "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/kBSSbN1sOiJtXjAGVZXxHJR9Kox.jpg",
        genres = "Action,Drama",
        rating = 4.5f,
        mediaType = "movie"
    ),
    Movie(
        id = 11,
        tmdbId = "27205",
        title = "Inception",
        year = 2010,
        director = "Christopher Nolan",
        synopsis = "Your mind is the scene of the crime.",
        overview = "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible.",
        runtimeMinutes = 148,
        posterUrl = "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/8ZTVqvKDQ8emSGUEMjsS4yHAwrp.jpg",
        genres = "Action,Science Fiction,Adventure",
        rating = 4.6f,
        mediaType = "movie"
    ),
    Movie(
        id = 12,
        tmdbId = "475557",
        title = "Joker",
        year = 2019,
        director = "Todd Phillips",
        synopsis = "Put on a happy face.",
        overview = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
        runtimeMinutes = 122,
        posterUrl = "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg",
        genres = "Crime,Thriller,Drama",
        rating = 4.4f,
        mediaType = "movie"
    ),
    Movie(
        id = 13,
        tmdbId = "244786",
        title = "Whiplash",
        year = 2014,
        director = "Damien Chazelle",
        synopsis = "The road to greatness can take you to the edge.",
        overview = "Under the direction of a ruthless instructor, a talented young drummer begins to pursue perfection at any cost, even his humanity.",
        runtimeMinutes = 106,
        posterUrl = "https://image.tmdb.org/t/p/w500/7fn624j5lj3xTme2SgiLCeuedmO.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/fRGxZuo7jJUWQsVg9PREb98Aclp.jpg",
        genres = "Drama,Music",
        rating = 4.5f,
        mediaType = "movie"
    ),
    Movie(
        id = 14,
        tmdbId = "313369",
        title = "La La Land",
        year = 2016,
        director = "Damien Chazelle",
        synopsis = "Here's to the fools who dream.",
        overview = "Mia, an aspiring actress, serves lattes to movie stars in between auditions and Sebastian, a jazz musician, scrapes by playing cocktail party gigs in dingy bars.",
        runtimeMinutes = 128,
        posterUrl = "https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/nlPCdZlHtRNcF6C9hzUH4ebmV1w.jpg",
        genres = "Comedy,Drama,Romance,Music",
        rating = 4.3f,
        mediaType = "movie"
    ),
    Movie(
        id = 15,
        tmdbId = "278",
        title = "The Shawshank Redemption",
        year = 1994,
        director = "Frank Darabont",
        synopsis = "Fear can hold you prisoner. Hope can set you free.",
        overview = "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison.",
        runtimeMinutes = 142,
        posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
        genres = "Drama,Crime",
        rating = 4.9f,
        mediaType = "movie"
    )
)

// === SAMPLE SERIES (5 TV shows) ===
val sampleSeries = listOf(
    Movie(
        id = 16,
        tmdbId = "1399",
        title = "Game of Thrones",
        year = 2011,
        director = "David Benioff, D.B. Weiss",
        synopsis = "Winter is coming.",
        overview = "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north.",
        runtimeMinutes = 60,
        posterUrl = "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/suopoADq0k8YZr4dQXcU6pToj6s.jpg",
        genres = "Sci-Fi & Fantasy,Drama,Action & Adventure",
        rating = 4.5f,
        mediaType = "series",
        seasonCount = 8,
        episodeCount = 73
    ),
    Movie(
        id = 17,
        tmdbId = "66732",
        title = "Stranger Things",
        year = 2016,
        director = "The Duffer Brothers",
        synopsis = "Every ending has a beginning.",
        overview = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces, and one strange little girl.",
        runtimeMinutes = 50,
        posterUrl = "https://image.tmdb.org/t/p/original/cVxVGwHce6xnW8UaVUggaPXbmoE.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/sqVXe7s3QAvr9WObl8Xo88GGoL0.jpg",
        genres = "Drama,Mystery,Sci-Fi & Fantasy",
        rating = 4.6f,
        mediaType = "series",
        seasonCount = 4,
        episodeCount = 34
    ),
    Movie(
        id = 18,
        tmdbId = "1396",
        title = "Breaking Bad",
        year = 2008,
        director = "Vince Gilligan",
        synopsis = "All hail the king.",
        overview = "When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live, he becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost.",
        runtimeMinutes = 45,
        posterUrl = "https://image.tmdb.org/t/p/w500/3xnWaLQjelJDDF7LT1WBo6f4BRe.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
        genres = "Drama,Crime",
        rating = 4.9f,
        mediaType = "series",
        seasonCount = 5,
        episodeCount = 62
    ),
    Movie(
        id = 19,
        tmdbId = "94997",
        title = "House of the Dragon",
        year = 2022,
        director = "Ryan Condal, George R.R. Martin",
        synopsis = "Fire will reign.",
        overview = "The Targaryen dynasty is at the absolute apex of its power, with more than 15 dragons under their yoke. Most combatants combatant would not dare to challenge the Targaryen house, but the civil war that follows threatens to destroy the kingdom.",
        runtimeMinutes = 60,
        posterUrl = "https://image.tmdb.org/t/p/w500/z2yahl2uefxDCl0nogcRBstwruJ.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/etj8E2o0Bud0HkONVQPjyCkIvpv.jpg",
        genres = "Sci-Fi & Fantasy,Drama,Action & Adventure",
        rating = 4.4f,
        mediaType = "series",
        seasonCount = 2,
        episodeCount = 18
    ),
    Movie(
        id = 20,
        tmdbId = "60735",
        title = "The Flash",
        year = 2014,
        director = "Greg Berlanti",
        synopsis = "The fastest man alive.",
        overview = "After a particle accelerator causes a freak storm, CSI Investigator Barry Allen is struck by lightning and falls into a coma. When he wakes up after nine months, he discovers that he can achieve great speeds.",
        runtimeMinutes = 44,
        posterUrl = "https://image.tmdb.org/t/p/original/yZevl2vHQgmosfwUdVNzviIfaWS.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/z59kJfcElR9eHO9rJbWp4qWMuee.jpg",
        genres = "Drama,Sci-Fi & Fantasy",
        rating = 4.0f,
        mediaType = "series",
        seasonCount = 9,
        episodeCount = 184
    )
)

// Combined list of all movies and series
val allMedia = sampleMovies + sampleSeries

// === SAMPLE ALBUMS ===
val sampleAlbums = listOf(
    Album(
        id = 1,
        ownerId = "1",
        title = "A-MUST-WATCH OAT!!",
        description = "Masterpieces that everyone should watch at least once in their lifetime!",
        coverUrl = "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
        privacy = "public",
        movieCount = 10
    ),
    Album(
        id = 2,
        ownerId = "1",
        title = "Christopher Nolan Collection",
        description = "All films directed by the master of mind-bending cinema.",
        coverUrl = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        privacy = "public",
        movieCount = 4
    ),
    Album(
        id = 3,
        ownerId = "1",
        title = "Best of 2023",
        description = "Top films and series released in 2023.",
        coverUrl = "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
        privacy = "public",
        movieCount = 5
    ),
    Album(
        id = 4,
        ownerId = "1",
        title = "Superhero Movies",
        description = "The best superhero films from DC and Marvel.",
        coverUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
        privacy = "public",
        movieCount = 4
    ),
    Album(
        id = 5,
        ownerId = "1",
        title = "Binge-worthy Series",
        description = "Series you can't stop watching once you start.",
        coverUrl = "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8AIqMGskD.jpg",
        privacy = "public",
        movieCount = 5
    )
)
