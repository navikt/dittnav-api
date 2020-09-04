package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBruker

class VarselService(private val varselConsumer: VarselConsumer) {

    suspend fun getActiveVarselEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return getVarselEvents(innloggetBruker) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest == null }
        }
    }

    suspend fun getInactiveVarselEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return getVarselEvents(innloggetBruker) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest != null }
        }
    }

    private suspend fun getVarselEvents(
        innloggetBruker: InnloggetBruker,
        getEvents: suspend (InnloggetBruker) -> List<Varsel>
    ): List<BeskjedDTO> {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            externalEvents.map { varsel -> toVarselDTO(varsel) }
        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Varsel", exception)
        }
    }

}
