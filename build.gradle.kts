/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.4.2/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */

group = "com.corgibytes"

buildscript {
    repositories {
        mavenLocal() // for local testing of shipkit
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.shipkit:shipkit-auto-version:1.+")
        classpath("org.shipkit:shipkit-changelog:1.+")
        classpath("io.github.gradle-nexus:publish-plugin:1.3.0")
    }
}

repositories {
    mavenLocal() // for local testing of shipkit
    mavenCentral()
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

apply("gradle/release.gradle")
apply("gradle/ide.gradle")

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.20"

    id("com.github.ben-manes.versions") version "0+"
    id("se.patrikerdes.use-latest-versions") version "0+"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:32.1.2-jre")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    val ktor_version: String by project
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktor_version")
    implementation("io.github.pdvrieze.xmlutil:core-jvm:0.86.2")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.86.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jsoup:jsoup:1.16.1")

    testImplementation("io.mockk:mockk:1.13.7")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest()
        }
    }
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}
