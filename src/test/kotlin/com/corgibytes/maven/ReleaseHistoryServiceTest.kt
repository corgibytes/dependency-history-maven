package com.corgibytes.maven

import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ReleaseHistoryServiceTest {
    @Test
    fun retrievesReleaseHistory() {
        val service = ReleaseHistoryService()

        val actualResults = service.getVersionHistory("org.apache.maven", "apache-maven")

        val expectedResults = mapOf(
            "2.0.9" to "Thu, 10 Apr 2008 00:16:46 GMT",
            "2.0.10" to "Tue, 10 Feb 2009 02:57:59 GMT",
            "2.0.11" to "Fri, 12 Feb 2010 05:57:11 GMT",
            "2.1.0-M1" to "Thu, 18 Sep 2008 19:58:12 GMT",
            "2.1.0" to "Wed, 18 Mar 2009 19:16:55 GMT",
            "2.2.0" to "Fri, 26 Jun 2009 13:08:51 GMT",
            "2.2.1" to "Thu, 06 Aug 2009 19:18:53 GMT",
            "3.0-alpha-2" to "Thu, 05 Feb 2009 06:22:38 GMT",
            "3.0-alpha-3" to "Mon, 09 Nov 2009 16:08:33 GMT",
            "3.0-alpha-4" to "Fri, 13 Nov 2009 18:10:27 GMT",
            "3.0-alpha-5" to "Mon, 23 Nov 2009 15:58:15 GMT",
            "3.0-alpha-6" to "Wed, 06 Jan 2010 11:06:09 GMT",
            "3.0-alpha-7" to "Tue, 09 Mar 2010 22:34:35 GMT",
            "3.0-beta-1" to "Mon, 19 Apr 2010 17:03:53 GMT",
            "3.0-beta-2" to "Sat, 07 Aug 2010 11:03:55 GMT",
            "3.0-beta-3" to "Mon, 30 Aug 2010 12:47:24 GMT",
            "3.0" to "Mon, 04 Oct 2010 11:54:21 GMT",
            "3.0.1" to "Tue, 23 Nov 2010 11:02:58 GMT",
            "3.0.2" to "Sun, 09 Jan 2011 01:01:17 GMT",
            "3.0.3" to "Mon, 28 Feb 2011 17:33:57 GMT",
            "3.0.4" to "Tue, 17 Jan 2012 08:48:07 GMT",
            "3.0.5" to "Tue, 19 Feb 2013 13:54:33 GMT",
            "3.1.0-alpha-1" to "Sat, 01 Jun 2013 13:05:46 GMT",
            "3.1.0" to "Fri, 28 Jun 2013 02:17:49 GMT",
            "3.1.1" to "Tue, 17 Sep 2013 15:24:21 GMT",
            "3.2.1" to "Fri, 14 Feb 2014 17:40:18 GMT",
            "3.2.2" to "Tue, 17 Jun 2014 13:53:25 GMT",
            "3.2.3" to "Mon, 11 Aug 2014 20:59:36 GMT",
            "3.2.5" to "Sun, 14 Dec 2014 17:30:51 GMT",
            "3.3.1" to "Fri, 13 Mar 2015 20:12:31 GMT",
            "3.3.3" to "Wed, 22 Apr 2015 11:59:30 GMT",
            "3.3.9" to "Tue, 10 Nov 2015 16:44:21 GMT",
            "3.5.0-alpha-1" to "Thu, 23 Feb 2017 15:06:59 GMT",
            "3.5.0-beta-1" to "Mon, 20 Mar 2017 17:00:22 GMT",
            "3.5.0" to "Mon, 03 Apr 2017 19:41:19 GMT",
            "3.5.2" to "Wed, 18 Oct 2017 07:59:58 GMT",
            "3.5.3" to "Sat, 24 Feb 2018 19:51:34 GMT",
            "3.5.4" to "Sun, 17 Jun 2018 18:35:37 GMT",
            "3.6.0" to "Wed, 24 Oct 2018 18:43:51 GMT",
            "3.6.1" to "Thu, 04 Apr 2019 19:03:01 GMT",
            "3.6.2" to "Tue, 27 Aug 2019 15:10:13 GMT",
            "3.6.3" to "Tue, 19 Nov 2019 19:36:44 GMT",
            "3.8.1" to "Tue, 30 Mar 2021 17:21:46 GMT",
            "3.8.2" to "Wed, 04 Aug 2021 19:07:37 GMT",
            "3.8.3" to "Mon, 27 Sep 2021 18:32:54 GMT",
            "3.8.4" to "Sun, 14 Nov 2021 09:19:02 GMT",
            "3.8.5" to "Sat, 05 Mar 2022 15:41:10 GMT",
        )

        expectedResults.forEach { version, rawExpectedDateTime ->
            val expectedDateTime = ZonedDateTime.parse(rawExpectedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
            assertEquals(expectedDateTime, actualResults.get(version))
        }
    }

    @Test
    fun retrievePackageVersions() {
        val service = ReleaseHistoryService()

        val actualResults = service.getVersionsFromMetadata("org.apache.maven", "apache-maven")

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
        val service = ReleaseHistoryService()

        assertEquals(
            "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/maven-metadata.xml",
            service.buildMetadataUrl("org.apache.maven", "apache-maven")
        )
    }

    @Test
    fun buildMetadataUrlWithAlternativeRepository() {
        val service = ReleaseHistoryService()

        service.repositoryUrl = "https://repo.spring.io/artifactory/release/"

        assertEquals(
            "https://repo.spring.io/artifactory/release/io/spring/platform/platform/maven-metadata.xml",
            service.buildMetadataUrl("io.spring.platform", "platform")
        )
    }

    @Test
    fun buildVersionPomUrl() {
        val service = ReleaseHistoryService()

        assertEquals(
            "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/2.0.10/apache-maven-2.0.10.pom",
            service.buildVersionPomUrl("org.apache.maven", "apache-maven", "2.0.10")
        )
    }

    @Test
    fun getVersionReleaseDate() {
        val service = ReleaseHistoryService()

        val expectedDateTime =
            ZonedDateTime.parse("Tue, 10 Feb 2009 02:57:59 GMT", DateTimeFormatter.RFC_1123_DATE_TIME)
        runBlocking {
            assertEquals(
                expectedDateTime,
                service.getVersionReleaseDate("org.apache.maven", "apache-maven", "2.0.10")
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

        val service = ReleaseHistoryService()

        val parsedVersions = service.parseVersionsFromMetadataResponse(xmlDocument)

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

}