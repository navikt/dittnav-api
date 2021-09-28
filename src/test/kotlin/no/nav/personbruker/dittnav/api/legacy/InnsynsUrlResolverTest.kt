package no.nav.personbruker.dittnav.api.legacy

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class InnsynsUrlResolverTest {

    @Test
    fun `Skal returnere prod-lenker for alle sakstemaer`() {
        val resolver = InnsynsUrlResolver(true)
        val temaerMedSpesifikkeLenkerIProd = innsynslenkerProd.keys

        temaerMedSpesifikkeLenkerIProd.forEach { tema ->
            resolver.urlFor(tema) `should be equal to` innsynslenkerProd[tema]
        }

        val genereltTema = "GEN"
        resolver.urlFor(genereltTema).toString() `should be equal to` "$generellInnsynslenkeProd$genereltTema"
    }

    @Test
    fun `Skal returnere dev-lenker for alle sakstemaer`() {
        val resolver = InnsynsUrlResolver(false)
        val temaerMedSpesifikkeLenkerIDev = innsynslenkerDev.keys

        temaerMedSpesifikkeLenkerIDev.forEach { tema ->
            resolver.urlFor(tema) `should be equal to` innsynslenkerDev[tema]
        }

        val genereltTema = "GEN"
        resolver.urlFor(genereltTema).toString() `should be equal to` "$generellInnsynslenkeDev$genereltTema"
    }

}
