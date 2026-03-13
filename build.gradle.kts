plugins {
    java
    id("io.qameta.allure") version "2.11.2"
}

group = "com.player"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

val restAssuredVersion = "5.4.0"
val testNgVersion = "7.9.0"
val allureVersion = "2.25.0"
val jacksonVersion = "2.16.1"

dependencies {
    // REST Assured
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")

    // TestNG
    implementation("org.testng:testng:$testNgVersion")

    // Allure
    implementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    implementation("io.qameta.allure:allure-testng:$allureVersion")

    // Jackson for JSON serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    // Owner for configuration
    implementation("org.aeonbits.owner:owner:1.0.12")

    // DataFaker for test data generation
    implementation("net.datafaker:datafaker:2.1.0")

    // SLF4J + Logback for logging
    implementation("org.slf4j:slf4j-api:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.test {
    useTestNG {
        suites("src/test/resources/testng.xml")
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}
