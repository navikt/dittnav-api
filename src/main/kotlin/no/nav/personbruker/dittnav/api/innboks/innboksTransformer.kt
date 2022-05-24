package no.nav.personbruker.dittnav.api.innboks

fun toInnboksDTO(innboks: Innboks): InnboksDTO =
        innboks.let {
            InnboksDTO(
                    eventTidspunkt = it.eventTidspunkt,
                    forstBehandlet = it.forstBehandlet,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    produsent = it.produsent,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }

fun toMaskedInnboksDTO(innboks: Innboks): InnboksDTO =
        innboks.let {
            var maskedInnboksDTO = toInnboksDTO(innboks)
            return maskedInnboksDTO.copy(tekst = "***", link = "***", produsent = "***")
        }
