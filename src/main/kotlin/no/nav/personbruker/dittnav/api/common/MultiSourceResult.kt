package no.nav.personbruker.dittnav.api.common

import io.ktor.http.*
import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType

/**
 * Resultatobjekt som kan brukes for å samle sammen resultater fra flere kilder, og enkelt gi en fornuftig http-respons-kode.
 *
 * Hver kilde må bruke denne resultatobjekttypen, da kan resultatene fra de forskellige kilene helt enklet adderes sammen.
 *
 * Resultatojbketet inneholder oversikt over:
 * * results: alle resultater som kom tilbake.
 * * successFullSources: navnet på kildene som svarte uten feil.
 * * failedSources: navnet på kilene som feilet.
 *
 * R: Resultattypen, som er den egentlige typen man får svar i
 * S: kildetypen, kildetypen som brukes for å angi kilder som svarte uten feil og kilder som feilet
 */
data class MultiSourceResult<R, S>(
    private val results: List<R>,
    private val successFullSources: List<S>,
    private val failedSources: List<S> = emptyList()
) {

    companion object {
        fun <R, S> createSuccessfulResult(results: List<R>, source: S) = MultiSourceResult(
            results,
            listOf(source)
        )

        fun <R, S> createErrorResult(source: S) = MultiSourceResult(
            emptyList<R>(),
            emptyList<S>(),
            listOf(source)
        )

        fun <R> createEmptyResult(): MultiSourceResult<R, KildeType> = MultiSourceResult(
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    operator fun plus(other: MultiSourceResult<R, S>): MultiSourceResult<R, S> =
        MultiSourceResult(
            this.results + other.results,
            this.successFullSources + other.successFullSources,
            this.failedSources + other.failedSources
        )

    fun results() = mutableListOf<R>().apply { addAll(results) }
    fun successFullSources() = mutableListOf<S>().apply { addAll(successFullSources) }

    fun hasErrors() = failedSources.isNotEmpty()
    fun failedSources() = mutableListOf<S>().apply { addAll(failedSources) }

    fun determineHttpCode(): HttpStatusCode {
        return when {
            hasPartialResult() -> HttpStatusCode.PartialContent
            allSourcesFailed() -> HttpStatusCode.ServiceUnavailable
            else -> HttpStatusCode.OK
        }
    }

    private fun hasPartialResult(): Boolean = successFullSources.isNotEmpty() && failedSources.isNotEmpty()
    private fun allSourcesFailed(): Boolean = successFullSources.isEmpty() && failedSources.isNotEmpty()

}
