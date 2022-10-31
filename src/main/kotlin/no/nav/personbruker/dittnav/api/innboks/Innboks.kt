@file:UseSerializers(ZonedDateTimeSerializer::class)

package no.nav.personbruker.dittnav.api.innboks

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class Innboks(
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

    private fun toInnboksDTO(): InnboksDTO =
        InnboksDTO(
            forstBehandlet = forstBehandlet,
            eventId = eventId,
            tekst = tekst,
            link = link,
            produsent = produsent,
            sistOppdatert = sistOppdatert,
            sikkerhetsnivaa = sikkerhetsnivaa,
            eksternVarslingSendt = eksternVarslingSendt,
            eksternVarslingKanaler = eksternVarslingKanaler
        )

    private fun toMaskedInnboksDTO(): InnboksDTO =
        toInnboksDTO().copy(tekst = "***", link = "***", produsent = "***")


    fun toInnboksDTO(loginLevel: Int): InnboksDTO {
        return if(loginLevel >= sikkerhetsnivaa) {
            toInnboksDTO()
        } else {
            toMaskedInnboksDTO()
        }
    }

}

@Serializable
data class InnboksDTO(
    val forstBehandlet: ZonedDateTime,
    val eventId: String,
    val tekst: String,
    val link: String,
    val produsent: String,
    val sistOppdatert: ZonedDateTime,
    val sikkerhetsnivaa: Int,
    val eksternVarslingSendt: Boolean,
    val eksternVarslingKanaler: List<String>
)
