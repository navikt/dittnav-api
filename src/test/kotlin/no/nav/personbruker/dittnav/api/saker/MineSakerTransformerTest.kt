package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.saker.ekstern.SakstemaObjectMother
import no.nav.personbruker.dittnav.api.saker.ekstern.SisteSakstemaerObjectMother
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class MineSakerTransformerTest {

    @Test
    fun `Skal kunne konvertere fra eksterne Sakstema til intern modell`() {
        val external = SakstemaObjectMother.giveMeSakstemaDagpenger()

        val internal = external.toInternal()

        internal.kode `should be equal to` external.kode
        internal.navn `should be equal to` external.navn
        internal.sistEndret `should be equal to` external.sistEndret
        internal.detaljvisningUrl `should be equal to` external.detaljvisningUrl
    }

    @Test
    fun `Skal kunne konvertere eksterne SisteSakstemaer til intern modell`() {
        val external = SisteSakstemaerObjectMother.giveMeSisteSakstemaer()

        val internal = external.toInternal()

        internal.sakstemaer.size `should be equal to` external.sistEndrede.size
        internal.dagpengerSistEndret `should be equal to` external.dagpengerSistEndret
    }

}
