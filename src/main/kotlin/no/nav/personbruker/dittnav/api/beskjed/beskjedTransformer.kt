package no.nav.personbruker.dittnav.api.beskjed

fun toBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
        beskjed.let {
            BeskjedDTO(
                    uid = it.uid,
                    eventTidspunkt = it.eventTidspunkt,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    produsent = it.produsent,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa
            )
        }

fun toMaskedBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
        beskjed.let {
            var maskedBeskjedDTO = toBeskjedDTO(beskjed)
            return maskedBeskjedDTO.copy(tekst = "***", link = "***", produsent = "***")
        }
