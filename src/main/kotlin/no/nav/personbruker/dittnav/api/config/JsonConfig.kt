package no.nav.personbruker.dittnav.api.config

import kotlinx.serialization.json.Json

fun jsonConfig(ignoreUnknownKeys: Boolean = true): Json {
    return Json {
        this.ignoreUnknownKeys = ignoreUnknownKeys
    }
}
