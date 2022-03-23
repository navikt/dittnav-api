package no.nav.personbruker.dittnav.api.mininnboks

import no.nav.personbruker.dittnav.api.mininnboks.UbehandledeMeldingerType.*
import no.nav.personbruker.dittnav.api.mininnboks.external.UbehandletMeldingExternal

object UbehandletMeldingExternalObjectMother {
    fun createUbehandletOppgave(): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = OPPGAVE_VARSEL.name)
    }

    fun createUbehandletDokument(): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = DOKUMENT_VARSEL.name)
    }

    fun createUbehandletUlestMelding(): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = "UNKNOWN", statuser = listOf("ULEST"))
    }

    fun createUbehandletUbesvartMelding(): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = "UNKNOWN", statuser = listOf("UBESVART"))
    }

    fun createUbehandletOppgaveWithVarselId(varselId: String): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = OPPGAVE_VARSEL.name, varselid = varselId)
    }

    fun createUbehandletOppgaveWithUndertype(undertype: String): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = OPPGAVE_VARSEL.name, undertype = undertype)
    }

    fun createUbehandletOppgaveWithVarselIdAndUndertype(varselId: String, undertype: String): UbehandletMeldingExternal {
        return UbehandletMeldingExternal(type = OPPGAVE_VARSEL.name, varselid = varselId, undertype = undertype)
    }
}
