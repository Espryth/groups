plugins {
  id("plugin.common-conventions")
  id("com.github.johnrengelman.shadow") version ("8.1.1")
  id("io.papermc.paperweight.userdev") version ("1.7.1")
}

dependencies {
  compileOnlyApi(libs.paper)
  paperweight.paperDevBundle(libs.versions.paper)
  api(libs.jdbi)
  api(libs.guice)
  api(libs.command)
  api(libs.hikari)
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

}
