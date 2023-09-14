package com.corgibytes.maven

class LastModifiedHeaderNotFound(url: String): Exception("Last-Modified header is not present for $url") {
}