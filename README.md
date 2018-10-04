# About

[![Kotlin](https://img.shields.io/badge/kotlin-1.2.71-blue.svg)](http://kotlinlang.org)
[![Travis](https://img.shields.io/travis/christophpickl/payconiq.svg)](https://travis-ci.org/christophpickl/payconiq)
[![Issues](https://img.shields.io/github/issues/christophpickl/payconiq.svg)](https://github.com/christophpickl/payconiq/issues?q=is%3Aopen) 

A simple "RPC over HTTP" (a.k.a. ReST) application written in Kotlin to offer some basic CRUD operations for stocks.

See full [requirements PDF](https://github.com/christophpickl/payconiq/blob/master/requirements.pdf) for more details.

## Tech Stack

* Kotlin
* Gradle
* Spring Boot 2
* AspectJ
* JUnit 5, AssertJ, Mockito
* Travis CI
* Postman

# How to run

    $ git clone https://github.com/christophpickl/payconiq.git
    $ cd payconiq
    $ ./gradlw bootRun
    $ curl http://localhost:8080/api/stocks

In order to see a list of all stocks, upon up your browser and enter the following address: [http://localhost:8080](http://localhost:8080)

For a more convenient approach to test the API, use the [Postman 2.1 collection](https://github.com/christophpickl/payconiq/blob/master/payconiq.postman_collection.json).

## Notes

* Regarding "Provide us with a merge request to master of this repository", it was decided to split each requirement into seperate pull requests/[issues](https://github.com/christophpickl/payconiq/issues?q=is%3Aissue+is%3Aclosed).

## Further possible doings

* Use Java 11
* Build a Docker image and deploy to PaaS ([Dokku](http://dokku.viewdocs.io/dokku/), [Heroku](https://www.heroku.com/) or [AWS free Tier](https://aws.amazon.com/free/))
* Add authentication (and authorization) via Spring Security
* Use Kotlin-DSL for Gradle scripts
* Support pagination
* Swagger documentation
* Run release on Travis (at least GIT tag it)
* Split up unit and integration tests in separate source sets
* Split into modules (rest, service, persistence)
