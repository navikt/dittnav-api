package no.nav.personbruker.dittnav.api

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal fun JsonObject.int(key: String): Int =
    this[key]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Fant ikke integer med nøkkel $key")

internal fun JsonObject.bool(key: String): Boolean =
    this[key]?.jsonPrimitive?.boolean ?: throw IllegalArgumentException("Fant ikke boolean med nøkkel $key")

internal fun JsonObject.string(key: String): String =
    this[key]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Fant ikke boolean med nøkkel $key")

internal fun JsonObject.localdate(key: String, datePattern: String? = null) =
    this.localdateOrNull(key,datePattern) ?: throw IllegalArgumentException("Fant ikke localdate med nøkkel $key")

internal fun JsonElement?.isNullObject(key: String): Boolean {
    return when {
        this == null -> true
        this.jsonObject[key] is JsonNull -> true
        else -> false
    }
}
internal fun JsonObject.localdateOrNull(key: String, datePattern: String? = null): LocalDate? =
    this[key]?.let { dateJsonObject ->
        if (dateJsonObject is JsonNull) {
            null
        } else {
            require(dateJsonObject.jsonPrimitive.isString)
            datePattern?.let {
                LocalDate.parse(dateJsonObject.jsonPrimitive.content, DateTimeFormatter.ofPattern(datePattern))
            } ?: LocalDate.parse(dateJsonObject.jsonPrimitive.content)
        }
    }