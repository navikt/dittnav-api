package no.nav.personbruker.dittnav.api.meldekort

import io.kotest.matchers.shouldBe
import no.nav.personbruker.dittnav.api.assert
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MeldekortExternalTest {

    @Test
    fun `should handle empty answer from meldekort`() {
        val meldekortstatus = MeldekortstatusExternal()
        meldekortstatus.toInternal().assert {
            nyeMeldekort.nesteMeldekort shouldBe null
            etterregistrerteMeldekort shouldBe 0
            resterendeFeriedager shouldBe 0
            meldekortbruker shouldBe false
        }
    }

    @Test
    fun `should transform external meldekortstatus to meldekortinfo`() {
        val meldekortstatusExternal = createMeldekortStatus()

        val meldekortExternal = meldekortstatusExternal.nesteMeldekort

        val result = meldekortstatusExternal.toInternal()

        result.meldekortbruker shouldBe true
        result.etterregistrerteMeldekort shouldBe meldekortstatusExternal.etterregistrerteMeldekort
        result.resterendeFeriedager shouldBe meldekortstatusExternal.antallGjenstaaendeFeriedager

        val resultNyeMeldekort = result.nyeMeldekort

        resultNyeMeldekort.nesteInnsendingAvMeldekort shouldBe meldekortstatusExternal.nesteInnsendingAvMeldekort
        resultNyeMeldekort.antallNyeMeldekort shouldBe meldekortstatusExternal.meldekort

        val resultNesteMeldekort = resultNyeMeldekort.nesteMeldekort

        resultNesteMeldekort?.til shouldBe meldekortExternal?.til
        resultNesteMeldekort?.fra shouldBe meldekortExternal?.fra
        resultNesteMeldekort?.uke shouldBe meldekortExternal?.uke
        resultNesteMeldekort?.kanSendesFra shouldBe meldekortExternal?.kanSendesFra
        resultNesteMeldekort?.risikerTrekk shouldBe false
        resultNesteMeldekort?.sisteDatoForTrekk shouldBe meldekortExternal?.til?.plusDays(8)
    }

    @Test
    fun `should correctly judge risikertrekk`() {
        val normalMeldekort = createMeldekort(til = LocalDate.now())
        val meldekortAtRisk = createMeldekort(til = LocalDate.now().minusWeeks(2))

        val normalResult = normalMeldekort.toInternal()
        val atRiskResult = meldekortAtRisk.toInternal()

        normalResult.risikerTrekk shouldBe false
        atRiskResult.risikerTrekk shouldBe true
    }
}