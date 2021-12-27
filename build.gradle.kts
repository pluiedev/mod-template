import java.net.URL
import java.net.HttpURLConnection

plugins {
    id("fabric-loom") version "0.10-SNAPSHOT"
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
            // add all the jars that should be included when publishing to maven
            artifact(tasks["sourcesJar"]) {
                classifier = "sources"
                builtBy(tasks.remapSourcesJar)
            }
        }
    }
    repositories {
        mavenLocal()
    }
}


/*
// Temporary hack since TerraformersMC's maven is straight up cursed
val terraformersMaven: String
    get() {
        val terraformersUrl = "https://maven.terraformersmc.com/"
        return if (pingUrl(terraformersUrl))
            terraformersUrl
        else
            "https://maven.kotlindiscord.com/repository/terraformers/"
    }


fun pingUrl(address: String) = try {
    val conn = URL(address).openConnection() as HttpURLConnection
    val responseCode = conn.responseCode
    responseCode in 200..399
} catch (ignored: java.io.IOException) {
    false
}
*/
