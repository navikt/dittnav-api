package no.nav.personbruker.dittnav.api.config

object NaisEnvironment {

    fun currentClusterName() : String? = System.getenv("NAIS_CLUSTER_NAME")

    fun isRunningInProd(): Boolean {
        val clusterName = currentClusterName()
        return clusterName == "prod-sbs" || clusterName == "prod-gcp"
    }

    fun isRunningInDev(): Boolean {
        return !isRunningInProd()
    }

}
