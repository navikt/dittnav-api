package no.nav.personbruker.dittnav.api.innboks

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBruker

class InnboksService (private val innboksConsumer: InnboksConsumer) {

    suspend fun getActiveInnboksEvents(innloggetBruker: InnloggetBruker): List<InnboksDTO> {
        return getInnboksEvents(innloggetBruker) {
            innboksConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveInnboksEvents(innloggetBruker: InnloggetBruker): List<InnboksDTO> {
        return getInnboksEvents(innloggetBruker) {
            innboksConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getInnboksEvents(
            innloggetBruker: InnloggetBruker,
            getEvents: suspend (InnloggetBruker) -> List<Innboks>
    ): List<InnboksDTO> {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            externalEvents.map { innboks -> transformToDTO(innboks, innloggetBruker) }
        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Innboks", exception)
        }
    }

    private fun transformToDTO(innboks: Innboks, innloggetBruker: InnloggetBruker): InnboksDTO {
        return if(innloggetBrukerIsAllowedToViewAllDataInEvent(innboks, innloggetBruker)) {
            toInnboksDTO(innboks)
        } else {
            toMaskedInnboksDTO(innboks)
        }
    }

    private fun innloggetBrukerIsAllowedToViewAllDataInEvent(innboks: Innboks, innloggetBruker: InnloggetBruker): Boolean {
        return innloggetBruker.innloggingsnivaa >= innboks.sikkerhetsnivaa
    }
}
