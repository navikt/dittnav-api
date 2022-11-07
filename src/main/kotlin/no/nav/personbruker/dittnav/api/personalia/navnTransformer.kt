package no.nav.personbruker.dittnav.api.personalia

import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.pdl.generated.dto.HentNavn
import no.nav.personbruker.dittnav.api.common.TransformationException

fun Navn.toInternalNavnDTO(): NavnDTO {
    return NavnDTO(
        navn = concatenateToNavn(fornavn, mellomnavn, etternavn)
    )
}

fun toExternalNavn(result: GraphQLClientResponse<HentNavn.Result>) =
    result.data?.hentPerson?.navn?.map {
        Navn(
            fornavn = it.fornavn,
            mellomnavn = it.mellomnavn,
            etternavn = it.etternavn
        )
    } ?: throw TransformationException("Klarte ikke å transformere responsen til Navn")

private fun concatenateToNavn(fornavn: String?, mellomnavn: String?, etternavn: String?): String {
    return listOf(fornavn, mellomnavn, etternavn)
        .filter { navn -> !navn.isNullOrBlank() }
        .joinToString(" ")
}
