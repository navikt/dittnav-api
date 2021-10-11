package no.nav.personbruker.dittnav.api.saker

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class MineSakerTransformerTest {

    @Test
    fun `Skal kunne konvertere fra ekstern til intern modell`() {
        val external = SakstemaObjectMother.giveMeSakstemaDagpenger()

        val internal = external.toInternal()

        internal.kode `should be equal to` external.kode
        internal.navn `should be equal to` external.navn
        internal.sistEndret `should be equal to` external.sistEndret
        internal.detaljvisningUrl `should be equal to` external.detaljvisningUrl
    }

}
