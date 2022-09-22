package no.nav.personbruker.dittnav.api

import io.ktor.application.Application
import io.ktor.client.HttpClient
import io.ktor.server.testing.withTestApplication
import io.micrometer.prometheus.PrometheusMeterRegistry
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

class TestApplication {
    @Test
    fun `ny startmekanisme-test`() {
        withTestApplication({
            mockApi()
        }) {

        }
    }
}

private fun Application.mockApi(
    corsAllowedOrigins: String = "",
    corsAllowedSchemes: String = "",
    corsAllowedHeaders: List<String> = emptyList(),
    appMicrometerRegistry: PrometheusMeterRegistry = mockk(relaxed = true),
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
    httpClient: HttpClient = mockk(relaxed = true),
    httpClientIgnoreUnknownKeys: HttpClient = mockk(relaxed = true)
) {
    api(
        corsAllowedOrigins = corsAllowedOrigins,
        corsAllowedSchemes = corsAllowedSchemes,
        corsAllowedHeaders = corsAllowedHeaders,
        appMicrometerRegistry = appMicrometerRegistry,
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
        httpClient = httpClient,
        httpClientIgnoreUnknownKeys = httpClientIgnoreUnknownKeys
    )
}