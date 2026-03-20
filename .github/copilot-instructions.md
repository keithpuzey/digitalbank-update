# Copilot instructions for Digital Bank

## Big picture architecture
- Multi-module Maven build: root aggregator in [pom.xml](pom.xml) with two Spring Boot apps: bank and credit ([bank](bank) and [credit](credit)).
- bank is the primary web app (UI + APIs) and can integrate with credit; credit is a standalone service. See module docs in [bank/README.md](bank/README.md) and [credit/README.md](credit/README.md).
- Deployments are available via Docker and compose variants in [deploy/docker-compose](deploy/docker-compose) and Kubernetes manifests in [deploy/k8s](deploy/k8s).
- Messaging is via Apache Artemis (see module READMEs), with broker container artifacts in [broker](broker).
- Test automation examples (Robot Framework/Taurus) live in [test-automation](test-automation) and are separate from Java tests.

## Critical developer workflows
- Build/test modules with Maven from module folders:
  - Unit tests: `mvn clean test`
  - Serenity BDD acceptance tests: `mvn clean verify`
  - Package with build number: `mvn clean package -DbuildNumber=###`
- Docker images are built per module using each module’s Dockerfile, after updating build number in [bank/Dockerfile](bank/Dockerfile) or [credit/Dockerfile](credit/Dockerfile).
- Compose-based local stacks use specific files in [deploy/docker-compose](deploy/docker-compose) (H2, MSSQL, MySQL, Postgres variants).

## Project-specific conventions and patterns
- UI templates/resources are under each module’s webapp/resources tree (e.g., [bank/src/main/webapp](bank/src/main/webapp) and [bank/src/main/resources](bank/src/main/resources)).
- Cucumber feature files live under module test resources; tag filtering uses Maven `-Dcucumber.options="--tags {tag}"` (see [bank/README.md](bank/README.md)).
- Swagger UI endpoints are module-specific (`/bank/swagger-ui.html`, `/credit/swagger-ui.html`) per module docs.

## Integration points
- bank ↔ credit integration is an explicit deployment choice (standalone vs integrated); compose and k8s descriptors show wiring (see [deploy/docker-compose](deploy/docker-compose) and [deploy/k8s](deploy/k8s)).
- Database choice is environment-driven via compose files and SQL schemas under module targets (e.g., [bank/target/classes](bank/target/classes)).

## When editing code
- Keep changes scoped to the correct module (bank vs credit) and update related module README if you change build/deploy behavior.
- If adding tests, place unit tests under module test trees and Serenity/Cucumber artifacts alongside existing feature files in module test resources.
