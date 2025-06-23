package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.News
import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem(
                news = News(
                    id = 1,
                    uuid = "1",
                    title = "Politika01 Title01 001",
                    snippet = "Snippet 123456789",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = false,
                    source = "Source 1",
                    publishedDate = "01-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 2,
                    uuid = "2",
                    title = "Sport01 Title01 001",
                    snippet = "Snippet abcdefghi",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = true,
                    source = "Source 2",
                    publishedDate = "02-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 3,
                    uuid = "3",
                    title = "Nauka01 Title01 001",
                    snippet = "Snippet jklmnopqr",
                    imageUrl = null,
                    category = "Nauka/tehnologija",
                    isFeatured = false,
                    source = "Source 3",
                    publishedDate = "03-01-2030"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 4,
                    uuid = "4",
                    title = "Politika02 Title02 002",
                    snippet = "Snippet stuvwxyza",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = true,
                    source = "Source 4",
                    publishedDate = "04-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 5,
                    uuid = "5",
                    title = "Sport02 Title02 002",
                    snippet = "Snippet 987654321",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = false,
                    source = "Source 5",
                    publishedDate = "05-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 6,
                    uuid = "6",
                    title = "Nauka02 Title02 002",
                    snippet = "Snippet 543216789",
                    imageUrl = null,
                    category = "Nauka/tehnologija",
                    isFeatured = true,
                    source = "Source 6",
                    publishedDate = "06-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 7,
                    uuid = "7",
                    title = "Politika03 Title03 003",
                    snippet = "Snippet 345678912",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = false,
                    source = "Source 7",
                    publishedDate = "07-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 8,
                    uuid = "8",
                    title = "Sport03 Title03 003",
                    snippet = "Snippet 891234567",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = true,
                    source = "Source 8",
                    publishedDate = "08-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 9,
                    uuid = "9",
                    title = "Nauka03 Title03 003",
                    snippet = "Snippet 678123945",
                    imageUrl = null,
                    category = "Nauka/tehnologija",
                    isFeatured = false,
                    source = "Source 9",
                    publishedDate = "09-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 10,
                    uuid = "10",
                    title = "Politika04 Title04 004",
                    snippet = "Snippet ihgfedcba",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = true,
                    source = "Source 10",
                    publishedDate = "10-01-2020"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 11,
                    uuid = "11",
                    title = "Sport04 Title04 004",
                    snippet = "Snippet rqponmlkj",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = false,
                    source = "Source 11",
                    publishedDate = "11-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 12,
                    uuid = "12",
                    title = "Nauka04 Title04 004",
                    snippet = "Snippet azyxwvuts",
                    imageUrl = null,
                    category = "Nauka/tehnologija",
                    isFeatured = true,
                    source = "Source 12",
                    publishedDate = "12-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 13,
                    uuid = "13",
                    title = "Politika05 Title05 005",
                    snippet = "Snippet qwertyuio",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = false,
                    source = "Source 13",
                    publishedDate = "13-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 14,
                    uuid = "14",
                    title = "Sport05 Title05 005",
                    snippet = "Snippet asdfghjkl",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = true,
                    source = "Source 14",
                    publishedDate = "14-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 15,
                    uuid = "15",
                    title = "Nauka05 Title05 005",
                    snippet = "Snippet zxcvbnmlk",
                    imageUrl = null,
                    category = "Nauka/tehnologija",
                    isFeatured = false,
                    source = "Source 15",
                    publishedDate = "15-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 16,
                    uuid = "16",
                    title = "Politika06 Title06 006",
                    snippet = "Snippet qazwsxedc",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = true,
                    source = "Source 16",
                    publishedDate = "16-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 17,
                    uuid = "17",
                    title = "Sport06 Title06 006",
                    snippet = "Snippet plmoknijb",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = false,
                    source = "Source 17",
                    publishedDate = "17-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 18,
                    uuid = "18",
                    title = "Nauka06 Title06 006",
                    snippet = "Snippet tgbyhnujm",
                    imageUrl = null,
                    category = "Nauka/tehnologija",
                    isFeatured = true,
                    source = "Source 18",
                    publishedDate = "18-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 19,
                    uuid = "19",
                    title = "Politika07 Title07 007",
                    snippet = "Snippet fghvbniop",
                    imageUrl = null,
                    category = "Politika",
                    isFeatured = false,
                    source = "Source 19",
                    publishedDate = "19-01-2025"
                ),
                tags = emptyList()
            ),
            NewsItem(
                news = News(
                    id = 20,
                    uuid = "20",
                    title = "Sport07 Title07 007",
                    snippet = "Snippet ertklmvxca",
                    imageUrl = null,
                    category = "Sport",
                    isFeatured = true,
                    source = "Source 20",
                    publishedDate = "20-01-2025"
                ),
                tags = emptyList()
            )
        )
    }}