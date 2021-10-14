package no.nav.personbruker.dittnav.api.legacy.saksoversikt

object LegacySakstemaerResponsObjectMother {

    fun giveMeLegacyResponseWithDagpenger(): LegacySakstemaerRespons {
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

}
