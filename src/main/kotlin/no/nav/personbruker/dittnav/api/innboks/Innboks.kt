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
)

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

fun toInnboksDTO(innboks: Innboks): InnboksDTO =
    innboks.let {
        InnboksDTO(
            forstBehandlet = it.forstBehandlet,
            eventId = it.eventId,
            tekst = it.tekst,
            link = it.link,
            produsent = it.produsent,
            sistOppdatert = it.sistOppdatert,
            sikkerhetsnivaa = it.sikkerhetsnivaa,
            eksternVarslingSendt = it.eksternVarslingSendt,
            eksternVarslingKanaler = it.eksternVarslingKanaler
        )
    }

fun toMaskedInnboksDTO(innboks: Innboks): InnboksDTO =
    innboks.let {
        var maskedInnboksDTO = toInnboksDTO(innboks)
        return maskedInnboksDTO.copy(tekst = "***", link = "***", produsent = "***")
    }
