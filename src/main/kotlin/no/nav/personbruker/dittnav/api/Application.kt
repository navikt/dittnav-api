package no.nav.personbruker.dittnav.api

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import no.nav.personbruker.dittnav.api.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.config.ApplicationContext
import no.nav.personbruker.dittnav.api.config.api
import no.nav.personbruker.dittnav.api.digisos.DigiSosClient
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingConsumer
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.personalia.PersonaliaConsumer
import no.nav.personbruker.dittnav.api.personalia.PersonaliaService
import no.nav.personbruker.dittnav.api.personalia.PersonaliaTokendings
import no.nav.personbruker.dittnav.api.saker.MineSakerConsumer
import no.nav.personbruker.dittnav.api.saker.MineSakerTokendings
import no.nav.personbruker.dittnav.api.saker.SakerService
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.unleash.UnleashService

fun main() {
    val appContext = ApplicationContext()

    embeddedServer(Netty, port = 8080) {
        api(
            corsAllowedOrigins = appContext.environment.corsAllowedOrigins,
            corsAllowedSchemes = appContext.environment.corsAllowedSchemes,
            corsAllowedHeaders = appContext.environment.corsAllowedHeaders,
            appMicrometerRegistry = appContext.appMicrometerRegistry,
            meldekortService = appContext.meldekortService,
            oppfolgingService = appContext.oppfolgingService,
            oppgaveService = appContext.oppgaveService,
            beskjedMergerService = appContext.beskjedMergerService,
            innboksService = appContext.innboksService,
            sakerService = appContext.sakerService,
            personaliaService = appContext.personaliaService,
            unleashService = appContext.unleashService,
            digiSosService = appContext.digiSosService,
            doneProducer = appContext.doneProducer,
            httpClient = appContext.httpClient,
            httpClientIgnoreUnknownKeys = appContext.httpClientIgnoreUnknownKeys,
        )
    }
}