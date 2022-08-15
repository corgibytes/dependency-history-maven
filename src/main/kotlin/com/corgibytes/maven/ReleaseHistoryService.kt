package com.corgibytes.maven

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime

class ReleaseHistoryService(
    private val targetRepository: MavenRepository,
    private val mavenCentralRepository: MavenRepository) {

    init {
        if (!mavenCentralRepository.isMavenCentral) {
            throw Exception("Second repository must be configured to communicate with Maven Central")
        }
    }

    constructor(repositoryUrl: String = MavenRepositoryImpl.mavenCentralUrl) : this(
        MavenRepositoryImpl(repositoryUrl),
        MavenRepositoryImpl(MavenRepositoryImpl.mavenCentralUrl))

    fun getVersionHistory(groupId: String, artifactId: String): Map<String, ZonedDateTime> {
        if (targetRepository.isMavenCentral) {
            return getVersionHistoryFromRepository(groupId, artifactId, targetRepository)
        }

        val targetRepositoryVersions = getVersionHistoryFromRepository(groupId, artifactId, targetRepository)
        val mavenCentralRepositoryVersions =
            getVersionHistoryFromRepository(groupId, artifactId, mavenCentralRepository)

        val result: MutableMap<String, ZonedDateTime> = LinkedHashMap()
        result.putAll(targetRepositoryVersions)
        mavenCentralRepositoryVersions.forEach { entry ->
            if (targetRepositoryVersions.containsKey(entry.key)) {
                result.replace(entry.key, minOf(entry.value, result[entry.key]!!))
            } else {
                result[entry.key] = entry.value
            }
        }
        return result
    }

    private fun getVersionHistoryFromRepository(
        groupId: String,
        artifactId: String,
        repository: MavenRepository
    ): Map<String, ZonedDateTime> {
        var versions = repository.getVersionsFromMetadata(groupId, artifactId)

        val result: Map<String, ZonedDateTime>

        runBlocking {
            result = versions.map { version ->
                version to async { repository.getVersionReleaseDate(groupId, artifactId, version) }
            }.associate { pair ->
                pair.first to pair.second.await()
            }
        }

        return result
    }
}
