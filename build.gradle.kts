plugins {
    kotlin("jvm") version "2.4.10"
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
