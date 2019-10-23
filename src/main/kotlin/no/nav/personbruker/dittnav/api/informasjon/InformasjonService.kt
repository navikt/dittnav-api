package no.nav.personbruker.dittnav.api.informasjon

import io.ktor.http.auth.HttpAuthHeader
import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import org.slf4j.LoggerFactory

class InformasjonService(private val informasjonConsumer: InformasjonConsumer) {

    private val log = LoggerFactory.getLogger(InformasjonService::class.java)

    suspend fun getInformasjonEventsAsBrukernotifikasjoner(authHeader: HttpAuthHeader?): List<Brukernotifikasjon> {
        try {
            informasjonConsumer.getEvents(authHeader).let {
                return InformasjonTransformer.toBrukernotifikasjonList(it)
            }
        } catch (exception: Exception) {
            log.error(exception)
        }
        return emptyList()
    }
}
