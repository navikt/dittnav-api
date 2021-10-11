package no.nav.personbruker.dittnav.api.config

import io.kotest.extensions.system.withEnvironment
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class NaisEnvironmentTest {

    @Test
    fun `Skal skunne identifisere at appen kjorer i prod-klusteret onprem`() {
        withEnvironment("NAIS_CLUSTER_NAME" to "prod-sbs") {
            NaisEnvironment.isRunningInProd() `should be equal to` true
            NaisEnvironment.isRunningInDev() `should be equal to` false
        }
    }

    @Test
    fun `Skal skunne identifisere at appen kjorer i prod-klusteret paa GCP`() {
        withEnvironment("NAIS_CLUSTER_NAME" to "prod-gcp") {
            NaisEnvironment.isRunningInProd() `should be equal to` true
            NaisEnvironment.isRunningInDev() `should be equal to` false
        }
    }

    @Test
    fun `Skal skunne identifisere at appen kjorer i et dev-kluster`() {
        withEnvironment("NAIS_CLUSTER_NAME" to "dev-sbs") {
            NaisEnvironment.isRunningInProd() `should be equal to` false
            NaisEnvironment.isRunningInDev() `should be equal to` true
        }
    }

    @Test
    fun `Kjorende miljo skal identifiseres som dev hvis NAIS_CLUSTER_NAME er satt til null`() {
        withEnvironment("NAIS_CLUSTER_NAME" to null) {
            NaisEnvironment.isRunningInProd() `should be equal to` false
            NaisEnvironment.isRunningInDev() `should be equal to` true
        }
    }

    @Test
    fun `Kjorende miljo skal identifiseres som dev hvis NAIS_CLUSTER_NAME har ugyldig verdi`() {
        withEnvironment("NAIS_CLUSTER_NAME" to null) {
            NaisEnvironment.isRunningInProd() `should be equal to` false
            NaisEnvironment.isRunningInDev() `should be equal to` true
        }
    }

    @Test
    fun `Hvis appen ikke kjores i et prod-kluster, saa skal det identifiseres som at appen kjorer i dev`() {
        NaisEnvironment.isRunningInProd() `should be equal to` false
        NaisEnvironment.isRunningInDev() `should be equal to` true
    }

}
