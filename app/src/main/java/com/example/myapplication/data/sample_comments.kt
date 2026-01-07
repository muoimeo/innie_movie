package com.example.myapplication.data

import com.example.myapplication.data.local.entities.Comment

/**
 * Sample comments for demo - uses real users from database
 * Covers albums, news, shots, and reviews
 */
val sampleComments = listOf(
    // ========== ALBUM 1 - Best of 2024 ==========
    Comment(userId = "user_paul", targetType = "album", targetId = 1,
        content = "Great collection! These are all must-watch films. 🎬",
        createdAt = System.currentTimeMillis() - 3600000 * 2),
    Comment(userId = "user_miles", targetType = "album", targetId = 1,
        content = "Love this list! Spider-Verse should definitely be here too 🕷️",
        createdAt = System.currentTimeMillis() - 3600000 * 5),
    Comment(userId = "user_evelyn", targetType = "album", targetId = 1,
        content = "Perfect for a movie marathon weekend!",
        createdAt = System.currentTimeMillis() - 3600000 * 8),
    Comment(userId = "user_chani", targetType = "album", targetId = 1,
        content = "Dune Part Two should be #1 in my opinion 🏜️",
        createdAt = System.currentTimeMillis() - 86400000 * 1),
    
    // ========== ALBUM 2 - Hidden Gems ==========
    Comment(userId = "user_nolan_fan", targetType = "album", targetId = 2,
        content = "The Nolan collection is essential viewing 🎬",
        createdAt = System.currentTimeMillis() - 86400000 * 2),
    Comment(userId = "user_marquee", targetType = "album", targetId = 2,
        content = "Excellent curation! Every film here is a masterpiece.",
        createdAt = System.currentTimeMillis() - 3600000 * 12),
    Comment(userId = "user_cobb", targetType = "album", targetId = 2,
        content = "Inception still blows my mind every time!",
        createdAt = System.currentTimeMillis() - 86400000 * 1),
    
    // ========== ALBUM 3 - Christopher Nolan Filmography ==========
    Comment(userId = "user_scientist", targetType = "album", targetId = 3,
        content = "Oppenheimer is Nolan's best work IMO",
        createdAt = System.currentTimeMillis() - 3600000 * 6),
    Comment(userId = "user_cooper", targetType = "album", targetId = 3,
        content = "Interstellar deserves more recognition! 🚀",
        createdAt = System.currentTimeMillis() - 3600000 * 18),
    
    // ========== ALBUM 4 - Mind-Bending Films ==========
    Comment(userId = "user_arthur", targetType = "album", targetId = 4,
        content = "These films really make you think",
        createdAt = System.currentTimeMillis() - 86400000 * 3),
    Comment(userId = "user_evelyn", targetType = "album", targetId = 4,
        content = "Everything Everywhere is life-changing ❤️",
        createdAt = System.currentTimeMillis() - 3600000 * 4),
        
    // ========== ALBUM 5 - Asian Cinema ==========
    Comment(userId = "user_bong", targetType = "album", targetId = 5,
        content = "Once you overcome subtitles, you discover amazing films!",
        createdAt = System.currentTimeMillis() - 86400000 * 1),
    Comment(userId = "user_miles", targetType = "album", targetId = 5,
        content = "Parasite is still the GOAT 🏆",
        createdAt = System.currentTimeMillis() - 3600000 * 7),
        
    // ========== NEWS 1 ==========
    Comment(userId = "user_marquee", targetType = "news", targetId = 1,
        content = "Finally! Stranger Things 5 is going to be epic.",
        createdAt = System.currentTimeMillis() - 3600000 * 3),
    Comment(userId = "user_paul", targetType = "news", targetId = 1,
        content = "Can't wait for this. The Duffer Brothers never disappoint!",
        createdAt = System.currentTimeMillis() - 3600000 * 6),
    Comment(userId = "user_miles", targetType = "news", targetId = 1,
        content = "I've been waiting for this news for so long! 🎉",
        createdAt = System.currentTimeMillis() - 3600000 * 9),
        
    // ========== NEWS 2 ==========
    Comment(userId = "user_nolan_fan", targetType = "news", targetId = 2,
        content = "Dune Part Three confirmed? My wallet is ready 💸",
        createdAt = System.currentTimeMillis() - 3600000 * 2),
    Comment(userId = "user_chani", targetType = "news", targetId = 2,
        content = "The spice will flow! Cannot wait 🏜️",
        createdAt = System.currentTimeMillis() - 3600000 * 5),
    Comment(userId = "user_paul", targetType = "news", targetId = 2,
        content = "Denis Villeneuve is a genius. Day one watch for sure!",
        createdAt = System.currentTimeMillis() - 3600000 * 8),
        
    // ========== NEWS 3 ==========
    Comment(userId = "user_evelyn", targetType = "news", targetId = 3,
        content = "Very insightful analysis. This is why I love film criticism.",
        createdAt = System.currentTimeMillis() - 86400000 * 1),
    Comment(userId = "user_marquee", targetType = "news", targetId = 3,
        content = "Great perspective on the industry trends",
        createdAt = System.currentTimeMillis() - 3600000 * 14),
        
    // ========== NEWS 4 ==========
    Comment(userId = "user_bruce", targetType = "news", targetId = 4,
        content = "The Batman sequel is going to be amazing!",
        createdAt = System.currentTimeMillis() - 3600000 * 4),
    Comment(userId = "user_vengeance", targetType = "news", targetId = 4,
        content = "Pattinson deserves all the praise. I am vengeance! 🦇",
        createdAt = System.currentTimeMillis() - 3600000 * 7),
        
    // ========== NEWS 5 ==========
    Comment(userId = "user_ken", targetType = "news", targetId = 5,
        content = "Barbie was such a surprise hit! Ryan Gosling killed it",
        createdAt = System.currentTimeMillis() - 86400000 * 2),
    Comment(userId = "user_evelyn", targetType = "news", targetId = 5,
        content = "Margot Robbie was perfect for the role 💖",
        createdAt = System.currentTimeMillis() - 3600000 * 16),
        
    // ========== SHOT 1 ==========
    Comment(userId = "user_miles", targetType = "shot", targetId = 1,
        content = "This scene is absolutely stunning! The cinematography 🔥",
        createdAt = System.currentTimeMillis() - 3600000 * 1),
    Comment(userId = "user_chani", targetType = "shot", targetId = 1,
        content = "The sandworm reveal still gives me chills.",
        createdAt = System.currentTimeMillis() - 3600000 * 2),
    Comment(userId = "user_marquee", targetType = "shot", targetId = 1,
        content = "Villeneuve is a visual master. Every frame is a painting.",
        createdAt = System.currentTimeMillis() - 3600000 * 4),
    Comment(userId = "user_paul", targetType = "shot", targetId = 1,
        content = "The best scene in the entire movie! 🎬",
        createdAt = System.currentTimeMillis() - 3600000 * 6),
    Comment(userId = "user_nolan_fan", targetType = "shot", targetId = 1,
        content = "This is pure cinema. No dialogue needed.",
        createdAt = System.currentTimeMillis() - 3600000 * 8),
        
    // ========== SHOT 2 ==========
    Comment(userId = "user_bruce", targetType = "shot", targetId = 2,
        content = "The lighting in this scene is incredible",
        createdAt = System.currentTimeMillis() - 3600000 * 3),
    Comment(userId = "user_cobb", targetType = "shot", targetId = 2,
        content = "One of the most memorable shots in film history",
        createdAt = System.currentTimeMillis() - 3600000 * 5),
        
    // ========== SHOT 3 ==========
    Comment(userId = "user_maverick", targetType = "shot", targetId = 3,
        content = "The practical effects here are insane! No CGI needed 🛩️",
        createdAt = System.currentTimeMillis() - 3600000 * 2),
    Comment(userId = "user_cooper", targetType = "shot", targetId = 3,
        content = "Tom Cruise really does his own stunts. Legend!",
        createdAt = System.currentTimeMillis() - 3600000 * 4),
        
    // ========== REVIEWS ==========
    Comment(userId = "user_paul", targetType = "review", targetId = 1,
        content = "Totally agree with this review! Pattinson nailed it.",
        createdAt = System.currentTimeMillis() - 86400000 * 2),
    Comment(userId = "user_evelyn", targetType = "review", targetId = 1,
        content = "The Riddler was so creepy, loved it!",
        createdAt = System.currentTimeMillis() - 86400000 * 1),
    Comment(userId = "user_nolan_fan", targetType = "review", targetId = 3,
        content = "The spice must flow! 🏜️ Great review!",
        createdAt = System.currentTimeMillis() - 86400000 * 3),
    Comment(userId = "user_chani", targetType = "review", targetId = 3,
        content = "Villeneuve is a master of sci-fi. Well said!",
        createdAt = System.currentTimeMillis() - 86400000 * 2),
    Comment(userId = "user_scientist", targetType = "review", targetId = 5,
        content = "Oppenheimer really made me think about history differently",
        createdAt = System.currentTimeMillis() - 86400000 * 4),
    Comment(userId = "user_marquee", targetType = "review", targetId = 5,
        content = "Best film of 2023 and it's not even close",
        createdAt = System.currentTimeMillis() - 86400000 * 3)
)
