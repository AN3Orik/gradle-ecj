import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.samwithreceiver)
    alias(libs.plugins.plugin.publish)
}

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    explicitApi()

    target {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    apiVersion = KotlinVersion.KOTLIN_2_2
                    languageVersion = KotlinVersion.KOTLIN_2_2
                }
            }
        }

        compilations.named("main").configure {
            compileTaskProvider.configure {
                compilerOptions {
                    apiVersion = KotlinVersion.KOTLIN_2_2
                    languageVersion = KotlinVersion.KOTLIN_2_2
                }
            }
        }
    }
}

group = "host.anzo.gradle.ecj"
version = "1.0"

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
        options.release = 22
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_22
        }
    }

    withType<Jar>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        includeEmptyDirs = false
    }
}

dependencies {
    compileOnlyApi(kotlin("stdlib"))
}