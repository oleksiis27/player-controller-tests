# Player Controller API Test Automation Framework

API test automation framework for testing the Player Controller API.

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 11+ | Language |
| TestNG | Test runner + assertions |
| REST Assured | HTTP client |
| Allure | Reporting |
| SLF4J + Logback | Logging |
| Gradle (Kotlin DSL) | Build tool |
| DataFaker | Test data generation |
| Owner | Configuration via properties |

## Architecture

```
Tests → Steps → API (per endpoint) → REST Assured
                 ↓
          BaseApi (shared RequestSpecification + Allure filter)
```

**Layers:**
- **API Layer** (`src/main`) — one class per endpoint, each extends `BaseApi` with shared spec
- **Steps Layer** (`src/main`) — business logic orchestration with `@Step` annotations for Allure
- **Models** (`src/main`) — single `PlayerDto` with Builder pattern, `Gender`/`Role` enums
- **Tests** (`src/test`) — only test classes, clean test methods using steps
- **Test Data** (`src/main`) — `TestDataHelper` with DataFaker for data generation

## Project Structure

```
src/
├── main/java/com/player/
│   ├── api/
│   │   ├── BaseApi.java                   # Shared RequestSpecification + Allure filter
│   │   ├── CreatePlayerApi.java           # GET /player/create/{editor}
│   │   ├── GetPlayerApi.java              # POST /player/get + GET /player/get/all
│   │   ├── UpdatePlayerApi.java           # PATCH /player/update/{editor}/{id}
│   │   └── DeletePlayerApi.java           # DELETE /player/delete/{editor}
│   ├── config/
│   │   └── AppConfig.java                 # Owner interface for configuration
│   ├── data/
│   │   └── TestDataHelper.java            # DataFaker-based test data generation
│   ├── listeners/
│   │   └── TestListener.java              # TestNG listener for logging
│   ├── models/
│   │   ├── PlayerDto.java                 # Single DTO with Builder, @JsonInclude(NON_NULL)
│   │   ├── Gender.java                    # Enum: MALE, FEMALE
│   │   └── Role.java                      # Enum: SUPERVISOR, ADMIN, USER
│   └── steps/
│       ├── CreatePlayerSteps.java
│       ├── GetPlayerSteps.java
│       ├── UpdatePlayerSteps.java
│       └── DeletePlayerSteps.java
├── test/java/com/player/tests/
│   ├── BaseTest.java                      # Setup/teardown, cleanup tracking
│   ├── CreatePlayerTest.java
│   ├── GetPlayerTest.java
│   ├── UpdatePlayerTest.java
│   ├── DeletePlayerTest.java
│   └── GetAllPlayersTest.java
└── test/resources/
    ├── testng.xml                         # parallel="methods" thread-count="3"
    ├── app.properties
    ├── allure.properties
    └── logback-test.xml
```

## How to Run

### Prerequisites
- Java 11+ installed
- `JAVA_HOME` environment variable set

### Run all tests
```bash
./gradlew test
```

### Run specific test class
```bash
./gradlew test --tests "com.player.tests.CreatePlayerTest"
```

### Generate Allure report
```bash
./gradlew allureReport
```

### View Allure report
```bash
./gradlew allureServe
```

## CI/CD

Tests run automatically on every push and pull request to `main` via GitHub Actions.

Allure report is published to GitHub Pages after each run.

## Test Execution

Tests run in **3 parallel threads** (`parallel="methods"`, `thread-count="3"`).
Each test class creates its own test data and cleans up after all methods via `@AfterClass`.

## Found Bugs

### BUG-001: Admin editor cannot create players
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 200 OK
- **Actual:** 403 Forbidden

### BUG-003: Duplicate login returns existing player instead of error
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400
- **Actual:** 200, returns existing player data

### BUG-004: Duplicate screenName creates new player
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400
- **Actual:** 200, creates player with duplicate screenName

### BUG-005: Invalid password accepted on create
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400 (password too short / no special chars)
- **Actual:** 200

### BUG-006: Password returned in GET response
- **Endpoint:** POST /player/get
- **Expected:** Password not included in response
- **Actual:** Password returned in plain text

### BUG-008: Duplicate screenName allowed on update
- **Endpoint:** PATCH /player/update/{editor}/{id}
- **Expected:** 400
- **Actual:** 200

### BUG-010: Invalid password accepted on update
- **Endpoint:** PATCH /player/update/{editor}/{id}
- **Expected:** 400
- **Actual:** 200

### BUG-012: User role can delete players
- **Endpoint:** DELETE /player/delete/{editor}
- **Expected:** 403
- **Actual:** 204
