package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBruker

class BeskjedService(private val beskjedConsumer: BeskjedConsumer) {

    suspend fun getActiveBeskjedEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return getBeskjedEvents(innloggetBruker) {
            beskjedConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveBeskjedEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return getBeskjedEvents(innloggetBruker) {
            beskjedConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getBeskjedEvents(
            innloggetBruker: InnloggetBruker,
            getEvents: suspend (InnloggetBruker) -> List<Beskjed>
    ): List<BeskjedDTO> {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            externalEvents.map { beskjed -> transformToDTO(beskjed, innloggetBruker) }
        } catch(exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Beskjed", exception)
        }
    }

    private fun transformToDTO(beskjed: Beskjed, innloggetBruker: InnloggetBruker): BeskjedDTO {
        return if(innloggetBrukerIsAllowedToViewAllDataInEvent(beskjed, innloggetBruker)) {
            toBeskjedDTO(beskjed)
        } else {
            toMaskedBeskjedDTO(beskjed)
        }
    }

    private fun innloggetBrukerIsAllowedToViewAllDataInEvent(beskjed: Beskjed, innloggetBruker: InnloggetBruker): Boolean {
        return innloggetBruker.innloggingsnivaa >= beskjed.sikkerhetsnivaa
    }
}
