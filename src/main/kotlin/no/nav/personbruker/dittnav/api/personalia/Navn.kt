package no.nav.personbruker.dittnav.api.personalia

import com.expediagroup.graphql.client.types.GraphQLClientResponse
import kotlinx.serialization.Serializable
import no.nav.personbruker.dittnav.api.common.TransformationException
import no.nav.personbruker.dittnav.api.pdl.generated.dto.HentNavn

@Serializable
data class NavnDTO(
    val navn: String?
)

fun toExternalNavn(result: GraphQLClientResponse<HentNavn.Result>) =
    result.data?.hentPerson?.navn?.map {
        NavnExternal(
            fornavn = it.fornavn,
            mellomnavn = it.mellomnavn,
            etternavn = it.etternavn
        )
    } ?: throw TransformationException("Klarte ikke å transformere responsen til Navn")


