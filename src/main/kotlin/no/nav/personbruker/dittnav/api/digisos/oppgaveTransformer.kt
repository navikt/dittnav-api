package no.nav.personbruker.dittnav.api.digisos

import no.nav.personbruker.dittnav.api.oppgave.OppgaveDTO

fun List<Ettersendelse>.toInternals(): List<OppgaveDTO> {
    return map { external ->
        external.toInternal()
    }
}

fun Ettersendelse.toInternal() = OppgaveDTO(
    eventTidspunkt = eventTidspunkt.toZonedDateTime(),
    eventId = eventId,
    tekst = cropTextIfOverMaxLengthOfOppgave(tekst),
    link = link,
    produsent = "digiSos",
    sistOppdatert = sistOppdatert.toZonedDateTime(),
    sikkerhetsnivaa = 3,
    aktiv = aktiv,
    grupperingsId = grupperingsId
)

const val maxOppgaveTextLength = 90

fun cropTextIfOverMaxLengthOfOppgave(text: String): String {
    return if (text.length <= maxOppgaveTextLength) {
        text

    } else {
        text.substring(0, maxOppgaveTextLength - 3) + "..."
    }
}
