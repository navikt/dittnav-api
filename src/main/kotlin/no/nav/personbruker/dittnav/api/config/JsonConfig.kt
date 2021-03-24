package no.nav.personbruker.dittnav.api.config

import kotlinx.serialization.json.Json

fun json(ignoreUnknownKeys: Boolean = false): Json {
    return Json {
        this.ignoreUnknownKeys = ignoreUnknownKeys
    }
}
