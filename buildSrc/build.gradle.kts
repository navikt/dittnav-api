plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    maven("https://jitpack.io")
}

val dittNavDependenciesVersion = "2021.08.31-14.26-eb38ce748636"

dependencies {
    implementation("com.github.navikt:dittnav-dependencies:$dittNavDependenciesVersion")
}
