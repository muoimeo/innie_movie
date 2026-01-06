package com.example.myapplication.data

import com.example.myapplication.data.local.entities.News

/**
 * Sample news articles with rich content for BBC/Variety-style display.
 * Images from TMDB backdrops.
 */
val sampleNews = listOf(
    News(
        id = 1,
        title = "'Stranger Things 5' Just Did What Shows Like 'Game of Thrones' Couldn't",
        subtitle = "The Duffer Brothers deliver a masterclass in how to end a beloved series on its own terms.",
        content = "The Duffer Brothers have confirmed that the final season will deliver on every promise made since Season 1.",
        body = """
            <p>In an era where beloved television series often stumble at the finish line, the Duffer Brothers are taking a radically different approach with the final season of <i>Stranger Things</i>.</p>
            
            <p>"We've had the ending planned since season two," Matt Duffer revealed in an exclusive interview. "Every character arc, every mystery, every emotional beat has been building to this moment. We're not just wrapping things up—we're fulfilling the promise we made to audiences from day one."</p>
            
            <p>Unlike <i>Game of Thrones</i>, which faced criticism for its rushed final season, <i>Stranger Things 5</i> will feature extended episodes, with some running close to two hours. Netflix has given the creators complete creative freedom, a rare luxury in the streaming era.</p>
            
            <p>The final season promises to bring back familiar faces while introducing new threats that will push our heroes to their limits. "This is the Empire Strikes Back of Stranger Things," Ross Duffer teased. "But also the Return of the Jedi. It's everything."</p>
            
            <p>Production has been ongoing in Atlanta, with elaborate practical effects and minimal CGI—staying true to the show's 80s aesthetic. The premiere is expected in late 2026, capping off one of Netflix's most successful original series.</p>
        """.trimIndent(),
        authorName = "Emily Zhang",
        authorImageUrl = "https://i.pravatar.cc/150?img=5",
        sourceName = "Variety",
        sourceUrl = "https://variety.com",
        imageUrl = "https://image.tmdb.org/t/p/original/sqVXe7s3QAvr9WObl8Xo88GGoL0.jpg",
        imageCaption = "The cast of Stranger Things on set in Atlanta. Photo: Netflix",
        category = "Television",
        tags = "Stranger Things,Netflix,Final Season,Duffer Brothers",
        likeCount = 3200,
        viewCount = 20450,
        commentCount = 677,
        readTimeMinutes = 4
    ),
    News(
        id = 2,
        title = "Dune: Part Three Officially Greenlit by Warner Bros",
        subtitle = "Denis Villeneuve's epic sci-fi saga will continue with 'Dune: Messiah' adaptation.",
        content = "Warner Bros has officially announced the third installment in the Dune franchise, adapting Frank Herbert's Dune: Messiah.",
        body = """
            <p>Warner Bros. Pictures has officially greenlit <i>Dune: Part Three</i>, continuing Denis Villeneuve's acclaimed adaptation of Frank Herbert's legendary science fiction novels.</p>
            
            <p>The film will adapt <i>Dune: Messiah</i>, the second book in Herbert's series, which takes place twelve years after the events of <i>Dune: Part Two</i> and explores the consequences of Paul Atreides' rise to power.</p>
            
            <p>"This is the story I've been waiting to tell," Villeneuve said in a statement. "Messiah is darker, more contemplative, and asks difficult questions about power and prophecy. It's the perfect conclusion to Paul's journey."</p>
            
            <p>Timothée Chalamet and Zendaya are both confirmed to return, with production expected to begin in early 2027. The film will once again be shot in IMAX, with Hans Zimmer returning to compose the score.</p>
            
            <p>The success of <i>Dune: Part Two</i>, which grossed over $700 million worldwide, made this announcement inevitable. Villeneuve has indicated this will be his final Dune film, though the franchise could continue with other directors.</p>
        """.trimIndent(),
        authorName = "Marcus Chen",
        authorImageUrl = "https://i.pravatar.cc/150?img=12",
        sourceName = "Hollywood Reporter",
        sourceUrl = "https://hollywoodreporter.com",
        imageUrl = "https://image.tmdb.org/t/p/original/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
        imageCaption = "Timothée Chalamet as Paul Atreides. Photo: Warner Bros.",
        category = "Film News",
        tags = "Dune,Warner Bros,Denis Villeneuve,Sci-Fi",
        likeCount = 16800,
        viewCount = 89200,
        commentCount = 1234,
        readTimeMinutes = 3
    ),
    News(
        id = 3,
        title = "Inside the Making of Oppenheimer: Nolan's Most Personal Film",
        subtitle = "How Christopher Nolan recreated the Trinity test and captured the conscience of a generation.",
        content = "Christopher Nolan explains how he recreated the Trinity test without CGI, using practical effects and IMAX cameras.",
        body = """
            <p>When Christopher Nolan decided to make a film about J. Robert Oppenheimer, he knew he was taking on perhaps the most ambitious project of his career—not in terms of scale, but in terms of moral weight.</p>
            
            <p>"This is a story about ideas," Nolan explained during a rare set visit. "About the responsibility that comes with knowledge. How do you make that cinematic? You start with the practical."</p>
            
            <p>The Trinity test sequence, which depicts the first nuclear explosion in human history, was created entirely with practical effects. Working with special effects supervisor Scott Fisher, Nolan's team designed a rig using magnesium, gasoline, and propane to create a massive fireball that could be filmed in IMAX.</p>
            
            <p>"We wanted the audience to feel what those scientists felt," Nolan said. "The awe, the terror, the realization of what they had created. CGI couldn't give us that. We needed something real."</p>
            
            <p>The result earned Nolan his first Academy Award for Best Director, with the film winning seven Oscars total including Best Picture. It stands as a testament to what's possible when a filmmaker commits fully to their vision.</p>
        """.trimIndent(),
        authorName = "Sarah Mitchell",
        authorImageUrl = "https://i.pravatar.cc/150?img=25",
        sourceName = "Empire",
        sourceUrl = "https://empireonline.com",
        imageUrl = "https://image.tmdb.org/t/p/original/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg",
        imageCaption = "Cillian Murphy as J. Robert Oppenheimer. Photo: Universal Pictures",
        category = "Feature",
        tags = "Oppenheimer,Christopher Nolan,Oscars,IMAX,Behind the Scenes",
        likeCount = 9100,
        viewCount = 67300,
        commentCount = 456,
        readTimeMinutes = 5
    ),
    News(
        id = 4,
        title = "The Batman 2: Matt Reeves Reveals Villain Details",
        subtitle = "The Court of Owls and a 'broken' Gotham await in the highly anticipated sequel.",
        content = "Matt Reeves shares exclusive details about The Batman sequel, teasing the Court of Owls and a deeper exploration of Gotham's corruption.",
        body = """
            <p>Director Matt Reeves has pulled back the curtain on <i>The Batman 2</i>, revealing that the sequel will delve deep into Gotham's most secretive organization: The Court of Owls.</p>
            
            <p>"The first film was about uncovering corruption," Reeves explained. "The sequel is about realizing that corruption goes much deeper than anyone imagined. The Court isn't just powerful—they've been shaping Gotham for centuries."</p>
            
            <p>Robert Pattinson returns as Bruce Wayne, now established in his role as the Dark Knight. The film picks up one year after the events of the first movie, with Gotham still reeling from the flooding and the Riddler's attacks.</p>
            
            <p>"Bruce is exhausted," Reeves said. "He's been fighting crime non-stop, but he's starting to realize that punching criminals isn't enough. The problems are systemic. And then he discovers the Court."</p>
            
            <p>Production is currently underway in Liverpool and London, with a 2026 release date targeted. Zoë Kravitz, Jeffrey Wright, and Andy Serkis all reprise their roles.</p>
        """.trimIndent(),
        authorName = "James Rodriguez",
        authorImageUrl = "https://i.pravatar.cc/150?img=33",
        sourceName = "Deadline",
        sourceUrl = "https://deadline.com",
        imageUrl = "https://image.tmdb.org/t/p/original/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg",
        imageCaption = "Robert Pattinson as The Batman. Photo: Warner Bros.",
        category = "Film News",
        tags = "The Batman,Matt Reeves,DC,Court of Owls,Robert Pattinson",
        likeCount = 12400,
        viewCount = 78900,
        commentCount = 890,
        readTimeMinutes = 4
    ),
    News(
        id = 5,
        title = "Oscar Predictions 2027: Early Frontrunners Emerge",
        subtitle = "From festival darlings to blockbuster surprises, here's who's leading the race.",
        content = "Awards season is heating up as critics and industry insiders weigh in on this year's strongest contenders.",
        body = """
            <p>With film festivals wrapping up and early screenings generating buzz, the 2027 Oscar race is beginning to take shape. Here are the early frontrunners that have emerged from the pack.</p>
            
            <h3>Best Picture Contenders</h3>
            <p>Leading the conversation is Paul Thomas Anderson's <i>The Conductor</i>, a sprawling drama about classical music and American ambition that has drawn comparisons to <i>There Will Be Blood</i>. Close behind is Greta Gerwig's <i>Chronicles of Narnia</i> reboot, which has surprised critics with its emotional depth.</p>
            
            <h3>Acting Categories</h3>
            <p>The Best Actor race looks like a showdown between Daniel Craig's transformative performance in <i>The Conductor</i> and Austin Butler's return to music biopics with <i>Mercury Rising</i>, a Freddie Mercury origin story.</p>
            
            <p>For Best Actress, Saoirse Ronan is generating Oscar buzz for her portrayal of a war correspondent in <i>Last Light</i>, while Zendaya's dramatic turn in <i>Dune: Messiah</i> could finally earn her a nomination.</p>
            
            <p>The ceremony is scheduled for March 2, 2027. Stay tuned for our monthly updates as the race intensifies.</p>
        """.trimIndent(),
        authorName = "Rachel Kim",
        authorImageUrl = "https://i.pravatar.cc/150?img=41",
        sourceName = "IndieWire",
        sourceUrl = "https://indiewire.com",
        imageUrl = "https://image.tmdb.org/t/p/original/wDWwtvkRRlgTiUr6TyLSMX8FCuZ.jpg",
        imageCaption = "The Oscar statuette. Photo: AMPAS",
        category = "Awards",
        tags = "Oscars,Academy Awards,Predictions,Awards Season",
        likeCount = 5600,
        viewCount = 41200,
        commentCount = 234,
        readTimeMinutes = 6
    ),
    News(
        id = 6,
        title = "Marvel Studios Announces Phase 7 Slate",
        subtitle = "A return to storytelling basics with 'smaller, more personal' superhero films.",
        content = "Kevin Feige unveils Marvel's new direction: fewer films, higher stakes, and a focus on character-driven narratives.",
        body = """
            <p>After years of criticism about superhero fatigue, Marvel Studios president Kevin Feige has unveiled a dramatically different approach for Phase 7 of the Marvel Cinematic Universe.</p>
            
            <p>"We listened," Feige said at a press event in Los Angeles. "Audiences were telling us they felt overwhelmed. So we're scaling back—not in ambition, but in quantity. Every film needs to matter."</p>
            
            <p>Phase 7 will feature just six films spread over three years, compared to the twelve films in Phase 5. Each project will receive extended development time and larger budgets for practical effects.</p>
            
            <h3>Confirmed Projects</h3>
            <ul>
                <li><strong>The Fantastic Four: Doom</strong> - The sequel to the reboot, introducing Doctor Doom</li>
                <li><strong>Wolverine</strong> - A standalone film set in the MCU's past</li>
                <li><strong>Avengers: Secret Wars</strong> - The culminating event of the Multiverse Saga</li>
            </ul>
            
            <p>"Secret Wars will be our Endgame," Feige promised. "Everything has been building to this. And this time, we're giving ourselves the time to make it right."</p>
        """.trimIndent(),
        authorName = "David Park",
        authorImageUrl = "https://i.pravatar.cc/150?img=52",
        sourceName = "The Wrap",
        sourceUrl = "https://thewrap.com",
        imageUrl = "https://image.tmdb.org/t/p/original/9xfDWXAUbFXQK585JvByT5pEAhe.jpg",
        imageCaption = "Kevin Feige at San Diego Comic-Con. Photo: Marvel Studios",
        category = "Industry",
        tags = "Marvel,MCU,Phase 7,Kevin Feige,Avengers",
        likeCount = 23400,
        viewCount = 156000,
        commentCount = 2890,
        readTimeMinutes = 4
    )
)
