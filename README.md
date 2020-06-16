## Overview

Spring Boot App Example

## API

* `/sections` - CRUD API for Sections and Geological Classes
* `/import` - API for data import (in CSV format). Protected with Basic Auth
* `/export` - API for data export (in CSV format). Protected with Basic Auth
* `/users` - Simple user management API. Protected with Basic Auth

## Configuration

To create default user set environment variable `APP_USER` and `APP_PASSWORD`.

## Development

### Build

`./gradlew build`

### Run

`./gradlew bootRun`
