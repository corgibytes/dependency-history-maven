package com.corgibytes.maven

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.*
import org.jsoup.Jsoup
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MavenRepositoryImpl(repositoryUrl: String) : MavenRepository {

    private val repositoryUrl = repositoryUrl.removeSuffix("/")
    private val client = HttpClient(CIO) {
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            retryOnExceptionIf(maxRetries = 5) { _, cause ->
                cause is IOException || cause is UnresolvedAddressException
            }
            // a base delay of 1.35 will put the 5th delay at about 4.5 seconds (1.35 ** 5)
            exponentialDelay(base = 1.35)
        }
    }

    override val isMavenCentral: Boolean
        get() = repositoryUrl == mavenCentralUrl

    override suspend fun getVersionReleaseDate(groupId: String, artifactId: String, version: String): ZonedDateTime {
        val url = buildVersionPomUrl(groupId, artifactId, version)
        val response = client.head(url)

        if (response.status == HttpStatusCode.NotFound) {
            throw PomFileNotFoundException(url)
        }

        var headerValue = response.headers["Last-Modified"]
        if (headerValue == null) {
            headerValue = response.headers["last-modified"]
        }
        if (headerValue == null) {
            throw LastModifiedHeaderNotFound(url)
        }

        return ZonedDateTime.parse(headerValue, DateTimeFormatter.RFC_1123_DATE_TIME)
    }

    @Serializable
    private data class MetadataVersion(@XmlValue(true) val value: String)

    @Serializable
    private data class MetadataVersioning(
        @XmlElement(true)
        @XmlSerialName("latest", "", "")
        val latest: String? = null,
        @XmlElement(true)
        @XmlSerialName("release", "", "")
        val release: String? = null,
        @XmlElement(true)
        @XmlSerialName("versions", "", "")
        @XmlChildrenName("version", "", "")
        val versions: List<MetadataVersion>,
        @XmlElement(true)
        @XmlSerialName("lastUpdated", "", "")
        val lastUpdated: String? = null
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

    override fun getVersionsFromMetadata(groupId: String, artifactId: String): List<String> {
        val wasRequestSuccessful: Boolean
        val responseBody: String
        runBlocking {
            val response = client.get(buildMetadataUrl(groupId, artifactId))
            wasRequestSuccessful = response.status.value < 400
            responseBody = response.body()
        }

        return if (wasRequestSuccessful) {
            parseVersionsFromMetadataResponse(responseBody)
        } else {
            getVersionsFromDirectoryListing(groupId, artifactId)
        }
    }

    private fun getVersionsFromDirectoryListing(groupId: String, artifactId: String): List<String> {
        val wasRequestSuccessful: Boolean
        val responseBody: String
        runBlocking {
            val response = client.get(buildDirectoryListingUrl(groupId, artifactId))
            wasRequestSuccessful = response.status.value < 400
            responseBody = response.body()
        }

        return if (wasRequestSuccessful) {
            parseVersionsFromDirectoryListingResponse(groupId, artifactId, responseBody)
        } else {
            emptyList()
        }
    }

    private fun parseVersionsFromDirectoryListingResponse(groupId: String, artifactId: String, responseBody: String): List<String> {
        val document = Jsoup.parse(responseBody)
        val links = document.select("a")
        var results = emptyList<String>()
        links.forEach {
            val target = it.attr("href").removeSuffix("/")
            if (target != "..") {
                if (isVersionNumberFor(groupId, artifactId, target)) {
                    results = results.plus(target)
                }
            }
        }
        return results
    }

    private fun isVersionNumberFor(groupId: String, artifactId: String, target: String): Boolean {
        val wasRequestSuccessful: Boolean
        val responseBody: String
        runBlocking {
            val response = client.get(buildDirectoryListingUrl(groupId, artifactId, target))
            wasRequestSuccessful = response.status.value < 400
            responseBody = response.body()
        }

        return if (wasRequestSuccessful) {
            doesContainVersionFor(groupId, artifactId, target, responseBody)
        } else {
            false
        }
    }

    private fun doesContainVersionFor(groupId: String, artifactId: String, target: String, responseBody: String): Boolean {
        val document = Jsoup.parse(responseBody)
        val links = document.select("a")
        return links.map { it.attr("href").removeSuffix("/") }.contains("$artifactId-$target.pom")
    }

    fun buildDirectoryListingUrl(groupId: String, artifactId: String, target: String? = null): String {
        val artifactListingUrl = "$repositoryUrl/${groupId.replace(".", "/")}/$artifactId"
        return if (target == null) {
            artifactListingUrl
        } else {
            "$artifactListingUrl/$target"
        }
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

    companion object {
        const val mavenCentralUrl = "https://repo.maven.apache.org/maven2"
    }
}

