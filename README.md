# Random Users App

## Overview

The Random Users App is an Android application that fetches random user data from an API and displays it in a user-friendly interface. The app is built using Kotlin, Jetpack Compose, and Ktor for networking. It follows MVVM pattern and clean architecture and uses Koin for dependency injection.

## Project Structure

The project is organized into the following main packages:

- **data**: Contains data-related classes and interfaces.
  - **local**: Contains local database classes (Realm).
  - **model**: Contains data models representing the user and related entities.
  - **remote**: Contains API service interfaces and implementations.
  - **repository**: Contains repository classes that handle data operations.

- **domain**: Contains use cases that encapsulate business logic.
  - **usecase**: Contains use case classes for various operations like fetching users, deleting users, etc.

- **presentation**: Contains UI-related classes and ViewModels.
  - **viewmodel**: Contains ViewModel classes that manage UI-related data.
  - **composable**: Contains Jetpack Compose UI components.

- **di**: Contains dependency injection setup using Koin.

## Features

- **Fetch Random Users**: Fetches a list of random users from the API and displays them in a list.
- **View User Details**: Allows users to view detailed information about a selected user.
- **Delete User**: Allows users to delete a user from the list.
- **Filter Users**: Allows users to filter the list of users based on a search term.

## Dependencies

The project uses the following main dependencies:

- **Kotlin**: Programming language.
- **Jetpack Compose**: UI toolkit for building native Android UI.
- **Ktor**: Framework for making HTTP requests.
- **Koin**: Dependency injection framework.
- **Realm**: Local database for storing user data.
- **MockK**: Library for mocking in tests.
- **JUnit**: Testing framework.
- **Kotlinx Coroutines**: Library for asynchronous programming.
