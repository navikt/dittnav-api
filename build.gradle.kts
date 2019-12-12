import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val prometheusVersion = "0.6.0"
val ktorVersion = "1.2.6"
val logstashVersion = 5.2
val logbackVersion = "1.2.3"
val kotlinVersion = "1.3.50"
val jacksonVersion = "2.9.9"
val spekVersion = "2.0.6"
val mockKVersion = "1.9.3"
val assertJVersion = "3.12.2"
val junitVersion = "5.4.1"
val kluentVersion = "1.56"
val tokensupportVersion = "1.1.0"
val kotlinxCoroutinesVersion = "1.3.2"
val kotlinxHtmlVersion = "0.6.12"


plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    val kotlinVersion = "1.3.50"
    kotlin("jvm").version(kotlinVersion)
    kotlin("plugin.allopen").version(kotlinVersion)

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    compile("no.nav.security:token-validation-ktor:$tokensupportVersion")
    compile("io.prometheus:simpleclient_common:$prometheusVersion")
    compile("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    compile("io.prometheus:simpleclient_logback:$prometheusVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-auth:$ktorVersion")
    compile("io.ktor:ktor-auth-jwt:$ktorVersion")
    compile("io.ktor:ktor-client-apache:$ktorVersion")
    compile("io.ktor:ktor-client-json:$ktorVersion")
    compile("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    compile("io.ktor:ktor-client-jackson:$ktorVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    compile("io.ktor:ktor-client-logging:$ktorVersion")
    compile("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:${kotlinxHtmlVersion}")
    compile("io.ktor:ktor-html-builder:$ktorVersion")
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile(kotlin("test-junit5"))
    testCompile("io.ktor:ktor-client-mock:$ktorVersion")
    testCompile("io.ktor:ktor-client-mock-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockKVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }

        from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        environment("LEGACY_API_URL", "http://localhost:8090/person/dittnav-legacy-api")
        environment("EVENT_HANDLER_URL", "http://localhost:8092")
        environment("CORS_ALLOWED_ORIGINS", "localhost:9002")
        main = application.mainClassName
        classpath = sourceSets["main"].runtimeClasspath
    }
}
