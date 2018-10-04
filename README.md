# About

[![Kotlin](https://img.shields.io/badge/kotlin-1.2.71-blue.svg)](http://kotlinlang.org)
[![Travis](https://img.shields.io/travis/christophpickl/payconiq.svg)](https://travis-ci.org/christophpickl/payconiq)
[![Issues](https://img.shields.io/github/issues/christophpickl/payconiq.svg)](https://github.com/christophpickl/payconiq/issues?q=is%3Aopen) 

A simple "RPC over HTTP" (a.k.a. ReST) application written in Kotlin to offer some basic CRUD operations.

Used tech stack:

* Gradle
* Kotlin
* Spring Boot 2
* JUnit 5, AssertJ, Mockito

# Howto run

    $ git clone https://github.com/christophpickl/payconiq.git
    $ cd payconiq
    $ ./gradlw bootRun
    $ curl http://localhost:8080/api/stocks

For a more convenient approach to test the service, use the [Postman 2.1 collection](https://github.com/christophpickl/payconiq/blob/master/payconiq.postman_collection.json).

## Notes

* Regarding "Provide us with a merge request to master of this repository", it was decided to split each requirement into seperate pull requests/issues.

### Deliberately left out

* Unit and integration tests were not split up (own source paths) in order to keep it as simple as possible for this assignment.
* There are no separate modules (rest, service, persistence) as it would have only unnecessarily increased the complexity.

## Further possible doings

* Add authentication (and authorization) via Spring Security
* Use Java 11
* Use Kotlin-DSL for gradle scripts
* Build a Docker image and deploy to some (free) PaaS vendor
* Support for pagination
* Swagger ReST documentation
