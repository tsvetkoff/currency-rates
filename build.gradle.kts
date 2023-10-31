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

val shedlockVersion = "5.9.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.liquibase:liquibase-core")
    implementation("net.javacrumbs.shedlock:shedlock-spring:${shedlockVersion}")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:${shedlockVersion}")

    jooqGenerator("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
                    url = System.getenv('DB_JDBC_URL')
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
                        isDaos = true
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
