package no.nav.personbruker.dittnav.api.mininnboks

import no.nav.personbruker.dittnav.api.mininnboks.UbehandledeMeldingerType.*
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletDokument
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletOppgave
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletOppgaveWithUndertype
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletOppgaveWithVarselId
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletOppgaveWithVarselIdAndUndertype
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletUbesvartMelding
import no.nav.personbruker.dittnav.api.mininnboks.UbehandletMeldingExternalObjectMother.createUbehandletUlestMelding
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should not contain`
import org.junit.jupiter.api.Test

internal class UbehandledeMeldingerTransformerTest {

    private val mininnboksUrl = "http://mininnboks"
    private val innloggingsinfoUrl = "http://innloggingsinfo"

    private val transformer = UbehandledeMeldingerTransformer(mininnboksUrl, innloggingsinfoUrl)

    @Test
    fun `should handle external with type OPPGAVE_VARSEL`() {
        val external = listOf(createUbehandletOppgave())

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` OPPGAVE_VARSEL
        result.antall `should be equal to` 1
        result.url `should be equal to` "$innloggingsinfoUrl/type/oppgave"
    }

    @Test
    fun `should handle external with type DOKUMENT_VARSEL`() {
        val external = listOf(createUbehandletDokument())

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` DOKUMENT_VARSEL
        result.antall `should be equal to` 1
        result.url `should be equal to` "$innloggingsinfoUrl/type/dokument"
    }

    @Test
    fun `should handle external with type ULEST`() {
        val external = listOf(createUbehandletUlestMelding())

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` ULEST
        result.antall `should be equal to` 1
        result.url `should be equal to` mininnboksUrl
    }

    @Test
    fun `should handle external with type UBESVART`() {
        val external = listOf(createUbehandletUbesvartMelding())

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` UBESVART
        result.antall `should be equal to` 1
        result.url `should be equal to` mininnboksUrl
    }

    @Test
    fun `should create correct path to innloggingsinfo for single result with varselId`() {
        val varselId = "123"

        val external = listOf(createUbehandletOppgaveWithVarselId(varselId))

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` OPPGAVE_VARSEL
        result.antall `should be equal to` 1
        result.url `should be equal to` "$innloggingsinfoUrl/type/oppgave/varselid/$varselId"
    }

    @Test
    fun `should create correct path to innloggingsinfo for single result with undertype`() {
        val underype = "utyp1"

        val external = listOf(createUbehandletOppgaveWithUndertype(underype))

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` OPPGAVE_VARSEL
        result.antall `should be equal to` 1
        result.url `should be equal to` "$innloggingsinfoUrl/type/oppgave/undertype/$underype"
    }

    @Test
    fun `should create correct path to innloggingsinfo for single result with varselId and underype`() {
        val varselId = "123"
        val underype = "utyp1"

        val external = listOf(createUbehandletOppgaveWithVarselIdAndUndertype(varselId, underype))

        val result = transformer.toInternal(external).first()

        result.type `should be equal to` OPPGAVE_VARSEL
        result.antall `should be equal to` 1
        result.url `should be equal to` "$innloggingsinfoUrl/type/oppgave/undertype/$underype/varselid/$varselId"
    }

     @Test
     fun `should group external by type`() {
         val external = listOf(
             createUbehandletOppgave(),
             createUbehandletDokument(),
             createUbehandletDokument(),
             createUbehandletUlestMelding(),
             createUbehandletUlestMelding(),
             createUbehandletUlestMelding(),
             createUbehandletUbesvartMelding(),
             createUbehandletUbesvartMelding(),
             createUbehandletUbesvartMelding(),
             createUbehandletUbesvartMelding()
         )

         val internal = transformer.toInternal(external)

         val internalAsMap = internal.associate { it.type to it.antall }

         internalAsMap[OPPGAVE_VARSEL] `should be equal to` 1
         internalAsMap[DOKUMENT_VARSEL] `should be equal to` 2
         internalAsMap[ULEST] `should be equal to` 3
         internalAsMap[UBESVART] `should be equal to` 4
     }

    @Test
    fun `should include undertype if there is only one distinct for type`() {
        val undertype = "utyp1"

        val external = listOf(
            createUbehandletOppgaveWithUndertype(undertype),
            createUbehandletOppgaveWithUndertype(undertype)
        )

        val internal = transformer.toInternal(external).first()

        internal.type `should be equal to` OPPGAVE_VARSEL
        internal.antall `should be equal to` 2
        internal.url `should contain` undertype
    }

    @Test
    fun `should not include undertype if there are multiple distinct for type`() {
        val undertype1 = "utyp1"
        val undertype2 = "utyp2"

        val external = listOf(
            createUbehandletOppgaveWithUndertype(undertype1),
            createUbehandletOppgaveWithUndertype(undertype2)
        )

        val internal = transformer.toInternal(external).first()

        internal.type `should be equal to` OPPGAVE_VARSEL
        internal.antall `should be equal to` 2
        internal.url `should not contain` undertype1
        internal.url `should not contain` undertype2
    }
}
