# Dependency History Maven

This project is a small library that knows how to retrieve the release history for a package that is published in a Maven repository.

## Usage

```kotlin
val service = ReleaseHistoryService()

val actualResults = service.getVersionHistory("org.apache.maven", "apache-maven")

println(actualResults["2.0.10"])
// outputs: 2009-02-10T02:57:59Z
```

### Changing the Maven repository

By default the `ReleaseHistoryService` uses Maven Central to retrieve release history information. To use another repository set the `repositoryUrl` property to the appropriate value.

For example, here's how to create an instance of the service that communicates with the repository used by the Spring project.

```kotlin
val service = ReleaseHistoryService()
service.repositoryUrl = "https://repo.spring.io/artifactory/release/"
```

## About

The library is used by the [Freshli](https://github.com/corgibytes/freshli) family of projects to provide support for working with Maven repositories. 
