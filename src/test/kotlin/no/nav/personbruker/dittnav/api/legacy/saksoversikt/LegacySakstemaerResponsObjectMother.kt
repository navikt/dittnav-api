package no.nav.personbruker.dittnav.api.legacy.saksoversikt

object LegacySakstemaerResponsObjectMother {

    fun giveMeLegacyResponse() = LegacySakstemaerRespons(
        1, listOf(
            LegacySakstemaObjectMother.giveMeSakstemaDagpenger(),
            LegacySakstemaObjectMother.giveMeSakstemaOppfolging()
        )
    )

}
