val exposed_version: String by project
val h2_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {

    implementation("io.insert-koin:koin-core:3.1.3")

    implementation("io.insert-koin:koin-ktor:3.1.3")

    implementation("io.insert-koin:koin-logger-slf4j:3.1.3")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.exposed:exposed-dao:${exposed_version}")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.github.flaxoos:ktor-server-rate-limiting:2.2.1")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-serialization-gson:3.1.3")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.6")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
