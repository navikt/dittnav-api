package no.nav.personbruker.dittnav.api.personalia

object PersonaliaObjectMother {

    fun giveMeNavn(): PersonaliaNavnDTO {
        return PersonaliaNavnDTO("TestName")
    }

    fun giveMeIdent(): PersonaliaIdentDTO {
        return PersonaliaIdentDTO("1234")
    }

}
