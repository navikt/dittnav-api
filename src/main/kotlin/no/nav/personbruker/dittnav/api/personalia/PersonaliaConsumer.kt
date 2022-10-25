package no.nav.personbruker.dittnav.api.personalia

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.config.ConsumePersonaliaException
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class PersonaliaConsumer(
    private val client: HttpClient,
    private val personaliaTokendings: PersonaliaTokendings,
    personaliaApiURL: URL
) {

    private val navnEndpoint = URL("$personaliaApiURL/navn")
    private val identEndpoint = URL("$personaliaApiURL/ident")

    suspend fun hentNavn(user: AuthenticatedUser): PersonaliaNavnDTO =
        try {
            val exchangedToken = personaliaTokendings.exchangeToken(user)
            client.get(navnEndpoint, exchangedToken)
        } catch (e: Exception) {
            throw ConsumePersonaliaException("Klarte ikke å hente navn", e)
        }


    suspend fun hentIdent(user: AuthenticatedUser): PersonaliaIdentDTO =
        try {
            val exchangedToken = personaliaTokendings.exchangeToken(user)
            client.get(identEndpoint, exchangedToken)

        } catch (e: Exception) {
            throw ConsumePersonaliaException("Klarte ikke å hente ident", e)
        }


}
