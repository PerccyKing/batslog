import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "cn.com.pism"
version = "${project.findProperty("CUSTOM_VERSION") ?: LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HHmm"))}-${project.findProperty("PUBLISH")}"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2019.2")
    type.set("IU") // Target IDE Platform

    plugins.set(listOf("com.intellij.database"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    patchPluginXml {
        sinceBuild.set("192")
        untilBuild.set("241.*")
    }

}
dependencies {
    implementation(kotlin("script-runtime"))
    implementation("junit:junit:4.13.2")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.22")
    implementation("com.alibaba:druid:1.2.15")
    implementation("cn.hutool:hutool-http:5.8.11")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.slf4j:slf4j-api:2.0.5")
    compileOnly("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}
