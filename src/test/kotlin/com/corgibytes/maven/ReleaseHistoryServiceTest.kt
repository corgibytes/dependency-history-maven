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
    fun retrieveReleaseHistoryFromAlternativeLocation() {
        val service = ReleaseHistoryService()

        service.repositoryUrl = "http://repo.spring.io/release"

        val actualResults = service.getVersionHistory("org.springframework", "spring-core")

        val expectedResults = mapOf(
            "1.0" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.0-rc1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.0.1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1-rc1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1-rc2" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1.1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1.2" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1.3" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1.4" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.1.5" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2-rc1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2-rc2" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.1" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.2" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.3" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.4" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.5" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.6" to "Fri, 23 Dec 2005 17:06:05 GMT",
            "1.2.7" to "Wed, 10 May 2006 04:03:59 GMT",
            "1.2.8" to "Thu, 24 Aug 2006 22:37:56 GMT",
            "1.2.9" to "Fri, 09 Mar 2007 02:35:42 GMT",
            "2.0" to "Wed, 04 Oct 2006 09:49:19 GMT",
            "2.0-m1" to "Tue, 14 Feb 2006 02:27:32 GMT",
            "2.0-m2" to "Wed, 10 May 2006 04:03:59 GMT",
            "2.0-m4" to "Wed, 10 May 2006 04:03:59 GMT",
            "2.0.1" to "Sat, 25 Nov 2006 22:32:40 GMT",
            "2.0.2" to "Tue, 09 Jan 2007 18:32:52 GMT",
            "2.0.3" to "Fri, 09 Mar 2007 22:36:28 GMT",
            "2.0.4" to "Tue, 10 Apr 2007 21:35:12 GMT",
            "2.0.5" to "Tue, 08 May 2007 01:44:07 GMT",
            "2.0.6" to "Tue, 19 Jun 2007 17:56:13 GMT",
            "2.0.7" to "Tue, 02 Oct 2007 01:55:15 GMT",
            "2.0.8" to "Thu, 10 Jan 2008 02:57:49 GMT",
            "2.5" to "Tue, 25 Dec 2007 19:39:37 GMT",
            "2.5.1" to "Thu, 10 Jan 2008 02:57:49 GMT",
            "2.5.2" to "Thu, 06 Mar 2008 08:16:30 GMT",
            "2.5.3" to "Mon, 07 Apr 2008 09:28:51 GMT",
            "2.5.4" to "Mon, 28 Apr 2008 17:52:37 GMT",
            "2.5.5" to "Mon, 23 Jun 2008 17:28:49 GMT",
            "2.5.6" to "Sun, 02 Nov 2008 01:45:05 GMT",
            "2.5.6.SEC01" to "Wed, 22 Apr 2009 22:56:24 GMT",
            "2.5.6.SEC02" to "Fri, 18 Jun 2010 23:03:42 GMT",
            "2.5.6.SEC03" to "Fri, 09 Sep 2011 22:43:38 GMT",
            "3.0.0.RELEASE" to "Thu, 17 Dec 2009 00:07:52 GMT",
            "3.0.1.RELEASE" to "Fri, 19 Feb 2010 00:12:07 GMT",
            "3.0.2.RELEASE" to "Fri, 02 Apr 2010 22:48:28 GMT",
            "3.0.3.RELEASE" to "Tue, 15 Jun 2010 22:51:40 GMT",
            "3.0.4.RELEASE" to "Thu, 19 Aug 2010 23:07:43 GMT",
            "3.0.5.RELEASE" to "Wed, 20 Oct 2010 22:50:27 GMT",
            "3.0.6.RELEASE" to "Fri, 19 Aug 2011 13:56:36 GMT",
            "3.0.7.RELEASE" to "Fri, 23 Dec 2011 23:40:24 GMT",
            "3.1.0.RELEASE" to "Tue, 13 Dec 2011 16:42:36 GMT",
            "3.1.1.RELEASE" to "Thu, 16 Feb 2012 23:42:40 GMT",
            "3.1.2.RELEASE" to "Sat, 07 Jul 2012 22:43:57 GMT",
            "3.1.3.RELEASE" to "Wed, 31 Oct 2012 18:33:35 GMT",
            "3.1.4.RELEASE" to "Wed, 23 Jan 2013 15:31:26 GMT",
            "3.2.0.RELEASE" to "Thu, 13 Dec 2012 15:55:17 GMT",
            "3.2.1.RELEASE" to "Thu, 24 Jan 2013 18:40:59 GMT",
            "3.2.10.RELEASE" to "Tue, 15 Jul 2014 23:34:26 GMT",
            "3.2.11.RELEASE" to "Thu, 04 Sep 2014 13:27:11 GMT",
            "3.2.12.RELEASE" to "Tue, 11 Nov 2014 10:25:06 GMT",
            "3.2.13.RELEASE" to "Tue, 30 Dec 2014 16:22:00 GMT",
            "3.2.14.RELEASE" to "Tue, 30 Jun 2015 17:35:03 GMT",
            "3.2.15.RELEASE" to "Thu, 15 Oct 2015 08:40:50 GMT",
            "3.2.16.RELEASE" to "Thu, 17 Dec 2015 13:02:17 GMT",
            "3.2.17.RELEASE" to "Fri, 06 May 2016 10:22:50 GMT",
            "3.2.18.RELEASE" to "Wed, 21 Dec 2016 18:41:06 GMT",
            "3.2.2.RELEASE" to "Wed, 13 Mar 2013 21:13:45 GMT",
            "3.2.3.RELEASE" to "Mon, 20 May 2013 17:18:09 GMT",
            "3.2.4.RELEASE" to "Tue, 06 Aug 2013 22:58:56 GMT",
            "3.2.5.RELEASE" to "Wed, 06 Nov 2013 19:50:39 GMT",
            "3.2.6.RELEASE" to "Thu, 12 Dec 2013 09:06:00 GMT",
            "3.2.7.RELEASE" to "Tue, 28 Jan 2014 22:36:16 GMT",
            "3.2.8.RELEASE" to "Wed, 19 Feb 2014 05:51:05 GMT",
            "3.2.9.RELEASE" to "Tue, 20 May 2014 11:48:03 GMT",
            "4.0.0.RELEASE" to "Thu, 12 Dec 2013 07:14:49 GMT",
            "4.0.1.RELEASE" to "Tue, 28 Jan 2014 20:12:17 GMT",
            "4.0.2.RELEASE" to "Wed, 19 Feb 2014 00:51:10 GMT",
            "4.0.3.RELEASE" to "Thu, 27 Mar 2014 04:21:47 GMT",
            "4.0.4.RELEASE" to "Wed, 30 Apr 2014 22:39:17 GMT",
            "4.0.5.RELEASE" to "Tue, 20 May 2014 13:36:17 GMT",
            "4.0.6.RELEASE" to "Tue, 08 Jul 2014 03:46:42 GMT",
            "4.0.7.RELEASE" to "Thu, 04 Sep 2014 08:15:46 GMT",
            "4.0.8.RELEASE" to "Tue, 11 Nov 2014 06:47:16 GMT",
            "4.0.9.RELEASE" to "Tue, 30 Dec 2014 15:12:48 GMT",
            "4.1.0.RELEASE" to "Thu, 04 Sep 2014 11:40:48 GMT",
            "4.1.1.RELEASE" to "Wed, 01 Oct 2014 08:45:55 GMT",
            "4.1.2.RELEASE" to "Tue, 11 Nov 2014 08:32:16 GMT",
            "4.1.3.RELEASE" to "Tue, 09 Dec 2014 10:23:18 GMT",
            "4.1.4.RELEASE" to "Tue, 30 Dec 2014 10:56:32 GMT",
            "4.1.5.RELEASE" to "Fri, 20 Feb 2015 11:07:24 GMT",
            "4.1.6.RELEASE" to "Wed, 25 Mar 2015 16:19:42 GMT",
            "4.1.7.RELEASE" to "Tue, 30 Jun 2015 17:07:20 GMT",
            "4.1.8.RELEASE" to "Thu, 15 Oct 2015 09:38:40 GMT",
            "4.1.9.RELEASE" to "Thu, 17 Dec 2015 08:45:18 GMT",
            "4.2.0.RELEASE" to "Fri, 31 Jul 2015 09:01:50 GMT",
            "4.2.1.RELEASE" to "Tue, 01 Sep 2015 09:26:54 GMT",
            "4.2.2.RELEASE" to "Thu, 15 Oct 2015 12:35:33 GMT",
            "4.2.3.RELEASE" to "Sun, 15 Nov 2015 16:31:14 GMT",
            "4.2.4.RELEASE" to "Thu, 17 Dec 2015 09:05:56 GMT",
            "4.2.5.RELEASE" to "Thu, 25 Feb 2016 09:13:15 GMT",
            "4.2.6.RELEASE" to "Fri, 06 May 2016 07:52:54 GMT",
            "4.2.7.RELEASE" to "Mon, 04 Jul 2016 10:25:06 GMT",
            "4.2.8.RELEASE" to "Mon, 19 Sep 2016 14:41:46 GMT",
            "4.2.9.RELEASE" to "Wed, 21 Dec 2016 12:18:57 GMT",
            "4.3.0.RELEASE" to "Fri, 10 Jun 2016 08:57:53 GMT",
            "4.3.1.RELEASE" to "Mon, 04 Jul 2016 09:27:19 GMT",
            "4.3.10.RELEASE" to "Thu, 20 Jul 2017 11:56:33 GMT",
            "4.3.11.RELEASE" to "Mon, 11 Sep 2017 08:16:11 GMT",
            "4.3.12.RELEASE" to "Tue, 10 Oct 2017 13:53:39 GMT",
            "4.3.13.RELEASE" to "Mon, 27 Nov 2017 10:38:24 GMT",
            "4.3.14.RELEASE" to "Tue, 23 Jan 2018 09:02:20 GMT",
            "4.3.15.RELEASE" to "Tue, 03 Apr 2018 20:09:50 GMT",
            "4.3.16.RELEASE" to "Mon, 09 Apr 2018 14:56:58 GMT",
            "4.3.17.RELEASE" to "Tue, 08 May 2018 07:47:37 GMT",
            "4.3.18.RELEASE" to "Tue, 12 Jun 2018 14:48:44 GMT",
            "4.3.19.RELEASE" to "Fri, 07 Sep 2018 14:09:04 GMT",
            "4.3.2.RELEASE" to "Thu, 28 Jul 2016 08:24:17 GMT",
            "4.3.20.RELEASE" to "Mon, 15 Oct 2018 08:47:26 GMT",
            "4.3.21.RELEASE" to "Tue, 27 Nov 2018 07:39:15 GMT",
            "4.3.22.RELEASE" to "Wed, 09 Jan 2019 09:00:01 GMT",
            "4.3.23.RELEASE" to "Sun, 31 Mar 2019 07:22:35 GMT",
            "4.3.24.RELEASE" to "Thu, 09 May 2019 08:58:48 GMT",
            "4.3.25.RELEASE" to "Fri, 02 Aug 2019 08:06:52 GMT",
            "4.3.26.RELEASE" to "Tue, 14 Jan 2020 11:06:11 GMT",
            "4.3.27.RELEASE" to "Tue, 28 Apr 2020 09:27:11 GMT",
            "4.3.28.RELEASE" to "Tue, 21 Jul 2020 06:58:36 GMT",
            "4.3.29.RELEASE" to "Tue, 15 Sep 2020 10:50:44 GMT",
            "4.3.3.RELEASE" to "Mon, 19 Sep 2016 15:10:02 GMT",
            "4.3.30.RELEASE" to "Wed, 09 Dec 2020 08:31:27 GMT",
            "4.3.4.RELEASE" to "Mon, 07 Nov 2016 21:34:37 GMT",
            "4.3.5.RELEASE" to "Wed, 21 Dec 2016 11:10:17 GMT",
            "4.3.6.RELEASE" to "Wed, 25 Jan 2017 13:33:12 GMT",
            "4.3.7.RELEASE" to "Wed, 01 Mar 2017 08:52:00 GMT",
            "4.3.8.RELEASE" to "Tue, 18 Apr 2017 14:45:17 GMT",
            "4.3.9.RELEASE" to "Wed, 07 Jun 2017 19:28:44 GMT",
            "5.0.0.RELEASE" to "Thu, 28 Sep 2017 11:27:33 GMT",
            "5.0.1.RELEASE" to "Tue, 24 Oct 2017 15:14:07 GMT",
            "5.0.10.RELEASE" to "Mon, 15 Oct 2018 08:00:58 GMT",
            "5.0.11.RELEASE" to "Tue, 27 Nov 2018 08:53:06 GMT",
            "5.0.12.RELEASE" to "Wed, 09 Jan 2019 09:50:54 GMT",
            "5.0.13.RELEASE" to "Sun, 31 Mar 2019 08:02:35 GMT",
            "5.0.14.RELEASE" to "Thu, 09 May 2019 09:33:31 GMT",
            "5.0.15.RELEASE" to "Fri, 02 Aug 2019 08:32:06 GMT",
            "5.0.16.RELEASE" to "Tue, 14 Jan 2020 10:36:34 GMT",
            "5.0.17.RELEASE" to "Tue, 28 Apr 2020 09:08:08 GMT",
            "5.0.18.RELEASE" to "Tue, 21 Jul 2020 07:05:11 GMT",
            "5.0.19.RELEASE" to "Tue, 15 Sep 2020 09:40:44 GMT",
            "5.0.2.RELEASE" to "Mon, 27 Nov 2017 10:51:30 GMT",
            "5.0.20.RELEASE" to "Wed, 09 Dec 2020 08:10:29 GMT",
            "5.0.3.RELEASE" to "Tue, 23 Jan 2018 09:41:35 GMT",
            "5.0.4.RELEASE" to "Mon, 19 Feb 2018 11:11:58 GMT",
            "5.0.5.RELEASE" to "Tue, 03 Apr 2018 20:10:31 GMT",
            "5.0.6.RELEASE" to "Tue, 08 May 2018 08:33:26 GMT",
            "5.0.7.RELEASE" to "Tue, 12 Jun 2018 15:08:54 GMT",
            "5.0.8.RELEASE" to "Thu, 26 Jul 2018 07:48:40 GMT",
            "5.0.9.RELEASE" to "Fri, 07 Sep 2018 12:14:56 GMT",
            "5.1.0.RELEASE" to "Fri, 21 Sep 2018 07:25:17 GMT",
            "5.1.1.RELEASE" to "Mon, 15 Oct 2018 07:19:22 GMT",
            "5.1.10.RELEASE" to "Sat, 28 Sep 2019 11:26:28 GMT",
            "5.1.11.RELEASE" to "Sat, 02 Nov 2019 07:16:10 GMT",
            "5.1.12.RELEASE" to "Tue, 03 Dec 2019 08:04:57 GMT",
            "5.1.13.RELEASE" to "Tue, 14 Jan 2020 07:36:53 GMT",
            "5.1.14.RELEASE" to "Tue, 25 Feb 2020 16:22:47 GMT",
            "5.1.15.RELEASE" to "Tue, 28 Apr 2020 07:29:05 GMT",
            "5.1.16.RELEASE" to "Tue, 09 Jun 2020 08:04:19 GMT",
            "5.1.17.RELEASE" to "Tue, 21 Jul 2020 07:09:06 GMT",
            "5.1.18.RELEASE" to "Tue, 15 Sep 2020 09:02:17 GMT",
            "5.1.19.RELEASE" to "Tue, 27 Oct 2020 13:26:07 GMT",
            "5.1.2.RELEASE" to "Mon, 29 Oct 2018 10:32:07 GMT",
            "5.1.20.RELEASE" to "Wed, 09 Dec 2020 07:22:10 GMT",
            "5.1.3.RELEASE" to "Tue, 27 Nov 2018 09:27:56 GMT",
            "5.1.4.RELEASE" to "Wed, 09 Jan 2019 12:42:18 GMT",
            "5.1.5.RELEASE" to "Wed, 13 Feb 2019 05:50:21 GMT",
            "5.1.6.RELEASE" to "Sun, 31 Mar 2019 08:41:41 GMT",
            "5.1.7.RELEASE" to "Thu, 09 May 2019 14:54:52 GMT",
            "5.1.8.RELEASE" to "Thu, 13 Jun 2019 14:03:49 GMT",
            "5.1.9.RELEASE" to "Fri, 02 Aug 2019 09:01:04 GMT",
            "5.2.0.RELEASE" to "Mon, 30 Sep 2019 08:56:41 GMT",
            "5.2.1.RELEASE" to "Sat, 02 Nov 2019 08:18:30 GMT",
            "5.2.10.RELEASE" to "Tue, 27 Oct 2020 13:48:18 GMT",
            "5.2.11.RELEASE" to "Tue, 10 Nov 2020 07:08:52 GMT",
            "5.2.12.RELEASE" to "Wed, 09 Dec 2020 06:34:10 GMT",
            "5.2.13.RELEASE" to "Tue, 16 Feb 2021 11:39:09 GMT",
            "5.2.14.RELEASE" to "Tue, 13 Apr 2021 11:47:15 GMT",
            "5.2.15.RELEASE" to "Wed, 12 May 2021 06:17:07 GMT",
            "5.2.16.RELEASE" to "Wed, 14 Jul 2021 08:00:54 GMT",
            "5.2.17.RELEASE" to "Wed, 15 Sep 2021 08:11:07 GMT",
            "5.2.18.RELEASE" to "Thu, 14 Oct 2021 10:26:06 GMT",
            "5.2.19.RELEASE" to "Thu, 16 Dec 2021 09:39:27 GMT",
            "5.2.2.RELEASE" to "Tue, 03 Dec 2019 08:58:42 GMT",
            "5.2.20.RELEASE" to "Thu, 31 Mar 2022 10:02:04 GMT",
            "5.2.21.RELEASE" to "Wed, 13 Apr 2022 10:32:53 GMT",
            "5.2.22.RELEASE" to "Wed, 11 May 2022 07:32:12 GMT",
            "5.2.3.RELEASE" to "Tue, 14 Jan 2020 08:00:01 GMT",
            "5.2.4.RELEASE" to "Tue, 25 Feb 2020 16:42:18 GMT",
            "5.2.5.RELEASE" to "Tue, 24 Mar 2020 11:32:45 GMT",
            "5.2.6.RELEASE" to "Tue, 28 Apr 2020 08:22:42 GMT",
            "5.2.7.RELEASE" to "Tue, 09 Jun 2020 06:51:22 GMT",
            "5.2.8.RELEASE" to "Tue, 21 Jul 2020 08:20:24 GMT",
            "5.2.9.RELEASE" to "Tue, 15 Sep 2020 08:30:31 GMT",
            "5.3.0" to "Tue, 27 Oct 2020 14:53:28 GMT",
            "5.3.1" to "Tue, 10 Nov 2020 09:03:37 GMT",
            "5.3.10" to "Wed, 15 Sep 2021 07:25:39 GMT",
            "5.3.11" to "Thu, 14 Oct 2021 09:36:02 GMT",
            "5.3.12" to "Thu, 21 Oct 2021 05:57:47 GMT",
            "5.3.13" to "Thu, 11 Nov 2021 07:43:33 GMT",
            "5.3.14" to "Thu, 16 Dec 2021 08:47:09 GMT",
            "5.3.15" to "Thu, 13 Jan 2022 11:23:42 GMT",
            "5.3.16" to "Thu, 17 Feb 2022 07:46:50 GMT",
            "5.3.17" to "Thu, 17 Mar 2022 10:51:58 GMT",
            "5.3.18" to "Thu, 31 Mar 2022 09:05:40 GMT",
            "5.3.19" to "Wed, 13 Apr 2022 09:25:02 GMT",
            "5.3.2" to "Wed, 09 Dec 2020 06:11:58 GMT",
            "5.3.20" to "Wed, 11 May 2022 07:09:30 GMT",
            "5.3.21" to "Wed, 15 Jun 2022 08:18:36 GMT",
            "5.3.22" to "Thu, 14 Jul 2022 08:51:44 GMT",
            "5.3.3" to "Tue, 12 Jan 2021 06:35:01 GMT",
            "5.3.4" to "Tue, 16 Feb 2021 10:52:47 GMT",
            "5.3.5" to "Tue, 16 Mar 2021 08:13:15 GMT",
            "5.3.6" to "Tue, 13 Apr 2021 11:16:13 GMT",
            "5.3.7" to "Wed, 12 May 2021 05:51:12 GMT",
            "5.3.8" to "Wed, 09 Jun 2021 07:50:49 GMT",
            "5.3.9" to "Wed, 14 Jul 2021 06:49:05 GMT",
        )

        expectedResults.forEach { version, rawExpectedDateTime ->
            val expectedDateTime = ZonedDateTime.parse(rawExpectedDateTime, DateTimeFormatter.RFC_1123_DATE_TIME)
            assertEquals(expectedDateTime, actualResults.get(version), version)
        }
    }
    
    @Test
    fun retrieveReleaseHistoryFallsBackToMavenCentralIfPackageNotFoundInAlternativeRepository() {
        val service = ReleaseHistoryService()

        service.repositoryUrl = "http://repo.spring.io/release"

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