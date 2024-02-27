plugins {
    application
    eclipse
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.hbhbnr.OAuth2Example"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
