/*
 * Minosoft
 * Copyright (C) 2020-2022 Moritz Zwerger
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This software is not affiliated with Mojang AB, the original developer of Minecraft.
 */

import de.bixilon.kutil.os.Architectures
import de.bixilon.kutil.os.OSTypes
import de.bixilon.kutil.os.PlatformInfo
import org.ajoberstar.grgit.Grgit
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.7.20"
    `jvm-test-suite`
    application
    id("org.ajoberstar.grgit") version "2.3.0"
}

fun getProperty(name: String): String {
    val value = property(name) ?: throw NullPointerException("Can not find $name property")
    return value.toString()
}

group = "de.bixilon.minosoft"
version = "0.1-pre"
var stable = false

val javafxVersion = getProperty("javafx.version")
val lwjglVersion = getProperty("lwjgl.version")
val ikonliVersion = getProperty("ikonli.version")
val nettyVersion = getProperty("netty.version")
val jacksonVersion = getProperty("jackson.version")
val kutilVersion = getProperty("kutil.version")


repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

buildscript {
    dependencies {
        classpath("de.bixilon", "kutil", "1.17")
    }
}

var lwjglNatives = ""
var zstdNatives = ""
var javafxNatives = ""

when (PlatformInfo.OS) {
    OSTypes.LINUX -> {
        lwjglNatives += "linux"
        zstdNatives += "linux"
        javafxNatives += "linux"

        when (PlatformInfo.ARCHITECTURE) {
            Architectures.AMD64 -> {
                zstdNatives += "_amd64"
            }

            Architectures.AARCH64, Architectures.ARM -> {
                lwjglNatives += "-arm64"
                zstdNatives += "_aarch64"
                javafxNatives += "-aarch64"
            }

            else -> throw IllegalArgumentException("Can not determinate linux natives on ${PlatformInfo.ARCHITECTURE}")
        }
    }

    OSTypes.MAC -> {
        lwjglNatives += "macos"
        zstdNatives += "darwin"
        javafxNatives += "mac"

        when (PlatformInfo.ARCHITECTURE) {
            Architectures.AMD64, Architectures.X86 -> {
                zstdNatives += "_x86_64"
            }

            Architectures.AARCH64, Architectures.ARM -> {
                lwjglNatives += "-arm64"
                zstdNatives += "_aarch64"
                javafxNatives += "-aarch64"
            }

            else -> throw IllegalArgumentException("Can not determinate macos natives on ${PlatformInfo.ARCHITECTURE}")
        }
    }

    OSTypes.WINDOWS -> {
        lwjglNatives += "windows"
        zstdNatives += "win"
        javafxNatives += "win"

        when (PlatformInfo.ARCHITECTURE) {
            Architectures.AMD64 -> {
                zstdNatives += "_amd64"
            }

            Architectures.X86 -> {
                lwjglNatives += "-x86"
                zstdNatives += "-x86"
                javafxNatives += "-x86"
            }

            else -> throw IllegalArgumentException("Can not determinate windows natives on ${PlatformInfo.ARCHITECTURE}")
        }
    }

    else -> {
        throw IllegalArgumentException("Can not determinate natives for ${PlatformInfo.OS} on ${PlatformInfo.ARCHITECTURE}")
    }
}


testing {
    suites {

        val test by getting(JvmTestSuite::class) {
            testType.set(TestSuiteType.UNIT_TEST)
            useJUnitJupiter()

            dependencies {
                implementation(project)
                // implementation("org.jetbrains.kotlin:kotlin-test:1.7.20")
                implementation("de.bixilon:kutil:$kutilVersion")
                implementation("org.jetbrains.kotlin:kotlin-test:1.7.20")
            }

            targets {
                all {
                    testTask.configure {
                        filter {
                            isFailOnNoMatchingTests = true
                        }
                        testLogging {
                            exceptionFormat = TestExceptionFormat.FULL
                            showExceptions = true
                            showStandardStreams = true
                            events(
                                TestLogEvent.PASSED,
                                TestLogEvent.FAILED,
                                TestLogEvent.SKIPPED,
                                TestLogEvent.STANDARD_OUT,
                                TestLogEvent.STANDARD_ERROR,
                            )
                        }
                    }
                }
            }
            sources {
                kotlin {
                    setSrcDirs(listOf("src/test/java"))
                }
            }
        }


        val integrationTest by registering(JvmTestSuite::class) {
            testType.set(TestSuiteType.INTEGRATION_TEST)
            useTestNG()

            dependencies {
                implementation(project)
                // implementation("org.jetbrains.kotlin:kotlin-test:1.7.20")

                implementation("org.objenesis:objenesis:3.3")

                // ToDo: Include dependencies from project
                implementation("de.bixilon:kutil:$kutilVersion")
                implementation("de.bixilon:kotlin-glm:0.9.9.1-6")
            }

            targets {
                all {
                    testTask.configure {
                        filter {
                            isFailOnNoMatchingTests = true
                        }
                        testLogging {
                            exceptionFormat = TestExceptionFormat.FULL
                            showExceptions = true
                            showStandardStreams = true
                            events(
                                TestLogEvent.PASSED,
                                TestLogEvent.FAILED,
                                TestLogEvent.SKIPPED,
                                TestLogEvent.STANDARD_OUT,
                                TestLogEvent.STANDARD_ERROR,
                            )
                        }
                        options {
                            val options = this as TestNGOptions
                            options.preserveOrder = true
                        }
                        shouldRunAfter(test)
                    }
                }
            }
            sources {
                kotlin {
                    setSrcDirs(listOf("src/integration-test/kotlin", "src/test-util/kotlin"))
                }
            }
        }
        val benchmarkTest by registering(JvmTestSuite::class) {
            testType.set(TestSuiteType.PERFORMANCE_TEST)
            useTestNG()

            dependencies {
                implementation(project)
                // implementation("org.jetbrains.kotlin:kotlin-test:1.7.20")

                implementation("org.objenesis:objenesis:3.3")

                // ToDo: Include dependencies from project
                implementation("de.bixilon:kutil:$kutilVersion")
                implementation("de.bixilon:kotlin-glm:0.9.9.1-6")
            }

            targets {
                all {
                    testTask.configure {
                        filter {
                            isFailOnNoMatchingTests = false
                        }
                        testLogging {
                            exceptionFormat = TestExceptionFormat.FULL
                            showExceptions = true
                            showStandardStreams = true
                            events(
                                TestLogEvent.PASSED,
                                TestLogEvent.FAILED,
                                TestLogEvent.SKIPPED,
                                TestLogEvent.STANDARD_OUT,
                                TestLogEvent.STANDARD_ERROR,
                            )
                        }
                    }
                }
            }
            sources {
                kotlin {
                    setSrcDirs(listOf("src/benchmark/kotlin", "src/test-util/kotlin"))
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("test"), testing.suites.named("integrationTest"))
}

fun DependencyHandler.javafx(name: String) {
    implementation("org.openjfx", "javafx-$name", javafxVersion, classifier = javafxNatives) {
        version { strictly(javafxVersion) }
    }
}

fun DependencyHandler.ikonli(name: String) {
    implementation("org.kordamp.ikonli", "ikonli-$name", ikonliVersion)
}

fun DependencyHandler.jackson(group: String, name: String) {
    implementation("com.fasterxml.jackson.$group", "jackson-$group-$name", jacksonVersion)
}

fun DependencyHandler.netty(name: String) {
    implementation("io.netty", "netty-$name", nettyVersion)
}

fun DependencyHandler.lwjgl(name: String) {
    var artifactId = "lwjgl"
    if (name.isNotEmpty()) {
        artifactId += "-$name"
    }
    implementation("org.lwjgl", artifactId, lwjglVersion)
    runtimeOnly("org.lwjgl", artifactId, lwjglVersion, classifier = "natives-$lwjglNatives")
}

dependencies {
    implementation("org.slf4j", "slf4j-api", "2.0.3")
    implementation("com.google.guava", "guava", "31.1-jre")
    implementation("dnsjava", "dnsjava", "3.5.1")
    implementation("net.sourceforge.argparse4j", "argparse4j", "0.9.0")
    implementation("org.jline", "jline", "3.21.0")
    implementation("org.l33tlabs.twl", "pngdecoder", "1.0")
    implementation("com.github.oshi", "oshi-core", "6.2.2")
    implementation("com.github.luben", "zstd-jni", "1.5.2-3", classifier = zstdNatives)
    implementation("org.apache.commons", "commons-lang3", "3.12.0")
    implementation("org.kamranzafar", "jtar", "2.3")
    implementation("org.reflections", "reflections", "0.10.2")
    implementation("it.unimi.dsi", "fastutil-core", "8.5.9")


    // ikonli
    ikonli("fontawesome5-pack")
    ikonli("javafx")

    // jackson
    jackson("module", "kotlin")
    jackson("datatype", "jsr310")


    // de.bixilon
    implementation("de.bixilon", "kutil", kutilVersion)
    implementation("de.bixilon", "jiibles", "1.1.1")
    implementation("de.bixilon", "kotlin-glm", "0.9.9.1-6")
    implementation("de.bixilon", "mbf-kotlin", "0.2.1") { exclude("com.github.luben", "zstd-jni") }
    implementation("de.bixilon.javafx", "javafx-svg", "0.3") { exclude("org.openjfx", "javafx-controls") }

    // netty
    netty("buffer")
    netty("handler")


    // lwjgl
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    lwjgl("")
    lwjgl("glfw")
    lwjgl("openal")
    lwjgl("opengl")
    lwjgl("stb")

    // kotlin
    implementation(kotlin("reflect"))


    // platform specific
    if (PlatformInfo.OS == OSTypes.LINUX) {
        val nettyNatives = when (PlatformInfo.ARCHITECTURE) {
            Architectures.AMD64, Architectures.X86 -> "x86_64"
            Architectures.ARM, Architectures.AARCH64 -> "aarch64"
            else -> throw IllegalArgumentException("Can not determinate netty natives for ${PlatformInfo.ARCHITECTURE}")
        }
        implementation("io.netty", "netty-transport-native-epoll", nettyVersion, classifier = "linux-$nettyNatives")
    } else {
        compileOnly("io.netty", "netty-transport-native-epoll", nettyVersion)
    }

    // javafx
    javafx("base")
    javafx("graphics")
    javafx("controls")
    javafx("fxml")
}

tasks.test {
    useJUnitPlatform()
}

lateinit var git: Grgit

fun loadGit() {
    git = Grgit.open(mapOf("currentDir" to project.rootDir))
    val commit = git.log().first()
    val tag = git.tag.list().find { it.commit == commit }
    val nextVersion = if (tag != null) {
        stable = true
        tag.name
    } else {
        commit.abbreviatedId
    }
    if (project.version != nextVersion) {
        project.version = nextVersion
        println("Version changed to ${project.version}")
    }
}
loadGit()


val task = tasks.register("versions.json") {
    outputs.upToDateWhen { false }

    doFirst {
        loadGit()

        fun generateGit(): Map<String, Any> {
            val commit = git.log().first()
            return mapOf(
                "branch" to git.branch.current().name,
                "commit" to commit.id,
                "commit_short" to commit.abbreviatedId,
                "dirty" to git.status().isClean,
            )
        }

        val versionInfo: MutableMap<String, Any> = mutableMapOf(
            "general" to mutableMapOf(
                "name" to project.version,
                "stable" to stable,
            )
        )
        try {
            versionInfo["git"] = generateGit()
        } catch (exception: Throwable) {
            exception.printStackTrace()
        }
        val file = File(project.buildDir.path + "/resources/main/assets/minosoft/version.json")
        file.writeText(groovy.json.JsonOutput.toJson(versionInfo))
    }
}

tasks.getByName("processResources").finalizedBy(task)

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    // kotlinOptions.useK2 = true // ToDo: Really? boosts the performance a lot
}

application {
    mainClass.set("de.bixilon.minosoft.Minosoft")
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}-fat-${PlatformInfo.OS.name.toLowerCase()}-${PlatformInfo.ARCHITECTURE.name.toLowerCase()}")
    manifest {
        attributes["Implementation-Title"] = project.name.capitalized()
        attributes["Implementation-Version"] = project.version
        attributes["Main-Class"] = application.mainClass
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}