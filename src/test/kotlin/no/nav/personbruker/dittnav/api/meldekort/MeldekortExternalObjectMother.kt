package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortExternal
import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

object MeldekortExternalObjectMother {
    fun createEmptyMeldekortStatus(): MeldekortstatusExternal {
        return MeldekortstatusExternal()
    }

    fun createMeldekortStatus(meldekort: Int = 1, etterregistrerte: Int = 3, feriedager: Int = 5): MeldekortstatusExternal {
        val meldekortExternal = createMeldekort()

        val nesteInnsending = LocalDate.now()

        return MeldekortstatusExternal(
            meldekort,
            etterregistrerte,
            feriedager,
            meldekortExternal,
            nesteInnsending
        )
    }

    fun createMeldekort(fra: LocalDate = LocalDate.now(), til: LocalDate = fra.plusMonths(1)): MeldekortExternal {
        val uke = WeekFields.of(Locale.getDefault()).weekOfYear().toString()

        return MeldekortExternal(uke, fra, fra, til)
    }
}
