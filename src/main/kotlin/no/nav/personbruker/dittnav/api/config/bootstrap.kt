package no.nav.personbruker.dittnav.api.config

import com.auth0.jwk.JwkProvider
import io.ktor.client.HttpClient
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.hotspot.DefaultExports
import kotlinx.serialization.json.Json
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserFactory
import no.nav.personbruker.dittnav.api.authentication.PrincipalWithTokenString
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.beskjed.beskjed
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer
import no.nav.personbruker.dittnav.api.digisos.digiSos
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.done.doneApi
import no.nav.personbruker.dittnav.api.health.authenticationCheck
import no.nav.personbruker.dittnav.api.health.healthApi
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.innboks
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingConsumer
import no.nav.personbruker.dittnav.api.oppfolging.oppfolgingApi
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.oppgave
import no.nav.personbruker.dittnav.api.personalia.PersonaliaConsumer
import no.nav.personbruker.dittnav.api.personalia.personalia
import no.nav.personbruker.dittnav.api.saker.MineSakerConsumer
import no.nav.personbruker.dittnav.api.saker.saker
import no.nav.personbruker.dittnav.api.varsel.varsel

fun Application.api(
    corsAllowedOrigins: String,
    corsAllowedSchemes: String,
    corsAllowedHeaders: List<String>,
    oppfolgingConsumer: OppfolgingConsumer,
    oppgaveConsumer: OppgaveConsumer,
    beskjedMergerService: BeskjedMergerService,
    innboksConsumer: InnboksConsumer,
    mineSakerConsumer: MineSakerConsumer,
    personaliaConsumer: PersonaliaConsumer,
    digiSosConsumer: DigiSosConsumer,
    doneProducer: DoneProducer,
    httpClient: HttpClient,
    jwtAudience: String,
    jwkProvider: JwkProvider,
    jwtIssuer: String
) {

    DefaultExports.initialize()
    val collectorRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(DefaultHeaders)
    installStatusPages()

    install(CORS) {
        allowHost(corsAllowedOrigins, schemes = listOf(corsAllowedSchemes))
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        corsAllowedHeaders.forEach { approvedHeader ->
            allowHeader(approvedHeader)
        }
    }



    install(Authentication) {
        jwt {
            verifier(jwkProvider, jwtIssuer) {
                withAudience(jwtAudience)
            }

            authHeader {
                val cookie = it.request.cookies["selvbetjening-idtoken"] ?: throw CookieNotSetException()
                HttpAuthHeader.Single("Bearer", cookie)
            }

            validate { credentials ->
                requireNotNull(credentials.payload.claims["pid"]) {
                    "Token må inneholde fødselsnummer for personen i pid claim"
                }
                PrincipalWithTokenString(
                    accessToken = request.cookies["selvbetjening-idtoken"] ?: throw CookieNotSetException(),
                    payload = credentials.payload
                )
            }
        }
    }

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    install(MicrometerMetrics) {
        registry = collectorRegistry
    }

    routing {
        route("/dittnav-api") {
            healthApi(collectorRegistry)
            authenticate {
                oppfolgingApi(oppfolgingConsumer)
                oppgave(oppgaveConsumer)
                beskjed(beskjedMergerService)
                innboks(innboksConsumer)
                saker(mineSakerConsumer)
                personalia(personaliaConsumer)
                digiSos(digiSosConsumer)
                authenticationCheck()
                doneApi(doneProducer)
                varsel(beskjedMergerService, oppgaveConsumer, innboksConsumer)
            }
        }
    }
    configureShutdownHook(httpClient)
}

class CookieNotSetException : Throwable() {}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

val PipelineContext<Unit, ApplicationCall>.authenticatedUser: AuthenticatedUser
    get() = AuthenticatedUserFactory.createNewAuthenticatedUser(call)

fun jsonConfig(ignoreUnknownKeys: Boolean = true): Json {
    return Json {
        this.ignoreUnknownKeys = ignoreUnknownKeys
    }
}
