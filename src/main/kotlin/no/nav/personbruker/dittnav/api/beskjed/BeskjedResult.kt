package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.http.*

data class BeskjedResult(
    private val results: List<BeskjedDTO>,
    private val errors: List<KildeType> = emptyList()
) {
    constructor(errors: List<KildeType>) : this(emptyList(), errors)

    operator fun plus(result: BeskjedResult): BeskjedResult =
        BeskjedResult(this.results + result.results, this.errors + result.errors)

    fun results() = mutableListOf<BeskjedDTO>().apply { addAll(results) }

    fun hasErrors() = errors.isNotEmpty()
    fun errors() = mutableListOf<KildeType>().apply { addAll(errors) }

    fun determineHttpCode(): HttpStatusCode {
        return when {
            hasPartialResult() -> HttpStatusCode.PartialContent
            allSourcesFailed() -> HttpStatusCode.ServiceUnavailable
            else -> HttpStatusCode.OK
        }
    }

    private fun hasPartialResult(): Boolean = errors.size == 1
    private fun allSourcesFailed(): Boolean = errors.size == KildeType.values().size

}

enum class KildeType { VARSELINNBOKS, EVENTHANDLER }
