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
) {

    private fun toBeskjedDTO(): BeskjedDTO =
        BeskjedDTO(
            forstBehandlet = forstBehandlet,
            eventId = eventId,
            tekst = tekst,
            link = link,
            produsent = produsent,
            sistOppdatert = sistOppdatert,
            sikkerhetsnivaa = sikkerhetsnivaa,
            aktiv = aktiv,
            grupperingsId = grupperingsId,
            eksternVarslingSendt = eksternVarslingSendt,
            eksternVarslingKanaler = eksternVarslingKanaler
        )

    private fun toMaskedBeskjedDTO(): BeskjedDTO =
        toBeskjedDTO().copy(tekst = "***", link = "***", produsent = "***")


    internal fun toBeskjedDto(operatingLoginLevel: Int) =
        if (operatingLoginLevel >= sikkerhetsnivaa) {
            toBeskjedDTO()
        } else {
            toMaskedBeskjedDTO()
        }
}

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

