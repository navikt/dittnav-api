package no.nav.personbruker.dittnav.api.logging

import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not contain`
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class MaskedLoggingEventTest {
    @Test
    fun masked() {
        assertMasked("-12345678901")
        assertMasked("12345678901")
        assertMasked(" 12345678901")
        assertMasked("12345678901 ")
        assertMasked(" 12345678901 ")
        assertMasked("abc 12345678901 def")
        assertMasked("callId=7b7c<12345676543>c8c32129c837808f7")
    }

    @Test
    fun unmasked() {
        assertUnmasked("")
        assertUnmasked("abc")
        assertUnmasked("1234")
        assertUnmasked("1234567890")
        assertUnmasked("123456789012")
        assertUnmasked("callId=7b7c12345676543c8c32129c837808f7")
    }

    @Test
    fun nullValue() {
        MaskedLoggingEvent.mask(null) `should equal` null
    }

    @Test
    fun formatting() {
        assertMaskedAS(
            "12345678901-12345678901 12345678901",
            "$MASKED_FNR-$MASKED_FNR $MASKED_FNR"
        )
        assertMaskedAS(
            "12345678901,12345678901",
            "$MASKED_FNR,$MASKED_FNR"
        )
    }

    private fun assertMasked(string: String) {
        LOGGER.info(string)
        MaskedLoggingEvent.mask(string)!! `should contain` MASKED_FNR
    }

    private fun assertUnmasked(string: String) {
        LOGGER.info(string)
        MaskedLoggingEvent.mask(string)!! `should not contain` "*"
    }

    private fun assertMaskedAS(string: String, expected: String) {
        LOGGER.info(string)
        MaskedLoggingEvent.mask(string)!! `should equal` expected
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MaskedLoggingEventTest::class.java)
        const val MASKED_FNR = "***********"
    }
}
