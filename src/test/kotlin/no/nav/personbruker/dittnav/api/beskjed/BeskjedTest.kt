package no.nav.personbruker.dittnav.api.beskjed

import io.kotest.matchers.shouldBe
import no.nav.personbruker.dittnav.api.createBeskjed
import org.junit.jupiter.api.Test

class BeskjedTest {

    @Test
    fun `should transform from Beskjed to BeskjedDTO`() {
        val beskjed1 = createBeskjed(eventId = "1", fodselsnummer = "1",  aktiv = true, sikkerhetsnivaa = 4)
        val beskjed2 = createBeskjed(eventId = "2", fodselsnummer = "2", aktiv = true, sikkerhetsnivaa = 4)
        val beskjedDTOList = listOf(beskjed1, beskjed2).map { it.toBeskjedDto(operatingLoginLevel = 4) }
        val beskjedDTO = beskjedDTOList.first()

        beskjedDTO.forstBehandlet shouldBe beskjed1.forstBehandlet
        beskjedDTO.eventId shouldBe beskjed1.eventId
        beskjedDTO.tekst shouldBe beskjed1.tekst
        beskjedDTO.link shouldBe beskjed1.link
        beskjedDTO.produsent shouldBe beskjed1.produsent
        beskjedDTO.sistOppdatert shouldBe beskjed1.sistOppdatert
        beskjedDTO.sikkerhetsnivaa shouldBe beskjed1.sikkerhetsnivaa
        beskjedDTO.grupperingsId shouldBe beskjed1.grupperingsId
        beskjedDTO.eksternVarslingSendt shouldBe beskjed1.eksternVarslingSendt
        beskjedDTO.eksternVarslingKanaler shouldBe beskjed1.eksternVarslingKanaler
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val beskjed = createBeskjed(eventId = "1", fodselsnummer = "1", aktiv = true, sikkerhetsnivaa = 4)
        val beskjedDTO = beskjed.toBeskjedDto(operatingLoginLevel = 3)
        beskjedDTO.forstBehandlet shouldBe beskjed.forstBehandlet
        beskjedDTO.eventId shouldBe beskjed.eventId
        beskjedDTO.tekst shouldBe "***"
        beskjedDTO.link shouldBe "***"
        beskjedDTO.produsent shouldBe "***"
        beskjedDTO.sistOppdatert shouldBe beskjed.sistOppdatert
        beskjedDTO.sikkerhetsnivaa shouldBe beskjed.sikkerhetsnivaa
        beskjedDTO.grupperingsId shouldBe beskjed.grupperingsId
        beskjedDTO.eksternVarslingSendt shouldBe beskjed.eksternVarslingSendt
        beskjedDTO.eksternVarslingKanaler shouldBe beskjed.eksternVarslingKanaler
    }
}