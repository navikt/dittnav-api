package no.nav.personbruker.dittnav.api.meldinger

import io.ktor.http.auth.HttpAuthHeader
import no.nav.personbruker.dittnav.api.domain.Melding
import no.nav.personbruker.dittnav.api.config.Environment

class MeldingService {
    private val eventConsumer = EventConsumer()
    private val eventTransformer = MeldingTransformer()

    suspend fun getMeldinger(environment: Environment, authHeader: HttpAuthHeader): List<Melding> {
        val inbound = eventConsumer.getEvents(environment, authHeader)
        return eventTransformer.toOutbound(inbound)
    }
}
