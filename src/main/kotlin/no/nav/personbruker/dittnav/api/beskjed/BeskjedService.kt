package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import org.slf4j.LoggerFactory

class BeskjedService(private val beskjedConsumer: BeskjedConsumer) {

    private val log = LoggerFactory.getLogger(BeskjedService::class.java)

    suspend fun getBeskjedEventsAsBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        return try {
            beskjedConsumer.getExternalEvents(token).map { beskjed ->
                toBrukernotifikasjon(beskjed)
            }
        } catch (exception: Exception) {
            log.error(exception)
            emptyList()
        }
    }
}
