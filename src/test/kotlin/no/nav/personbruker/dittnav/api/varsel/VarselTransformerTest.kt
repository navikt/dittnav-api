package no.nav.personbruker.dittnav.api.varsel

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test

class VarselTransformerTest {

    @Test
    fun `Skal transformere fra Varsel til BeskjedDTO`() {
        val original = createLestVarsel("1")
        val beskjedDTO = toVarselDTO(original)

        beskjedDTO.uid!! `should be equal to` "${original.meldingsType}-${original.id}"
        beskjedDTO.eventTidspunkt.toString() `should be equal to` original.datoOpprettet.toString()
        beskjedDTO.eventId `should be equal to` original.varselId
        beskjedDTO.tekst `should be equal to` original.varseltekst
        beskjedDTO.link `should be equal to` original.url
        beskjedDTO.produsent!! `should be equal to` "varselinnboks"
        beskjedDTO.sistOppdatert.toString() `should be equal to` original.datoLest!!.toString()
        beskjedDTO.sikkerhetsnivaa `should be` 3
    }

}
