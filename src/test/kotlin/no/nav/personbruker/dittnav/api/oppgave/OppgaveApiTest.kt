package no.nav.personbruker.dittnav.api.oppgave;

import io.ktor.client.request.get
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test


class OppgaveApiTest {

    @Test
    fun testGetOppgave() = testApplication {
        client.get("/oppgave").apply {
           // TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOppgaveInaktiv() = testApplication {
        client.get("/oppgave/inaktiv").apply {
          //  TODO("Please write your test here")
        }
    }
}