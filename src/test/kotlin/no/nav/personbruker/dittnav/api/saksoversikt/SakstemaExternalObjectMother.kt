package no.nav.personbruker.dittnav.api.saksoversikt

import no.nav.personbruker.dittnav.api.saksoversikt.external.SakstemaExternal
import java.time.LocalDate

object SakstemaExternalObjectMother {

    private val defaultKode = "kode"
    private val defaultNavn = "navn"
    private val defaultAntall = 1
    private val defaultSistOppdatert = LocalDate.now()

    fun createSakstema(): SakstemaExternal {
        return SakstemaExternal(defaultKode, defaultNavn, defaultAntall, defaultSistOppdatert)
    }

    fun createSakstema(kode: String, navn: String, antall: Int, sistOppdatert: LocalDate): SakstemaExternal {
        return SakstemaExternal(kode, navn, antall, sistOppdatert)
    }

    fun createSakstemaWithSistoppdatert(vararg sistOppdatert: LocalDate): List<SakstemaExternal> {
        return sistOppdatert.map {
            SakstemaExternal(defaultKode, defaultNavn, defaultAntall, it)
        }
    }
}
