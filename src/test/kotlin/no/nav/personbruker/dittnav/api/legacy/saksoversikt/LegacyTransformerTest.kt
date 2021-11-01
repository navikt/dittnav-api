package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class LegacyTransformerTest {

    @Test
    fun `Skal stotte at bruker kun har et sakstema`() {
        val externals = LegacySakstemaerResponsObjectMother.giveMeLegacyResponseWithJustOneSakstema()

        val internal = externals.toInternal()

        internal.sakstemaer.size `should be equal to` 1
        internal.dagpengerSistEndret.shouldBeNull()
    }

    @Test
    fun `Skal stotte at dagpenger er en av de to siste sakstemaene`() {
        val externals = LegacySakstemaerResponsObjectMother.giveMeLegacyResponseWithDagpengerSomEnAvDeToSiste()

        val internal = externals.toInternal()

        internal.sakstemaer.size `should be equal to` 2
        internal.dagpengerSistEndret.shouldNotBeNull()
        val dagpengerSomEnAvDeToSiste = internal.sakstemaer[0]
        dagpengerSomEnAvDeToSiste.kode `should be equal to` "DAG"
        internal.dagpengerSistEndret `should be equal to` dagpengerSomEnAvDeToSiste.sistEndret
    }

    @Test
    fun `Skal stotte at dagpenger IKKE er en av de to siste, og plukke ut sist endret datoen for den gamle dagpenge saken`() {
        val externals = LegacySakstemaerResponsObjectMother.giveMeLegacyResponseWithDagpenger()

        val internal = externals.toInternal()

        internal.sakstemaer.size `should be equal to` 2
        internal.dagpengerSistEndret.shouldNotBeNull()
    }

    @Test
    fun `Skal stotte at en bruker aldri har hatt dagpenger noen gang, da skal de ikke returneres dato for dagpenger`() {
        val externals = LegacySakstemaerResponsObjectMother.giveMeLegacyResponseWithoutDagpenger()

        val internal = externals.toInternal()

        internal.sakstemaer.size `should be equal to` 2
        internal.dagpengerSistEndret.shouldBeNull()
    }

    @Test
    fun `Skal kunne transformere fra et konkret eksternt sakstema til intern modell`() {
        val external = LegacySakstemaObjectMother.giveMeSakstemaDagpenger()

        val internal = external.toInternal()

        internal.kode `should be equal to` external.temakode
        internal.navn `should be equal to` external.temanavn
        internal.sistEndret `should be equal to` external.sisteOppdatering
        internal.detaljvisningUrl.toString() `should contain` "dagpenger"
    }

}
