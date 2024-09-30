rootProject.name = "groups"

pluginManagement {
    includeBuild("build-logic")
}

sequenceOf("plugin").forEach {
    include("${rootProject.name}-$it")
    project(":${rootProject.name}-$it").projectDir = file(it)
}
