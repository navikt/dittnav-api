package no.nav.personbruker.dittnav.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.features.json.JacksonSerializer

fun buildJsonSerializer(): JacksonSerializer {
    return JacksonSerializer {
        enableDittNavJsonConfig()
    }
}

fun ObjectMapper.enableDittNavJsonConfig() {
    registerModule(JavaTimeModule())
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}
