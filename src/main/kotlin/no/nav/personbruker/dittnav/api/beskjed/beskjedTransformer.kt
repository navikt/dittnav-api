package no.nav.personbruker.dittnav.api.beskjed

fun toBeskjedDTO(inbound: Beskjed): BeskjedDTO =
        inbound.let{
            BeskjedDTO(
                    eventId = it.eventId,
                    produsent = it.produsent,
                    eventTidspunkt = it.eventTidspunkt,
                    link = it.link,
                    sistOppdatert = it.sistOppdatert,
                    tekst = it.tekst
            )
    }
