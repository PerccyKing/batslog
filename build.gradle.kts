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
        releases()
        marketplace()
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
            untilBuild = "243.*"
        }
    }

}

dependencies {
    intellijPlatform {


        intellijIdeaUltimate("2022.3")


//        instrumentationTools()

//        javaCompiler("17")

        bundledPlugin("com.intellij.database")
        pluginVerifier()
        zipSigner()
        instrumentationTools()

//        pluginModule(implementation("com.intellij.database"))
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


//tasks {
//    // Set the JVM compatibility versions
//    withType<JavaCompile> {
//        sourceCompatibility = "17"
//        targetCompatibility = "17"
//    }
//    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//        kotlinOptions.jvmTarget = "17"
//    }
//
//    patchPluginXml {
//        sinceBuild.set("223")
//        untilBuild.set("243.*")
//    }
//
//    signPlugin {
//        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
//        privateKey.set(System.getenv("PRIVATE_KEY"))
//        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
//    }
//
//    publishPlugin {
//        token.set(System.getenv("PUBLISH_TOKEN"))
//    }
//}
