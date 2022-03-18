plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version("1.6.10")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.compileKotlin {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
        }
    }
}

subprojects {

    apply(plugin = "java")

    dependencies {
        implementation("org.fusesource.jansi:jansi:2.4.0")
        implementation("org.joml:joml:1.10.3")
        implementation("org.yaml:snakeyaml:1.29")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}
