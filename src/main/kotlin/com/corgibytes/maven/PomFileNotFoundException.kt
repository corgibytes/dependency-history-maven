package com.corgibytes.maven

class PomFileNotFoundException(url: String) : Exception("Unable to retrieve $url") {
}