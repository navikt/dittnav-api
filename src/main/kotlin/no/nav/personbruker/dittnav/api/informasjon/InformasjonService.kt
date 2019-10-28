package no.nav.personbruker.dittnav.api.informasjon

import io.ktor.util.error
import no.nav.personbruker.dittnav.api.brukernotifikasjon.Brukernotifikasjon
import org.slf4j.LoggerFactory

class InformasjonService(private val informasjonConsumer: InformasjonConsumer) {

    private val log = LoggerFactory.getLogger(InformasjonService::class.java)

    suspend fun getInformasjonEventsAsBrukernotifikasjoner(token: String): List<Brukernotifikasjon> {
        try {
            informasjonConsumer.getEvents(token).let {
                return InformasjonTransformer.toBrukernotifikasjonList(it)
            }
        } catch (exception: Exception) {
            log.error(exception)
        }
        return emptyList()
    }
}
