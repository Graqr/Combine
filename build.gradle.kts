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
}
// properties --------------------
version = "0.0.1"
group = "com.graqr"
var jdkVersion = "17"
var threshrVersion = "0.0.12"
val kotlinVersion = project.properties["kotlinVersion"]

//dependencies --------------------
dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("com.graqr:threshr:${threshrVersion}") {
        exclude(group = "org.apache.maven.reporting", module = "maven-reporting-api")
    }
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("io.micronaut:micronaut-http-client")
}

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



