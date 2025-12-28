# Advent of Code 2025 (Kotlin)

This repository contains a clean, extensible **Kotlin framework** for solving **Advent of Code 2025**, using the following interface:

```kotlin
interface Aoc2025 {
    fun setInput(lines: List<String>)
    fun solvePart1(): String
    fun solvePart2(): String
}
```

Each day’s solution implements this interface and **registers itself automatically** via a central registry.

---

## 📦 Project Structure

```
kt-advent-of-code-2025/
│
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
│
├── input/                      # cached input files (auto-created)
│     └── day01.txt …
│
├── src/main/kotlin/aoc2025/
│   ├── Main.kt                 # CLI entry point
│   ├── net/
│   │     └── AocNet.kt         # input loading + online fetch
│   ├── registry/
│   │     ├── Solution.kt       # Aoc2025 interface
│   │     └── DayRegistry.kt    # day auto-registration
│   └── days/
│         ├── Day01.kt
│         ├── …
│         └── Day12.kt
│
├── src/test/kotlin/aoc2025/
│   └── days/                   # JUnit 2 tests per day
│
└── src/jmh/kotlin/aoc2025/
    └── bench/                  # JMH full-pipeline benchmarks
```

---

## 🚀 Running Solutions

Run a **single day**:

```bash
./gradlew run --args="1"
```

Run **multiple days**:

```bash
./gradlew run --args="1 4 5"
```

The CLI accepts any number of days between **1–12**.

Each requested day will:
1. Load its input
2. Run Part 1
3. Run Part 2
4. Print results in order

---

## 🌐 Automatic Input Download from adventofcode.com

This framework supports **automatic input downloading** using your personal Advent of Code **session cookie**.

Advent of Code does **not** provide OAuth or an official API.
Even when signing in via Google/GitHub, AoC internally authenticates using a cookie named:

```
session=YOUR_SESSION_TOKEN
```

If this cookie is included in HTTP requests, puzzle input can be fetched programmatically.

---

## 🔑 How to Retrieve Your Session Token

1. Log in to: https://adventofcode.com/
2. Open browser developer tools  
   - **Safari**: ⌥ Option + ⌘ Command + I  
   - **Chrome**: F12 → Application tab  
   - **Firefox**: F12 → Storage tab
3. Go to **Cookies → https://adventofcode.com**
4. Find the cookie named: `session`
5. Copy its value (a long hex-like string)

⚠️ **This token is private.**  
Do **not** commit it to Git or share it.

---

## 🧷 Enabling Automatic Download

Set the following environment variables:

```bash
export AOC_SESSION="your-session-token"
export AOC_ONLINE=1
```

Now, when you run:

```bash
./gradlew run --args="1"
```

the program will:

1. Attempt to download  
   ```
   https://adventofcode.com/2025/day/1/input
   ```
2. Save it to  
   ```
   input/day01.txt
   ```
3. Use the cached file for future runs

If downloading fails, the framework **falls back to local files** automatically.

---

## 🧪 Unit Tests

Each day has:
- **Example-based JUnit 5 tests**
- Located in `src/test/kotlin/aoc2025/days`
- Uses shared helpers like `splitLines`

Run all tests:

```bash
./gradlew test
```

---

## ⏱️ Benchmarks (JMH)

Benchmarks are implemented using **JMH** and measure the **full pipeline**:

```
setInput → solvePart1 → solvePart2
```

Benchmarks live in:

```
src/jmh/kotlin/aoc2025/bench
```

Run all benchmarks:

```bash
./gradlew jmh
```

> Inputs must be downloaded first so benchmarks use cached files.

---

## 📊 Benchmark Summary — Apple M4 (macOS / arm64)

| Day | Full Pipeline (µs) |
| --- | ------------------ |
| 01  | 330                |
| 02  | 13                 |
| 03  | 71                 |
| 04  | 736                |
| 05  | 50                 |
| 06  | 152                |
| 07  | 27                 |
| 08  | 98 700             |
| 09  | 37 300             |
| 10  | 99 200             |
| 11  | 179                |
| 12  | 414                |

> All benchmarks measure **real AoC workloads** using actual input files.

---

## 🧩 Design Goals

- Zero boilerplate per day
- Automatic day registration
- Deterministic, testable solutions
- Clear separation of parsing vs solving
- Realistic performance benchmarking
- Idiomatic Kotlin (no Java-style noise)
