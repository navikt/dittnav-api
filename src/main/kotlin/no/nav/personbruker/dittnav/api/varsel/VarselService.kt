package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedResult
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.InnloggetBruker

class VarselService(private val varselConsumer: VarselConsumer) {

    suspend fun getActiveVarselEvents(innloggetBruker: InnloggetBruker): BeskjedResult {
        return getVarselEvents(innloggetBruker) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest == null }
        }
    }

    suspend fun getInactiveVarselEvents(innloggetBruker: InnloggetBruker): BeskjedResult {
        return getVarselEvents(innloggetBruker) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest != null }
        }
    }

    private suspend fun getVarselEvents(
        innloggetBruker: InnloggetBruker,
        getEvents: suspend (InnloggetBruker) -> List<Varsel>
    ): BeskjedResult {
        return try {
            val externalEvents = getEvents(innloggetBruker)
            val results = externalEvents.map { varsel -> toVarselDTO(varsel) }
            BeskjedResult(results)

        } catch (exception: Exception) {
            BeskjedResult(listOf(KildeType.VARSELINNBOKS))
        }
    }
}
