package no.nav.personbruker.dittnav.api.oppgave

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OppgaveTransformerTest {

    @Test
    fun `should transform from Oppgave to OppgaveDTO`() {
        val oppgave1 = createOppgave("1", "1", true)
        val oppgave2 = createOppgave("2", "2", true)
        val oppgaveDTOList = listOf(oppgave1, oppgave2).map { toOppgaveDTO(it) }
        val oppgaveDTO = oppgaveDTOList.first()

        oppgaveDTO.eventTidspunkt shouldBe oppgave1.eventTidspunkt
        oppgaveDTO.forstBehandlet shouldBe oppgave1.forstBehandlet
        oppgaveDTO.eventId shouldBe oppgave1.eventId
        oppgaveDTO.tekst shouldBe oppgave1.tekst
        oppgaveDTO.link shouldBe oppgave1.link
        oppgaveDTO.produsent!! shouldBe oppgave1.produsent!!
        oppgaveDTO.sistOppdatert shouldBe oppgave1.sistOppdatert
        oppgaveDTO.sikkerhetsnivaa shouldBe oppgave1.sikkerhetsnivaa
        oppgaveDTO.aktiv shouldBe oppgave1.aktiv
        oppgaveDTO.grupperingsId shouldBe oppgave1.grupperingsId
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val oppgave = createOppgave("1", "1", true)
        val oppgaveDTO = toMaskedOppgaveDTO(oppgave)
        oppgaveDTO.eventTidspunkt shouldBe oppgave.eventTidspunkt
        oppgaveDTO.forstBehandlet shouldBe oppgave.forstBehandlet
        oppgaveDTO.eventId shouldBe oppgave.eventId
        oppgaveDTO.tekst shouldBe "***"
        oppgaveDTO.link shouldBe "***"
        oppgaveDTO.produsent!! shouldBe "***"
        oppgaveDTO.sistOppdatert shouldBe oppgave.sistOppdatert
        oppgaveDTO.sikkerhetsnivaa shouldBe oppgave.sikkerhetsnivaa
    }
}
