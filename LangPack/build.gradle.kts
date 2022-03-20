plugins {
  id("java")
}

apply(plugin = "java")

val VERSION = "${findProperty("LANGPACK_CORE_VERSION")!!}"

group = "com.asledgehammer.crafthammer.langpack"
version = VERSION

repositories {
  mavenCentral()
  mavenLocal()
}
