package com.corgibytes.maven

import java.time.ZonedDateTime

interface MavenRepository {
    fun getVersionsFromMetadata(groupId: String, artifactId: String): List<String>
    suspend fun getVersionReleaseDate(groupId: String, artifactId: String, version: String) : ZonedDateTime
    val isMavenCentral: Boolean
}
