package com.example.myapplication.data

/**
 * Real Cast and Crew data for all movies in the app.
 * Uses TMDB profile images where available, UI Avatars as fallback.
 */

// === Data Classes ===
data class CastInfo(
    val name: String,
    val character: String,
    val imageUrl: String
)

data class CrewInfo(
    val name: String,
    val role: String,
    val imageUrl: String
)

data class MovieCastCrew(
    val movieId: Int,
    val cast: List<CastInfo>,
    val crew: List<CrewInfo>
)

// Helper to generate avatar URL from name - uses ui-avatars with professional styling
// This provides a fallback when TMDB profile images are not available
private fun avatarUrl(name: String): String {
    val encoded = name.replace(" ", "+")
    // Using UI Avatars with a dark gray background and white text, professional look
    return "https://ui-avatars.com/api/?name=$encoded&background=4A5568&color=fff&size=200&bold=true&format=png"
}

// === CAST & CREW DATA BY MOVIE ===

val movieCastCrewMap: Map<Int, MovieCastCrew> = mapOf(
    // === 1. The Batman (2022) ===
    1 to MovieCastCrew(
        movieId = 1,
        cast = listOf(
            CastInfo("Robert Pattinson", "Bruce Wayne / Batman", "https://image.tmdb.org/t/p/original/8A4PS5iG7GWEAVFftyqMZKl3qcr.jpg"),
            CastInfo("Zoë Kravitz", "Selina Kyle / Catwoman", "https://image.tmdb.org/t/p/original/zx74kkI931iiQATJybvRKWYnOe9.jpg"),
            CastInfo("Paul Dano", "Edward Nashton / Riddler", "https://image.tmdb.org/t/p/original/zEJJsm0z07EPNl2Pi1h67xuCmcA.jpg"),
            CastInfo("Jeffrey Wright", "Lt. James Gordon", "https://image.tmdb.org/t/p/original/yGcuHGW4glqRpOPxgiCvjcren7F.jpg"),
            CastInfo("Colin Farrell", "Oswald Cobblepot / Penguin", "https://image.tmdb.org/t/p/w185/vujYfMbWy1eMIOgbO4Fq6bmZz0M.jpg"),
            CastInfo("Andy Serkis", "Alfred Pennyworth", "https://image.tmdb.org/t/p/w185/2aKSLoJfvrCBlM5LciGLGbIDBBl.jpg"),
            CastInfo("John Turturro", "Carmine Falcone", "https://image.tmdb.org/t/p/w185/h9gpPw7gjy5WVoLbYFVQZyAatPF.jpg")
        ),
        crew = listOf(
            CrewInfo("Matt Reeves", "Director", "https://image.tmdb.org/t/p/w185/9v1dULoVvHUNFKQzOVc1qsMOWqN.jpg"),
            CrewInfo("Dylan Clark", "Producer", avatarUrl("Dylan Clark")),
            CrewInfo("Michael Giacchino", "Composer", "https://image.tmdb.org/t/p/w185/6113HkozbFE0JGNdU8dprCxS6ga.jpg"),
            CrewInfo("Greig Fraser", "Cinematographer", avatarUrl("Greig Fraser"))
        )
    ),

    // === 2. Dune: Part Two (2024) ===
    2 to MovieCastCrew(
        movieId = 2,
        cast = listOf(
            CastInfo("Timothée Chalamet", "Paul Atreides", "https://image.tmdb.org/t/p/w185/BE2sdjpgsa2rNTFa66f7upkaOP.jpg"),
            CastInfo("Zendaya", "Chani", "https://image.tmdb.org/t/p/w185/xLnsfJX4dbNNsWpJpWwtHm7Ecy1.jpg"),
            CastInfo("Rebecca Ferguson", "Lady Jessica", "https://image.tmdb.org/t/p/w185/lJloTOheuQSirSLXNA3JHsrMNfH.jpg"),
            CastInfo("Josh Brolin", "Gurney Halleck", "https://image.tmdb.org/t/p/w185/sX2etBbIkxRaCsATyw5ZpOVMPTD.jpg"),
            CastInfo("Austin Butler", "Feyd-Rautha Harkonnen", "https://image.tmdb.org/t/p/w185/4Bhp0nYt4X8KXDCHSBmZepYxgmk.jpg"),
            CastInfo("Florence Pugh", "Princess Irulan", "https://image.tmdb.org/t/p/w185/4SYTH5FdB0dAORV98Nwg3llgVnY.jpg"),
            CastInfo("Javier Bardem", "Stilgar", "https://image.tmdb.org/t/p/w185/3iSFMWdCpXkq0JyHnNW4FNjdLfD.jpg"),
            CastInfo("Christopher Walken", "Emperor Shaddam IV", "https://image.tmdb.org/t/p/w185/vGXptEdezoxlaLfMQfPMeFYM4k8.jpg")
        ),
        crew = listOf(
            CrewInfo("Denis Villeneuve", "Director", "https://image.tmdb.org/t/p/w185/zdDx9Xs93UIrJFWYApEW6pD6qpr.jpg"),
            CrewInfo("Hans Zimmer", "Composer", "https://image.tmdb.org/t/p/w185/8dxfNPV2THJL5m6saZABqdD3FHe.jpg"),
            CrewInfo("Greig Fraser", "Cinematographer", avatarUrl("Greig Fraser")),
            CrewInfo("Mary Parent", "Producer", avatarUrl("Mary Parent"))
        )
    ),

    // === 3. Oppenheimer (2023) ===
    3 to MovieCastCrew(
        movieId = 3,
        cast = listOf(
            CastInfo("Cillian Murphy", "J. Robert Oppenheimer", "https://image.tmdb.org/t/p/w185/dm6V24NjjvjMiCtbMkc8Y2WPm2e.jpg"),
            CastInfo("Emily Blunt", "Kitty Oppenheimer", "https://image.tmdb.org/t/p/w185/5nCSG5TL1bP1geD8aaBfaLnLLCD.jpg"),
            CastInfo("Matt Damon", "Leslie Groves", "https://image.tmdb.org/t/p/original/aCvBXTAR9B1qRjIRzMBYhhbm1fR.jpg"),
            CastInfo("Robert Downey Jr.", "Lewis Strauss", "https://image.tmdb.org/t/p/w185/im9SAqJPZKEbVZGmjXuLI4O7RvM.jpg"),
            CastInfo("Florence Pugh", "Jean Tatlock", "https://image.tmdb.org/t/p/w185/4SYTH5FdB0dAORV98Nwg3llgVnY.jpg"),
            CastInfo("Josh Hartnett", "Ernest Lawrence", "https://image.tmdb.org/t/p/original/dCfu2EN7FjISACcjilaJu7evwEc.jpg"),
            CastInfo("Casey Affleck", "Boris Pash", "https://image.tmdb.org/t/p/original/304ilSygaCRWykoBWAL67TOw8g9.jpg"),
            CastInfo("Rami Malek", "David Hill", "https://image.tmdb.org/t/p/original/ewr46CGOdsx5NzAJdIzEBz2yIQh.jpg")
        ),
        crew = listOf(
            CrewInfo("Christopher Nolan", "Director", "https://image.tmdb.org/t/p/w185/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg"),
            CrewInfo("Emma Thomas", "Producer", "https://image.tmdb.org/t/p/original/utc1PS6WVWR5tknzTJqXtnD0kBp.jpg"),
            CrewInfo("Ludwig Göransson", "Composer", "https://image.tmdb.org/t/p/original/5pcMPnicCdndaVYqWu163T5Zy3I.jpg"),
            CrewInfo("Hoyte van Hoytema", "Cinematographer", "https://image.tmdb.org/t/p/original/y2HXvac1oPzciwxfdyWc5syThRk.jpg")
        )
    ),

    // === 4. Interstellar (2014) ===
    4 to MovieCastCrew(
        movieId = 4,
        cast = listOf(
            CastInfo("Matthew McConaughey", "Joseph Cooper", "https://image.tmdb.org/t/p/w185/wJiGedOCZhwMx9DezU6FDh3ChPu.jpg"),
            CastInfo("Anne Hathaway", "Dr. Amelia Brand", "https://image.tmdb.org/t/p/w185/tLelKoPNiyJCSEtQTz1FGv4TLGc.jpg"),
            CastInfo("Jessica Chastain", "Murphy Cooper", "https://image.tmdb.org/t/p/w185/lodMzLKSdrPcBry6TdoDsMN3Vge.jpg"),
            CastInfo("Michael Caine", "Professor Brand", "https://image.tmdb.org/t/p/w185/hZruclwEPCKqUY8aZQSilPxxHGw.jpg"),
            CastInfo("Matt Damon", "Dr. Mann", "https://image.tmdb.org/t/p/w185/5lBz2l8M3RU0kS6FIVHZ1X0m10a.jpg"),
            CastInfo("Casey Affleck", "Tom Cooper", "https://image.tmdb.org/t/p/w185/hcggAoOQmPT7AjN8brV8TKCe0u6.jpg"),
            CastInfo("Mackenzie Foy", "Young Murphy", "https://image.tmdb.org/t/p/w185/5YGHJ8DJlQnSGHUZMf0RPNbvpBR.jpg")
        ),
        crew = listOf(
            CrewInfo("Christopher Nolan", "Director", "https://image.tmdb.org/t/p/w185/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg"),
            CrewInfo("Hans Zimmer", "Composer", "https://image.tmdb.org/t/p/w185/8dxfNPV2THJL5m6saZABqdD3FHe.jpg"),
            CrewInfo("Hoyte van Hoytema", "Cinematographer", avatarUrl("Hoyte van Hoytema")),
            CrewInfo("Emma Thomas", "Producer", avatarUrl("Emma Thomas"))
        )
    ),

    // === 5. The Dark Knight (2008) ===
    5 to MovieCastCrew(
        movieId = 5,
        cast = listOf(
            CastInfo("Christian Bale", "Bruce Wayne / Batman", "https://image.tmdb.org/t/p/w185/qCpZn2e3dimwbryLnqxZuI88PTi.jpg"),
            CastInfo("Heath Ledger", "Joker", "https://image.tmdb.org/t/p/w185/5Y9HnYYa9jF4NunY9lSgJGjSe8E.jpg"),
            CastInfo("Aaron Eckhart", "Harvey Dent / Two-Face", "https://image.tmdb.org/t/p/w185/74nNquS1sqaoEtQqBGuBKCvr0FP.jpg"),
            CastInfo("Michael Caine", "Alfred Pennyworth", "https://image.tmdb.org/t/p/w185/hZruclwEPCKqUY8aZQSilPxxHGw.jpg"),
            CastInfo("Maggie Gyllenhaal", "Rachel Dawes", "https://image.tmdb.org/t/p/w185/7OGKL8L7xvbWeOI4FV6bKcHTTQF.jpg"),
            CastInfo("Gary Oldman", "James Gordon", "https://image.tmdb.org/t/p/w185/2v9FVVBUrrkW2m3QOcYkuhq9A6o.jpg"),
            CastInfo("Morgan Freeman", "Lucius Fox", "https://image.tmdb.org/t/p/w185/tLvr8BAAodVzCjY8LLdL5FDqqRE.jpg")
        ),
        crew = listOf(
            CrewInfo("Christopher Nolan", "Director", "https://image.tmdb.org/t/p/w185/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg"),
            CrewInfo("Hans Zimmer", "Composer", "https://image.tmdb.org/t/p/w185/8dxfNPV2THJL5m6saZABqdD3FHe.jpg"),
            CrewInfo("James Newton Howard", "Composer", avatarUrl("James Newton Howard")),
            CrewInfo("Wally Pfister", "Cinematographer", avatarUrl("Wally Pfister"))
        )
    ),

    // === 6. Parasite (2019) ===
    6 to MovieCastCrew(
        movieId = 6,
        cast = listOf(
            CastInfo("Song Kang-ho", "Ki-taek", "https://image.tmdb.org/t/p/w185/vUaKvj2T0qPJNz7b8X2QwMPsqfN.jpg"),
            CastInfo("Lee Sun-kyun", "Park Dong-ik", avatarUrl("Lee Sun-kyun")),
            CastInfo("Cho Yeo-jeong", "Yeon-kyo", avatarUrl("Cho Yeo-jeong")),
            CastInfo("Choi Woo-shik", "Ki-woo", "https://image.tmdb.org/t/p/w185/bQZy6I3ubA3w3s5h9x0kZG41bwD.jpg"),
            CastInfo("Park So-dam", "Ki-jung", "https://image.tmdb.org/t/p/w185/c1A3fxqAq2m3PALdZoRz4xVh4lg.jpg"),
            CastInfo("Jang Hye-jin", "Chung-sook", avatarUrl("Jang Hye-jin"))
        ),
        crew = listOf(
            CrewInfo("Bong Joon-ho", "Director", "https://image.tmdb.org/t/p/w185/4hJMspmmM0WIJlS2m3S0BpENIxE.jpg"),
            CrewInfo("Bong Joon-ho", "Writer", "https://image.tmdb.org/t/p/w185/4hJMspmmM0WIJlS2m3S0BpENIxE.jpg"),
            CrewInfo("Jung Jae-il", "Composer", avatarUrl("Jung Jae-il")),
            CrewInfo("Hong Kyung-pyo", "Cinematographer", avatarUrl("Hong Kyung-pyo"))
        )
    ),

    // === 7. Spider-Man: Across the Spider-Verse (2023) ===
    7 to MovieCastCrew(
        movieId = 7,
        cast = listOf(
            CastInfo("Shameik Moore", "Miles Morales (voice)", "https://image.tmdb.org/t/p/w185/uJNaSTsfBOvtFWsPP23zNthknsB.jpg"),
            CastInfo("Hailee Steinfeld", "Gwen Stacy (voice)", "https://image.tmdb.org/t/p/w185/q1tylX5YiHoGkBtXsYIBPWMvVaD.jpg"),
            CastInfo("Oscar Isaac", "Miguel O'Hara (voice)", "https://image.tmdb.org/t/p/w185/uy3rbdGqSNFlymA8YhfYLADoUBV.jpg"),
            CastInfo("Jake Johnson", "Peter B. Parker (voice)", "https://image.tmdb.org/t/p/w185/aZloaXymHnH9gyR0GMtCdh7ozdw.jpg"),
            CastInfo("Issa Rae", "Jessica Drew (voice)", "https://image.tmdb.org/t/p/w185/62dCH33H5wO63rdBYpLMH6XT5wI.jpg"),
            CastInfo("Daniel Kaluuya", "Hobie Brown (voice)", "https://image.tmdb.org/t/p/w185/bVr9oY3n9BqniROB22rqyGSN3Cj.jpg"),
            CastInfo("Brian Tyree Henry", "Jeff Morales (voice)", "https://image.tmdb.org/t/p/w185/mxoGGpF9mH7cS5zzKA1NjEXLQ56.jpg")
        ),
        crew = listOf(
            CrewInfo("Joaquim Dos Santos", "Director", avatarUrl("Joaquim Dos Santos")),
            CrewInfo("Kemp Powers", "Director", avatarUrl("Kemp Powers")),
            CrewInfo("Justin K. Thompson", "Director", avatarUrl("Justin K Thompson")),
            CrewInfo("Daniel Pemberton", "Composer", avatarUrl("Daniel Pemberton")),
            CrewInfo("Phil Lord", "Producer", avatarUrl("Phil Lord")),
            CrewInfo("Chris Miller", "Producer", avatarUrl("Chris Miller"))
        )
    ),

    // === 8. Everything Everywhere All at Once (2022) ===
    8 to MovieCastCrew(
        movieId = 8,
        cast = listOf(
            CastInfo("Michelle Yeoh", "Evelyn Wang", "https://image.tmdb.org/t/p/w185/v3TJnPSQNB2RLfAI0SB0BKrMvxA.jpg"),
            CastInfo("Ke Huy Quan", "Waymond Wang", "https://image.tmdb.org/t/p/w185/7qTQBD6g0eCePjpD5Gaw5vOauzq.jpg"),
            CastInfo("Stephanie Hsu", "Joy Wang / Jobu Tupaki", "https://image.tmdb.org/t/p/w185/kvKt5GyWCVb3A0dVQUYRfYBXlXC.jpg"),
            CastInfo("Jamie Lee Curtis", "Deirdre Beaubeirdre", "https://image.tmdb.org/t/p/w185/vYibpB8AiZz6IWPHXXbLWU9QgBH.jpg"),
            CastInfo("James Hong", "Gong Gong", avatarUrl("James Hong"))
        ),
        crew = listOf(
            CrewInfo("Daniel Kwan", "Director", avatarUrl("Daniel Kwan")),
            CrewInfo("Daniel Scheinert", "Director", avatarUrl("Daniel Scheinert")),
            CrewInfo("Son Lux", "Composer", avatarUrl("Son Lux")),
            CrewInfo("Larkin Seiple", "Cinematographer", avatarUrl("Larkin Seiple"))
        )
    ),

    // === 9. Barbie (2023) ===
    9 to MovieCastCrew(
        movieId = 9,
        cast = listOf(
            CastInfo("Margot Robbie", "Barbie", "https://image.tmdb.org/t/p/w185/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg"),
            CastInfo("Ryan Gosling", "Ken", "https://image.tmdb.org/t/p/w185/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg"),
            CastInfo("America Ferrera", "Gloria", "https://image.tmdb.org/t/p/w185/iZ1M7MKIhLt5UdKmZpXhvSrPSWV.jpg"),
            CastInfo("Kate McKinnon", "Weird Barbie", "https://image.tmdb.org/t/p/w185/xn1dHkEmOhJfWWQP8voXRRuAXPy.jpg"),
            CastInfo("Will Ferrell", "CEO", "https://image.tmdb.org/t/p/w185/pMgovQyp05J2RcxVPCH8RNcHN3W.jpg"),
            CastInfo("Simu Liu", "Ken", "https://image.tmdb.org/t/p/w185/2Dj6TqTTNT0bYGaiqzXrZLNL6AC.jpg"),
            CastInfo("Ariana Greenblatt", "Sasha", "https://image.tmdb.org/t/p/w185/bXRnHkNNbR3qgykDz4BzokKaxJU.jpg")
        ),
        crew = listOf(
            CrewInfo("Greta Gerwig", "Director", "https://image.tmdb.org/t/p/w185/yFqpcxmpyZ1KpBOqxOTMALwPoIF.jpg"),
            CrewInfo("Noah Baumbach", "Writer", avatarUrl("Noah Baumbach")),
            CrewInfo("Mark Ronson", "Composer", avatarUrl("Mark Ronson")),
            CrewInfo("Rodrigo Prieto", "Cinematographer", avatarUrl("Rodrigo Prieto"))
        )
    ),

    // === 10. Top Gun: Maverick (2022) ===
    10 to MovieCastCrew(
        movieId = 10,
        cast = listOf(
            CastInfo("Tom Cruise", "Capt. Pete 'Maverick' Mitchell", "https://image.tmdb.org/t/p/w185/8qBylBsQf4llkGrWR3qAsOtOU8O.jpg"),
            CastInfo("Miles Teller", "Lt. Bradley 'Rooster' Bradshaw", "https://image.tmdb.org/t/p/w185/cg3LY082Jl0iuXlzs71lxWNSggZ.jpg"),
            CastInfo("Jennifer Connelly", "Penny Benjamin", "https://image.tmdb.org/t/p/w185/fYnndBQQx5w0V3yyC7cMb9b2xE3.jpg"),
            CastInfo("Jon Hamm", "Vice Admiral Cyclone", "https://image.tmdb.org/t/p/w185/pZCkzQf1HDCDRdnqN6ePM1z9F8t.jpg"),
            CastInfo("Glen Powell", "Lt. Jake 'Hangman' Seresin", "https://image.tmdb.org/t/p/w185/a3SJALIj6z4EihmXVp6vXxU3V3B.jpg"),
            CastInfo("Ed Harris", "Rear Admiral Chester 'Hammer'", "https://image.tmdb.org/t/p/w185/xDssFeipsLv2he1aEG9qIjtdsPG.jpg"),
            CastInfo("Val Kilmer", "Admiral Tom 'Iceman' Kazansky", "https://image.tmdb.org/t/p/w185/hMSjCk3H3XRxSNj4RLadLAC1KrK.jpg")
        ),
        crew = listOf(
            CrewInfo("Joseph Kosinski", "Director", avatarUrl("Joseph Kosinski")),
            CrewInfo("Tom Cruise", "Producer", "https://image.tmdb.org/t/p/w185/8qBylBsQf4llkGrWR3qAsOtOU8O.jpg"),
            CrewInfo("Hans Zimmer", "Composer", "https://image.tmdb.org/t/p/w185/8dxfNPV2THJL5m6saZABqdD3FHe.jpg"),
            CrewInfo("Claudio Miranda", "Cinematographer", avatarUrl("Claudio Miranda"))
        )
    ),

    // === 11. Inception (2010) ===
    11 to MovieCastCrew(
        movieId = 11,
        cast = listOf(
            CastInfo("Leonardo DiCaprio", "Dom Cobb", "https://image.tmdb.org/t/p/w185/wo2hJpn04vbtmh0B9utCFdsQhxM.jpg"),
            CastInfo("Joseph Gordon-Levitt", "Arthur", "https://image.tmdb.org/t/p/w185/dhv9f3AaozOjpvjAwVzOWlmmT2V.jpg"),
            CastInfo("Elliot Page", "Ariadne", avatarUrl("Elliot Page")),
            CastInfo("Tom Hardy", "Eames", "https://image.tmdb.org/t/p/w185/sGMA6pA2D6X0gun49igJT3piHs3.jpg"),
            CastInfo("Marion Cotillard", "Mal Cobb", "https://image.tmdb.org/t/p/w185/lJloTOheuQSirSLXNA3JHsrMNfH.jpg"),
            CastInfo("Ken Watanabe", "Saito", "https://image.tmdb.org/t/p/w185/psAXOYp9SBOXvg6AXzARDedNQ9P.jpg"),
            CastInfo("Cillian Murphy", "Robert Fischer", "https://image.tmdb.org/t/p/w185/dm6V24NjjvjMiCtbMkc8Y2WPm2e.jpg"),
            CastInfo("Michael Caine", "Miles", "https://image.tmdb.org/t/p/w185/hZruclwEPCKqUY8aZQSilPxxHGw.jpg")
        ),
        crew = listOf(
            CrewInfo("Christopher Nolan", "Director", "https://image.tmdb.org/t/p/w185/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg"),
            CrewInfo("Hans Zimmer", "Composer", "https://image.tmdb.org/t/p/w185/8dxfNPV2THJL5m6saZABqdD3FHe.jpg"),
            CrewInfo("Wally Pfister", "Cinematographer", avatarUrl("Wally Pfister")),
            CrewInfo("Emma Thomas", "Producer", avatarUrl("Emma Thomas"))
        )
    ),

    // === 12. Joker (2019) ===
    12 to MovieCastCrew(
        movieId = 12,
        cast = listOf(
            CastInfo("Joaquin Phoenix", "Arthur Fleck / Joker", "https://image.tmdb.org/t/p/w185/ls14M0nLeoJT4oRl7eH3nJGOxfI.jpg"),
            CastInfo("Robert De Niro", "Murray Franklin", "https://image.tmdb.org/t/p/w185/cT8htcckIuyI1Lqwt1CvD02ynTh.jpg"),
            CastInfo("Zazie Beetz", "Sophie Dumond", "https://image.tmdb.org/t/p/w185/xnySEQPoUAh2oHgnlaaoLLjjcBE.jpg"),
            CastInfo("Frances Conroy", "Penny Fleck", avatarUrl("Frances Conroy")),
            CastInfo("Brett Cullen", "Thomas Wayne", avatarUrl("Brett Cullen"))
        ),
        crew = listOf(
            CrewInfo("Todd Phillips", "Director", "https://image.tmdb.org/t/p/w185/A6FPht87DiqXzp456WjakLi2AtP.jpg"),
            CrewInfo("Hildur Guðnadóttir", "Composer", avatarUrl("Hildur Gudnadottir")),
            CrewInfo("Lawrence Sher", "Cinematographer", avatarUrl("Lawrence Sher")),
            CrewInfo("Bradley Cooper", "Producer", "https://image.tmdb.org/t/p/w185/DPnessSsWqVXRbKm93PtMjB4Us.jpg")
        )
    ),

    // === 13. Whiplash (2014) ===
    13 to MovieCastCrew(
        movieId = 13,
        cast = listOf(
            CastInfo("Miles Teller", "Andrew Neiman", "https://image.tmdb.org/t/p/w185/cg3LY082Jl0iuXlzs71lxWNSggZ.jpg"),
            CastInfo("J.K. Simmons", "Terence Fletcher", "https://image.tmdb.org/t/p/w185/ScmKoJ9eiSEUtrIqjsRgfwQh5Q.jpg"),
            CastInfo("Melissa Benoist", "Nicole", avatarUrl("Melissa Benoist")),
            CastInfo("Paul Reiser", "Jim Neiman", avatarUrl("Paul Reiser"))
        ),
        crew = listOf(
            CrewInfo("Damien Chazelle", "Director", avatarUrl("Damien Chazelle")),
            CrewInfo("Justin Hurwitz", "Composer", avatarUrl("Justin Hurwitz")),
            CrewInfo("Sharone Meir", "Cinematographer", avatarUrl("Sharone Meir")),
            CrewInfo("Jason Blum", "Producer", avatarUrl("Jason Blum"))
        )
    ),

    // === 14. La La Land (2016) ===
    14 to MovieCastCrew(
        movieId = 14,
        cast = listOf(
            CastInfo("Ryan Gosling", "Sebastian Wilder", "https://image.tmdb.org/t/p/w185/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg"),
            CastInfo("Emma Stone", "Mia Dolan", "https://image.tmdb.org/t/p/w185/eAkTn7bsr4A8USoGzCpY2TY8B5A.jpg"),
            CastInfo("John Legend", "Keith", avatarUrl("John Legend")),
            CastInfo("Rosemarie DeWitt", "Laura Wilder", avatarUrl("Rosemarie DeWitt")),
            CastInfo("J.K. Simmons", "Bill", "https://image.tmdb.org/t/p/w185/ScmKoJ9eiSEUtrIqjsRgfwQh5Q.jpg")
        ),
        crew = listOf(
            CrewInfo("Damien Chazelle", "Director", avatarUrl("Damien Chazelle")),
            CrewInfo("Justin Hurwitz", "Composer", avatarUrl("Justin Hurwitz")),
            CrewInfo("Linus Sandgren", "Cinematographer", avatarUrl("Linus Sandgren")),
            CrewInfo("Fred Berger", "Producer", avatarUrl("Fred Berger"))
        )
    ),

    // === 15. The Shawshank Redemption (1994) ===
    15 to MovieCastCrew(
        movieId = 15,
        cast = listOf(
            CastInfo("Tim Robbins", "Andy Dufresne", "https://image.tmdb.org/t/p/w185/djLVFETFTvPyVJbm3NM3tBk4g07.jpg"),
            CastInfo("Morgan Freeman", "Ellis Boyd 'Red' Redding", "https://image.tmdb.org/t/p/w185/tLvr8BAAodVzCjY8LLdL5FDqqRE.jpg"),
            CastInfo("Bob Gunton", "Warden Norton", avatarUrl("Bob Gunton")),
            CastInfo("William Sadler", "Heywood", avatarUrl("William Sadler")),
            CastInfo("Clancy Brown", "Captain Hadley", avatarUrl("Clancy Brown")),
            CastInfo("Gil Bellows", "Tommy Williams", avatarUrl("Gil Bellows"))
        ),
        crew = listOf(
            CrewInfo("Frank Darabont", "Director", avatarUrl("Frank Darabont")),
            CrewInfo("Thomas Newman", "Composer", avatarUrl("Thomas Newman")),
            CrewInfo("Roger Deakins", "Cinematographer", avatarUrl("Roger Deakins")),
            CrewInfo("Niki Marvin", "Producer", avatarUrl("Niki Marvin"))
        )
    ),

    // === TV SERIES ===

    // === 16. Game of Thrones ===
    16 to MovieCastCrew(
        movieId = 16,
        cast = listOf(
            CastInfo("Emilia Clarke", "Daenerys Targaryen", "https://image.tmdb.org/t/p/w185/86jeYFV40KctQMDQIWhWYaEqiKz.jpg"),
            CastInfo("Kit Harington", "Jon Snow", "https://image.tmdb.org/t/p/w185/4MqUjb1SYrzHmFSyGiXnlJWLMmY.jpg"),
            CastInfo("Peter Dinklage", "Tyrion Lannister", "https://image.tmdb.org/t/p/w185/xuB7b4GbARu4HN6gq5zMqjGbkwF.jpg"),
            CastInfo("Lena Headey", "Cersei Lannister", avatarUrl("Lena Headey")),
            CastInfo("Nikolaj Coster-Waldau", "Jaime Lannister", avatarUrl("Nikolaj Coster-Waldau")),
            CastInfo("Sophie Turner", "Sansa Stark", avatarUrl("Sophie Turner")),
            CastInfo("Maisie Williams", "Arya Stark", avatarUrl("Maisie Williams"))
        ),
        crew = listOf(
            CrewInfo("David Benioff", "Creator", avatarUrl("David Benioff")),
            CrewInfo("D.B. Weiss", "Creator", avatarUrl("D.B. Weiss")),
            CrewInfo("Ramin Djawadi", "Composer", avatarUrl("Ramin Djawadi"))
        )
    ),

    // === 17. Stranger Things ===
    17 to MovieCastCrew(
        movieId = 17,
        cast = listOf(
            CastInfo("Millie Bobby Brown", "Eleven", "https://image.tmdb.org/t/p/w185/3Qblbk5JIMxzlGVd1k1ucSqRnfc.jpg"),
            CastInfo("Finn Wolfhard", "Mike Wheeler", "https://image.tmdb.org/t/p/w185/63cWxnAlZPK8h8YkRRLuZfPE1cU.jpg"),
            CastInfo("Winona Ryder", "Joyce Byers", "https://image.tmdb.org/t/p/w185/5XHycU9n8MG7r0wSDo9NpNfmGdf.jpg"),
            CastInfo("David Harbour", "Jim Hopper", "https://image.tmdb.org/t/p/w185/6FyixIYC2cZ8hSOCgBNYPOq5smI.jpg"),
            CastInfo("Gaten Matarazzo", "Dustin Henderson", avatarUrl("Gaten Matarazzo")),
            CastInfo("Caleb McLaughlin", "Lucas Sinclair", avatarUrl("Caleb McLaughlin")),
            CastInfo("Noah Schnapp", "Will Byers", avatarUrl("Noah Schnapp")),
            CastInfo("Sadie Sink", "Max Mayfield", avatarUrl("Sadie Sink"))
        ),
        crew = listOf(
            CrewInfo("Matt Duffer", "Creator", avatarUrl("Matt Duffer")),
            CrewInfo("Ross Duffer", "Creator", avatarUrl("Ross Duffer")),
            CrewInfo("Kyle Dixon", "Composer", avatarUrl("Kyle Dixon")),
            CrewInfo("Michael Stein", "Composer", avatarUrl("Michael Stein"))
        )
    ),

    // === 18. Breaking Bad ===
    18 to MovieCastCrew(
        movieId = 18,
        cast = listOf(
            CastInfo("Bryan Cranston", "Walter White", "https://image.tmdb.org/t/p/w185/7Jahy5LZX2Fo8fGJltMreAI49hC.jpg"),
            CastInfo("Aaron Paul", "Jesse Pinkman", "https://image.tmdb.org/t/p/w185/8Ac9uuoYwZoYVAiJpzU5GXkHmS0.jpg"),
            CastInfo("Anna Gunn", "Skyler White", avatarUrl("Anna Gunn")),
            CastInfo("Dean Norris", "Hank Schrader", avatarUrl("Dean Norris")),
            CastInfo("Betsy Brandt", "Marie Schrader", avatarUrl("Betsy Brandt")),
            CastInfo("RJ Mitte", "Walter White Jr.", avatarUrl("RJ Mitte")),
            CastInfo("Bob Odenkirk", "Saul Goodman", "https://image.tmdb.org/t/p/w185/p0T42ToClscrGU7ywrOuQKjkI1W.jpg"),
            CastInfo("Giancarlo Esposito", "Gus Fring", "https://image.tmdb.org/t/p/w185/lBvDQZjxhIGMbH61iHnqerpbqHc.jpg")
        ),
        crew = listOf(
            CrewInfo("Vince Gilligan", "Creator", avatarUrl("Vince Gilligan")),
            CrewInfo("Dave Porter", "Composer", avatarUrl("Dave Porter")),
            CrewInfo("Michael Slovis", "Cinematographer", avatarUrl("Michael Slovis"))
        )
    ),

    // === 19. House of the Dragon ===
    19 to MovieCastCrew(
        movieId = 19,
        cast = listOf(
            CastInfo("Paddy Considine", "King Viserys I Targaryen", avatarUrl("Paddy Considine")),
            CastInfo("Matt Smith", "Prince Daemon Targaryen", "https://image.tmdb.org/t/p/w185/dCgG1wYlf4OsqEfJz3J99Djt24o.jpg"),
            CastInfo("Emma D'Arcy", "Princess Rhaenyra Targaryen", avatarUrl("Emma D'Arcy")),
            CastInfo("Olivia Cooke", "Queen Alicent Hightower", avatarUrl("Olivia Cooke")),
            CastInfo("Rhys Ifans", "Ser Otto Hightower", avatarUrl("Rhys Ifans")),
            CastInfo("Steve Toussaint", "Lord Corlys Velaryon", avatarUrl("Steve Toussaint")),
            CastInfo("Fabien Frankel", "Ser Criston Cole", avatarUrl("Fabien Frankel"))
        ),
        crew = listOf(
            CrewInfo("Ryan Condal", "Creator", avatarUrl("Ryan Condal")),
            CrewInfo("George R.R. Martin", "Creator", avatarUrl("George R.R. Martin")),
            CrewInfo("Ramin Djawadi", "Composer", avatarUrl("Ramin Djawadi"))
        )
    ),

    // === 20. The Flash ===
    20 to MovieCastCrew(
        movieId = 20,
        cast = listOf(
            CastInfo("Grant Gustin", "Barry Allen / The Flash", avatarUrl("Grant Gustin")),
            CastInfo("Candice Patton", "Iris West-Allen", avatarUrl("Candice Patton")),
            CastInfo("Danielle Panabaker", "Caitlin Snow / Frost", avatarUrl("Danielle Panabaker")),
            CastInfo("Carlos Valdes", "Cisco Ramon", avatarUrl("Carlos Valdes")),
            CastInfo("Jesse L. Martin", "Joe West", avatarUrl("Jesse L. Martin")),
            CastInfo("Tom Cavanagh", "Harrison Wells", avatarUrl("Tom Cavanagh"))
        ),
        crew = listOf(
            CrewInfo("Greg Berlanti", "Creator", avatarUrl("Greg Berlanti")),
            CrewInfo("Andrew Kreisberg", "Creator", avatarUrl("Andrew Kreisberg")),
            CrewInfo("Blake Neely", "Composer", avatarUrl("Blake Neely"))
        )
    )
)

// Helper function to get cast/crew for a movie
fun getCastCrewForMovie(movieId: Int): MovieCastCrew? = movieCastCrewMap[movieId]
