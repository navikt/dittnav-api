package no.nav.personbruker.dittnav.api

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import no.nav.personbruker.dittnav.api.config.ApplicationContext
import no.nav.personbruker.dittnav.api.config.LoginserviceMetadata
import no.nav.personbruker.dittnav.api.config.api
import java.net.URL
import java.util.concurrent.TimeUnit

fun main() {
    val appContext = ApplicationContext()
    val loginserviceMetadata =
        LoginserviceMetadata.get(appContext.httpClient, appContext.environment.loginservicDiscoveryUrl)
    val jwkProvider = JwkProviderBuilder(URL(loginserviceMetadata.jwks_uri))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    embeddedServer(Netty, port = 8080) {
        api(
            corsAllowedOrigins = appContext.environment.corsAllowedOrigins,
            corsAllowedSchemes = appContext.environment.corsAllowedSchemes,
            corsAllowedHeaders = appContext.environment.corsAllowedHeaders,
            oppfolgingConsumer = appContext.oppfolgingConsumer,
            oppgaveConsumer = appContext.oppgaveConsumer,
            beskjedMergerService = appContext.beskjedMergerService,
            innboksConsumer = appContext.innboksConsumer,
            mineSakerConsumer = appContext.mineSakerConsumer,
            personaliaConsumer = appContext.personaliaConsumer,
            digiSosConsumer = appContext.digiSosConsumer,
            doneProducer = appContext.doneProducer,
            httpClient = appContext.httpClient,
            jwtAudience = appContext.environment.loginserviceIdportenAudience,
            jwkProvider = jwkProvider,
            jwtIssuer = loginserviceMetadata.issuer,
        )

    }.start(wait = true)
}
