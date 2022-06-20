package no.nav.personbruker.dittnav.api.beskjed

fun toBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
        beskjed.let {
            BeskjedDTO(
                    forstBehandlet = it.forstBehandlet,
                    eventId = it.eventId,
                    tekst = it.tekst,
                    link = it.link,
                    produsent = it.produsent,
                    sistOppdatert = it.sistOppdatert,
                    sikkerhetsnivaa = it.sikkerhetsnivaa,
                    aktiv = it.aktiv,
                    grupperingsId = it.grupperingsId
            )
        }

fun toMaskedBeskjedDTO(beskjed: Beskjed): BeskjedDTO =
        beskjed.let {
            val maskedBeskjedDTO = toBeskjedDTO(beskjed)
            return maskedBeskjedDTO.copy(tekst = "***", link = "***", produsent = "***")
        }
