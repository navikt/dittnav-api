package no.nav.personbruker.dittnav.api.beskjed

import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class BeskjedTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        val beskjedAsString = beskjed.toString()
        beskjedAsString `should contain` "fodselsnummer=***"
        beskjedAsString `should contain` "tekst=***"
        beskjedAsString `should contain` "link=***"

    }
}
