plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    maven("https://jitpack.io")
}

val dittNavDependenciesVersion = "2020.11.10-15.38-096678d820e8"

dependencies {
    implementation("com.github.navikt:dittnav-dependencies:$dittNavDependenciesVersion")
}
