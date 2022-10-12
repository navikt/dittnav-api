package no.nav.personbruker.dittnav.api.meldekort

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class MeldekortExternal(
    val uke: String,
    @Serializable(with = MeldekortLocalDateSerializer::class) val kanSendesFra: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val fra: LocalDate,
    @Serializable(with = MeldekortLocalDateSerializer::class) val til: LocalDate,
){
    fun toInternal(): Meldekort {

        val (sisteDatoForTrekk, risikerTrekk) = calculateSisteDatoForTrekk()

        return Meldekort(
            uke,
            kanSendesFra,
            fra,
            til,
            sisteDatoForTrekk,
            risikerTrekk
        )
    }

    private fun calculateSisteDatoForTrekk(): Pair<LocalDate, Boolean> {
        val sisteDatoForTrekk = til.plusDays(8)
        val risikerTrekk = LocalDate.now().isAfter(sisteDatoForTrekk)

        return sisteDatoForTrekk to risikerTrekk
    }
}

@Serializable
data class MeldekortstatusExternal(
    val meldekort: Int = 0,
    val etterregistrerteMeldekort: Int = 0,
    val antallGjenstaaendeFeriedager: Int = 0,
    val nesteMeldekort: MeldekortExternal? = null,
    @Serializable(with = MeldekortLocalDateSerializer::class) val nesteInnsendingAvMeldekort: LocalDate? = null,
) {
    fun toInternal(): Meldekortinfo {

        val nesteMeldekort = nesteMeldekort?.let { it.toInternal() }

        val nyeMeldekort = NyeMeldekort(
            meldekort,
            nesteMeldekort,
            nesteInnsendingAvMeldekort
        )

        return Meldekortinfo(
            nyeMeldekort,
            antallGjenstaaendeFeriedager,
            etterregistrerteMeldekort,
            isMeldekortbruker()
        )
    }

    private fun isMeldekortbruker(): Boolean {
        return when {
            nesteMeldekort != null -> true
            nesteInnsendingAvMeldekort != null -> true
            antallGjenstaaendeFeriedager > 0 -> true
            etterregistrerteMeldekort > 0 -> true
            meldekort > 0 -> true
            else -> false
        }
    }
}