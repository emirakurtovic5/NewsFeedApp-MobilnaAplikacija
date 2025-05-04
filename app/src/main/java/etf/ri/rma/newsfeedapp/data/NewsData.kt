package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem("1", "Politika01 Title01 001", "Snippet 123456789", null, "Politika", false, "Source 1", "01-01-2025"),
            NewsItem("2", "Sport01 Title01 001", "Snippet abcdefghi", null, "Sport", true, "Source 2", "02-01-2025"),
            NewsItem("3", "Nauka01 Title01 001", "Snippet jklmnopqr", null, "Nauka/tehnologija", false, "Source 3", "03-01-2030"),
            NewsItem("4", "Politika02 Title02 002", "Snippet stuvwxyza", null, "Politika", true, "Source 4", "04-01-2025"),
            NewsItem("5", "Sport02 Title02 002", "Snippet 987654321", null, "Sport", false, "Source 5", "05-01-2025"),
            NewsItem("6", "Nauka02 Title02 002", "Snippet 543216789", null, "Nauka/tehnologija", true, "Source 6", "06-01-2025"),
            NewsItem("7", "Politika03 Title03 003", "Snippet 345678912", null, "Politika", false, "Source 7", "07-01-2025"),
            NewsItem("8", "Sport03 Title03 003", "Snippet 891234567", null, "Sport", true, "Source 8", "08-01-2025"),
            NewsItem("9", "Nauka03 Title03 003", "Snippet 678123945", null, "Nauka/tehnologija", false, "Source 9", "09-01-2025"),
            NewsItem("10", "Politika04 Title04 004", "Snippet ihgfedcba", null, "Politika", true, "Source 10", "10-01-2020"),
            NewsItem("11", "Sport04 Title04 004", "Snippet rqponmlkj", null, "Sport", false, "Source 11", "11-01-2025"),
            NewsItem("12", "Nauka04 Title04 004", "Snippet azyxwvuts", null, "Nauka/tehnologija", true, "Source 12", "12-01-2025"),
            NewsItem("13", "Politika05 Title05 005", "Snippet qwertyuio", null, "Politika", false, "Source 13", "13-01-2025"),
            NewsItem("14", "Sport05 Title05 005", "Snippet asdfghjkl", null, "Sport", true, "Source 14", "14-01-2025"),
            NewsItem("15", "Nauka05 Title05 005", "Snippet zxcvbnmlk", null, "Nauka/tehnologija", false, "Source 15", "15-01-2025"),
            NewsItem("16", "Politika06 Title06 006", "Snippet qazwsxedc", null, "Politika", true, "Source 16", "16-01-2025"),
            NewsItem("17", "Sport06 Title06 006", "Snippet plmoknijb", null, "Sport", false, "Source 17", "17-01-2025"),
            NewsItem("18", "Nauka06 Title06 006", "Snippet tgbyhnujm", null, "Nauka/tehnologija", true, "Source 18", "18-01-2025"),
            NewsItem("19", "Politika07 Title07 007", "Snippet fghvbniop", null, "Politika", false, "Source 19", "19-01-2025"),
            NewsItem("20", "Sport07 Title07 007", "Snippet ertklmvxca", null, "Sport", true, "Source 20", "20-01-2025")
        )
    }}