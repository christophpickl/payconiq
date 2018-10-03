# About

[![Kotlin](https://img.shields.io/badge/kotlin-1.2.71-blue.svg)](http://kotlinlang.org)
[![Travis](https://img.shields.io/travis/christophpickl/payconiq.svg)](https://travis-ci.org/christophpickl/payconiq)
[![Issues](https://img.shields.io/github/issues/christophpickl/payconiq.svg)](https://github.com/christophpickl/payconiq/issues?q=is%3Aopen) 

A simple "RPC over HTTP" application written in Kotlin using Spring Boot.

Used tech stack:

* Spring Boot 2
* JUnit 5, AssertJ, Mockito

# TODO (for me)

* add postman collection
* proper logging

## Notes

* Regarding "Provide us with a merge request to master of this repository", it was decided to split each requirement into seperate pull requests/issues.

### Deliberately left out

* Unit and integration tests were not split up (own source paths) in order to keep it as simple as possible for this assignment.
* There are no separate modules (rest, service, persistence) as it would have only unnecessarily increased the complexity.

## Further possible doings

* Switch to Java 11
* Use Kotlin-DSL for gradle scripts
* Build a Docker image and deploy to some (free) PaaS vendor
* Add implicit logging for certain (service/annotated) classes, rather do it manually
