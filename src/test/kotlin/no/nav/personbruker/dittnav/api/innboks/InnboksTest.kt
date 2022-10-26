package no.nav.personbruker.dittnav.api.innboks

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.api.TestUser
import no.nav.personbruker.dittnav.api.assert
import org.junit.jupiter.api.Test

class InnboksTest {

    @Test
    fun `should not mask events with security level equal to the current user`() {
        val innboks1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivå = 4)

        innboks1.toInnboksDTO(operatingLoginLevel = 4).assert {
            forstBehandlet shouldBe innboks1.forstBehandlet
            eventId shouldBe innboks1.eventId
            tekst shouldBe innboks1.tekst
            link shouldBe innboks1.link
            produsent shouldBe innboks1.produsent
            sistOppdatert shouldBe innboks1.sistOppdatert
            sikkerhetsnivaa shouldBe innboks1.sikkerhetsnivaa
            eksternVarslingSendt shouldBe innboks1.eksternVarslingSendt
            eksternVarslingKanaler shouldBe innboks1.eksternVarslingKanaler
        }
    }

    @Test
    fun `should not mask events with security level lower than current user`() {
        val innboks1 = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivå = 3)

        innboks1.toInnboksDTO(operatingLoginLevel = 4).assert {
            forstBehandlet shouldBe innboks1.forstBehandlet
            eventId shouldBe innboks1.eventId
            tekst shouldBe innboks1.tekst
            link shouldBe innboks1.link
            produsent shouldBe innboks1.produsent
            sistOppdatert shouldBe innboks1.sistOppdatert
            sikkerhetsnivaa shouldBe innboks1.sikkerhetsnivaa
            eksternVarslingSendt shouldBe innboks1.eksternVarslingSendt
            eksternVarslingKanaler shouldBe innboks1.eksternVarslingKanaler
        }
    }

    @Test
    fun `should mask events with security level higher than current user`() {
        val innboks = createInnboks(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivå = 4)
        innboks.toInnboksDTO(operatingLoginLevel = 3).assert {
            forstBehandlet shouldBe innboks.forstBehandlet
            eventId shouldBe innboks.eventId
            tekst shouldBe "***"
            link shouldBe "***"
            produsent shouldBe "***"
            sistOppdatert shouldBe innboks.sistOppdatert
            sikkerhetsnivaa shouldBe innboks.sikkerhetsnivaa
            eksternVarslingSendt shouldBe innboks.eksternVarslingSendt
            eksternVarslingKanaler shouldBe innboks.eksternVarslingKanaler
        }

    }

}
