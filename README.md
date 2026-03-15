# Player Controller API Test Automation Framework

API test automation framework for testing the Player Controller API.

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 17+ | Language |
| TestNG | Test runner + assertions |
| REST Assured | HTTP client |
| Allure | Reporting |
| SLF4J + Logback | Logging |
| Gradle (Kotlin DSL) | Build tool |
| DataFaker | Test data generation |
| Owner | Configuration via properties |

## Architecture

```
Tests → PlayerSteps → PlayerApiClient → REST Assured
                          ↓
                   BaseApiClient (shared RequestSpecification + Allure filter)
```

**Layers:**
- **API Layer** (`src/main`) — one class per entity, extends `BaseApiClient` with shared spec
- **Steps Layer** (`src/main`) — one class per entity, business logic with `@Step` annotations for Allure
- **Models** (`src/main`) — single `PlayerDto` with Builder pattern, `Gender`/`Role`/`StatusCode` enums
- **Tests** (`src/test`) — only test classes, clean test methods using steps
- **Test Data** (`src/main`) — `TestDataHelper` with DataFaker for data generation
- **Listeners** (`src/main`) — `TestListener` for logging, `DynamicThreadListener` for runtime thread-count

## Project Structure

```
src/
├── main/java/com/player/
│   ├── api/
│   │   ├── BaseApiClient.java              # Shared RequestSpecification + Allure filter
│   │   └── PlayerApiClient.java           # All player endpoints (create, get, update, delete)
│   ├── config/
│   │   └── AppConfig.java                 # Owner interface for configuration
│   ├── data/
│   │   └── TestDataHelper.java            # DataFaker-based test data generation
│   ├── listeners/
│   │   ├── TestListener.java              # TestNG listener for logging
│   │   └── DynamicThreadListener.java     # Runtime thread-count from config/system property
│   ├── models/
│   │   ├── PlayerDto.java                 # Single DTO with Builder, @JsonInclude(NON_NULL)
│   │   ├── Gender.java                    # Enum: MALE, FEMALE
│   │   ├── Role.java                      # Enum: SUPERVISOR, ADMIN, USER
│   │   └── StatusCode.java               # Enum: OK, NO_CONTENT, BAD_REQUEST, FORBIDDEN
│   └── steps/
│       └── PlayerSteps.java               # All player business logic (create, get, update, delete)
├── test/java/com/player/tests/
│   ├── BaseTest.java                      # Setup/teardown, cleanup tracking, DataProvider
│   ├── CreatePlayerTest.java
│   ├── GetPlayerTest.java
│   ├── UpdatePlayerTest.java
│   ├── DeletePlayerTest.java
│   └── GetAllPlayersTest.java
└── test/resources/
    ├── testng.xml                         # parallel="methods", dynamic thread-count
    ├── app.properties
    ├── allure.properties
    └── logback-test.xml
```

## How to Run

### Prerequisites
- Java 17+ installed
- `JAVA_HOME` environment variable set

### Run all tests
```bash
./gradlew test
```

### Run with custom thread count
```bash
./gradlew test -Dthread.count=5
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

Tests run in parallel (`parallel="methods"`), thread count is configured via `app.properties` (default: 3) and can be overridden with `-Dthread.count=N`.

Each test class creates its own test data and cleans up after all methods via `@AfterClass`.

## API Design Notes

The API under test has several endpoints that do not follow REST conventions:
- **GET** `/player/create/{editor}` — player creation uses GET instead of POST
- **POST** `/player/get` — fetching a player by ID uses POST instead of GET
- **DELETE** `/player/delete/{editor}` — passes player ID in request body instead of path

## Found Bugs

### BUG-001: Admin editor cannot create players
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 200 OK
- **Actual:** 403 Forbidden

### BUG-002: Duplicate login returns existing player instead of error
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400
- **Actual:** 200, returns existing player data

### BUG-003: Duplicate screenName creates new player
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400
- **Actual:** 200, creates player with duplicate screenName

### BUG-004: Invalid password accepted on create
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400 (password too short / too long / no digits / no letters)
- **Actual:** 200

### BUG-005: Password returned in GET response
- **Endpoint:** POST /player/get
- **Expected:** Password not included in response
- **Actual:** Password returned in plain text

### BUG-006: Duplicate screenName allowed on update
- **Endpoint:** PATCH /player/update/{editor}/{id}
- **Expected:** 400
- **Actual:** 200

### BUG-007: Invalid password accepted on update
- **Endpoint:** PATCH /player/update/{editor}/{id}
- **Expected:** 400 (password too short / too long / no digits / no letters)
- **Actual:** 200

### BUG-008: User role can delete players
- **Endpoint:** DELETE /player/delete/{editor}
- **Expected:** 403
- **Actual:** 204

### BUG-009: Age above maximum accepted on create
- **Endpoint:** GET /player/create/{editor}
- **Expected:** 400 (age=60, should be younger than 60)
- **Actual:** 200

### BUG-010: Invalid age accepted on update
- **Endpoint:** PATCH /player/update/{editor}/{id}
- **Expected:** 400 (age outside 17-59 range)
- **Actual:** 200
