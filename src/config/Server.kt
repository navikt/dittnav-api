package no.nav.personbruker.dittnav.proxy

import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import no.nav.personbruker.dittnav.proxy.api.healthApi
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object Server {
    const val portNumber = 8090

    val log = LoggerFactory.getLogger(Server::class.java)

    fun configure(): NettyApplicationEngine {
        val app = embeddedServer(Netty, port = portNumber) {
            install(DefaultHeaders)

            routing {
                healthApi()
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
