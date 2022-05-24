package no.nav.personbruker.dittnav.api.common

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class IdentityClaimTest {

    @Test
    fun `should convert valid strings to enum`() {
        IdentityClaim.fromClaimName("pid") shouldBe IdentityClaim.PID
        IdentityClaim.fromClaimName("PID") shouldBe IdentityClaim.PID
        IdentityClaim.fromClaimName("sub") shouldBe IdentityClaim.SUBJECT
        IdentityClaim.fromClaimName("SUB") shouldBe IdentityClaim.SUBJECT
    }

    @Test
    fun `should throw exception if invalid value is received`() {
        shouldThrow<IllegalArgumentException> {
            IdentityClaim.fromClaimName("")
        }
    }

}
