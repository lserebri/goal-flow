# Goal Flow

A productivity tracker that gamifies time management. You create goals (good habits) and distractions (bad habits), log time spent on each, and earn or lose XP accordingly. The app uses exponential level progression-early levels come quick, higher levels take real effort.

## Demo
[goalflow-demo.webm](https://github.com/user-attachments/assets/42bae15f-d8dc-4fab-9abc-78fd59335105)


## Tech Stack

**UI:** Jetpack Compose with Material3. Horizontal pager for tab navigation, custom animated circular progress bar for level display.

**Architecture:** MVVM with a repository layer. ViewModels use Hilt for DI and expose state via `StateFlow`. Room handles persistence with reactive `Flow` queries.

**Testing:**
Unit tests with JUnit and MockK

## Running Locally

Standard Android Studio setup. Clone, open, sync Gradle, run. Database uses destructive migration, so schema changes wipe data.
