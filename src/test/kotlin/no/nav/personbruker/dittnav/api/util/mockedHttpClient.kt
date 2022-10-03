package no.nav.personbruker.dittnav.api.util

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import no.nav.personbruker.dittnav.api.config.jsonConfig


fun ApplicationTestBuilder.applicationHttpClient() =
    createClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(jsonConfig())
        }
        install(HttpTimeout)
    }