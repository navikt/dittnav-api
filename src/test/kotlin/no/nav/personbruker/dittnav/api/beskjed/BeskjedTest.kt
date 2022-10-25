package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import no.nav.personbruker.dittnav.api.createBeskjed
import org.junit.jupiter.api.Test

class BeskjedTest {

    @Test
    fun `should mask beskjed with security level higher than current user`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivaa = 4)
        beskjed.toBeskjedDto(operatingLoginLevel = 3).apply {
            forstBehandlet shouldBe beskjed.forstBehandlet
            eventId shouldBe beskjed.eventId
            tekst shouldBe "***"
            link shouldBe "***"
            produsent shouldBe "***"
            sistOppdatert shouldBe beskjed.sistOppdatert
            sikkerhetsnivaa shouldBe beskjed.sikkerhetsnivaa
            grupperingsId shouldBe beskjed.grupperingsId
            eksternVarslingSendt shouldBe beskjed.eksternVarslingSendt
            eksternVarslingKanaler shouldBe beskjed.eksternVarslingKanaler
        }
    }

    @Test
    fun `should not mask beskjed with security level lower than current user`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivaa = 3)
        beskjed.toBeskjedDto(4).apply {
            forstBehandlet shouldBe beskjed.forstBehandlet
            eventId shouldBe beskjed.eventId
            tekst shouldBe beskjed.tekst
            link shouldBe beskjed.link
            produsent shouldBe beskjed.produsent
            sistOppdatert shouldBe beskjed.sistOppdatert
            sikkerhetsnivaa shouldBe beskjed.sikkerhetsnivaa
            grupperingsId shouldBe beskjed.grupperingsId
            eksternVarslingSendt shouldBe beskjed.eksternVarslingSendt
            eksternVarslingKanaler shouldBe beskjed.eksternVarslingKanaler
        }
    }

    @Test
    fun `should not mask beskjed with security level equal to the current user`() {
        val beskjed1 = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivaa = 4)
        beskjed1.toBeskjedDto(4).apply {
            forstBehandlet shouldBe beskjed1.forstBehandlet
            eventId shouldBe beskjed1.eventId
            tekst shouldBe beskjed1.tekst
            link shouldBe beskjed1.link
            produsent shouldBe beskjed1.produsent
            sistOppdatert shouldBe beskjed1.sistOppdatert
            sikkerhetsnivaa shouldBe beskjed1.sikkerhetsnivaa
            grupperingsId shouldBe beskjed1.grupperingsId
            eksternVarslingSendt shouldBe beskjed1.eksternVarslingSendt
            eksternVarslingKanaler shouldBe beskjed1.eksternVarslingKanaler
        }
    }
}
