package services

import model.Publication
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup

class ScholarScraperService(private val url: String = "https://scholar.google.com/") {
    fun getPublications(authorIds: List<String>) = authorIds
        .flatMap { authorId -> getPublications(authorId) }
        .distinctBy { it.title }
        .shuffled()
        .sortedByDescending { publication -> publication.year }

    fun getPublications(authorId: String = "IPQZ4bkAAAAJ"): List<Publication> {
        // Get HTML
        try {
            val doc = Jsoup.connect("$url/citations?hl=da&user=$authorId&view_op=list_works&sortby=pubdate").get()
            // Process HTML
            val publications = doc.select("#gsc_a_b tr.gsc_a_tr").mapNotNull { element ->
                val title = element.select(".gsc_a_t a.gsc_a_at").text()
                val authors = element.select("div.gs_gray").getOrNull(0)?.text()
                val journal = element.select("div.gs_gray").getOrNull(1)?.let { journalElement ->
                    journalElement.text().replace(journalElement.select("span").text(), "")
                } ?: ""
                val year = element.select(".gsc_a_y span").text().toIntOrNull()
                // Check for mandatory fields
                when {
                    (title.isNullOrEmpty() || authors.isNullOrEmpty()) -> null
                    else -> Publication(title = title, author = authors, journal = journal, year = year)
                }
            }
            // Construct Author
            return publications
        } catch (e: HttpStatusException) {
            return emptyList()
        }
    }
}
