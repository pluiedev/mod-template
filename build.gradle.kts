import java.net.URL
import java.net.HttpURLConnection

plugins {
    id("fabric-loom") version "0.11-SNAPSHOT"
    kotlin("jvm") version "1.6.10"
    `maven-publish`
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "TerraformersMC" }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(libs.fabric.yarn) {
        artifact {
            classifier = "v2"
        }
    }
    modImplementation(libs.bundles.fabric)

    modImplementation(libs.modmenu)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"

        // Minecraft 1.18 upwards uses Java 17.
        options.release.set(17)
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    jar {
        from("LICENSE") {
            val archivesBaseName: String by rootProject
            rename { "${it}_$archivesBaseName"}
        }
    }

    processResources {
        inputs.property("version", rootProject.version)

        filesMatching("fabric.mod.json") {
            expand("version" to rootProject.version)
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {

        }
    }
    repositories {
    }
}