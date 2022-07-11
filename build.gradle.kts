
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.0"
    id ("org.jetbrains.kotlin.plugin.allopen") version "1.5.0"
}

group = "com.bus.reservation"
version = "1.0.0"

object Versions {
    const val JUNIT = "5.8.2"
    const val MOCKK = "1.12.0"
    const val ASSERTJ = "3.20.2"
    const val ARROW = "1.1.2"
    const val FAKER = "1.0.2"
    const val REST_ASSURED = "5.0.1"
    const val WIREMOCK = "2.27.2"
    const val KTOR = "2.0.3"
    const val LOGBACK = "1.2.11"
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("io.arrow-kt:arrow-core:${Versions.ARROW}")
    implementation("io.ktor:ktor-server-core-jvm:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-netty-jvm:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-content-negotiation:${Versions.KTOR}")
    implementation("io.ktor:ktor-serialization-jackson:${Versions.KTOR}")
    implementation("ch.qos.logback:logback-classic:${Versions.LOGBACK}")

    testImplementation(group= "com.github.javafaker", name= "javafaker", version= Versions.FAKER) {
        exclude(group = "org.yaml")
    }
    testImplementation("io.ktor:ktor-server-tests-jvm:${Versions.KTOR}")
    testImplementation("io.ktor:ktor-server-test-host:${Versions.KTOR}")
    testImplementation(group = "io.mockk", name = "mockk", version = Versions.MOCKK)
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
    testImplementation(group = "org.assertj", name = "assertj-core", version = Versions.ASSERTJ)
    testImplementation("io.rest-assured:rest-assured:${Versions.REST_ASSURED}")
    testImplementation("com.github.tomakehurst:wiremock:${Versions.WIREMOCK}")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
}

tasks.apply {
    test {
        maxParallelForks = 1
        enableAssertions = true
        useJUnitPlatform {}
    }
}
