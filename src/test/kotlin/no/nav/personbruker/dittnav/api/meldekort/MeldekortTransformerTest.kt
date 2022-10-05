package no.nav.personbruker.dittnav.api.meldekort

import io.kotest.matchers.shouldBe
import no.nav.personbruker.dittnav.api.meldekort.external.MeldekortstatusExternal
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MeldekortTransformerTest {

    @Test
    fun `should handle empty answer from meldekort`() {
        val meldekortstatus = MeldekortstatusExternal()

        val result = MeldekortTransformer.toInternal(meldekortstatus)

        result.nyeMeldekort.nesteMeldekort shouldBe null
        result.etterregistrerteMeldekort shouldBe 0
        result.resterendeFeriedager shouldBe 0
        result.meldekortbruker shouldBe false
    }

    @Test
    fun `should transform external meldekortstatus to meldekortinfo`() {
        val meldekortstatusExternal = createMeldekortStatus()

        val meldekortExternal = meldekortstatusExternal.nesteMeldekort

        val result = MeldekortTransformer.toInternal(meldekortstatusExternal)

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

        val normalResult = MeldekortTransformer.toInternal(normalMeldekort)
        val atRiskResult = MeldekortTransformer.toInternal(meldekortAtRisk)

        normalResult.risikerTrekk shouldBe false
        atRiskResult.risikerTrekk shouldBe true
    }
}