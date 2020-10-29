package no.nav.personbruker.dittnav.api.beskjed

object BeskjedResultObjectMother {

    fun createNumberOfActiveBeskjed(number: Int, baseEventId: String = "beskjed"): BeskjedResult {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createActiveBeskjed("$baseEventId-$i"))
        }
        return BeskjedResult(list)
    }

    fun createNumberOfInactiveBeskjed(number: Int, baseEventId: String = "beskjed"): BeskjedResult {
        val list = mutableListOf<BeskjedDTO>()
        for (i in 1..number) {
            list.add(BeskjedDtoObjectMother.createInactiveBeskjed("$baseEventId-$i"))
        }
        return BeskjedResult(list)
    }

}
