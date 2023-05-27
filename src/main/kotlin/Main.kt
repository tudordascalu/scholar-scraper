import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.ListSerializer
import model.Publication
import org.slf4j.LoggerFactory
import services.ScholarScraperService
import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("MainLogger")
    // Constants
    val authorIds = listOf("IPQZ4bkAAAAJ", "CQkvlpUAAAAJ", "5AXE0skAAAAJ")
    // Get publications
    val scholarScraperService = ScholarScraperService()
    val publications = scholarScraperService.getPublications(authorIds)
    // Serialize publications
    val publicationsSerialized = Yaml.default.encodeToString(ListSerializer(Publication.serializer()), publications)
    // Save publications
    try {
        val file = File("/app/publications.yaml")
        file.writeText(publicationsSerialized)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    // Log publications
    print(publicationsSerialized)
}
