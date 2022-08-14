package com.corgibytes.maven

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

enum class Mode {
    CONFIGURED_REPOSITORY,
    MAVEN_CENTRAL
}

class ReleaseHistoryService {
    private val mavenCentralUrl = "https://repo.maven.apache.org/maven2"

    var repositoryUrl: String = mavenCentralUrl
    set(value) {
        field = value.removeSuffix("/")
    }

    val isUsingMavenCentral: Boolean
        get() {
            return repositoryUrl == mavenCentralUrl
        }


    private val client = HttpClient(CIO);

    fun getVersionHistory(groupId: String, artifactId: String): Map<String, ZonedDateTime> {
        var mode = Mode.CONFIGURED_REPOSITORY
        var versions = getVersionsFromMetadata(groupId, artifactId, mode)

        if (versions.isEmpty() && !isUsingMavenCentral) {
            mode = Mode.MAVEN_CENTRAL
            versions = getVersionsFromMetadata(groupId, artifactId, mode)
        }

        val result: Map<String, ZonedDateTime>

        runBlocking {
            result = versions.map { version ->
                version to async { getVersionReleaseDate(groupId, artifactId, version, mode) }
            }.associate { pair ->
                pair.first to pair.second.await()
            }
        }

        return result
    }

    suspend fun getVersionReleaseDate(groupId: String, artifactId: String, version: String, mode: Mode = Mode.CONFIGURED_REPOSITORY): ZonedDateTime {
        val url = buildVersionPomUrl(groupId, artifactId, version, mode)
        val response = client.head(url)
        var headerValue = response.headers["Last-Modified"]
        if (headerValue == null) {
            headerValue = response.headers["last-modified"]
        }
        if (headerValue == null) {
            throw Exception("Last-Modified header is not present for $url")
        }

        return ZonedDateTime.parse(headerValue, DateTimeFormatter.RFC_1123_DATE_TIME)
    }

    @Serializable
    private data class MetadataVersion(@XmlValue(true) val value: String)

    @Serializable
    private data class MetadataVersioning(
        @XmlElement(true)
        @XmlSerialName("latest", "", "")
        val latest: String,
        @XmlElement(true)
        @XmlSerialName("release", "", "")
        val release: String? = null,
        @XmlElement(true)
        @XmlSerialName("versions", "", "")
        @XmlChildrenName("version", "", "")
        val versions: List<MetadataVersion>,
        @XmlElement(true)
        @XmlSerialName("lastUpdated", "", "")
        val lastUpdated: String
    )

    @Serializable
    @XmlSerialName("metadata", "", "")
    private data class Metadata(
        @XmlElement(false)
        @XmlSerialName("modelVersion", "", "")
        val modelVersion: String? = null,
        @XmlElement(true)
        @XmlSerialName("groupId", "", "")
        val groupId: String,
        @XmlElement(true)
        @XmlSerialName("artifactId", "", "")
        val artifactId: String,
        @XmlElement(true)
        @XmlSerialName("version", "", "")
        val version: String? = null,
        @XmlElement(true)
        @XmlSerialName("versioning", "", "")
        val versioning: MetadataVersioning
    )

    fun getVersionsFromMetadata(groupId: String, artifactId: String, mode: Mode = Mode.CONFIGURED_REPOSITORY): List<String> {
        val wasRequestSuccessful: Boolean
        val responseBody: String
        runBlocking {
            val response = client.get(buildMetadataUrl(groupId, artifactId, mode))
            wasRequestSuccessful = response.status.value < 400
            responseBody = response.body()
        }

        return if (wasRequestSuccessful) {
            parseVersionsFromMetadataResponse(responseBody)
        } else {
            emptyList()
        }
    }

    fun buildMetadataUrl(groupId: String, artifactId: String, mode: Mode = Mode.CONFIGURED_REPOSITORY): String {
        val targetRepositoryUrl = repositoryUrlFrom(mode)
        return "$targetRepositoryUrl/${groupId.replace(".", "/")}/$artifactId/maven-metadata.xml"
    }

    fun buildVersionPomUrl(groupId: String, artifactId: String, version: String, mode: Mode = Mode.CONFIGURED_REPOSITORY): String {
        val targetRepositoryUrl = repositoryUrlFrom(mode)
        return "$targetRepositoryUrl/${groupId.replace(".", "/")}/$artifactId/$version/$artifactId-$version.pom"
    }

    fun parseVersionsFromMetadataResponse(responseBody: String): List<String> {
        val metadata: Metadata = XML.decodeFromString(responseBody)
        return metadata.versioning.versions.map { it.value }
    }

    private fun repositoryUrlFrom(mode: Mode) = when (mode) {
        Mode.CONFIGURED_REPOSITORY -> {
            repositoryUrl
        }

        Mode.MAVEN_CENTRAL -> {
            mavenCentralUrl
        }
    }
}
