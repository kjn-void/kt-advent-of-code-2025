package aoc2025.net

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path

object AocNet {
    private const val YEAR = 2025

    fun loadDay(day: Int): List<String> {
        val path = Path.of("input/day%02d.txt".format(day))
        if (Files.exists(path)) {
            return Files.readAllLines(path)
        }

        val session = System.getenv("AOC_SESSION")
            ?: error("AOC_SESSION not set")

        val request = HttpRequest.newBuilder()
            .uri(URI("https://adventofcode.com/$YEAR/day/$day/input"))
            .header("Cookie", "session=$session")
            .header("User-Agent", "github.com/kt-advent-of-code-2025")
            .build()

        val client = HttpClient.newHttpClient()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            error("Failed to fetch input: ${response.statusCode()}")
        }

        Files.createDirectories(path.parent)
        Files.writeString(path, response.body())

        return response.body().trimEnd().lines()
    }
}