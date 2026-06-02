# LaunchKit

LaunchKit is a Java CLI tool that generates ready-to-use developer project setups.

It helps developers quickly bootstrap backend projects, sample applications, Docker Compose files, environment examples and CI workflows.

## Why LaunchKit?

Starting a new project often means repeating the same setup steps:

- Create backend structure
- Add Docker Compose
- Configure database connection
- Add environment variables
- Add CI workflow
- Add sample endpoints

LaunchKit automates this setup so developers can start building faster.

## Features

- Java-based CLI
- Spring Boot backend generation
- PostgreSQL Docker Compose generation
- `.env.example` generation
- GitHub Actions CI workflow generation
- Todo sample application with CRUD API
- Maven-based generated backend
- Clean project structure

## Tech Stack

LaunchKit itself:

- Java 21
- Maven
- Picocli
- JUnit 5

Generated projects:

- Spring Boot 3
- PostgreSQL
- Docker Compose
- GitHub Actions

## Installation

Build the jar:

```bash
mvn clean package