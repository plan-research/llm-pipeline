plugins {
    kotlin("jvm") version "1.9.24"
    `maven-publish`
}

group = "org.jetbrains.plan"
version = "0.0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("runner") {
            groupId = "org.jetbrains.plan"
            artifactId = "experiment-runner"
            version = project.version.toString()

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/plan-research/kotlin-maven")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.token")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}



kotlin {
    jvmToolchain(17)
}