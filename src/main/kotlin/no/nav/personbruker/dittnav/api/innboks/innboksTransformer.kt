package no.nav.personbruker.dittnav.api.innboks

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
