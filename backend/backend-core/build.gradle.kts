plugins {}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.0")
    }
}

dependencies {
    implementation(project(":backend-persistence"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.orm:hibernate-spatial:6.6.0.Final")

    testImplementation("org.junit.jupiter:junit-jupiter-params")
}
