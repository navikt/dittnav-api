import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val prometheusVersion = "0.6.0"
val ktorVersion = "1.2.2"
val logstashVersion = 5.2
val logbackVersion = "1.2.3"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version("1.3.31")
    kotlin("plugin.allopen").version("1.3.31")

    id("org.flywaydb.flyway") version("5.2.4")

    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    maven("http://packages.confluent.io/maven")
    mavenLocal()
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("io.prometheus:simpleclient_common:$prometheusVersion")
    compile("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-auth:$ktorVersion")
    compile("io.ktor:ktor-auth-jwt:$ktorVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
}

application {
    mainClassName = "no.nav.personbruker.dittnav.api.AppKt"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }

    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.register("runServer", JavaExec::class) {
    main = application.mainClassName
    classpath = sourceSets["main"].runtimeClasspath
}
