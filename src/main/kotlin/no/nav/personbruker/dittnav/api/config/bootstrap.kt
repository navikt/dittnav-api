package no.nav.personbruker.dittnav.api.config

import com.auth0.jwk.JwkProvider
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.pipeline.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUserFactory
import no.nav.personbruker.dittnav.api.authentication.PrincipalWithTokenString
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.beskjed.beskjed
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.digisos.digiSos
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.done.doneApi
import no.nav.personbruker.dittnav.api.health.authenticationCheck
import no.nav.personbruker.dittnav.api.health.healthApi
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.innboks.innboks
import no.nav.personbruker.dittnav.api.meldekort.MeldekortService
import no.nav.personbruker.dittnav.api.meldekort.meldekortApi
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingService
import no.nav.personbruker.dittnav.api.oppfolging.oppfolgingApi
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.oppgave.oppgave
import no.nav.personbruker.dittnav.api.personalia.PersonaliaService
import no.nav.personbruker.dittnav.api.personalia.personalia
import no.nav.personbruker.dittnav.api.saker.SakerService
import no.nav.personbruker.dittnav.api.saker.saker
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.api.unleash.unleash


val log = KotlinLogging.logger { }
fun Application.api(
    corsAllowedOrigins: String,
    corsAllowedSchemes: String,
    corsAllowedHeaders: List<String>,
    meldekortService: MeldekortService,
    oppfolgingService: OppfolgingService,
    oppgaveService: OppgaveService,
    beskjedMergerService: BeskjedMergerService,
    innboksService: InnboksService,
    sakerService: SakerService,
    personaliaService: PersonaliaService,
    unleashService: UnleashService,
    digiSosService: DigiSosService,
    doneProducer: DoneProducer,
    httpClientIgnoreUnknownKeys: HttpClient,
    jwtAudience: String,
    jwkProvider: JwkProvider,
    jwtIssuer: String
) {

    val collectorRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(DefaultHeaders)

    install(StatusPages) {
        exception<CookieNotSetException> {
            log.info("401: fant ikke selvbetjening-idtoken")
            call.respond(HttpStatusCode.Unauthorized)
        }
    }

    install(CORS) {
        host(corsAllowedOrigins, schemes = listOf(corsAllowedSchemes))
        allowCredentials = true
        header(HttpHeaders.ContentType)
        corsAllowedHeaders.forEach { approvedHeader ->
            header(approvedHeader)
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
        json(no.nav.personbruker.dittnav.api.config.json())
    }

    install(MicrometerMetrics) {
        registry = collectorRegistry
    }

    routing {
        healthApi(collectorRegistry)
        authenticate {

            meldekortApi(meldekortService)
            oppfolgingApi(oppfolgingService)
            oppgave(oppgaveService)
            beskjed(beskjedMergerService)
            innboks(innboksService)
            saker(sakerService)
            personalia(personaliaService)
            unleash(unleashService)
            if (isRunningInDev()) {
                digiSos(digiSosService)
            }
            authenticationCheck()
            doneApi(doneProducer)
        }

        configureShutdownHook(httpClientIgnoreUnknownKeys)
    }
}

class CookieNotSetException : Throwable() {}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

private fun isRunningInDev(clusterName: String? = System.getenv("NAIS_CLUSTER_NAME")): Boolean {
    var runningInDev = true
    if (clusterName != null && clusterName == "prod-sbs") {
        runningInDev = false
    }
    return runningInDev
}

val PipelineContext<Unit, ApplicationCall>.authenticatedUser: AuthenticatedUser
    get() = AuthenticatedUserFactory.createNewAuthenticatedUser(call)
