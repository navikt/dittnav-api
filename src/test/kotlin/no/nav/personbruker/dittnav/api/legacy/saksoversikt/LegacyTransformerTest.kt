package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class LegacyTransformerTest {

    @Test
    fun `Skal kunne transformere fra LegacySakstemaerRespons til intern modell`() {
        val externals = LegacySakstemaerResponsObjectMother.giveMeLegacyResponseWithDagpenger()

        val internal = externals.toInternal()

        internal.sakstemaer.size `should be equal to` 2
        internal.dagpengerSistEndret.shouldNotBeNull()
    }

    @Test
    fun `Skal kunne transformere fra LegacySakstema til intern modell`() {
        val external = LegacySakstemaObjectMother.giveMeSakstemaDagpenger()

        val internal = external.toInternal()

        internal.kode `should be equal to` external.temakode
        internal.navn `should be equal to` external.temanavn
        internal.sistEndret `should be equal to` external.sisteOppdatering
        internal.detaljvisningUrl.toString() `should contain` "dagpenger"
    }

}
