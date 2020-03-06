package no.nav.personbruker.dittnav.api.innboks

fun toInnboksDTO(innboks: Innboks): InnboksDTO =
        innboks.let {
            InnboksDTO(
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }
