plugins {
    kotlin("jvm") version "2.0.0"
    application
    id("me.champeau.jmh") version "0.7.2"
}

application {
    mainClass.set("aoc2025.MainKt")
}

group = "aoc2025"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    jmh(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = false
    }
}

jmh {
    warmupIterations.set(1)
    iterations.set(3)
    fork.set(1)

    benchmarkMode.set(listOf("avgt"))
    timeUnit.set("us")
    timeOnIteration.set("10s")

    jvmArgs.set(
        listOf(
            "-Xms2g",
            "-Xmx2g",
            "-XX:+AlwaysPreTouch",
            "-XX:+UseG1GC"
        )
    )
}