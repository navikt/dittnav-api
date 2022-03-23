package no.nav.personbruker.dittnav.api.meldekort

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MeldekortTransformerTest {

    @Test
    fun `should handle empty answer from meldekort`() {
        val meldekortstatus = MeldekortExternalObjectMother.createEmptyMeldekortStatus()

        val result = MeldekortTransformer.toInternal(meldekortstatus)

        result.nyeMeldekort.nesteMeldekort `should be equal to` null
        result.etterregistrerteMeldekort `should be equal to` 0
        result.resterendeFeriedager `should be equal to` 0
        result.meldekortbruker `should be equal to` false
    }

    @Test
    fun `should transform external meldekortstatus to meldekortinfo`() {
        val meldekortstatusExternal = MeldekortExternalObjectMother.createMeldekortStatus()

        val meldekortExternal = meldekortstatusExternal.nesteMeldekort

        val result = MeldekortTransformer.toInternal(meldekortstatusExternal)

        result.meldekortbruker `should be equal to` true
        result.etterregistrerteMeldekort `should be equal to` meldekortstatusExternal.etterregistrerteMeldekort
        result.resterendeFeriedager `should be equal to` meldekortstatusExternal.antallGjenstaaendeFeriedager

        val resultNyeMeldekort = result.nyeMeldekort

        resultNyeMeldekort.nesteInnsendingAvMeldekort `should be equal to` meldekortstatusExternal.nesteInnsendingAvMeldekort
        resultNyeMeldekort.antallNyeMeldekort `should be equal to` meldekortstatusExternal.meldekort

        val resultNesteMeldekort = resultNyeMeldekort.nesteMeldekort

        resultNesteMeldekort?.til `should be equal to` meldekortExternal?.til
        resultNesteMeldekort?.fra `should be equal to` meldekortExternal?.fra
        resultNesteMeldekort?.uke `should be equal to` meldekortExternal?.uke
        resultNesteMeldekort?.kanSendesFra `should be equal to` meldekortExternal?.kanSendesFra
        resultNesteMeldekort?.risikerTrekk `should be equal to` false
        resultNesteMeldekort?.sisteDatoForTrekk `should be equal to` meldekortExternal?.til?.plusDays(8)
    }

    @Test
    fun `should correctly judge risikertrekk`() {
        val normalMeldekort = MeldekortExternalObjectMother.createMeldekort(til = LocalDate.now())
        val meldekortAtRisk = MeldekortExternalObjectMother.createMeldekort(til = LocalDate.now().minusWeeks(2))

        val normalResult = MeldekortTransformer.toInternal(normalMeldekort)
        val atRiskResult = MeldekortTransformer.toInternal(meldekortAtRisk)

        normalResult.risikerTrekk `should be equal to` false
        atRiskResult.risikerTrekk `should be equal to` true
    }
}
