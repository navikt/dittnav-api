package no.nav.personbruker.dittnav.api.innboks

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import org.slf4j.LoggerFactory

class InnboksService (private val innboksConsumer: InnboksConsumer) {
    private val log = LoggerFactory.getLogger(InnboksService::class.java)

    suspend fun getInnboksEventsAsBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        return try {
            innboksConsumer.getExternalEvents(token).map { innboks ->
                toBrukernotifikasjon(innboks)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }

}