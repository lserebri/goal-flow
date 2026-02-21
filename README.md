# Goal Flow

A productivity tracker that gamifies time management. You create goals (good habits) and distractions (bad habits), log time spent on each, and earn or lose XP accordingly. The app uses exponential level progression-early levels come quick, higher levels take real effort.

## Demo
[goal-flow demo.webm](https://github.com/user-attachments/assets/9e38c130-18da-48a8-948e-9c7e368905a6)

## Tech Stack

**UI:** Jetpack Compose with Material3. Horizontal pager for tab navigation, custom animated circular progress bar for level display.

**Architecture:** MVVM with a repository layer. ViewModels use Hilt for DI and expose state via `StateFlow`. Room handles persistence with reactive `Flow` queries.

**Testing:**
Unit tests with JUnit and MockK

## Running Locally

Standard Android Studio setup. Clone, open, sync Gradle, run. Database uses destructive migration, so schema changes wipe data.
