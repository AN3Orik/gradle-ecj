import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version("0.16.3")
    id("org.jetbrains.kotlin.jvm") version("2.0.20")
    id("org.jetbrains.kotlin.plugin.sam.with.receiver") version("2.0.20")
    id("com.gradle.plugin-publish") version("1.2.2")
}

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

group = "host.anzo.gradle.ecj"
version = "1.2"

kotlin {
    explicitApi()

    target {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    apiVersion = KotlinVersion.KOTLIN_1_7
                    languageVersion = KotlinVersion.KOTLIN_1_7
                }
            }
        }

        compilations.named("main").configure {
            compileTaskProvider.configure {
                compilerOptions {
                    apiVersion = KotlinVersion.KOTLIN_1_7
                    languageVersion = KotlinVersion.KOTLIN_1_7
                }
            }
        }
    }
}

gradlePlugin {
    website = "https://github.com/AN3Orik/gradle-ecj"
    vcsUrl = "https://github.com/AN3Orik/gradle-ecj.git"

    plugins {
        register("ecj") {
            id = "host.anzo.gradle.ecj"
            displayName = "Gradle Eclipse Compiler for Java Plugin"
            description = "A Gradle plugin for fast Java files compiling using the Eclipse Compiler for Java (ECJ)"
            tags.addAll("compile", "ecj", "eclipse compiler for java", "java")

            implementationClass = "host.anzo.gradle.ecj.plugins.ECJPlugin"
        }
    }
}

samWithReceiver {
    annotation("org.gradle.api.HasImplicitReceiver")
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = 17
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    withType<Jar>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        includeEmptyDirs = false
    }
}