package no.nav.personbruker.dittnav.api.legacy.saksoversikt

object LegacySakstemaerResponsObjectMother {

    fun giveMeLegacyResponseWithDagpengerSomEnAvDeToSiste(): LegacySakstemaerRespons {
        val sakstemaer = listOf(
            LegacySakstemaObjectMother.giveMeSakstemaDagpenger(),
            LegacySakstemaObjectMother.giveMeSakstemaOppfolging(),
            LegacySakstemaObjectMother.giveMeSakstemaBil(),
            LegacySakstemaObjectMother.giveMeSakstemaSosialhjelp()
        )
        return LegacySakstemaerRespons(
            sakstemaer.size,
            sakstemaer
        )
    }

    fun giveMeLegacyResponseWithDagpenger(): LegacySakstemaerRespons {
        val sakstemaer = listOf(
            LegacySakstemaObjectMother.giveMeSakstemaOppfolging(),
            LegacySakstemaObjectMother.giveMeSakstemaBil(),
            LegacySakstemaObjectMother.giveMeSakstemaSosialhjelp(),
            LegacySakstemaObjectMother.giveMeSakstemaDagpenger()
        )
        return LegacySakstemaerRespons(
            sakstemaer.size,
            sakstemaer
        )
    }

    fun giveMeLegacyResponseWithoutDagpenger(): LegacySakstemaerRespons {
        val sakstemaer = listOf(
            LegacySakstemaObjectMother.giveMeSakstemaOppfolging(),
            LegacySakstemaObjectMother.giveMeSakstemaBil(),
            LegacySakstemaObjectMother.giveMeSakstemaSosialhjelp()
        )
        return LegacySakstemaerRespons(
            sakstemaer.size,
            sakstemaer
        )
    }

}
