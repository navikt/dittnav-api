package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
import java.time.LocalDate


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

