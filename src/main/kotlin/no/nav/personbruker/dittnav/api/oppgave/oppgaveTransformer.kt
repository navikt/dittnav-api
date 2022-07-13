package no.nav.personbruker.dittnav.api.oppgave

fun toOppgaveDTO(inbound: Oppgave): OppgaveDTO =
    inbound.let {
        OppgaveDTO(
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

fun toMaskedOppgaveDTO(oppgave: Oppgave): OppgaveDTO =
    oppgave.let {
        val maskedOppgaveDTO = toOppgaveDTO(oppgave)
        return maskedOppgaveDTO.copy(tekst = "***", link = "***", produsent = "***")
    }
