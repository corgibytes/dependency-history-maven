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

class ReleaseHistoryService {
    var repositoryUrl: String = "https://repo.maven.apache.org/maven2"
    set(value) {
        field = value.removeSuffix("/")
    }

    private val client = HttpClient(CIO);

    fun getVersionHistory(groupId: String, artifactId: String): Map<String, ZonedDateTime> {
        val versions = getVersionsFromMetadata(groupId, artifactId)

        val result: Map<String, ZonedDateTime>

        runBlocking {
            result = versions.map { version ->
                version to async { getVersionReleaseDate(groupId, artifactId, version) }
            }.associate { pair ->
                pair.first to pair.second.await()
            }
        }

        return result
    }

    suspend fun getVersionReleaseDate(groupId: String, artifactId: String, version: String): ZonedDateTime {
        val url = buildVersionPomUrl(groupId, artifactId, version)
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

    fun getVersionsFromMetadata(groupId: String, artifactId: String): List<String> {
        val responseBody: String
        runBlocking {
            responseBody = client.get(buildMetadataUrl(groupId, artifactId)).body()
        }

        return parseVersionsFromMetadataResponse(responseBody)
    }

    fun buildMetadataUrl(groupId: String, artifactId: String): String {
        return "$repositoryUrl/${groupId.replace(".", "/")}/$artifactId/maven-metadata.xml"
    }

    fun buildVersionPomUrl(groupId: String, artifactId: String, version: String): String {
        return "$repositoryUrl/${groupId.replace(".", "/")}/$artifactId/$version/$artifactId-$version.pom"
    }

    fun parseVersionsFromMetadataResponse(responseBody: String): List<String> {
        val metadata: Metadata = XML.decodeFromString(responseBody)
        return metadata.versioning.versions.map { it.value }
    }

}
