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
    implementation("com.auth0:mvc-auth-commons:1.+")
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
