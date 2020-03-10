package no.nav.personbruker.dittnav.api.common

import no.nav.personbruker.dittnav.api.common.OIDCValidationContextPrincipalObjectMother.createPrincipalForAzure
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.ktor.OIDCValidationContextPrincipal
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldNotBeNullOrBlank
import org.junit.jupiter.api.Test
import java.util.*

internal class InnloggetBrukerFactoryTest {

    @Test
    fun `should throw exception if principal is missing`() {
        invoking {
            InnloggetBrukerFactory.createNewInnloggetBruker(null)
        } `should throw` Exception::class
    }

    @Test
    fun `should throw exception if the token context is empty`() {
        val context = TokenValidationContext(emptyMap())
        val principal = OIDCValidationContextPrincipal(context)
        invoking {
            InnloggetBrukerFactory.createNewInnloggetBruker(principal)
        } `should throw` NoSuchElementException::class
    }

    @Test
    fun `should extract identity from the claim with the name sub for tokens form Azure`() {
        val expectedIdent = "000"
        val expectedInnloggingsnivaa = 3

        val principal = createPrincipalForAzure(expectedIdent, expectedInnloggingsnivaa)


        val innloggetBruker = InnloggetBrukerFactory.createNewInnloggetBruker(principal)

        innloggetBruker.ident `should be equal to` expectedIdent
        innloggetBruker.innloggingsnivaa `should be equal to` expectedInnloggingsnivaa
        innloggetBruker.token.shouldNotBeNullOrBlank()
    }

}
