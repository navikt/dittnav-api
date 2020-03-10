package no.nav.personbruker.dittnav.api.common

import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

internal class IdentityClaimTest {

    @Test
    fun `should convert valid strings to enum`() {
        IdentityClaim.fromClaimName("pid") `should equal` IdentityClaim.PID
        IdentityClaim.fromClaimName("PID") `should equal` IdentityClaim.PID
        IdentityClaim.fromClaimName("sub") `should equal` IdentityClaim.SUBJECT
        IdentityClaim.fromClaimName("SUB") `should equal` IdentityClaim.SUBJECT
    }

    @Test
    fun `should throw exception if invalid value is received`() {
        invoking {
            IdentityClaim.fromClaimName("")
        } `should throw` IllegalArgumentException::class
    }

}
