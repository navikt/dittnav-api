package no.nav.personbruker.dittnav.api.legacy

object LegacySakstemaerResponsObjectMother {

    fun giveMeLegacyResponse() = LegacySakstemaerRespons(
        1, listOf(
            LegacySakstemaObjectMother.giveMeSakstemaDagpenger(),
            LegacySakstemaObjectMother.giveMeSakstemaOppfolging()
        )
    )

}
