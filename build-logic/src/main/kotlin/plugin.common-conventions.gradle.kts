import com.diffplug.gradle.spotless.FormatExtension
import java.util.*

plugins {
  `java-library`
  id("net.kyori.indra")
  id("net.kyori.indra.checkstyle")
  id("com.diffplug.spotless")
}

val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

indra {
  javaVersions {
    target(21)
    minimumToolchain(21)
    strictVersions(true)
  }
   checkstyle(libs.versions.checkstyle.get())
}

repositories {
  mavenCentral()
  mavenLocal()
  maven("https://nexus.minecast.us/repository/snapshots/")
}

dependencies {
  checkstyle(libs.stylecheck)
  compileOnly("org.jetbrains:annotations:22.0.0")
}

spotless {
  fun FormatExtension.applyCommon() {
    trimTrailingWhitespace()
    endWithNewline()
    indentWithSpaces(2)
  }
  java {
    importOrderFile(rootProject.file(".spotless/plugin.importorder"))
    removeUnusedImports()
    applyCommon()
  }
  kotlinGradle {
    applyCommon()
  }
}

tasks {
  jar {
    manifest {
      attributes(
        "Specification-Version" to project.version,
        "Specification-Vendor" to "espryth-groups",
        "Implementation-Build-Date" to Date()
      )
    }
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  compileJava {
    options.encoding = Charsets.UTF_8.name()
    dependsOn(spotlessApply)
    dependsOn(checkstyleMain)
    options.compilerArgs.add("-parameters")
  }
}
