package no.nav.personbruker.dittnav.api.melding

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.event.EventConsumer
import org.slf4j.LoggerFactory

class MeldingService(private val eventConsumer: EventConsumer) {
    private val log = LoggerFactory.getLogger(MeldingService::class.java)

    suspend fun getMeldinger(token: String): List<Melding> {
        try {
            eventConsumer.getEvents(token).let {
                return MeldingTransformer.toOutbound(it)
            }

        } catch (exception: Exception) {
            log.error(exception)
        }
        return emptyList() // Simpel løsning med retur av tom liste foreløpig, hva skal frontend motta ved feil?
    }
}
