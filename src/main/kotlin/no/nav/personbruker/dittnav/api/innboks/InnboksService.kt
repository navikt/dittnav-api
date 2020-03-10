package no.nav.personbruker.dittnav.api.innboks

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class InnboksService (private val innboksConsumer: InnboksConsumer) {

    private val log = LoggerFactory.getLogger(InnboksService::class.java)

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

    suspend fun getInnboksEventsAsBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return try {
            innboksConsumer.getExternalActiveEvents(innloggetBruker)
                    .filter { innboks -> innloggetBruker.innloggingsnivaa >= innboks.sikkerhetsnivaa }
                    .map { innboks-> toBrukernotifikasjon(innboks) }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
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
            log.error(exception)
            emptyList()
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
