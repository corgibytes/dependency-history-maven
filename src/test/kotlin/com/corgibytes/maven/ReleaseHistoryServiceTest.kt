package com.corgibytes.maven

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    fun retrieveReleaseHistoryForJavaxInject() {
        val service = ReleaseHistoryService()

        val actualResults = service.getVersionHistory("javax.inject", "javax.inject")

        val expectedResults = mapOf(
            "1" to "Tue, 13 Oct 2009 23:35:00 GMT",
        )

        expectedResults.forEach { version, rawExpectedDateTime ->
            val expectedDateTime = ZonedDateTime.parse(rawExpectedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
            assertEquals(expectedDateTime, actualResults.get(version))
        }
    }

    @Test
    fun retrieveReleaseHistoryForJcipAnnotations() {
        val service = ReleaseHistoryService()

        val actualResults = service.getVersionHistory("net.jcip", "jcip-annotations")

        val expectedResults = mapOf(
            "1.0" to "Thu, 14 Aug 2008 06:48:05 GMT",
        )

        expectedResults.forEach { version, rawExpectedDateTime ->
            val expectedDateTime = ZonedDateTime.parse(rawExpectedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
            assertEquals(expectedDateTime, actualResults.get(version))
        }
    }

    @Test
    fun retrieveReleaseHistoryFromAlternativeLocation() {
        val service = ReleaseHistoryService("https://repository.cloudera.com/artifactory/cloudera-repos")

        val actualResults = service.getVersionHistory("com.cloudera.api.swagger", "cloudera-manager-api-swagger")

        val expectedResults = mapOf(
            "6.0.0" to "Wed, 29 Aug 2018 19:02:53 GMT",
            "6.2.0" to "Fri, 29 Mar 2019 14:58:58 GMT",
            "6.2.1" to "Thu, 27 Jan 2022 15:51:15 GMT",
            "6.3.0" to "Mon, 05 Aug 2019 14:23:04 GMT",
            "6.3.4" to "Thu, 27 Jan 2022 15:10:31 GMT",
            "6.x.0" to "Wed, 24 Apr 2019 21:23:18 GMT",
            "7.0.3" to "Mon, 16 Dec 2019 09:51:26 GMT",
            "7.1.1" to "Mon, 05 Oct 2020 21:52:04 GMT",
            "7.1.2" to "Mon, 05 Oct 2020 21:53:27 GMT",
            "7.1.3" to "Mon, 05 Oct 2020 21:54:35 GMT",
            "7.1.4" to "Mon, 12 Oct 2020 22:51:30 GMT",
            "7.10.1" to "Tue, 13 Jun 2023 10:14:21 GMT",
            "7.11.0" to "Mon, 26 Jun 2023 14:39:42 GMT",
            "7.2.6" to "Fri, 01 Oct 2021 10:35:51 GMT",
            "7.3.1" to "Tue, 03 Aug 2021 13:52:21 GMT",
            "7.4.1" to "Thu, 22 Apr 2021 09:41:37 GMT",
            "7.4.2" to "Tue, 06 Jul 2021 15:24:12 GMT",
            "7.4.3" to "Wed, 08 Sep 2021 17:52:35 GMT",
            "7.4.4" to "Thu, 05 Aug 2021 11:43:14 GMT",
            "7.5.1" to "Wed, 29 Sep 2021 05:06:28 GMT",
            "7.5.2" to "Mon, 25 Oct 2021 17:43:27 GMT",
            "7.5.4" to "Mon, 08 Nov 2021 13:55:33 GMT",
            "7.5.5" to "Tue, 12 Apr 2022 15:40:30 GMT",
            "7.6.0" to "Fri, 25 Feb 2022 07:07:54 GMT",
            "7.6.1" to "Tue, 29 Mar 2022 13:35:26 GMT",
            "7.6.2" to "Thu, 12 May 2022 21:34:11 GMT",
            "7.6.5" to "Mon, 20 Jun 2022 11:13:50 GMT",
            "7.6.7" to "Wed, 01 Feb 2023 04:00:20 GMT",
            "7.7.1" to "Mon, 29 Aug 2022 20:39:36 GMT",
            "7.8.1" to "Thu, 17 Nov 2022 21:21:40 GMT",
            "7.9.0" to "Wed, 11 Jan 2023 11:41:58 GMT",
            "7.9.5" to "Fri, 20 Jan 2023 05:31:28 GMT",
        )

        expectedResults.forEach { version, rawExpectedDateTime ->
            val expectedDateTime = ZonedDateTime.parse(rawExpectedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
            assertEquals(expectedDateTime, actualResults.get(version), version)
        }
    }

    @Test
    fun retrieveReleaseHistoryFallsBackToMavenCentralIfPackageNotFoundInAlternativeRepository() {
        val service = ReleaseHistoryService("http://repo.spring.io/release")

        val actualResults = service.getVersionHistory("org.apache.ant", "ant")

        val expectedResults = mapOf(
            "1.7.0" to "Fri, 22 Dec 2006 13:37:31 GMT",
            "1.7.1" to "Wed, 09 Jul 2008 20:04:58 GMT",
            "1.8.0" to "Tue, 02 Feb 2010 00:37:15 GMT",
            "1.8.1" to "Fri, 30 Apr 2010 22:04:20 GMT",
            "1.8.2" to "Mon, 27 Dec 2010 16:10:08 GMT",
            "1.8.3" to "Sun, 26 Feb 2012 06:05:46 GMT",
            "1.8.4" to "Tue, 22 May 2012 06:01:29 GMT",
            "1.9.0" to "Mon, 11 Mar 2013 00:50:39 GMT",
            "1.9.1" to "Wed, 22 May 2013 03:04:13 GMT",
            "1.9.2" to "Mon, 08 Jul 2013 18:48:17 GMT",
            "1.9.3" to "Mon, 23 Dec 2013 16:26:35 GMT",
            "1.9.4" to "Wed, 30 Apr 2014 03:21:11 GMT",
            "1.9.5" to "Sun, 31 May 2015 14:32:14 GMT",
            "1.9.6" to "Mon, 29 Jun 2015 05:25:24 GMT",
            "1.9.7" to "Sat, 09 Apr 2016 07:08:40 GMT",
            "1.9.8" to "Sun, 25 Dec 2016 18:03:40 GMT",
            "1.9.9" to "Thu, 02 Feb 2017 18:40:04 GMT",
            "1.9.10" to "Sat, 03 Feb 2018 16:22:42 GMT",
            "1.9.11" to "Fri, 23 Mar 2018 17:19:16 GMT",
            "1.9.12" to "Tue, 19 Jun 2018 04:14:12 GMT",
            "1.9.13" to "Tue, 10 Jul 2018 04:27:04 GMT",
            "1.9.14" to "Tue, 12 Mar 2019 09:32:47 GMT",
            "1.9.15" to "Sun, 10 May 2020 13:57:08 GMT",
            "1.9.16" to "Sat, 10 Jul 2021 18:00:36 GMT",
            "1.10.0" to "Tue, 27 Dec 2016 06:56:05 GMT",
            "1.10.1" to "Thu, 02 Feb 2017 19:08:40 GMT",
            "1.10.2" to "Sat, 03 Feb 2018 16:58:39 GMT",
            "1.10.3" to "Sat, 24 Mar 2018 12:41:27 GMT",
            "1.10.4" to "Tue, 19 Jun 2018 06:50:53 GMT",
            "1.10.5" to "Tue, 10 Jul 2018 04:54:03 GMT",
            "1.10.6" to "Thu, 02 May 2019 13:27:39 GMT",
            "1.10.7" to "Sun, 01 Sep 2019 06:51:35 GMT",
            "1.10.8" to "Sun, 10 May 2020 14:40:05 GMT",
            "1.10.9" to "Sun, 27 Sep 2020 10:08:52 GMT",
            "1.10.10" to "Mon, 12 Apr 2021 04:00:22 GMT",
            "1.10.11" to "Sat, 10 Jul 2021 18:43:17 GMT",
            "1.10.12" to "Wed, 13 Oct 2021 05:09:56 GMT",
        )

        expectedResults.forEach { version, rawExpectedDateTime ->
            val expectedDateTime = ZonedDateTime.parse(rawExpectedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
            assertEquals(expectedDateTime, actualResults.get(version), version)
        }
    }

    @Test
    fun retrieveReleaseHistoryCombinesResultsFromTargetRepositoryAndMavenCentralRepository() {
        val targetRepository = mockk<MavenRepository>()
        val mavenCentralRepository = mockk<MavenRepository>()

        val groupId = "org.apache.maven"
        val artifactId = "maven"

        // In this scenario, version 1.0.0 is only available from the target repository, and 4.0.0 is only available
        // from the Maven Central repository
        val targetRepositoryVersions = listOf("1.0.0", "2.0.0", "3.0.0")
        val mavenCentralVersions = listOf("2.0.0", "3.0.0", "4.0.0")

        every { targetRepository.getVersionsFromMetadata(groupId, artifactId) } returns targetRepositoryVersions
        every { targetRepository.isMavenCentral } returns false
        every { mavenCentralRepository.getVersionsFromMetadata(groupId, artifactId) } returns mavenCentralVersions
        every { mavenCentralRepository.isMavenCentral } returns true

        every {
            runBlocking { targetRepository.getVersionReleaseDate(groupId, artifactId, "1.0.0") }
        } returns ZonedDateTime.parse("2022-01-01T12:00:00Z")
        // since this version has an older date than the maven central repository, it should be included in the
        // retrieved list
        every {
            runBlocking { targetRepository.getVersionReleaseDate(groupId, artifactId, "2.0.0") }
        } returns ZonedDateTime.parse("2020-02-02T12:00:00Z")
        every {
            runBlocking { targetRepository.getVersionReleaseDate(groupId, artifactId, "3.0.0") }
        } returns ZonedDateTime.parse("2022-03-03T12:00:00Z")

        every {
            runBlocking { mavenCentralRepository.getVersionReleaseDate(groupId, artifactId, "2.0.0") }
        } returns ZonedDateTime.parse("2022-02-02T12:00:00Z")
        // since this version has an older date than the target repository, it should be included in the retrieved list
        every {
            runBlocking { mavenCentralRepository.getVersionReleaseDate(groupId, artifactId, "3.0.0") }
        } returns ZonedDateTime.parse("2020-03-03T12:00:00Z")
        every {
            runBlocking { mavenCentralRepository.getVersionReleaseDate(groupId, artifactId, "4.0.0") }
        } returns ZonedDateTime.parse("2022-04-04T12:00:00Z")

        val expectedResults = mapOf(
            "1.0.0" to ZonedDateTime.parse("2022-01-01T12:00:00Z"),
            "2.0.0" to ZonedDateTime.parse("2020-02-02T12:00:00Z"),
            "3.0.0" to ZonedDateTime.parse("2020-03-03T12:00:00Z"),
            "4.0.0" to ZonedDateTime.parse("2022-04-04T12:00:00Z"),
        )

        val service = ReleaseHistoryService(targetRepository, mavenCentralRepository)
        val actualResults = service.getVersionHistory(groupId, artifactId)

        assertEquals(expectedResults, actualResults)
    }

    @Test
    fun retrieveReleaseHistoryDoesNotCombineResultsIfTargetRepositoryIsMavenCentral() {
        val targetRepository = mockk<MavenRepository>()
        val mavenCentralRepository = mockk<MavenRepository>()

        val groupId = "org.apache.maven"
        val artifactId = "maven"

        // In this scenario, only the target repository should be used. The second parameter to the
        // `ReleaseHistoryService` constructor should not be used at all.
        val targetRepositoryVersions = listOf("1.0.0", "2.0.0", "3.0.0")

        every { targetRepository.getVersionsFromMetadata(groupId, artifactId) } returns targetRepositoryVersions
        every { targetRepository.isMavenCentral } returns true
        every { mavenCentralRepository.isMavenCentral } returns true

        every {
            runBlocking { targetRepository.getVersionReleaseDate(groupId, artifactId, "1.0.0") }
        } returns ZonedDateTime.parse("2022-01-01T12:00:00Z")
        // since this version has an older date than the maven central repository, it should be included in the
        // retrieved list
        every {
            runBlocking { targetRepository.getVersionReleaseDate(groupId, artifactId, "2.0.0") }
        } returns ZonedDateTime.parse("2020-02-02T12:00:00Z")
        every {
            runBlocking { targetRepository.getVersionReleaseDate(groupId, artifactId, "3.0.0") }
        } returns ZonedDateTime.parse("2022-03-03T12:00:00Z")

        val expectedResults = mapOf(
            "1.0.0" to ZonedDateTime.parse("2022-01-01T12:00:00Z"),
            "2.0.0" to ZonedDateTime.parse("2020-02-02T12:00:00Z"),
            "3.0.0" to ZonedDateTime.parse("2022-03-03T12:00:00Z"),
        )

        val service = ReleaseHistoryService(targetRepository, mavenCentralRepository)
        val actualResults = service.getVersionHistory(groupId, artifactId)

        assertEquals(expectedResults, actualResults)

        verify(exactly = 0) { mavenCentralRepository.getVersionsFromMetadata(any(), any()) }
        verify(exactly = 0) {
            runBlocking { mavenCentralRepository.getVersionReleaseDate(any(), any(), any()) }
        }
        verify(atLeast = 1) { mavenCentralRepository.isMavenCentral }

        confirmVerified(mavenCentralRepository)
    }

    @Test
    fun retrieveReleaseHistoryRequiresSecondRepositoryParameterToBeMavenCentral() {
        val targetRepository = mockk<MavenRepository>()
        val mavenCentralRepository = mockk<MavenRepository>()

        every { targetRepository.isMavenCentral } returns false
        every { mavenCentralRepository.isMavenCentral } returns false

        assertFailsWith<Exception>("Second repository must be configured to communicate with Maven Central") {
            ReleaseHistoryService(targetRepository, mavenCentralRepository)
        }
    }
}