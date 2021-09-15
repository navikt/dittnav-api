package no.nav.personbruker.dittnav.api.config

import io.ktor.client.features.json.serializer.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import no.finn.unleash.DefaultUnleash
import no.finn.unleash.FakeUnleash
import no.finn.unleash.Unleash
import no.finn.unleash.util.UnleashConfig
import no.nav.personbruker.dittnav.api.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.digisos.DigiSosClient
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.health.DependencyPinger
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.loginstatus.InnloggingsstatusConsumer
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveMergerService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.unleash.ByEnvironmentStrategy
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.api.varsel.VarselConsumer
import no.nav.personbruker.dittnav.api.varsel.VarselService
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

class ApplicationContext {

    val environment = Environment()
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    val httpClient = HttpClientBuilder.build(KotlinxSerializer(json()))
    val httpClientIgnoreUnknownKeys = HttpClientBuilder.build(KotlinxSerializer(json(ignoreUnknownKeys = true)))
    val dependencyPinger = DependencyPinger(environment, httpClient)

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()
    val eventhandlerTokendings = EventhandlerTokendings(tokendingsService, environment.eventhandlerClientId)

    val legacyConsumer = LegacyConsumer(httpClient, environment.legacyApiURL)
    val oppgaveConsumer = OppgaveConsumer(httpClient, environment.eventHandlerURL)
    val beskjedConsumer = BeskjedConsumer(httpClient, environment.eventHandlerURL)
    val innboksConsumer = InnboksConsumer(httpClient, environment.eventHandlerURL)
    val varselConsumer = VarselConsumer(httpClient, environment.legacyApiURL)

    val doneProducer = DoneProducer(httpClient, eventhandlerTokendings, environment.eventHandlerURL)

    val innloggingsstatusConsumer = InnloggingsstatusConsumer(httpClientIgnoreUnknownKeys, environment.innloggingsstatusUrl)
    val loginLevelService = LoginLevelService(innloggingsstatusConsumer)

    val unleashService = createUnleashService(environment)

    val oppgaveService = OppgaveService(oppgaveConsumer, eventhandlerTokendings, loginLevelService)
    val beskjedService = BeskjedService(beskjedConsumer, eventhandlerTokendings, loginLevelService)
    val innboksService = InnboksService(innboksConsumer, eventhandlerTokendings, loginLevelService)
    val varselService = VarselService(varselConsumer)

    val digiSosConsumer = DigiSosClient(httpClient, environment.digiSosSoknadBaseURL, environment.digiSosInnsynBaseURL)
    val digiSosService = DigiSosService(digiSosConsumer)

    val beskjedMergerService = BeskjedMergerService(beskjedService, varselService, digiSosService, unleashService)
    val oppgaveMergerService = OppgaveMergerService(oppgaveService, digiSosService, unleashService)

    private fun createUnleashService(environment: Environment): UnleashService {

        val unleashClient = if (environment.unleashApiUrl == "fake") {
            createFakeUnleashClient(environment)
        } else {
            createUnleashClient(environment)
        }

        return UnleashService(unleashClient)
    }

    private fun createUnleashClient(environment: Environment): Unleash {
        val unleashUrl = environment.unleashApiUrl

        val appName = "dittnav-api"
        val envContext = if (environment.isRunningInDev) "dev" else "prod"

        val byEnvironment = ByEnvironmentStrategy(envContext)

        val config = UnleashConfig.builder()
            .appName(appName)
            .unleashAPI(unleashUrl)
            .build()

        return DefaultUnleash(config, byEnvironment)
    }

    private fun createFakeUnleashClient(environment: Environment): Unleash {
        return FakeUnleash().apply {
            if (environment.fakeUnleashIncludeVarsel) {
                enable(UnleashService.varselinnboksToggleName)
            } else {
                disable(UnleashService.varselinnboksToggleName)
            }
            if (environment.fakeUnleashIncludeDigiSos) {
                enable(UnleashService.digiSosToggleName)
            } else {
                disable(UnleashService.digiSosToggleName)
            }
        }
    }

}
