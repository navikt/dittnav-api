package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class InnboksTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val innboks = createInnboks("1", "1", true)
        val innboksAsString = innboks.toString()
        innboksAsString shouldContain "fodselsnummer=***"
        innboksAsString shouldContain "tekst=***"
        innboksAsString shouldContain "link=***"
    }
}
