package no.nav.personbruker.dittnav.api.config

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.client.HttpClient
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.legacy.legacyMeldinger
import no.nav.personbruker.dittnav.api.legacy.legacyPabegynte
import no.nav.personbruker.dittnav.api.legacy.legacyPersoninfo
import no.nav.personbruker.dittnav.api.event.EventConsumer
import no.nav.personbruker.dittnav.api.melding.MeldingService
import no.nav.personbruker.dittnav.api.melding.meldinger
import java.util.concurrent.TimeUnit

object Server {
    private const val portNumber = 8090

    fun configure(client: HttpClient): NettyApplicationEngine {
        DefaultExports.initialize()

        val environment = Environment()
        val legacyConsumer = LegacyConsumer(client, environment)
        val meldingService = MeldingService(EventConsumer(client, environment))


        val app = embeddedServer(Netty, port = portNumber) {
            install(DefaultHeaders)

            install(Authentication) {
                jwt {
                    setupOidcAuthentication(environment)
                }
            }

            install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            routing {
                healthApi()
                authenticate {
                    legacyMeldinger(legacyConsumer)
                    legacyPabegynte(legacyConsumer)
                    legacyPersoninfo(legacyConsumer)
                    meldinger(meldingService)
                }
            }
        }
        addGraceTimeAtShutdownToAllowRunningRequestsToComplete(app)
        return app
    }

    private fun addGraceTimeAtShutdownToAllowRunningRequestsToComplete(app: NettyApplicationEngine) {
        Runtime.getRuntime().addShutdownHook(Thread {
            app.stop(5, 60, TimeUnit.SECONDS)
        })
    }
}
