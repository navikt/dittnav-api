package no.nav.personbruker.dittnav.api.meldekort

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MeldekortLocalDateSerializer: KSerializer<LocalDate> {
    private val serializedDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun deserialize(decoder: Decoder): LocalDate {
        return deserializeString(decoder.decodeString())
    }

    fun deserializeString(timeString: String): LocalDate {
        return parseDateTimeFormat(timeString)
            ?: parseDateFormat(timeString)
            ?: throw RuntimeException("Forsøkte å parse dato på uventet format: $timeString")
    }

    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "no.nav.personbruker.dittnav.api.meldekort.MeldekortLocalDateSerializer",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val dateString = value.format(serializedDateFormat)

        encoder.encodeString(dateString)
    }

    private fun parseDateTimeFormat(timeString: String): LocalDate? {
        return try {
            LocalDate.parse(timeString, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    private fun parseDateFormat(timeString: String): LocalDate? {
        return try {
            LocalDate.parse(timeString, DateTimeFormatter.ISO_DATE)
        } catch (e: DateTimeParseException) {
            null
        }
    }

}
