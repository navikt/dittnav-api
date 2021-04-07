package no.nav.personbruker.dittnav.api.done

import io.ktor.application.*
import io.ktor.client.statement.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser
import no.nav.personbruker.dittnav.api.legacy.logWhenTokenIsAboutToExpire
import org.slf4j.LoggerFactory

fun Route.doneApi(doneProducer: DoneProducer) {

    val log = LoggerFactory.getLogger(DoneProducer::class.java)

    post("/produce/done") {
        log.logWhenTokenIsAboutToExpire(authenticatedUser)
        respondForParameterType<DoneDTO> { doneDto ->
            val response = doneProducer.postDoneEvents(doneDto, authenticatedUser)
            response
        }
    }
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondForParameterType(handler: (T) -> HttpResponse) {
    val postParametersDto: T = call.receive()
    val httpResponse = handler.invoke(postParametersDto)
    call.respond(httpResponse.status)
}
