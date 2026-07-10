plugins {
    java
    id("org.springframework.boot") version "4.0.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.parnasit.bas"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.46")
        annotationProcessor("org.projectlombok:lombok:1.18.46")
        testCompileOnly("org.projectlombok:lombok:1.18.46")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.46")

        testImplementation(platform("org.junit:junit-bom:5.11.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.mockito:mockito-core:5.14.0")
        testImplementation("org.mockito:mockito-junit-jupiter:5.14.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
