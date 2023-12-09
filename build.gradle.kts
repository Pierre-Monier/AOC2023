plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.register("generateDay") {
    group = "utils"

    val year = "2023"
    val session = "53616c7465645f5f2f6e0df735a752c6e77fee338ea56bdb58ab140584c5a0ed20760a559faa550050e052bbc520a2662da3608d4842219331648c602602b415"

    val folder = "src/main/kotlin"
    val dayNumber = "08"
    val dayInt = dayNumber.toInt()
    val dayFileName = "$folder/Day$dayNumber.kt"
    val dayFileContent = """
        class Day$dayNumber : GenericDay($dayInt) {
            override fun parseInput() {
            // TODO
            }
            
            override fun solvePart1(): Int {
              return 0
            }
            
            override fun solvePart2(): Int {
              return 0
            }
        }
    """.trimIndent()
    val dayFile = File(dayFileName)

    val mainFilePath = "$folder/Main.kt"
    val mainFileContent = """
        fun main(args: Array<String>) {
            val day = Day$dayNumber()
            day.printSolutions()
        }
    """.trimIndent()
    val mainFile = File(mainFilePath)

    if (dayFile.exists()) {
        throw GradleException("File already exist!")
    }

    dayFile.writeText(dayFileContent)
    mainFile.writeText(mainFileContent)

    val processBuilder = ProcessBuilder("curl", "-o", "input/aoc$dayNumber.txt", "https://adventofcode.com/$year/day/$dayInt/input", "-H", "cookie: session=$session")
    processBuilder.start().waitFor()
}

