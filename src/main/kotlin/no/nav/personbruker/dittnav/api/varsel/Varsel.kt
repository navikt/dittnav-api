package no.nav.personbruker.dittnav.api.varsel

import java.time.ZonedDateTime

data class Varsel(
    val id: Long,
    val aktoerID: String,
    val url: String,
    val varseltekst: String,
    val varselId: String,
    val meldingsType: String,
    val datoOpprettet: ZonedDateTime,
    val datoLest: ZonedDateTime?
) {
    override fun toString(): String {
        return "Varsel(" +
                "id=$id, " +
                "aktoerID=***, " +
                "url=***, " +
                "varseltekst=***, " +
                "varselId=$varselId, " +
                "meldingsType=$meldingsType, " +
                "datoOpprettet=$datoOpprettet, " +
                "datoLest=$datoLest)"
    }

}
