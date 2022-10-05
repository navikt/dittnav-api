package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortExternal
import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale


internal fun createMeldekort(fra: LocalDate = LocalDate.now(), til: LocalDate = fra.plusMonths(1)): MeldekortExternal {
    val uke = WeekFields.of(Locale.getDefault()).weekOfYear().toString()
    return MeldekortExternal(uke, fra, fra, til)
}

internal fun createMeldekortStatus(
    meldekort: Int = 1,
    etterregistrerte: Int = 3,
    feriedager: Int = 5
): MeldekortstatusExternal {
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

internal fun createMeldekortinfo(
    nyeMeldekort: NyeMeldekort = createNyeMeldekortObject(),
    resterendeFeriedager: Int = 10,
    etterregistrerteMeldekort: Int = 0,
    meldekortbruker: Boolean = true
) = Meldekortinfo(nyeMeldekort, resterendeFeriedager, etterregistrerteMeldekort, meldekortbruker)

internal fun createNyeMeldekortObject(
    antall: Int = 1,
    nesteMeldekort: Meldekort? = null,
    nesteInnsendingAvMeldekort: LocalDate? = null
) = NyeMeldekort(antall, nesteMeldekort, nesteInnsendingAvMeldekort)

internal fun LocalDate.weekNumber(): String = "${Calendar.getInstance().weekYear}"

internal fun createInternalMedldekortObject(
    uke: String = LocalDate.now().plusWeeks(1).weekNumber(),
    kanSendesFra: LocalDate = LocalDate.now().plusDays(5),
    fra: LocalDate = LocalDate.now().minusDays(7),
    til: LocalDate = LocalDate.now().plusDays(7),
    sisteDatoForTrekk: LocalDate = til.plusDays(8),
    risikererTrekk: Boolean = false
) = Meldekort(
    uke = uke,
    kanSendesFra = kanSendesFra,
    fra = fra,
    til =til,
    sisteDatoForTrekk = sisteDatoForTrekk,
    risikerTrekk = risikererTrekk
)

