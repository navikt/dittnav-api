package no.nav.personbruker.dittnav.api.health

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import java.net.URL

enum class Status {
    OK, ERROR
}

data class SelftestStatus(val status: Status, val statusMessage: String)

suspend fun getStatus(url: URL, client: HttpClient): SelftestStatus {
    return try {
        val statusCode = client.get<HttpStatusCode>(url)
        SelftestStatus(Status.OK, statusCode.toString())
    } catch (cause: Throwable) {
        SelftestStatus(Status.ERROR, cause.toString())
    }
}
