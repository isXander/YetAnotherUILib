plugins {
    java

    alias(libs.plugins.loom)

//    alias(libs.plugins.minotaur)
//    alias(libs.plugins.cursegradle)
//    alias(libs.plugins.github.release)
//    alias(libs.plugins.machete)
    alias(libs.plugins.grgit)
    `maven-publish`
}

group = "dev.isxander"
version = "1.0.0"

val buildTarget = libs.versions.minecraft.get()
val snapshot = "${grgit.branch.current().name.replace('/', '.')}-SNAPSHOT"

version = if (System.getenv().containsKey("GITHUB_ACTIONS")) {
    "$version+$snapshot"
} else {
    "$version+$buildTarget"
}

val testmod = sourceSets.create("testmod") {
    compileClasspath += sourceSets.main.get().runtimeClasspath
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

loom {
    runs {
        register("testmod") {
            client()
            ideConfigGenerated(true)
            name("Test Mod")
            source(testmod)
        }
    }

    createRemapConfigurations(testmod)
}

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.quiltmc.org/repository/release")
    maven("https://jitpack.io")
    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
}

val minecraftVersion = libs.versions.minecraft.get()

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.20.2:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.fabric.loader)

    modImplementation(libs.fabric.api)

    modImplementation(libs.mod.menu)

    libs.bundles.twelvemonkeys.imageio.let {
        implementation(it)
        include(it)
    }
    
    libs.mixin.extras.let {
        implementation(it)
        annotationProcessor(it)
        include(it)
        // "clientAnnotationProcessor"(it) // DO NOT FORGET THIS IF SPLIT SOURCEES
    }
}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project
        val githubProject: String by project

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubProject)

        filesMatching(listOf("fabric.mod.json", "quilt.mod.json")) {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubProject,
            )
        }
    }
    
    remapJar {
        archiveClassifier.set("fabric-$minecraftVersion")   
    }
    
    remapSourcesJar {
        archiveClassifier.set("fabric-$minecraftVersion-sources")   
    }

    register("releaseMod") {
        group = "mod"

//        dependsOn("modrinth")
//        dependsOn("modrinthSyncBody")
//        dependsOn("curseforge")
//        dependsOn("publish")
//        dependsOn("githubRelease")
    }
}

//machete {
//    json.enabled.set(false)
//}

java {
    withSourcesJar()
    withJavadocJar()
}

//val changelogText = file("changelogs/${project.version}.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
//
//val modrinthId: String by project
//if (modrinthId.isNotEmpty()) {
//    modrinth {
//        token.set(findProperty("modrinth.token")?.toString())
//        projectId.set(modrinthId)
//        versionNumber.set("${project.version}")
//        versionType.set("release")
//        uploadFile.set(tasks["remapJar"])
//        gameVersions.set(listOf("1.20.1"))
//        loaders.set(listOf("fabric", "quilt"))
//        changelog.set(changelogText)
//        syncBodyFrom.set(file("README.md").readText())
//    }
//}
//
//val curseforgeId: String by project
//if (hasProperty("curseforge.token") && curseforgeId.isNotEmpty()) {
//    curseforge {
//        apiKey = findProperty("curseforge.token")
//        project(closureOf<me.hypherionmc.cursegradle.CurseProject> {
//            mainArtifact(tasks["remapJar"], closureOf<me.hypherionmc.cursegradle.CurseArtifact> {
//                displayName = "${project.version}"
//            })
//
//            id = curseforgeId
//            releaseType = "release"
//            addGameVersion("1.20.2")
//            addGameVersion("Quilt")
//            addGameVersion("Fabric")
//            addGameVersion("Java 17")
//
//            changelog = changelogText
//            changelogType = "markdown"
//        })
//
//        options(closureOf<me.hypherionmc.cursegradle.Options> {
//            forgeGradleIntegration = false
//        })
//    }
//}
//
//githubRelease {
//    token(findProperty("github.token")?.toString())
//
//    val githubProject: String by project
//    val split = githubProject.split("/")
//    owner(split[0])
//    repo(split[1])
//    tagName("${project.version}")
//    targetCommitish("1.20.x/dev")
//    body(changelogText)
//    releaseAssets(tasks["remapJar"].outputs.files)
//}

publishing {
    publications {
        create<MavenPublication>("mod") {
            groupId = "dev.isxander"
            artifactId = "yet-another-ui-lib"

            artifact(tasks["remapJar"])
            artifact(tasks["remapSourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }

    repositories {
        val username = "XANDER_MAVEN_USER".let { System.getenv(it) ?: findProperty(it) }?.toString()
        val password = "XANDER_MAVEN_PASS".let { System.getenv(it) ?: findProperty(it) }?.toString()
        if (username != null && password != null) {
            maven(url = "https://maven.isxander.dev/releases") {
                name = "XanderReleases"
                credentials {
                    this.username = username
                    this.password = password
                }
            }

            maven(url = "https://maven.isxander.dev/snapshots") {
                name = "XanderSnapshots"
                credentials {
                    this.username = username
                    this.password = password
                }
            }
        } else {
            println("Xander Maven credentials not satisfied.")
        }
    }
}


