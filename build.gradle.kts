plugins {
    kotlin("jvm") version "1.9.25"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }
}
