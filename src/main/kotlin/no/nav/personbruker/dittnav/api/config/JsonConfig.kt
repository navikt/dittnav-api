package no.nav.personbruker.dittnav.api.config

import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json

fun buildJsonSerializer(): KotlinxSerializer {
    return KotlinxSerializer(json())
}

fun json(): Json {
    return Json {
        ignoreUnknownKeys = true
    }
}
