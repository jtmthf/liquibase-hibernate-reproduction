import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("plugin.jpa") version "1.2.71"
    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    id("org.liquibase.gradle") version "2.0.1"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
}

group = "com.jtmthf"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.postgresql:postgresql")
    liquibaseRuntime("org.liquibase:liquibase-core:3.6.3")
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate5:3.7")
    liquibaseRuntime(sourceSets.main.get().compileClasspath)
    liquibaseRuntime("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

if (!project.hasProperty("runList")) {
    project.ext.set("runList", "main")
}

project.ext.set(
    "diffChangelogFile",
    "src/main/resources/db/changelog/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_changelog.xml"
)

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "driver" to "org.postgresql.Driver",
            "url" to "jdbc:postgresql://localhost:5432/demo",
            "username" to "demo",
            "password" to "demo_password",
            "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.xml",
            "defaultSchemaName" to "",
            "logLevel" to "debug",
            "classpath" to "src/main/resources/"
        )
    }
    activities.register("diffLog") {
        this.arguments = mapOf(
            "driver" to "org.postgresql.Driver",
            "url" to "jdbc:postgresql://localhost:5432/demo",
            "username" to "demo",
            "password" to "demo_password",
            "changeLogFile" to project.ext.get("diffChangelogFile"),
            "referenceUrl" to "hibernate:spring:com.jtmthf.liquibasehibernatereproduction?" +
                    "dialect=org.hibernate.dialect.PostgreSQL95Dialect&" +
                    "hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy&" +
                    "hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
            "defaultSchemaName" to "",
            "logLevel" to "debug",
            "classpath" to "$buildDir/classes/kotlin/main"
        )
    }

    runList = project.ext.get("runList")
}