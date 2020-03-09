package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class BeskjedService(private val beskjedConsumer: BeskjedConsumer) {

    private val log = LoggerFactory.getLogger(BeskjedService::class.java)

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

    suspend fun getBeskjedEventsAsBrukernotifikasjoner(innloggetBruker: InnloggetBruker): List<Brukernotifikasjon> {
        return try {
            beskjedConsumer.getExternalActiveEvents(innloggetBruker)
                    .filter { beskjed ->  innloggetBruker.getSecurityLevel().level >= beskjed.sikkerhetsnivaa }
                    .map { beskjed -> toBrukernotifikasjon(beskjed) }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
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
            log.error(exception)
            emptyList()
        }
    }

    private fun transformToDTO(beskjed: Beskjed, innloggetBruker: InnloggetBruker): BeskjedDTO {
        return if(innloggetBruker.getSecurityLevel().level >= beskjed.sikkerhetsnivaa) {
            toBeskjedDTO(beskjed)
        } else {
            toMaskedBeskjedDTO(beskjed)
        }
    }
}
