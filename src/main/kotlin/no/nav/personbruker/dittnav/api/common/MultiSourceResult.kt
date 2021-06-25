package no.nav.personbruker.dittnav.api.common

import io.ktor.http.*

data class MultiSourceResult<R, S>(
    private val results: List<R>,
    private val successFullSources: List<S>,
    private val errors: List<S> = emptyList(),
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
    }

    operator fun plus(other: MultiSourceResult<R, S>): MultiSourceResult<R, S> =
        MultiSourceResult(
            this.results + other.results,
            this.successFullSources + other.successFullSources,
            this.errors + other.errors
        )

    fun results() = mutableListOf<R>().apply { addAll(results) }
    fun successFullSources() = mutableListOf<S>().apply { addAll(successFullSources) }

    fun hasErrors() = errors.isNotEmpty()
    fun errors() = mutableListOf<S>().apply { addAll(errors) }


    fun determineHttpCode(): HttpStatusCode {
        return when {
            hasPartialResult() -> HttpStatusCode.PartialContent
            allSourcesFailed() -> HttpStatusCode.ServiceUnavailable
            else -> HttpStatusCode.OK
        }
    }

    private fun hasPartialResult(): Boolean = successFullSources.isNotEmpty() && errors.isNotEmpty()
    private fun allSourcesFailed(): Boolean = successFullSources.isEmpty() && errors.isNotEmpty()

}
