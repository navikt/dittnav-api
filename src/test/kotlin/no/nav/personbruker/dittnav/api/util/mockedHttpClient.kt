package no.nav.personbruker.dittnav.api.util

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

fun createBasicMockedHttpClient(respond: MockRequestHandleScope.() -> HttpResponseData): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler {
                respond()
            }
        }
        install(JsonFeature)
    }
}
