package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.shouldBe
import no.nav.personbruker.dittnav.api.saker.ekstern.SakstemaObjectMother
import no.nav.personbruker.dittnav.api.saker.ekstern.SisteSakstemaerObjectMother
import org.junit.jupiter.api.Test

internal class MineSakerTransformerTest {

    @Test
    fun `Skal kunne konvertere fra eksterne Sakstema til intern modell`() {
        val external = SakstemaObjectMother.giveMeSakstemaDagpenger()

        val internal = external.toInternal()

        internal.kode shouldBe external.kode
        internal.navn shouldBe external.navn
        internal.sistEndret shouldBe external.sistEndret
        internal.detaljvisningUrl shouldBe external.detaljvisningUrl
    }

    @Test
    fun `Skal kunne konvertere eksterne SisteSakstemaer til intern modell`() {
        val external = SisteSakstemaerObjectMother.giveMeSisteSakstemaer()

        val internal = external.toInternal()

        internal.sakstemaer.size shouldBe external.sistEndrede.size
        internal.dagpengerSistEndret shouldBe external.dagpengerSistEndret
    }

}
