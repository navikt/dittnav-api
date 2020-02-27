package no.nav.personbruker.dittnav.api.done

import org.json.simple.JSONObject

data class DoneDto(
        val uid: String,
        val eventId: String
)

fun DoneDto.toJsonString(): String {
    return JSONObject.toJSONString(mapOf<String, String>("uid" to uid, "eventId" to eventId))
}


