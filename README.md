# Gradle Eclipse Compiler for Java Plugin

[![License](https://img.shields.io/badge/license-MIT-green.svg?style=for-the-badge&label=License)](https://github.com/TheMrMilchmann/gradle-ecj/blob/master/LICENSE)
![Gradle](https://img.shields.io/badge/Gradle-8.10-green.svg?style=for-the-badge&color=1ba8cb&logo=Gradle)
![Java](https://img.shields.io/badge/Java-17-green.svg?style=for-the-badge&color=b07219&logo=Java)
![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/host.anzo.gradle.ecj?style=for-the-badge&logo=Gradle&label=Gradle%20Plugin)

A Gradle plugin for fast Java files compiling using the Eclipse Compiler for Java (ECJ).

This plugin is based on [Gradle Eclipse Compiler for Java Plugin](https://github.com/TheMrMilchmann/gradle-ecj) with just some minor changes:
* JDK 17+ support
* Gradle 8.10+ support
* Annotation processors support (including Lombok via agent)
* Disabled annoying ECJ compiling warning

## Usage

```groovy
plugins {
    id "host.anzo.gradle.ecj" version "1.2"
}

dependencies {
    ecj("org.eclipse.jdt:ecj:3.39.0")
}
```
