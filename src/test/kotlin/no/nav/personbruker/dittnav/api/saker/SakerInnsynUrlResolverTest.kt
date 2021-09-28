package no.nav.personbruker.dittnav.api.saker

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class SakerInnsynUrlResolverTest {

    @Test
    fun `Skal returnere korrekte URL-er i dev`() {
        val resolver = SakerInnsynUrlResolver(false)

        resolver.getMineSakerUrl().toString() `should be equal to` "https://person.dev.nav.no/mine-saker"
        resolver.getSaksoversiktUrl().toString() `should be equal to` "https://tjenester-q1.nav.no/person/saksoversikt"
    }

    @Test
    fun `Skal returnere korrekte URL-er i prod`() {
        val resolver = SakerInnsynUrlResolver(true)

        resolver.getMineSakerUrl().toString() `should be equal to` "https://person.nav.no/mine-saker"
        resolver.getSaksoversiktUrl().toString() `should be equal to` "https://tjenester.nav.no/person/saksoversikt"
    }

}
