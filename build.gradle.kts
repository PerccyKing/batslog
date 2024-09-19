import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.intellij.platform") version "2.0.1"
}

group = "cn.com.pism"
version = "${
    project.findProperty("CUSTOM_VERSION")?.takeIf { it.toString().isNotBlank() } ?: LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yy.MM.dd.HHmm"))
}-${project.findProperty("PUBLISH")}"
println(version)


repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
        intellijDependencies()
        releases()
        marketplace()
        snapshots()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    pluginConfiguration {
        name = "batslog"
    }

    pluginConfiguration {

        ideaVersion {
            sinceBuild = "223"
//            sinceBuild = "243"
            untilBuild = "243.*"
        }
    }

}

dependencies {
    intellijPlatform {


//        intellijIdeaUltimate("243.12818.47")
        intellijIdeaUltimate("2022.3")
        bundledPlugin("com.intellij.database")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }



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

