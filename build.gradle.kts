import java.net.URL
import java.nio.channels.Channels
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.ZipFile

plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf(file("src")))
            outputDir = file("$buildDir/classes")
        }
    }
}

repositories {
    jcenter()
}

dependencies {
    implementation(fileTree("libs"))
    implementation("com.weblookandfeel:weblaf-ui:1.2.13")
}

val libsDirectory = file("libs")
val mapsDirectory = file("maps")

fun cleanEngine() {
    if (libsDirectory.exists()) {
        libsDirectory.deleteRecursively()
        println("Deleted old libs")
    }

    if (mapsDirectory.exists()) {
        mapsDirectory.deleteRecursively()
        println("Deleted old maps")
    }
}

fun updateEngine() {
    cleanEngine()

    val engineFile = file("$buildDir/engine.zip")
    if (engineFile.exists()) {
        engineFile.delete()
        println("Deleted old engine")
    } else {
        engineFile.parentFile.mkdirs()
    }

    println("Downloading latest engine")

    URL("https://www.coliseum.ai/resources/scaffold/AIC2020.zip").openStream().use { inputStream ->
        engineFile.outputStream().use { outputStream ->
            val outputChannel = outputStream.channel
            val inputChannel = Channels.newChannel(inputStream)

            outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE)
        }
    }

    ZipFile(engineFile).use { zip ->
        zip.entries().asIterator().forEach { entry ->
            if (entry.isDirectory) {
                return@forEach
            }

            val outputDirectory = when {
                entry.name.startsWith("AIC2020/client") -> "libs"
                entry.name.startsWith("AIC2020/jars") -> "libs"
                entry.name.startsWith("AIC2020/maps") -> "maps"
                else -> return@forEach
            }

            val fileName = entry.name.split("/").last()
            val outputFile = file("$outputDirectory/$fileName")

            if (!outputFile.exists()) {
                outputFile.parentFile.mkdirs()
                outputFile.createNewFile()
            }

            zip.getInputStream(entry).use { inputStream ->
                outputFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                    println("Created $outputFile")
                }
            }
        }
    }
}

tasks.named("compileJava") {
    dependsOn += "ensureEngine"
}

tasks.named("clean") {
    doLast {
        cleanEngine()
    }
}

tasks.withType<JavaExec> {
    classpath = sourceSets.main.get().runtimeClasspath
}

task("updateEngine") {
    group = "aic2020"
    description = "Downloads the latest version of the engine and unpacks it."

    doLast {
        updateEngine()
    }
}

task("ensureEngine") {
    group = "aic2020"
    description = "Ensures a version of the engine has been downloaded."

    doLast {
        if (!libsDirectory.exists() || !mapsDirectory.exists()) {
            updateEngine()
        }
    }
}

task("instrument") {
    group = "aic2020"
    description = "Instrument both packages."

    dependsOn += "instrument1"
    dependsOn += "instrument2"
}

for (i in 1..2) {
    task<JavaExec>("instrument$i") {
        group = "aic2020"
        description = "Instrument package $i."

        dependsOn += "classes"

        val packageName = project.property("package$i").toString()

        classpath = sourceSets.main.get().runtimeClasspath
        main = "instrumenter.Main"
        args = listOf(file("$buildDir/classes/$packageName").absolutePath, packageName, "true")
    }
}

task<JavaExec>("run") {
    group = "aic2020"
    description = "Runs a match without starting the client."

    dependsOn += "instrument"

    main = "aic2020.main.Main"
    jvmArgs = listOf("-noverify")
    args = listOf(
            project.property("package1").toString(),
            project.property("package2").toString(),
            project.property("map").toString(),
            "1",
            "0"
    )
}

task<JavaExec>("runAndOpen") {
    group = "aic2020"
    description = "Runs a match and opens it in the client."

    main = "Client"

    doFirst {
        args = listOf(fileTree("games").maxBy { it.lastModified() }!!.absolutePath)
    }
}

task<JavaExec>("client") {
    group = "aic2020"
    description = "Opens the client."

    main = "Client"
}

task<Zip>("createSubmission") {
    group = "aic2020"
    description = "Creates a submission zip."

    dependsOn += "classes"

    from(file("src"))
    include("camel_case/**/*")
    archiveFileName.set("${DateTimeFormatter.ofPattern("yyyy-MM-dd_kk-mm-ss").format(LocalDateTime.now())}.zip")
    destinationDirectory.set(file("$buildDir/submissions"))
}
