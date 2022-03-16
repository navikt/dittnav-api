package no.nav.personbruker.dittnav.api.mininnboks

import no.nav.personbruker.dittnav.api.mininnboks.UbehandledeMeldingerType.*
import no.nav.personbruker.dittnav.api.mininnboks.external.UbehandletMeldingExternal

class UbehandledeMeldingerTransformer(private val mininnboksUrl: String, private val innloggingsinfoUrl: String) {

    fun toInternal(meldinger: List<UbehandletMeldingExternal>): List<UbehandledeMeldinger> {

        return meldinger.groupBy { determineType(it) }
            .map { (type, meldinger) ->
                createUbehandledeMeldinger(type, meldinger)
            }
    }

    private fun createUbehandledeMeldinger(type: UbehandledeMeldingerType, meldinger: List<UbehandletMeldingExternal>): UbehandledeMeldinger {

        val url = createInnloggingsinfoUrl(meldinger, type)

        val antall = meldinger.size

        return UbehandledeMeldinger(type, url, antall)
    }

    private fun createInnloggingsinfoUrl(meldinger: List<UbehandletMeldingExternal>, type: UbehandledeMeldingerType): String {
        return if (type == ULEST || type == UBESVART) {
            mininnboksUrl
        } else if (meldinger.size == 1){
            createInnloggingsinfoUrlForSingle(meldinger.first(), type)
        } else {
            createInnloggingsinfoUrlForMultiple(meldinger, type)
        }
    }

    private fun createInnloggingsinfoUrlForSingle(melding: UbehandletMeldingExternal, type: UbehandledeMeldingerType): String {

        return when {
            melding.undertype == null && melding.varselid == null -> "$innloggingsinfoUrl/type/${type.externalPath}"
            melding.undertype == null -> "$innloggingsinfoUrl/type/${type.externalPath}/varselid/${melding.varselid}"
            melding.varselid == null -> "$innloggingsinfoUrl/type/${type.externalPath}/undertype/${melding.undertype}"
            else -> "$innloggingsinfoUrl/type/${type.externalPath}/undertype/${melding.undertype}/varselid/${melding.varselid}"
        }
    }

    private fun createInnloggingsinfoUrlForMultiple(meldinger: List<UbehandletMeldingExternal>, type: UbehandledeMeldingerType): String {
        val undertype = meldinger.mapNotNull { it.undertype }
            .distinct()
            .takeIf { it.size == 1 }
            ?.first()
            ?.lowercase()

        return if (undertype != null) {
            "$innloggingsinfoUrl/type/${type.externalPath}/undertype/$undertype}"
        } else {
            "$innloggingsinfoUrl/type/${type.externalPath}"
        }
    }

    private fun determineType(ubehandletMelding: UbehandletMeldingExternal): UbehandledeMeldingerType {
        return when {
            OPPGAVE_VARSEL.name == ubehandletMelding.type -> OPPGAVE_VARSEL
            DOKUMENT_VARSEL.name == ubehandletMelding.type -> DOKUMENT_VARSEL
            else -> ubesvartOrUlest(ubehandletMelding)
        }
    }

    private fun ubesvartOrUlest(ubehandletMelding: UbehandletMeldingExternal): UbehandledeMeldingerType {
        return if (ubehandletMelding.statuser.contains(UBESVART.name)) {
            UBESVART
        } else {
            ULEST
        }
    }
}
