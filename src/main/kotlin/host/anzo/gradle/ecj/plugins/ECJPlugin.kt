package host.anzo.gradle.ecj.plugins

import host.anzo.gradle.ecj.ECJConstants.ECJ_CONFIGURATION_NAME
import host.anzo.gradle.ecj.ECJConstants.MAIN
import host.anzo.gradle.ecj.ECJConstants.PREFERRED_JAVA_VERSION
import host.anzo.gradle.ecj.ECJConstants.REQUIRED_JAVA_VERSION
import host.anzo.gradle.ecj.ECJExtension
import host.anzo.gradle.ecj.internal.utils.applyTo
import org.gradle.api.*
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.*
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.compile.*
import org.gradle.jvm.toolchain.*
import org.gradle.process.CommandLineArgumentProvider

public class ECJPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = applyTo(target) project@ {
        /*
         * Make sure that the JavaPlugin is applied before this plugin, since we have to override some properties.
         * (Configuration happens in the same order in which the configuration actions are added.)
         */
        pluginManager.apply(JavaPlugin::class.java)

        val ecjExtension = extensions.create("ecj", ECJExtension::class.java)

        val ecjConfiguration = configurations.create(ECJ_CONFIGURATION_NAME) {
            isCanBeConsumed = false
            isCanBeResolved = true

            defaultDependencies {
                val compilerGroupId = ecjExtension.compilerGroupId.orNull ?: error("ECJ compilerGroupId may not be null")
                val compilerArtifactId = ecjExtension.compilerArtifactId.orNull ?: error("ECJ compilerArtifactId may not be null")
                val compilerVersion = ecjExtension.compilerVersion.orNull ?: error("ECJ compilerVersion may not be null")

                dependencies.add(target.dependencies.create("$compilerGroupId:$compilerArtifactId:$compilerVersion"))
            }
        }

        val java = extensions.getByType(JavaPluginExtension::class.java)
        val javaToolchains = extensions.getByType(JavaToolchainService::class.java)

        tasks.withType(JavaCompile::class.java).configureEach {
            /* ECJ does not support generating JNI headers. Make sure the property is not used. */
            options.headerOutputDirectory.set(provider { null })
            options.isFork = true
            options.forkOptions.jvmArgumentProviders.add(ECJCommandLineArgumentProvider(ecjConfiguration))

            val defaultJavaCompiler = provider {
                if (java.toolchain.languageVersion.orNull?.canCompileOrRun(REQUIRED_JAVA_VERSION) == true) {
                    javaToolchains.compilerFor(java.toolchain).orNull
                        ?: error("Could not get launcher for toolchain: ${java.toolchain}")
                } else {
                    javaToolchains.compilerFor {
                        languageVersion.set(JavaLanguageVersion.of(PREFERRED_JAVA_VERSION))
                    }.orNull ?: error("Could not provision launcher for Java $PREFERRED_JAVA_VERSION")
                }
            }

            this.javaCompiler.convention(defaultJavaCompiler)

            /* See https://docs.gradle.org/7.4.2/userguide/validation_problems.html#implementation_unknown */
            @Suppress("ObjectLiteralToLambda")
            doFirst(object : Action<Task> {
                override fun execute(t: Task) {
                    println("ECJ: Compiling project [${project.name}]...")

                    var lombokPath = ""
                    val it = options.annotationProcessorPath?.iterator();
                    if (it != null) {
                        while (it.hasNext()) {
                            val jar = it.next()
                            if (jar.path.contains("lombok-")) {
                                // Lombok support for ECJ
                                lombokPath = jar.path
                                break
                            }
                        }
                        // Annotation processing support
                        options.compilerArgs?.add("--processor-module-path")
                        options.compilerArgs?.add(options.annotationProcessorPath?.joinToString(";"))
                    }

                    val javacExecutable = javaCompiler.orElse(defaultJavaCompiler).get().executablePath.asFile

                    // We replace the "javac" part of the original executable name instead of simply resolving "java" to account for file extensions.
                    val javaExecutable = javacExecutable.resolveSibling(javacExecutable.name.replace("javac", "java"))

                    options.forkOptions.executable = javaExecutable.absolutePath

                    if (lombokPath.isNotEmpty()) {
                        // Lombok agent must be first
                        options.forkOptions.jvmArgs?.add(0, "-javaagent:$lombokPath=ECJ")
                    }
                }
            })
        }
    }

    private class ECJCommandLineArgumentProvider(@get:Classpath val compilerClasspath: FileCollection) : CommandLineArgumentProvider {
        override fun asArguments(): MutableIterable<String> {
            return mutableListOf("-classpath", compilerClasspath.asPath, MAIN, "-nowarn")
        }
    }
}