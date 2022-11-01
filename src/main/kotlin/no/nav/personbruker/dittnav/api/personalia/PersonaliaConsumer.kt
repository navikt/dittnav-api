package no.nav.personbruker.dittnav.api.personalia

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.api.config.get
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import java.net.URL

class PersonaliaConsumer(
    private val client: HttpClient,
    personaliaApiURL: URL
) {

    private val navnEndpoint = URL("$personaliaApiURL/navn")
    private val identEndpoint = URL("$personaliaApiURL/ident")

    suspend fun hentNavn(user: AccessToken): PersonaliaNavnDTO {
        return client.get(navnEndpoint, user)
    }

    suspend fun hentIdent(user: AccessToken): PersonaliaIdentDTO {
        return client.get(identEndpoint, user)
    }

}
