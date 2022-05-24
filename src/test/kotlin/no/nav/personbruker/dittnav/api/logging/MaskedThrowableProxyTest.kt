package no.nav.personbruker.dittnav.api.logging

import ch.qos.logback.classic.spi.ThrowableProxy
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import no.nav.personbruker.dittnav.api.logging.MaskedThrowableProxy.Companion.mask
import org.junit.jupiter.api.Test
import java.io.IOException

class MaskedThrowableProxyTest {
    @Test
    fun `sjekk at maskering av fodselsnummer skjer ved kastet feil`() {
        val sensitiveException = RuntimeException(
            FNR,
            IllegalArgumentException(
                FNR,
                Exception(FNR)
            )
        )
        sensitiveException.addSuppressed(
            IllegalStateException(
                FNR,
                IOException(FNR)
            )
        )
        sensitiveException.printStackTrace()
        val sensitiveThrowableProxy = ThrowableProxy(sensitiveException)
        val maskedThrowableProxy = mask(sensitiveThrowableProxy)

        ThrowableProxyUtil.asString(sensitiveThrowableProxy) shouldContain FNR
        ThrowableProxyUtil.asString(maskedThrowableProxy) shouldNotContain FNR
    }

    companion object {
        private const val FNR = "12345678901"
    }

}
