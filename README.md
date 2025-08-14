# Goal Flow

A productivity Android app that helps you track and manage your goals and distractions with a scoring system. Built with modern Android development technologies including Jetpack Compose, Room database, and Hilt dependency injection.

## 🎯 Overview

Goal Flow is a personal productivity app that uses a unique scoring system to help you stay focused on your goals while being mindful of distractions. The app features:

- **Goal Tracking**: Add and manage productive activities with customizable weights
- **Distraction Monitoring**: Track time-wasting activities and their impact
- **Scoring System**: Real-time score calculation based on time spent on goals vs distractions
- **Time Tracking**: Log time spent on activities with an intuitive time picker
- **Modern UI**: Clean, Material Design 3 interface built with Jetpack Compose

## ✨ Features

### Core Functionality
- **Dual Activity Management**: Separate tabs for Goals and Distractions
- **Weighted Scoring**: Each activity has a customizable weight that affects score calculation
- **Time Logging**: Track time spent on activities in hours and minutes
- **Real-time Score Updates**: Score changes immediately when you log activities
- **Activity Management**: Add, edit, and delete activities with confirmation dialogs

### User Interface
- **Material Design 3**: Modern, accessible UI components
- **Tab Navigation**: Easy switching between Goals and Distractions
- **Floating Action Button**: Quick access to add new activities
- **Responsive Layout**: Adapts to different screen sizes
- **Intuitive Interactions**: Tap activities to log time, swipe gestures for actions

### Data Persistence
- **Local Database**: Room database for offline data storage
- **Repository Pattern**: Clean separation of data access logic
- **Dependency Injection**: Hilt for managing dependencies

## 🏗️ Architecture

The app follows modern Android development best practices:

### Tech Stack
- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel) pattern
- **Database**: Room with Kotlin Coroutines
- **Dependency Injection**: Hilt
- **Navigation**: Compose Navigation
- **Testing**: JUnit, MockK, and Espresso

### Project Structure
```
app/src/main/java/com/example/goalflow/
├── data/                    # Data layer
│   ├── activity/           # Activity-related data models
│   ├── distraction/        # Distraction entity and repository
│   ├── goal/              # Goal entity and repository
│   ├── score/             # Score entity and repository
│   └── GoalFlowDatabase.kt # Room database configuration
├── di/                     # Dependency injection
│   └── AppModule.kt       # Hilt modules
├── ui/                     # UI layer
│   ├── activity/          # Activity list screens and view models
│   ├── components/        # Reusable UI components
│   ├── home/             # Main home screen and navigation
│   └── theme/            # App theming and styling
└── GoalFlowApplication.kt # Application class
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 28+ (API level 28)
- Kotlin 1.9.0+
- Java 17

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/goal-flow.git
   cd goal-flow
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Sync and Build**
   - Wait for Gradle sync to complete
   - Build the project (Build → Make Project)

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press Shift+F10

### Build Configuration

The app uses Gradle with Kotlin DSL. Key configuration:

- **Minimum SDK**: 28 (Android 9.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 36
- **Java Version**: 17

## 📱 Usage

### Adding Activities
1. Tap the floating action button (+)
2. Enter activity name and weight
3. Choose between Goals or Distractions tab
4. Tap "Save"

### Logging Time
1. Tap on any activity in the list
2. Use the time picker to select hours and minutes
3. Tap "Confirm" to log the time
4. Your score will update automatically

### Managing Activities
- **Edit**: Tap the edit icon (pencil) on any activity
- **Delete**: Tap the delete icon (trash) and confirm
- **View Score**: Your current score is displayed prominently at the top

### Scoring System
- **Goals**: Add points based on time spent × weight
- **Distractions**: Subtract points based on time spent × weight
- **Score Formula**: `(minutes / 60) × weight`
- **Minimum Score**: 0 (score cannot go negative)

## 🧪 Testing

The project includes comprehensive testing:

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
- ViewModel testing with MockK
- Repository testing with in-memory database
- UI testing with Compose testing framework

## 🔧 Development

### Key Dependencies
- **Jetpack Compose**: Modern UI toolkit
- **Room**: Local database
- **Hilt**: Dependency injection
- **Coroutines**: Asynchronous programming
- **Material Design 3**: UI components

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Include proper documentation for public APIs
- Follow MVVM architecture patterns

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## 📞 Support

If you encounter any issues or have questions:
- Open an issue on GitHub
- Check the existing issues for solutions
- Review the code documentation

---

**Goal Flow** - Stay focused, track progress, achieve your goals! 🎯