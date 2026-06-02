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
- Windows installer script
- macOS/Linux installer script

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

### Build

Build the jar:

```bash
mvn clean package
```

### Windows

Run the Windows installer:

```powershell
powershell -ExecutionPolicy Bypass -File .\install\install-windows.ps1
```

The installer creates a launcher and can add LaunchKit to your user PATH.

After installation, close and reopen your terminal, then run:

```powershell
launchkit --help
```

### macOS / Linux

Run the macOS/Linux installer:

```bash
chmod +x install/install-mac.sh
./install/install-mac.sh
```

The installer creates a launcher and can add LaunchKit to your PATH.

After installation, restart your terminal or source your shell config, then run:

```bash
launchkit --help
```

## Usage

Create a basic project:

```bash
launchkit create my-app --backend springboot --frontend none --db postgres
```

Create a Todo sample app:

```bash
launchkit sample todo todo-demo --backend springboot --db postgres
```

Generated structure:

```txt
todo-demo/
  backend/
    src/
    pom.xml
  docker-compose.yml
  .env.example
  README.md
  .github/
    workflows/
      ci.yml
```

## Run Generated Todo App

Start PostgreSQL:

```bash
cd todo-demo
docker compose up -d
```

Start the backend:

```bash
cd backend
mvn spring-boot:run
```

Health check:

```bash
curl http://localhost:8080/api/health
```

Create Todo:

```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Learn LaunchKit\",\"completed\":false}"
```

Get Todos:

```bash
curl http://localhost:8080/api/todos
```

## Current Status

LaunchKit is currently an MVP and under active development.

The first supported sample is a Spring Boot Todo API with PostgreSQL, Docker Compose and GitHub Actions.

## Roadmap

- Angular frontend generation
- React frontend generation
- JWT authentication template
- Spring Security setup
- FastAPI backend generation
- Config file support with `launchkit.yml`
- Custom database port option
- Testcontainers support
- Better terminal output
- GitHub release packages

## Author

Built by Leon Nando Tiefenthaler.
