import com.moowork.gradle.node.npm.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.github.node-gradle.node") version "2.2.4"
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}


/**
 * Node.js
 */

node {
    version = "12.19.0"
    npmVersion = "6.14.8"
    download = true
}

// Task for installing frontend dependencies in web
val npmInstallDependencies by tasks.registering(NpmTask::class) {
    setArgs(listOf("install"))
    setExecOverrides(closureOf<ExecSpec> {
        setWorkingDir("./frontend")
    })
}

// Task for executing build:gradle in web
val npmRunBuild by tasks.registering(NpmTask::class) {
    // Before buildWeb can run, installDependencies must run
    dependsOn(npmInstallDependencies)

    setArgs(listOf("run", "build", "--", "--dest", "../src/main/resources/static"))
    setExecOverrides(closureOf<ExecSpec> {
        setWorkingDir("./frontend")
    })
}


/**
 * Heroku
 */

val herokuStageBuildFrontend by tasks.registering {
    group = "heroku"
    dependsOn(npmRunBuild)
    doLast {
        delete("frontend/node_modules") // npm run buildを実行して不要になったnode_modulesディレクトリを削除してSlugサイズを稼ぐ
    }
}

val herokuStageBuild by tasks.registering {
    group = "heroku"
    dependsOn("bootJar")
    mustRunAfter(herokuStageBuildFrontend) // フロントエンドのビルドファイルをboot-jaeに含めるため先に実行させる
}

val herokuStage by tasks.registering {
    group = "heroku"
    dependsOn(herokuStageBuild)
    dependsOn(herokuStageBuildFrontend)
}
