package no.nav.personbruker.dittnav.api.health

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.slf4j.LoggerFactory
import java.net.URL

enum class Status {
    OK, ERROR
}

data class SelftestStatus(val status: Status, val statusMessage: String, val pingedURL: URL)

private val log = LoggerFactory.getLogger(SelftestStatus::class.java)

suspend fun getStatus(url: URL, client: HttpClient): SelftestStatus {
    return try {
        val statusCode = client.get<HttpStatusCode>(url)
        SelftestStatus(Status.OK, statusCode.toString(), url)
    } catch (exception: Exception) {
        log.error("Feil på Selftest mot $url", exception)
        SelftestStatus(Status.ERROR, exception.toString(), url)
    }
}
