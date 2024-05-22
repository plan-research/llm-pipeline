plugins {
    kotlin("jvm") version "1.9.24"
    `maven-publish`
}

group = "org.jetbrains.plan"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("runner") {
            groupId = "org.jetbrains.plan"
            artifactId = "experiment-runner"
            version = "0.0.1"

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}



kotlin {
    jvmToolchain(17)
}