plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm")
}

apply(plugin = "java")

val VERSION = "${findProperty("LANGPACK_CORE_VERSION")!!}"

group = "com.asledgehammer.crafthammer"
version = VERSION

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation(project(":CraftHammer_Util"))
}

tasks.register<Copy>("binClass") {
  doFirst {
    val dir = File("${projectDir}/../bin/class")
    if (!dir.exists()) dir.mkdirs()
  }
  from(
    layout.buildDirectory.dir("./classes/java/main"),
    layout.buildDirectory.dir("./classes/kotlin/main"),
    layout.buildDirectory.dir("./resources/")
  )
  into(layout.projectDirectory.dir("../bin/class"))
  exclude("**/META-INF")
}
