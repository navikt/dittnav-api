package no.nav.personbruker.dittnav.api.beskjed

object BeskjedResultObjectMother {

    fun createBeskjedResultWithoutErrors(number: Int, baseEventId: String = "beskjed"): BeskjedResult {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$i"))
        }
        return BeskjedResult(list)
    }

    fun createBeskjedResultWithOneError(number: Int, baseEventId: String = "beskjed"): BeskjedResult {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$i"))
        }
        return BeskjedResult(list, listOf(KildeType.VARSELINNBOKS))
    }

    fun createBeskjedResultWithTwoErrors(number: Int, baseEventId: String = "beskjed"): BeskjedResult {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$i"))
        }
        return BeskjedResult(list, listOf(KildeType.EVENTHANDLER, KildeType.VARSELINNBOKS))
    }

}
