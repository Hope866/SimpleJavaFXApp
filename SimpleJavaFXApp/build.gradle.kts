plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

javafx {
    version = "21.0.12"
    modules = listOf("javafx.controls")
}

application {
    mainModule = "com.example.customermanager"
    mainClass = "com.example.customermanager.HelloJavaFX"
}