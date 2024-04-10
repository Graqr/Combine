import java.util.*

// plugins -----------------------
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("groovy")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.5"
}

// repos --------------------------
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/graqr/threshr")
        credentials {
            username = System.getenv("USERNAME")
            password = System.getenv("TOKEN")
        }
    }
}

//dependencies --------------------
dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("com.graqr.threshr:0.0.11")
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("io.micronaut:micronaut-http-client")
}

// properties --------------------
version = "0.0.1"
group = "com.graqr"
var jdkVersion = "17"
val kotlinVersion = project.properties["kotlinVersion"]

// configuration -----------------
application { mainClass.set("com.graqr.ApplicationKt") }
java { sourceCompatibility = JavaVersion.toVersion(jdkVersion) }
graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("spock2")
    processing {
        incremental(true)
        annotations("com.graqr.*")
    }
}

// tasks --------------------------
tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>("dockerfile") {
    baseImage("eclipse-temurin:$jdkVersion-jre-jammy")
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") { jdkVersion.set(jdkVersion) }
task("loadEnv") {
    val envVars = Properties()
    file(".env").reader().use { reader -> envVars.load(reader) }
    envVars.forEach { key, value ->
        System.setProperty(key.toString(), value.toString())
    }
    println("Loaded environment variables from .env")
}


