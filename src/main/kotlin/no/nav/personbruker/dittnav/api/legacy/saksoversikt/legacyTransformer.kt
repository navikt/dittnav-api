package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import no.nav.personbruker.dittnav.api.saker.SakstemaDTO
import no.nav.personbruker.dittnav.api.saker.SisteSakstemaerDTO
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

private val log = LoggerFactory.getLogger("LegacyTransformerKt")

fun List<LegacySakstema>.toInternal(): List<SakstemaDTO> {
    return filterNotNull()
        .filter { externalSakstema -> externalSakstema.sisteOppdatering != null }
        .map { externalSakstema -> externalSakstema.toInternal() }
        .toList()
}

fun LegacySakstema.toInternal(): SakstemaDTO {
    return SakstemaDTO(
        navn = temanavn,
        kode = temakode,
        sistEndret = sisteOppdatering!!,
        detaljvisningUrl = innsynsUrlResolverSingleton.urlFor(temakode)
    )
}

fun LegacySakstemaerRespons.toInternal(): SisteSakstemaerDTO {
    val sakstemaer = sakstemaList.toInternal()
    return SisteSakstemaerDTO(
        sakstemaer.plukkUtDeToSomErSistEndretFraSortertListe(),
        sakstemaer.plukkUtSistEndretForDagpenger()
    )
}

fun List<SakstemaDTO>.plukkUtDeToSomErSistEndretFraSortertListe(): List<SakstemaDTO> {
    return if (moreThanTwoSakstemaer()) {
        subList(0, 2)
    } else {
        this
    }
}

private fun List<SakstemaDTO>.moreThanTwoSakstemaer() =
    size > 2

private fun List<SakstemaDTO>.plukkUtSistEndretForDagpenger(): ZonedDateTime? {
    return filterNotNull().find { s -> s.kode == "DAG" }?.sistEndret
}
