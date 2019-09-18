package no.nav.personbruker.dittnav.api

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.*
import no.nav.personbruker.dittnav.api.config.HttpClient
import java.io.BufferedReader
import java.io.File

class HttpClientMock: HttpClient() {

    override val client: io.ktor.client.HttpClient = io.ktor.client.HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Text.Plain.toString()))
                when (request.url.fullPath) {
                    "/person/dittnav-legacy-api/meldinger/ubehandlede" -> {
                        respond(getJsonFile("meldinger/ubehandlede"), headers = responseHeaders)
                    }
                    "/person/dittnav-legacy-api/saker/paabegynte" -> {
                    respond(getJsonFile("saker/paabegynte"), headers = responseHeaders)
                    }
                    "/person/dittnav-legacy-api/person/personinfo" -> {
                        respond(getJsonFile("person/personinfo"), headers = responseHeaders)
                    }
                    "/meldinger" -> {
                        respond(getJsonFile("meldinger"), headers = responseHeaders)
                    }
                    else -> error("Unhandled ${request.url.fullPath}")
                }
            }
        }
    }

    private fun getJsonFile(path: String): String {
        val bufferedReader: BufferedReader = File("src/test/resources/mockData/$path.json").bufferedReader()
        return bufferedReader.use { it.readText() }
    }
}
