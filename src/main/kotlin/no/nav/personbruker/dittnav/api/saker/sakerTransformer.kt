package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.saker.legacy.LegacySakstemaDTO

fun toSakerDTO(inbound: Saker): SakerDTO =
        inbound.let {
            SakerDTO(
                    navn = it.navn,
                    kode = it.kode,
                    sistEndret = it.sistEndret
            )
        }

fun toSakerDTOFromLegacy(inbound: LegacySakstemaDTO): SakerDTO =
        inbound.let {
            SakerDTO(
                    navn = it.temanavn,
                    kode = it.temakode,
                    sistEndret = it.sisteOppdatering
            )
        }
