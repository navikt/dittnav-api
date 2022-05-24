package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class OppgaveTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val oppgave = createOppgave("1", "1", true)
        val oppgaveAsString = oppgave.toString()
        oppgaveAsString shouldContain "fodselsnummer=***"
        oppgaveAsString shouldContain "tekst=***"
        oppgaveAsString shouldContain "link=***"
    }
}
