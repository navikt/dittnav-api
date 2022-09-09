package no.nav.personbruker.dittnav.api.meldekort

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.*

internal class MeldekortLocalDateSerializerTest {
    private val serializer = MeldekortLocalDateSerializer()

    @Test
    fun `Deserialiserer verdier på format uten klokkeslett`() {

        val currentDate = LocalDateTime.now()
        val toDeserialize = currentDate.format(ISO_DATE)

        val result = serializer.deserializeString(toDeserialize)

        result.year shouldBe currentDate.year
        result.month shouldBe currentDate.month
        result.dayOfMonth shouldBe currentDate.dayOfMonth
    }

    @Test
    fun `Deserialiserer verdier på format med klokkeslett`() {

        val currentDate = LocalDateTime.now()
        val toDeserialize = currentDate.format(ISO_DATE_TIME)

        val result = serializer.deserializeString(toDeserialize)

        result.year shouldBe currentDate.year
        result.month shouldBe currentDate.month
        result.dayOfMonth shouldBe currentDate.dayOfMonth
    }

    @Test
    fun `Feiler ved deserialisering av andre forat`() {

        val currentDate = LocalDate.now()
        val toDeserialize = currentDate.format(ofPattern("dd-MM-yyyy"))

        shouldThrow<RuntimeException> {
            serializer.deserializeString(toDeserialize)
        }
    }
}
