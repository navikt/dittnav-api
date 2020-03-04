package no.nav.personbruker.dittnav.api.innboks

fun toInnboksDTO(innboks: Innboks): InnboksDTO =
        innboks.let {
            InnboksDTO(
                    eventId = it.eventId,
                    produsent = it.produsent,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
            )
        }
