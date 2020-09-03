package no.nav.personbruker.dittnav.api.varsel

import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class VarselTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val varsel = createInactiveVarsel("1")
        val varselAsString = varsel.toString()
        varselAsString `should contain` "aktoerID=***"
        varselAsString `should contain` "tekst=***"
        varselAsString `should contain` "url=***"
    }

}
