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
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    implementation(DittNAVCommonLib.utils)
    implementation(Kotlinx.coroutines)
    implementation(Kotlinx.htmlJvm)
    implementation(Ktor2.Server.core)
    implementation(Ktor2.Server.netty)
    implementation(Ktor2.Server.auth)
    implementation(Ktor2.Server.authJwt)
    implementation(Ktor2.Server.defaultHeaders)
    implementation(Ktor2.Server.statusPages)
    implementation(Ktor2.Server.cors)
    implementation(Ktor2.Server.contentNegotiation)
    implementation(Ktor2.Client.apache)
    implementation(Ktor2.Client.core)
    implementation(Ktor2.Client.contentNegotiation)
    implementation(Ktor2.Serialization.kotlinX)
    implementation(Ktor2.Server.htmlDsl)
    implementation(Ktor2.Server.metricsMicrometer)
    implementation(Micrometer.registryPrometheus)

    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.logback)
    implementation(TmsKtorTokenSupport.tokendingsExchange)
    implementation(Unleash.clientJava)
    implementation(KotlinLogging.logging)


    testImplementation(Junit.api)
    testImplementation(Ktor2.Test.clientMock)
    testImplementation(Ktor2.Test.serverTestHost)
    testImplementation(NAV.tokenValidatorKtor)
    testImplementation(Kotest.runnerJunit5)
    testImplementation(Kotest.assertionsCore)
    testImplementation(Kotest.assertionsCore)
    testImplementation(Kotest.extensions)
    testImplementation(Mockk.mockk)
    testImplementation(Jjwt.api)
    testImplementation(Junit.params)

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
}

// TODO: Fjern følgende work around i ny versjon av Shadow-pluginet:
// Skal være løst i denne: https://github.com/johnrengelman/shadow/pull/612
project.setProperty("mainClassName", application.mainClass.get())
apply(plugin = Shadow.pluginId)
