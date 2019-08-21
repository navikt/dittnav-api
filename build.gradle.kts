import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val prometheusVersion = "0.6.0"
val ktorVersion = "1.2.2"
val junitVersion = "5.4.1"
val kafkaVersion = "2.2.0"
val confluentVersion = "5.2.0"
val dittNavSkjemaVersion = "1.2019.06.24-11.58-2bb2160a8909"
val logstashVersion = 5.2
val logbackVersion = "1.2.3"
val vaultJdbcVersion = "1.3.1"
val flywayVersion = "5.2.4"
val exposedVersion = "0.14.1"
val hikariCPVersion = "3.2.0"
val postgresVersion = "42.2.5"
val jacksonVersion = "2.9.9"

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
    compile("no.nav:vault-jdbc:$vaultJdbcVersion")
    compile("com.zaxxer:HikariCP:$hikariCPVersion")
    compile("org.postgresql:postgresql:$postgresVersion")
    compile("org.jetbrains.exposed:exposed:$exposedVersion")
    compile("org.flywaydb:flyway-core:$flywayVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    compile("io.prometheus:simpleclient_common:$prometheusVersion")
    compile("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-auth:$ktorVersion")
    compile("io.ktor:ktor-auth-jwt:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("org.apache.kafka:kafka-clients:$kafkaVersion")
    compile("io.confluent:kafka-avro-serializer:$confluentVersion")
    compile("no.nav.personbruker.dittnav:event-schemas:$dittNavSkjemaVersion")
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile("org.assertj:assertj-core:3.12.1")
    testCompile(kotlin("test-junit5"))
    testImplementation("no.nav:kafka-embedded-env:2.1.1")
    testImplementation("org.apache.kafka:kafka_2.12:$kafkaVersion")
    testImplementation("org.apache.kafka:kafka-streams:$kafkaVersion")
    testImplementation("io.confluent:kafka-schema-registry:$confluentVersion")
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
