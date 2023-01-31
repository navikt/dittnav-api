package no.nav.personbruker.dittnav.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.TestApplicationBuilder
import io.mockk.mockk
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.config.api
import no.nav.personbruker.dittnav.api.config.jsonConfig
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.personalia.PersonaliaConsumer
import no.nav.personbruker.dittnav.api.saker.MineSakerConsumer

private const val testIssuer = "test-issuer"
private val jwtStub = JwtStub(testIssuer)
private val stubToken = jwtStub.createTokenFor("subject", "audience")

internal fun TestApplicationBuilder.mockApi(
    corsAllowedOrigins: String = "*.nav.no",
    corsAllowedSchemes: String = "https",
    corsAllowedHeaders: List<String> = emptyList(),
    oppfolgingConsumer: OppfolgingConsumer = mockk(relaxed = true),
    oppgaveConsumer: OppgaveConsumer = mockk(relaxed = true),
    beskjedMergerService: BeskjedMergerService = mockk(relaxed = true),
    innboksConsumer: InnboksConsumer = mockk(relaxed = true),
    sakerConsumer: MineSakerConsumer = mockk(relaxed = true),
    personaliaConsumer: PersonaliaConsumer = mockk(relaxed = true),
    digiSosConsumer: DigiSosConsumer = mockk(relaxed = true),
    doneProducer: DoneProducer = mockk(relaxed = true),
    httpClientIgnoreUnknownKeys: HttpClient = mockk(relaxed = true),

    ) {

    this.application {
        api(
            corsAllowedOrigins = corsAllowedOrigins,
            corsAllowedSchemes = corsAllowedSchemes,
            corsAllowedHeaders = corsAllowedHeaders,
            oppfolgingConsumer = oppfolgingConsumer,
            oppgaveConsumer = oppgaveConsumer,
            beskjedMergerService = beskjedMergerService,
            innboksConsumer = innboksConsumer,
            mineSakerConsumer = sakerConsumer,
            personaliaConsumer = personaliaConsumer,
            digiSosConsumer = digiSosConsumer,
            doneProducer = doneProducer,
            httpClient = httpClientIgnoreUnknownKeys,
            jwtAudience = "audience",
            jwkProvider = jwtStub.stubbedJwkProvider(),
            jwtIssuer = testIssuer
        )
    }
}

internal fun ApplicationTestBuilder.applicationHttpClient() =
    createClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(jsonConfig())
        }
        install(HttpTimeout)
    }

internal suspend fun HttpClient.authenticatedGet(urlString: String, token: String = stubToken): HttpResponse = request {
    url(urlString)
    method = HttpMethod.Get
    header(HttpHeaders.Cookie, "selvbetjening-idtoken=$token")
}

internal suspend fun HttpClient.authenticatedPost(content: String, urlString: String, token: String = stubToken): HttpResponse = request {
    url(urlString)
    method = HttpMethod.Post
    contentType(ContentType.Application.Json)
    setBody(content)
    header(HttpHeaders.Cookie, "selvbetjening-idtoken=$token")
}

internal fun ApplicationTestBuilder.externalServiceWith500Response(testHost: String, route: String){
    externalServices {
        hosts(testHost) {
            routing {
                get(route) {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}

internal fun ApplicationTestBuilder.externalServiceWithJsonResponse(
    hostApiBase: String,
    endpoint: String,
    content: String
) {
    externalServices {
        hosts(hostApiBase) {
            routing {
                get(endpoint) {
                    call.respondRawJson(content)
                }
            }
        }
    }
}

internal suspend fun ApplicationCall.respondRawJson(content: String) =
    respondBytes(
        contentType = ContentType.Application.Json,
        provider = { content.toByteArray() })


internal inline fun <T> T.assert(block: T.() -> Unit): T =
    apply {
        block()
    }
