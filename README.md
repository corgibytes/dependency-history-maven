# Dependency History Maven

This project is a small library that knows how to retrieve the release history for a package that is published in a Maven repository.

## Usage

### Gradle

```groovy
dependencies {
    implementation("com.corgibytes:dependency-history-maven:2.0.+")
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.corgibytes</groupId>
        <artifactId>dependency-history-maven</artifactId>
        <version>[2.0.0,)</version>
    </dependency>
</dependencies>
```

### Example

```kotlin
val service = ReleaseHistoryService()

val actualResults = service.getVersionHistory("org.apache.maven", "apache-maven")

println(actualResults["2.0.10"])
// outputs: 2009-02-10T02:57:59Z
```

### Changing the Maven repository

By default the `ReleaseHistoryService` uses Maven Central to retrieve release history information. To use another repository pass the value into the constructor.

For example, here's how to create an instance of the service that communicates with the repository used by the Spring project.

```kotlin
val service = ReleaseHistoryService("https://repo.spring.io/artifactory/release/")
```

> :warning: When you specify an alternative repository, Maven Central will still be contacted to retrieve the information that it has for the package in question. This works around issues with some Maven repository proxies not preserving release history information. For each package version that is queried, the oldest date will be used.

## About

The library is used by the [Freshli](https://github.com/corgibytes/freshli) family of projects to provide support for working with Maven repositories. 
