package no.nav.personbruker.dittnav.api.brukernotifikasjon

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBruker

class BrukernotifikasjonService(private val brukernotifikasjonConsumer: BrukernotifikasjonConsumer) {

    suspend fun totalNumberOfEvents(innloggetBruker: InnloggetBruker): Int {
        return try {
            brukernotifikasjonConsumer.count(innloggetBruker)

        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke å finne ut om brukeren har brukernotifikasjoner", exception)
        }
    }

    suspend fun numberOfActive(innloggetBruker: InnloggetBruker): Int {
        return try {
            brukernotifikasjonConsumer.countActive(innloggetBruker)

        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke å finne ut om brukeren har aktive brukernotifikasjoner", exception)
        }
    }

}
