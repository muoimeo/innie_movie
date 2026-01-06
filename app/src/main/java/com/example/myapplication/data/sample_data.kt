package com.example.myapplication.data

import com.example.myapplication.R
import com.example.myapplication.data.local.entities.MovieReview
import com.example.myapplication.data.local.entities.UserProfile
import com.example.myapplication.data.local.entities.MovieItem


// --- LIST 1: DÀNH CHO TAB FOR YOU (10 items) ---
val forYouReviews = listOf(
    MovieReview(1, "The Substance", "2024", "The Marquee", 4, 34, "Coralie Fargeat’s second feature is an uncompromising and wildly entertaining body horror satire...", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(2, "Anora", "2024", "Sean Baker", 5, 120, "A breathtaking journey through the neon-lit streets of Brooklyn and the luxury of Vegas...", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(3, "The Irishman", "2019", "Adrian", 4, 8, "A masterclass in slow-burn storytelling. Scorsese proves he still owns the gangster genre.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(4, "Dune: Part Two", "2024", "Paul Atreides", 5, 45, "Visually stunning and emotionally heavy. One of the best sci-fi sequels ever made.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(5, "Oppenheimer", "2023", "Nolan Fan", 5, 89, "The sound design alone is worth the price of admission. Cillian Murphy is haunting.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(6, "The Dark Knight", "2008", "Bruce W.", 5, 210, "Still the gold standard for superhero movies. Ledger's Joker is legendary.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(7, "Parasite", "2019", "Bong Joon-ho", 5, 150, "A perfect mix of dark comedy, thriller, and social commentary. Truly unforgettable.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(8, "Interstellar", "2014", "Cooper", 4, 67, "Cried like a baby. The physics and the father-daughter bond are both mind-blowing.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(9, "Spider-Man: Across the Spider-Verse", "2023", "Miles", 5, 99, "Every frame is a piece of art. The animation is beyond anything I've seen before.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(10, "Zack Snyder's Justice League", "2021", "Audrey", 5, 25, "The 4-hour runtime flies by. This is the vision we deserved from the beginning.", R.drawable.the_irishman, R.drawable.the_irishman)
)

// --- LIST 2: DÀNH CHO TAB FOLLOWING (10 items) ---
val followingReviews = listOf(
    MovieReview(11, "Challengers", "2024", "Zendaya Fan", 4, 12, "The tension on and off the court is electric. A stylish, modern drama.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(12, "Everything Everywhere All At Once", "2022", "Evelyn", 5, 300, "A chaotic masterpiece that somehow makes perfect sense by the end. Amazing.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(13, "Poor Things", "2023", "Bella Baxter", 4, 56, "Emma Stone is incredible. A weird, wonderful, and visually unique journey.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(14, "Barbie", "2023", "Ken", 3, 400, "Bright, pink, and fun, but also surprisingly deep. Ryan Gosling stole the show.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(15, "Past Lives", "2023", "Nora", 5, 28, "Quietly devastating. A beautiful exploration of 'what if' and destiny.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(16, "Godzilla Minus One", "2023", "Kaiju Lover", 5, 76, "Best Godzilla movie in decades. The human story is just as good as the destruction.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(17, "tick, tick... BOOM!", "2021", "Rebecca", 4, 20, "Andrew Garfield's performance is high-energy and heartbreaking. A love letter to theater.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(18, "The Batman", "2022", "Vengeance", 4, 112, "Gritty, noir, and grounded. Pattinson is a fantastic, brooding Bruce Wayne.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(19, "Top Gun: Maverick", "2022", "Pete Mitchell", 5, 230, "The pure adrenaline of cinema. A movie that was made for the big screen.", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(20, "Avatar: The Way of Water", "2022", "James C.", 4, 180, "The visuals are second to none. A beautiful underwater world that feels real.", R.drawable.the_irishman, R.drawable.the_irishman)
)

val sampleProfile = UserProfile(
    name = "Kyran",
    username = "@kyran_d",
    avatarRes = R.drawable.the_irishman,
    backgroundRes = R.drawable.the_irishman,
    followersCount = 500,
    friendsCount = 67,
    followingCount = 420,
    watchedCount = 455,
    filmsThisYear = 33,
    reviewsCount = 30,
    favoriteFilms = listOf(
        R.drawable.the_irishman,
        R.drawable.the_irishman,
        R.drawable.the_irishman,
        R.drawable.the_irishman
    ),
    albumsCount = 4
)

val watchHistoryList = listOf(
    MovieItem("Stranger Things", R.drawable.the_irishman),
    MovieItem("Black Phone 2", R.drawable.the_irishman),
    MovieItem("Predator: Badlands", R.drawable.the_irishman),
    MovieItem("Avatar: Fire and Ash", R.drawable.the_irishman),
    MovieItem("Frankenstein", R.drawable.the_irishman),
    MovieItem("Five Nights at Freddy's 2", R.drawable.the_irishman),
    MovieItem("The Conjuring: Last Rites", R.drawable.the_irishman),
    MovieItem("Zootopia 2", R.drawable.the_irishman),
    MovieItem("Deadpool and Wolverine", R.drawable.the_irishman),
    MovieItem("Gladiator II", R.drawable.the_irishman),
    MovieItem("Wicked", R.drawable.the_irishman),
    MovieItem("Moana 2", R.drawable.the_irishman),
    MovieItem("Mufasa: The Lion King", R.drawable.the_irishman),
    MovieItem("Sonic the Hedgehog 3", R.drawable.the_irishman),
    MovieItem("Kraven the Hunter", R.drawable.the_irishman),
    MovieItem("The Lord of the Rings: The War of the Rohirrim", R.drawable.the_irishman),
    MovieItem("Nosferatu", R.drawable.the_irishman),
    MovieItem("A Minecraft Movie", R.drawable.the_irishman),
    MovieItem("Captain America: Brave New World", R.drawable.the_irishman)
)

val recentReviewedJaws = MovieReview(
    id = 100,
    movieTitle = "Jaws",
    year = "1975",
    reviewerName = "Kyran",
    rating = 5,
    commentCount = 10,
    reviewText = "A tragic tale of a lost and distressed fish hunted down by an aquaphobic police chief, a disgraced oceanographer trying to regain some of his tarnished reputation and a nasty drunk with a fetish for bowlegged women. All of them egged on by a corrupt mayor trying to find someone or something to blame for his small islands dwindling tourist industry and his poor taste in fashion. I wish it had a happy ending...",
    posterRes = R.drawable.the_irishman,
    avatarRes = R.drawable.the_irishman
)

enum class NotificationType {
    NEWS,     // Yellow - News/Articles
    COMMENT,  // Green - Comments/Replies
    TRAILER,  // Blue - New Trailers
    FRIEND    // Purple - Friend Activity
}

data class NotificationItem(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val isRead: Boolean,
    val avatarRes: Int,
    val type: NotificationType
)

val sampleNotifications = listOf(
    // NEWS type - Yellow
    NotificationItem(1, "Marvel Studios Announces Phase 7 Slate", "Big news! Marvel has unveiled their exciting Phase 7 lineup. Check out the full details!", "2 days ago", false, R.drawable.the_irishman, NotificationType.NEWS),
    NotificationItem(2, "Oscar Nominations Revealed!", "The Academy has announced this year's Oscar nominations. See who made the cut!", "3 days ago", true, R.drawable.the_irishman, NotificationType.NEWS),
    
    // TRAILER type - Blue
    NotificationItem(3, "New Trailer: Dune Part Three", "A new trailer for Dune: Part Three has been released! Watch it now.", "2 days ago", false, R.drawable.the_irishman, NotificationType.TRAILER),
    NotificationItem(4, "Avatar: Fire and Ash Trailer", "The first teaser for Avatar 4 is finally here. Prepare to be amazed!", "1 week ago", true, R.drawable.the_irishman, NotificationType.TRAILER),
    
    // COMMENT type - Green
    NotificationItem(5, "Emma replied to your comment", "Emma replied: 'I totally agree! The cinematography was stunning.'", "1 hour ago", false, R.drawable.the_irishman, NotificationType.COMMENT),
    NotificationItem(6, "New comment on Oppenheimer", "John commented on your review of Oppenheimer: 'Great analysis!'", "5 hours ago", true, R.drawable.the_irishman, NotificationType.COMMENT),
    NotificationItem(7, "Viral Review Alert!", "Your post about Parasite just hit 1,000 likes. Check out the latest comments!", "2 days ago", true, R.drawable.the_irishman, NotificationType.COMMENT),
    
    // FRIEND type - Purple
    NotificationItem(8, "Emma created a new album", "Your friend Emma created a new album: 'Best Horror Films 2024'.", "3 hours ago", false, R.drawable.the_irishman, NotificationType.FRIEND),
    NotificationItem(9, "John updated his watchlist", "John added 'Gladiator II' and 3 other movies to his watchlist.", "1 day ago", true, R.drawable.the_irishman, NotificationType.FRIEND),
    NotificationItem(10, "Sarah rated Interstellar ★★★★★", "Your friend Sarah gave Interstellar a 5-star rating!", "2 days ago", true, R.drawable.the_irishman, NotificationType.FRIEND),
    NotificationItem(11, "New friend request", "Mike wants to be your friend on Innie Movie.", "3 days ago", true, R.drawable.the_irishman, NotificationType.FRIEND),
    NotificationItem(12, "David started following you", "David is now following your movie activity.", "1 week ago", true, R.drawable.the_irishman, NotificationType.FRIEND)
)