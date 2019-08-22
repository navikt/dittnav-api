package no.nav.personbruker.dittnav.api.config

import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.DefaultHeaders
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.api.healthApi
import no.nav.personbruker.dittnav.api.api.test
import java.util.concurrent.TimeUnit

object Server {
    const val portNumber = 8090

    fun configure(environment: Environment): NettyApplicationEngine {
        DefaultExports.initialize()
        val app = embeddedServer(Netty, port = portNumber) {
            install(DefaultHeaders)

            install(Authentication) {
                jwt {
                    setupOidcAuthentication(environment)
                }
            }

            routing {
                healthApi()
                authenticate {
                    test()
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
