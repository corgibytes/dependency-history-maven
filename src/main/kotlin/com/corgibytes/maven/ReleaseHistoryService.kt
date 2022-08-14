package com.corgibytes.maven

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime

class ReleaseHistoryService(
    private val targetRepository: MavenRepository,
    private val mavenCentralRepository: MavenRepository) {

    constructor(repositoryUrl: String = MavenRepositoryImpl.mavenCentralUrl) : this(
        MavenRepositoryImpl(repositoryUrl),
        MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl))

    fun getVersionHistory(groupId: String, artifactId: String): Map<String, ZonedDateTime> {
        var versions = targetRepository.getVersionsFromMetadata(groupId, artifactId)

        var repositoryForVersions = targetRepository
        if (versions.isEmpty() && !targetRepository.isMavenCentral) {
            repositoryForVersions = mavenCentralRepository
            versions = mavenCentralRepository.getVersionsFromMetadata(groupId, artifactId)
        }

        val result: Map<String, ZonedDateTime>

        runBlocking {
            result = versions.map { version ->
                version to async { repositoryForVersions.getVersionReleaseDate(groupId, artifactId, version) }
            }.associate { pair ->
                pair.first to pair.second.await()
            }
        }

        return result
    }
}
