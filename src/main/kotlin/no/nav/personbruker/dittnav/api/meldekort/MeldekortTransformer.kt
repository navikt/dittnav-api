package no.nav.personbruker.dittnav.api.meldekort

import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortExternal
import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
import java.time.LocalDate

object MeldekortTransformer {

    fun toInternal(meldekortStatus: MeldekortstatusExternal): Meldekortinfo {

        val nesteMeldekort = meldekortStatus.nesteMeldekort?.let { toInternal(it) }

        val nyeMeldekort = NyeMeldekort(
            meldekortStatus.meldekort,
            nesteMeldekort,
            meldekortStatus.nesteInnsendingAvMeldekort
        )

        return Meldekortinfo(
            nyeMeldekort,
            meldekortStatus.antallGjenstaaendeFeriedager,
            meldekortStatus.etterregistrerteMeldekort,
            isMeldekortbruker(meldekortStatus)
        )
    }

    private fun toInternal(meldekortExternal: MeldekortExternal): Meldekort {

        val (sisteDatoForTrekk, risikerTrekk) = calculateSisteDatoForTrekk(meldekortExternal.til)

        return Meldekort(
            meldekortExternal.uke,
            meldekortExternal.kanSendesFra,
            meldekortExternal.fra,
            meldekortExternal.til,
            sisteDatoForTrekk,
            risikerTrekk
        )
    }

    private fun calculateSisteDatoForTrekk(tilDato: LocalDate): Pair<LocalDate, Boolean> {
        val sisteDatoForTrekk = tilDato.plusDays(8)
        val risikerTrekk = LocalDate.now().isAfter(sisteDatoForTrekk)

        return sisteDatoForTrekk to risikerTrekk
    }

    private fun isMeldekortbruker(externalStatus: MeldekortstatusExternal): Boolean {
        return when {
            externalStatus.nesteMeldekort != null -> true
            externalStatus.nesteInnsendingAvMeldekort != null -> true
            externalStatus.antallGjenstaaendeFeriedager > 0 -> true
            externalStatus.etterregistrerteMeldekort > 0 -> true
            externalStatus.meldekort > 0 -> true
            else -> false
        }
    }
}
