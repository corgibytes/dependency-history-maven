package com.corgibytes.maven

import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.Test


class MavenRepositoryImplTest {
    @Test
    fun retrievePackageVersions() {
        val repository = MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl)

        val actualResults = repository.getVersionsFromMetadata("org.apache.maven", "apache-maven")

        val expectedResults = listOf(
            "2.0.9",
            "2.0.10",
            "2.0.11",
            "2.1.0-M1",
            "2.1.0",
            "2.2.0",
            "2.2.1",
            "3.0-alpha-2",
            "3.0-alpha-3",
            "3.0-alpha-4",
            "3.0-alpha-5",
            "3.0-alpha-6",
            "3.0-alpha-7",
            "3.0-beta-1",
            "3.0-beta-2",
            "3.0-beta-3",
            "3.0",
            "3.0.1",
            "3.0.2",
            "3.0.3",
            "3.0.4",
            "3.0.5",
            "3.1.0-alpha-1",
            "3.1.0",
            "3.1.1",
            "3.2.1",
            "3.2.2",
            "3.2.3",
            "3.2.5",
            "3.3.1",
            "3.3.3",
            "3.3.9",
            "3.5.0-alpha-1",
            "3.5.0-beta-1",
            "3.5.0",
            "3.5.2",
            "3.5.3",
            "3.5.4",
            "3.6.0",
            "3.6.1",
            "3.6.2",
            "3.6.3",
            "3.8.1",
            "3.8.2",
            "3.8.3",
            "3.8.4",
            "3.8.5",
        )

        expectedResults.forEach { assertContains(actualResults, it) }
    }

    @Test
    fun buildMetadataUrlWithDefaultRepository() {
        val repository = MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl)

        assertEquals(
            "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/maven-metadata.xml",
            repository.buildMetadataUrl("org.apache.maven", "apache-maven")
        )
    }

    @Test
    fun buildMetadataUrlWithAlternativeRepository() {
        val repository = MavenRepositoryImpl("https://repo.spring.io/artifactory/release/")

        assertEquals(
            "https://repo.spring.io/artifactory/release/io/spring/platform/platform/maven-metadata.xml",
            repository.buildMetadataUrl("io.spring.platform", "platform")
        )
    }

    @Test
    fun buildVersionPomUrl() {
        val repository = MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl)

        assertEquals(
            "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/2.0.10/apache-maven-2.0.10.pom",
            repository.buildVersionPomUrl("org.apache.maven", "apache-maven", "2.0.10")
        )
    }

    @Test
    fun getVersionReleaseDate() {
        val repository = MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl)

        val expectedDateTime =
            ZonedDateTime.parse("Tue, 10 Feb 2009 02:57:59 GMT", DateTimeFormatter.RFC_1123_DATE_TIME)
        runBlocking {
            assertEquals(
                expectedDateTime,
                repository.getVersionReleaseDate("org.apache.maven", "apache-maven", "2.0.10")
            )
        }
    }

    @Test
    fun parseVersionsFromMetadataResponseWithOptionalFieldsMissing() {
        // this XML document does not contain the /metadata/release element
        val xmlDocument = """
            <metadata>
                <groupId>org.apache.maven</groupId>
                <artifactId>maven-project</artifactId>
                <versioning>
                    <latest>2.2.1</latest>
                    <versions>
                        <version>2.0-alpha-2</version>
                        <version>2.0-alpha-3</version>
                        <version>2.0-beta-1</version>
                        <version>2.0-beta-2</version>
                        <version>2.0-beta-3</version>
                        <version>2.0</version>
                        <version>2.0-1</version>
                        <version>2.0.1</version>
                        <version>2.0.2</version>
                        <version>2.0.3</version>
                        <version>2.0.4</version>
                        <version>2.0.5</version>
                        <version>2.0.6</version>
                        <version>2.0.7</version>
                        <version>2.0.8</version>
                        <version>2.0.9</version>
                        <version>2.0.10</version>
                        <version>2.0.11</version>
                        <version>2.1.0-M1</version>
                        <version>2.1.0</version>
                        <version>2.2.0</version>
                        <version>2.2.1</version>
                        <version>3.0-alpha-1</version>
                        <version>3.0-alpha-2</version>
                    </versions>
                    <lastUpdated>20100226050724</lastUpdated>
                </versioning>
            </metadata> 
        """.trimIndent()

        val repository = MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl)

        val parsedVersions = repository.parseVersionsFromMetadataResponse(xmlDocument)

        val expectedResults = listOf(
            "2.0-alpha-2",
            "2.0-alpha-3",
            "2.0-beta-1",
            "2.0-beta-2",
            "2.0-beta-3",
            "2.0",
            "2.0-1",
            "2.0.1",
            "2.0.2",
            "2.0.3",
            "2.0.4",
            "2.0.5",
            "2.0.6",
            "2.0.7",
            "2.0.8",
            "2.0.9",
            "2.0.10",
            "2.0.11",
            "2.1.0-M1",
            "2.1.0",
            "2.2.0",
            "2.2.1",
            "3.0-alpha-1",
            "3.0-alpha-2",
            "2.0.9",
            "2.0.10",
            "2.0.11",
            "2.1.0-M1",
            "2.1.0",
            "2.2.0",
            "2.2.1",
        )

        expectedResults.forEach { assertContains(parsedVersions, it) }

    }

    @Test
    fun parseVersionsFromMetadataResponseWithLatestAndLastUpdatedFieldsMissing() {
        val xmlDocument = """
            <metadata>
                <groupId>aopalliance</groupId>
                <artifactId>aopalliance</artifactId>
                <versioning>
                    <versions>
                        <version>1.0</version>
                    </versions>
                </versioning>
            </metadata>
        """.trimIndent()

        val repository = MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl)

        val parsedVersions = repository.parseVersionsFromMetadataResponse(xmlDocument)

        assertContains(parsedVersions, "1.0")
    }
}