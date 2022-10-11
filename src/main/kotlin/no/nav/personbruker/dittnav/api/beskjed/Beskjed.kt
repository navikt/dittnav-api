@file:UseSerializers(ZonedDateTimeSerializer::class)

package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class Beskjed(
    val forstBehandlet: ZonedDateTime,
    val fodselsnummer: String,
    val eventId: String,
    val grupperingsId: String,
    val tekst: String,
    val link: String,
    val produsent: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: ZonedDateTime,
    val aktiv: Boolean,
    val eksternVarslingSendt: Boolean,
    val eksternVarslingKanaler: List<String>
)

@Serializable
data class BeskjedDTO(
    val forstBehandlet: ZonedDateTime,
    val eventId: String,
    val tekst: String,
    val link: String,
    val produsent: String,
    val sistOppdatert: ZonedDateTime,
    val sikkerhetsnivaa: Int,
    val aktiv: Boolean,
    val grupperingsId: String,
    val eksternVarslingSendt: Boolean,
    val eksternVarslingKanaler: List<String>
)

enum class KildeType {
    EVENTHANDLER,
    DIGISOS
}

fun toBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
    beskjed.let {
        BeskjedDTO(
            forstBehandlet = it.forstBehandlet,
            eventId = it.eventId,
            tekst = it.tekst,
            link = it.link,
            produsent = it.produsent,
            sistOppdatert = it.sistOppdatert,
            sikkerhetsnivaa = it.sikkerhetsnivaa,
            aktiv = it.aktiv,
            grupperingsId = it.grupperingsId,
            eksternVarslingSendt = it.eksternVarslingSendt,
            eksternVarslingKanaler = it.eksternVarslingKanaler
        )
    }

fun toMaskedBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
    beskjed.let {
        val maskedBeskjedDTO = toBeskjedDTO(beskjed)
        return maskedBeskjedDTO.copy(tekst = "***", link = "***", produsent = "***")
    }

