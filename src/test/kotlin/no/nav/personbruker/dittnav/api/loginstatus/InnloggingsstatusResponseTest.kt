package no.nav.personbruker.dittnav.api.loginstatus

import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class InnloggingsstatusResponseTest {

    val unauthenticatedResponse = """
        {
            "authenticated": false,
            "stable": true
        }
    """.trimIndent()

    val authenticatedWithOpenAmOnlyResponse = """
        {
            "openAMToken": { 
                "authLevel": 3
            },
            "authenticated": true,
            "authLevel": 3
        }
    """.trimIndent()

    val authenticatedWithBothResponse = """
        {
            "oidcToken": {
                "subject": "12345678912",
                "authLevel": 3,
                "issueTime": "2020-10-10T00:00:00",
                "expiryTime": "2020-10-10T01:00:00"
            },
            "openAMToken": { 
                "subject": "12345678912",
                "authLevel": 4
            },
            "authenticated": true,
            "authLevel": 4
        }
    """.trimIndent()

    @Test
    fun `Can deserialize unauthenticated response`() {
        val response: InnloggingsstatusResponse = readResponse(unauthenticatedResponse)

        response.authenticated `should be equal to` false
        response.authLevel `should be equal to` null
    }

    @Test
    fun `Can deserialize response when authenticated with openAM only`() {
        val response: InnloggingsstatusResponse = readResponse(authenticatedWithOpenAmOnlyResponse)

        response.authenticated `should be equal to` true
        response.authLevel `should be equal to` 3
    }

    @Test
    fun `Can deserialize response when authenticated with openAM and oidc`() {
        val response: InnloggingsstatusResponse = readResponse(authenticatedWithBothResponse)

        response.authenticated `should be equal to` true
        response.authLevel `should be equal to` 4
    }

    private fun readResponse(json: String): InnloggingsstatusResponse {
        return json().decodeFromString(json)
    }
}
