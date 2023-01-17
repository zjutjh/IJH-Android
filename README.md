# IJH Android 

[![Android CI](https://github.com/I-Info/IJH-Android/actions/workflows/ci.yml/badge.svg)](https://github.com/I-Info/IJH-Android/actions/workflows/ci.yml)

IJH app for Android, a **work in progress** currently.

# Features
- [ ] All features supported by **WeJH**.
- [ ] Notifications.
- [ ] More...

# Architecture
The app follows the [official architecture guidance](https://developer.android.com/topic/architecture).

- data
  - network (Retrofit/OkHttp)
  - datastore (Protobuf)
  - database (Room)
- UI

## UI
UI is built with [Jetpack Compose](https://developer.android.com/jetpack/compose) and 
[Material Design 3](https://m3.material.io).

- Theme: IJH app uses the Dynamic color theme as possible, and provides a default theme for fallbacks.

## Dependency injection
IJH app uses DI(Dependency injection) between layers and uses [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) 
to implement automatic DI.
