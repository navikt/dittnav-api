package no.nav.personbruker.dittnav.api

import io.kotest.matchers.shouldBe
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.intellij.lang.annotations.Language
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

internal fun JsonObject.int(key: String): Int =
    this[key]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Fant ikke integer med nøkkel $key")

internal fun JsonObject.stringArray(key: String): List<String> = array(key).map { it.jsonPrimitive.content }
private fun JsonObject.array(key: String): JsonArray =
    this[key]?.jsonArray?: throw IllegalArgumentException("Fant ikke integer med nøkkel $key")

internal fun JsonObject.bool(key: String): Boolean =
    this[key]?.jsonPrimitive?.boolean ?: throw IllegalArgumentException("Fant ikke boolean med nøkkel $key")

internal fun JsonObject.string(key: String): String =
    this[key]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Fant ikke boolean med nøkkel $key")

internal fun JsonObject.localdate(key: String, datePattern: String? = null) =
    this.localdateOrNull(key,datePattern) ?: throw IllegalArgumentException("Fant ikke localdate med nøkkel $key")

internal fun JsonObject.zonedDateTime(key: String, datePattern: String? = null) =
    this.zonedDateTimeOrNull(key,datePattern) ?: throw IllegalArgumentException("Fant ikke zonedDateTime med nøkkel $key")

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

internal fun JsonObject.zonedDateTimeOrNull(key: String, datePattern: String? = null):ZonedDateTime? =
    this[key]?.let { dateJsonObject ->
        if (dateJsonObject is JsonNull) {
            null
        } else {
            require(dateJsonObject.jsonPrimitive.isString)
            datePattern?.let {
                ZonedDateTime.parse(dateJsonObject.jsonPrimitive.content, DateTimeFormatter.ofPattern(datePattern))
            } ?: ZonedDateTime.parse(dateJsonObject.jsonPrimitive.content)
        }
    }

internal fun String.jsonObject() = Json.parseToJsonElement(this).jsonObject
internal  fun String.jsonArray() = Json.parseToJsonElement(this).jsonArray
internal infix fun ZonedDateTime.shouldBeSameDateTimeAs(expected: ZonedDateTime) =
    this.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES) shouldBe expected.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES)

@Language("JSON")
internal fun rawEventHandlerVarsel(
    eventId: String = "12345",
    fodselsnummer: String = "5432176",
    grupperingsId: String = "gruppergrupp",
    førstBehandlet: String = "${ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES)}",
    produsent: String = "testprdusent",
    sikkerhetsnivå: Int = 4,
    sistOppdatert: String = "${ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)}",
    tekst: String = "Teskt som er tekst som er tekst",
    link: String = "https://test.link.tadda",
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = emptyList(),
    aktiv: Boolean
): String =
    """ {
        "fodselsnummer" : "$fodselsnummer",
        "grupperingsId": "$grupperingsId",
        "eventId": "$eventId",
        "forstBehandlet": "$førstBehandlet",
        "produsent": "$produsent",
        "sikkerhetsnivaa": "$sikkerhetsnivå",
        "sistOppdatert": "$sistOppdatert",
        "tekst": "$tekst",
        "link": "$link",
        "aktiv": $aktiv,
        "eksternVarslingSendt": $eksternVarslingSendt,
        "eksternVarslingKanaler": ${eksternVarslingKanaler.withDoublequotedValues()}
         }
        """.trimIndent()

private fun List<String>.withDoublequotedValues(): String = toSpesificJsonFormat { """"$this"""" }
internal fun <E> List<E>.toSpesificJsonFormat(formatter: E.() -> String) = StringBuilder("[").let { strBuilder ->
    val iterator = iterator()
    while (iterator.hasNext()) {
        strBuilder.append(iterator.next().formatter())
        if (iterator.hasNext())
            strBuilder.append(",")
    }
    strBuilder.append("]")
}.toString()