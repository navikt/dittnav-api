package no.nav.personbruker.dittnav.api.oppfolging

import no.nav.personbruker.dittnav.api.oppfolging.external.OppfolgingExternal

object OppfolgingTransformer {
    fun toInternal(oppfolging: OppfolgingExternal): Oppfolging {
        return Oppfolging(oppfolging.underOppfolging)
    }
}
