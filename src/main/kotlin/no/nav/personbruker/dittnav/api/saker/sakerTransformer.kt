package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.legacy.LegacySakstema
import java.net.URL

fun toSakerDTO(inbound: Saker): SakerDTO =
        inbound.let {
            SakerDTO(
                    navn = it.navn,
                    kode = it.kode,
                    sistEndret = it.sistEndret,
                    detaljvisningUrl = URL("https://dummy/mine-saker").toString()
            )
        }

fun toSakerDTOFromLegacy(inbound: LegacySakstema): SakerDTO =
        inbound.let {
            SakerDTO(
                    navn = it.temanavn,
                    kode = it.temakode,
                    sistEndret = it.sisteOppdatering,
                    detaljvisningUrl = URL("https://dummy/legacy").toString()
            )
        }
