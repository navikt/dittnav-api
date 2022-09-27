package no.nav.personbruker.dittnav.api

import io.kotest.matchers.shouldBe
import io.ktor.application.Application
import io.ktor.client.HttpClient
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.config.api
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.meldekort.MeldekortService
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.personalia.PersonaliaService
import no.nav.personbruker.dittnav.api.saker.SakerService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import org.junit.jupiter.api.Test

private const val testIssuer = "test-issuer"
private val jwtStub = JwtStub(testIssuer)
private val stubToken = jwtStub.createTokenFor("subject", "audience")

class TestApplication {
    private val authCheckEndpoint = "/authPing"

    @Test
    fun `200 for autorisert request`() {
        withTestApplication({
            mockApi()
        }) {
            handleRequest(HttpMethod.Get, authCheckEndpoint) {
                addHeader(HttpHeaders.Cookie, "selvbetjening-idtoken=$stubToken")
            }.apply {
                response.status() shouldBe HttpStatusCode.OK
            }

        }
    }

    @Test
    fun `401 if authcoockie is missing`() {
        withTestApplication({
            mockApi()
        }) {

            handleRequest(HttpMethod.Get, authCheckEndpoint).apply {
                response.status() shouldBe HttpStatusCode.Unauthorized
            }

        }
    }

    @Test
    fun `401 if token has expired`() {
        withTestApplication({
            mockApi()
        }) {
            handleRequest(HttpMethod.Get, authCheckEndpoint) {
            }.apply {
                response.status() shouldBe HttpStatusCode.Unauthorized
            }
        }
    }

}


private fun Application.mockApi(
    corsAllowedOrigins: String = "*.nav.no",
    corsAllowedSchemes: String = "https",
    corsAllowedHeaders: List<String> = emptyList(),
    meldekortService: MeldekortService = mockk(relaxed = true),
    oppfolgingService: OppfolgingService = mockk(relaxed = true),
    oppgaveService: OppgaveService = mockk(relaxed = true),
    beskjedMergerService: BeskjedMergerService = mockk(relaxed = true),
    innboksService: InnboksService = mockk(relaxed = true),
    sakerService: SakerService = mockk(relaxed = true),
    personaliaService: PersonaliaService = mockk(relaxed = true),
    unleashService: UnleashService = mockk(relaxed = true),
    digiSosService: DigiSosService = mockk(relaxed = true),
    doneProducer: DoneProducer = mockk(relaxed = true),
    httpClientIgnoreUnknownKeys: HttpClient = mockk(relaxed = true),

    ) {
    api(
        corsAllowedOrigins = corsAllowedOrigins,
        corsAllowedSchemes = corsAllowedSchemes,
        corsAllowedHeaders = corsAllowedHeaders,
        meldekortService = meldekortService,
        oppfolgingService = oppfolgingService,
        oppgaveService = oppgaveService,
        beskjedMergerService = beskjedMergerService,
        innboksService = innboksService,
        sakerService = sakerService,
        personaliaService = personaliaService,
        unleashService = unleashService,
        digiSosService = digiSosService,
        doneProducer = doneProducer,
        httpClientIgnoreUnknownKeys = httpClientIgnoreUnknownKeys,
        jwtAudience = "audience",
        jwkProvider = jwtStub.stubbedJwkProvider(),
        jwtIssuer = testIssuer

    )
}