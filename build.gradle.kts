import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)
    kotlin("plugin.serialization").version(Kotlin.version)

    id(Shadow.pluginId) version (Shadow.version)
    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    implementation(DittNAV.Common.utils)
    implementation(Kotlinx.coroutines)
    implementation(Kotlinx.htmlJvm)
    implementation(Ktor.auth)
    implementation(Ktor.authJwt)
    implementation(Ktor.clientApache)
    implementation(Ktor.clientJson)
    implementation(Ktor.clientLogging)
    implementation(Ktor.clientLoggingJvm)
    implementation(Ktor.clientSerializationJvm)
    implementation(Ktor.htmlBuilder)
    implementation(Ktor.metricsMicrometer)
    implementation(Micrometer.registryPrometheus)
    implementation(Ktor.serialization)
    implementation(Ktor.serverNetty)
    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.logback)
    implementation("com.github.navikt.tms-ktor-token-support:token-support-tokendings-exchange:2022.01.27-13.11-a6b55dd90347")
    implementation(Unleash.clientJava)
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")
    implementation("org.slf4j:slf4j-api:2.0.3")



    testImplementation(Junit.api)
    testImplementation(Ktor.clientMock)
    testImplementation(Ktor.clientMockJvm)
    testImplementation(Ktor.serverTestHost)
    testImplementation(NAV.tokenValidatorKtor)
    testImplementation(Kotest.runnerJunit5)
    testImplementation(Kotest.assertionsCore)
    testImplementation(Kotest.assertionsCore)
    testImplementation(Kotest.extensions)
    testImplementation(Mockk.mockk)
    testImplementation(Jjwt.api)

    testRuntimeOnly(Bouncycastle.bcprovJdk15on)
    testRuntimeOnly(Jjwt.impl)
    testRuntimeOnly(Jjwt.orgjson)
    testRuntimeOnly(Junit.engine)
}

application {
    mainClass.set("no.nav.personbruker.dittnav.api.ApplicationKt")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        println("Setting default environment variables for running with DittNAV docker-compose")
        DockerComposeDefaults.environomentVariables.forEach { (name, value) ->
            println("Setting the environment variable $name")
            environment(name, value)
        }
        environment("UNLEASH_API_URL", "fake")
        environment("DIGISOS_API_URL", "https://digisos/dummy/soknad")
        environment("DIGISOS_INNSYN_API_URL", "https://digisos/dummy/innsyn")
        environment("MINE_SAKER_API_URL", "http://localhost:8095/mine-saker-api")
        environment("FAKE_UNLEASH_INCLUDE_MINE_SAKER", false)
        environment("MINE_SAKER_API_CLIENT_ID", "mine-saker-dummy-client-id")
        environment("TOKEN_X_WELL_KNOWN_URL", "http://localhost:9000/.well-known/openid-configuration")
        environment("TOKEN_X_CLIENT_ID", "dummy-tokenX-client-id")
        val dummyJwks =
            """{"p":"-xwJW4TQT_KrMVnWR39Mpxf1WSnSaI67E2YXnCdtNoz0l_xc7nf8skc7vD-FbwOm-sEmdQk4jLKWCoTS8IHV0qUTD_Ppix4j88d2uBMMpPyfMXPmvYxUCw-nF4kI50DqweLfwtcpuyr1byMXAHvxuTuWrYnyTJ3AVDOGIaTnRB0","kty":"RSA","q":"ll0WRWtKFB0Hul9DG7ptHZ_VyaIGrPZ1-XEbaHrrbJVECsD5OfulhuINnGAt8OGWNuEmqcBOntC1rdZjOs0-3-yuGsHPbuRzM-mVM1weoUTV98KQjB_sdiE3Ih-CsKwyDmBpgrASApNN-r6LsisNiwqNgRqThsE5nHJTJvSbjJ0","d":"VuV5r7QkDt3Zoiyp5wenXcY3ptNFQdLYCKn-lfYm9TQ4o5ZvjpeXOxE0TEX-TFT67Vs0MpDgsVl7TbmTlqjSvJWxwpy_sSANWlIPImlQWaPsqTSAROQf_NpmZpk_4cvmCg0SM6BWv92g7TKtyX2yV5JdtSCOQU1wV0JD-CZ_bhT7CwwQ9ObEC--JM9ptL1Jy4ZQip7QkvNrfh-TjeIn9f9TW7kETh8GmlmovmYJFHXTiCOpdes1Io3IFYqIFoGtbfTXV1lCavfnHktHYnOrK9Tj5JNAf4Rp_aEymi-Y7sFccM9anWNHukVSc9rrtPIFEt5blAxlNeuYdqwAx0GuBQQ","e":"AQAB","use":"sig","kid":"KID","qi":"muEsRyWwBSl0CPBxmZsLXz7NpSsIgf9gWXMmfeMFGPQvcViL-3Wrv9IPveJy5ihJSzsZGOzPmiaquewntUVXAvm5dOLiIal4MZUtZPAhrhCRxCRspYqLp4u7Fb2aMhMo2CoCBFC5T4MfU250u_tAOZhlrfPzNch_igLLQqoRrGQ","dp":"HlSonLFSKBX7r55WT5SEwboXHIn8rDxxREqUl3v7qRclhCYrY3KCx1XrVTWm_F3IkYk7B-_xMK1xihu5Duvf0-20e7zOfMtLNGrnYByM7nDFGcgSGtsUW7GsUR9wP96LfJfWx0YN-FmcA6yNXrWZ4PHdpWCAL9juHj2K-g1dEdE","alg":"RS256","dq":"JdOfMbGO_kZbVlh2wngA0U4Pc10ufr616R26PmuF5FgcuPPY_uw-tRMTR36usAWgS4gSuOunG673tZbUect-gMjC9_o_2-7eyHV_0l7fWcS-a0joIkg5rXIns47nythW82Tvxi_TKBC0slrTO-w2yP7LoGn2KRVdD-1227r3ksU","n":"k328g4arE1iN78Ig9m--5vmymCO5K_HZul8LKiwOZW88-ALikb_ponB-pN12Dpudrasy0xTyMp10f4qu4EjVeImTey07eIho-57JUX_s7M0Yq9vjoe9uQE0JULLlQuHzky53FJ-CMHR7canGo0giTJGUAZRnOqkoNZaTrkfrjodYB8vuRwIT_PJCOmIIkHiR2i8KDUP2rxCDKnL7Ed-jiyaeyDW8TsB4z3Dmt6Jke03KzOU6061UMySHWeh-OjdHgseGf1iAwAcv1lfsRlLr3_E2PvzE0IVayBZBPy98Y4R8G2svg1EcyXMPJrhWkfuxzu4jTiKXOXKO0nEQfKOhyQ"}"""
        environment("TOKEN_X_PRIVATE_JWK", dummyJwks)

        main = application.mainClass.get()
        classpath = sourceSets["main"].runtimeClasspath
    }
}

// TODO: Fjern følgende work around i ny versjon av Shadow-pluginet:
// Skal være løst i denne: https://github.com/johnrengelman/shadow/pull/612
project.setProperty("mainClassName", application.mainClass.get())
apply(plugin = Shadow.pluginId)
