package no.nav.personbruker.dittnav.api.varsel

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class VarselTransformerTest {

    @Test
    fun `should transform from Varsel to BeskjedDTO`() {
        val varsel1 = createInactiveVarsel("1")
        val varsel2 = createInactiveVarsel("2")
        val varselDTOList = listOf(varsel1, varsel2).map { varsel ->
            toVarselDTO(varsel)
        }
        val beskjedDTO = varselDTOList.first()

        beskjedDTO.uid!! `should be equal to` varsel1.id.toString()
        beskjedDTO.eventTidspunkt.toString() `should be equal to` varsel1.datoOpprettet.toZonedDateTime().toString()
        beskjedDTO.eventId `should be equal to` varsel1.varselId
        beskjedDTO.tekst `should be equal to` varsel1.varseltekst
        beskjedDTO.link `should be equal to` varsel1.url
        beskjedDTO.produsent!! `should be equal to` "varselinnboks"
        beskjedDTO.sistOppdatert.toString() `should be equal to` varsel1.datoLest?.toZonedDateTime().toString()
        beskjedDTO.sikkerhetsnivaa `should be` 4
    }

    @Test
    fun `should mask tekst, link and produsent`() {
        val varsel = createInactiveVarsel("3")
        val beskjedDTO = toMaskedVarselDTO(varsel)
        beskjedDTO.uid!! `should be equal to` varsel.id.toString()
        beskjedDTO.eventTidspunkt.toString() `should be equal to` varsel.datoOpprettet.toZonedDateTime().toString()
        beskjedDTO.eventId `should be equal to` varsel.varselId
        beskjedDTO.tekst `should be equal to` "***"
        beskjedDTO.link `should be equal to` "***"
        beskjedDTO.produsent!! `should be equal to` "***"
        beskjedDTO.sistOppdatert.toString() `should be equal to` varsel.datoLest?.toZonedDateTime().toString()
        beskjedDTO.sikkerhetsnivaa `should be` 4
    }

}
