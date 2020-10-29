package no.nav.personbruker.dittnav.api.beskjed

data class BeskjedResult(
        private val results: List<BeskjedDTO>,
        private val errors: List<KildeType> = emptyList()
) {
    constructor(errors: List<KildeType>) : this(emptyList(), errors)

    operator fun plus(result: BeskjedResult): BeskjedResult = BeskjedResult(this.results + result.results, this.errors + result.errors)
    fun first() = results.first()
    val size: Int = results.size

    fun hasErrors() = errors.isNotEmpty()
    fun errors() = mutableListOf<KildeType>().apply { addAll(errors) }
}

enum class KildeType { VARSELINNBOKS, EVENTHANDLER }



