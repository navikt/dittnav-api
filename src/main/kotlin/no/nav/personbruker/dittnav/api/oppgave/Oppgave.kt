@file:UseSerializers(ZonedDateTimeSerializer::class)

package no.nav.personbruker.dittnav.api.oppgave

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class Oppgave(
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
    fun toMaskedOppgaveDTO(): OppgaveDTO =
        this.toOppgaveDTO()
            .copy(tekst = "***", link = "***", produsent = "***")

    fun toOppgaveDTO(): OppgaveDTO =
            OppgaveDTO(
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

}

@Serializable
data class OppgaveDTO(
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

