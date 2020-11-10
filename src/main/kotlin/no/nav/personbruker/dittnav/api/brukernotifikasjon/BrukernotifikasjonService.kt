package no.nav.personbruker.dittnav.api.brukernotifikasjon

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class BrukernotifikasjonService(private val brukernotifikasjonConsumer: BrukernotifikasjonConsumer) {

    suspend fun totalNumberOfEvents(user: AuthenticatedUser): Int {
        return try {
            brukernotifikasjonConsumer.count(user)

        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke å finne ut om brukeren har brukernotifikasjoner", exception)
        }
    }

    suspend fun numberOfInactive(user: AuthenticatedUser): Int {
        return try {
            brukernotifikasjonConsumer.countInactive(user)

        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke å finne ut om brukeren har inaktive brukernotifikasjoner", exception)
        }
    }

    suspend fun numberOfActive(user: AuthenticatedUser): Int {
        return try {
            brukernotifikasjonConsumer.countActive(user)

        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke å finne ut om brukeren har aktive brukernotifikasjoner", exception)
        }
    }

}
