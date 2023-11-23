plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("nu.studer.jooq") version "8.2.1"
}

group = "tsvetkoff"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    implementation("org.liquibase:liquibase-core")
    implementation("net.javacrumbs.shedlock:shedlock-spring:5.9.1")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:5.9.1")
    implementation("org.jsoup:jsoup:1.16.2")
    implementation("org.webjars:bootstrap:5.3.2")
    implementation("org.webjars:popper.js:2.11.7")
    implementation("org.webjars:highcharts:11.1.0")

    jooqGenerator("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql:1.19.1")
    testImplementation("org.testcontainers:junit-jupiter")
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName = "${project.name}.jar"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    version.set("3.18.4")
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = System.getenv("DB_JDBC_URL")
                    user = System.getenv("DB_USERNAME")
                    password = System.getenv("DB_PASSWORD")
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        includes = "public.bank | " +
                                "public.currency |" +
                                "public.rate"
                        excludes = ""
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = true
                        isDaos = false
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "tsvetkoff.currencyrates.jooq.main"
                        directory = "src/generated/jooq/main"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
