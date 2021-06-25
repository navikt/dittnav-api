package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.common.MultiSourceResult

object BeskjedResultObjectMother {

    fun createBeskjedResultWithoutErrors(number: Int, baseEventId: String = "beskjed"): MultiSourceResult<BeskjedDTO, KildeType> {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$i"))
        }
        return MultiSourceResult.createSuccessfulResult (list, KildeType.EVENTHANDLER)
    }

    fun createBeskjedResultWithOneError(number: Int, baseEventId: String = "beskjed"): MultiSourceResult<BeskjedDTO, KildeType> {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$i"))
        }
        return MultiSourceResult(list, listOf(KildeType.EVENTHANDLER), listOf(KildeType.VARSELINNBOKS))
    }

    fun createBeskjedResultWithTwoErrors(): MultiSourceResult<BeskjedDTO, KildeType> {
        return MultiSourceResult(emptyList(), emptyList(), listOf(KildeType.EVENTHANDLER, KildeType.VARSELINNBOKS))
    }

}
