import io.ktor.network.tls.certificates.*

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val slf4j_version: String by project
val exposed_version: String by project
val h2_version: String by project
val postgres_version: String by project
val hoplite_version: String by project

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.ktor:ktor-network-tls-certificates:2.3.4")
    }
}

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "cz.upce.bvwa2"
version = "0.0.1"

application {
    mainClass.set("cz.upce.bvwa2.ApplicationKt")

//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()

    maven { url = uri("https://jitpack.io") } // ktor-role-based-auth
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-id-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-compression-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-resources:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates-jvm:2.3.6")
    implementation("io.ktor:ktor-server-http-redirect:$ktor_version")

    // Auth
    implementation("com.github.omkar-tenkale:ktor-role-based-auth:0.2.0")

    //Pass encrypt
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.sqids:sqids_3:0.5.0")

    // Database
    implementation("com.h2database:h2:$h2_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-api:$slf4j_version")

    // Hoplite
    implementation("com.sksamuel.hoplite:hoplite-core:$hoplite_version")
    implementation("com.sksamuel.hoplite:hoplite-hocon:$hoplite_version")
    implementation("io.ktor:ktor-server-http-redirect-jvm:2.3.6")

    // Tests
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
}

tasks.test {
    useJUnitPlatform()

    outputs.upToDateWhen { false }
}

tasks.classes {
    dependsOn(generateSslCertificate)
}

val generateSslCertificate by tasks.registering {
    val keyStoreFile = File("build/keystore.jks")
    inputs.file(keyStoreFile)
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")
}