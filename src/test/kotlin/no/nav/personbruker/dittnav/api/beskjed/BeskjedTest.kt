package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class BeskjedTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true)
        val beskjedAsString = beskjed.toString()
        beskjedAsString shouldContain "fodselsnummer=***"
        beskjedAsString shouldContain "tekst=***"
        beskjedAsString shouldContain "link=***"

    }
}
