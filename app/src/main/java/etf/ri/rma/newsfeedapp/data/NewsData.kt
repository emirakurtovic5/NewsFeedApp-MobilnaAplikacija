package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem("1", "Politika01 Title01 001", "Snippet 123456789", null, "Politika", false, "Source 1", "2023-01-01"),
            NewsItem("2", "Sport01 Title01 001", "Snippet abcdefghi", null, "Sport", true, "Source 2", "2023-01-02"),
            NewsItem("3", "Nauka01 Title01 001", "Snippet jklmnopqr", null, "Nauka/tehnologija", false, "Source 3", "2030-01-03"),
            NewsItem("4", "Politika02 Title02 002", "Snippet stuvwxyza", null, "Politika", true, "Source 4", "2023-01-04"),
            NewsItem("5", "Sport02 Title02 002", "Snippet 987654321", null, "Sport", false, "Source 5", "2023-01-05"),
            NewsItem("6", "Nauka02 Title02 002", "Snippet 543216789", null, "Nauka/tehnologija", true, "Source 6", "2023-01-06"),
            NewsItem("7", "Politika03 Title03 003", "Snippet 345678912", null, "Politika", false, "Source 7", "2023-01-07"),
            NewsItem("8", "Sport03 Title03 003", "Snippet 891234567", null, "Sport", true, "Source 8", "2023-01-08"),
            NewsItem("9", "Nauka03 Title03 003", "Snippet 678123945", null, "Nauka/tehnologija", false, "Source 9", "2023-01-09"),
            NewsItem("10", "Politika04 Title04 004", "Snippet ihgfedcba", null, "Politika", true, "Source 10", "2020-01-10"),
            NewsItem("11", "Sport04 Title04 004", "Snippet rqponmlkj", null, "Sport", false, "Source 11", "2023-01-11"),
            NewsItem("12", "Nauka04 Title04 004", "Snippet azyxwvuts", null, "Nauka/tehnologija", true, "Source 12", "2023-01-12"),
            NewsItem("13", "Politika05 Title05 005", "Snippet qwertyuio", null, "Politika", false, "Source 13", "2023-01-13"),
            NewsItem("14", "Sport05 Title05 005", "Snippet asdfghjkl", null, "Sport", true, "Source 14", "2023-01-14"),
            NewsItem("15", "Nauka05 Title05 005", "Snippet zxcvbnmlk", null, "Nauka/tehnologija", false, "Source 15", "2023-01-15"),
            NewsItem("16", "Politika06 Title06 006", "Snippet qazwsxedc", null, "Politika", true, "Source 16", "2023-01-16"),
            NewsItem("17", "Sport06 Title06 006", "Snippet plmoknijb", null, "Sport", false, "Source 17", "2023-01-17"),
            NewsItem("18", "Nauka06 Title06 006", "Snippet tgbyhnujm", null, "Nauka/tehnologija", true, "Source 18", "2023-01-18"),
            NewsItem("19", "Politika07 Title07 007", "Snippet fghvbniop", null, "Politika", false, "Source 19", "2023-01-19"),
            NewsItem("20", "Sport07 Title07 007", "Snippet ertklmvxca", null, "Sport", true, "Source 20", "2023-01-20")
        )
    }}