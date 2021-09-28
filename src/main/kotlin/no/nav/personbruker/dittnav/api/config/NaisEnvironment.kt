package no.nav.personbruker.dittnav.api.config

object NaisEnvironment {

    fun isRunningInProd(): Boolean {
        val currentEnvironment = System.getenv("NAIS_CLUSTER_NAME")
        return currentEnvironment == "prod-sbs" || currentEnvironment == "prod-gcp"
    }

    fun isRunningInDev(): Boolean {
        return !isRunningInProd()
    }

}
