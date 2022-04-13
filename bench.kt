import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.lang.ProcessBuilder
import kotlin.system.measureTimeMillis

val difficulties = listOf("easy", "medium", "hard", "extreme")

fun main() {
  for (difficulty in difficulties) {
    val files = Files.newDirectoryStream(Paths.get("instances", difficulty))
    for (file in files) {
      val executionTime = measureTimeMillis { solve(file) }.toDouble() / 1000.0
      println("$file\t${executionTime}s")
    }
  }
}

fun solve(board: Path) {
  ProcessBuilder("java", "-cp", "build/libs/sudoku-solver-1.0-SNAPSHOT.jar", "AppKt", board.toString()).start().waitFor()
}
