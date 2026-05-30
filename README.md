# Fetch for Android

Fetch is a native Android prototype for linking TorBox and SceneNZBs accounts, reviewing cloud downloads, and searching torrent and NZB sources from one installable app. It does not require web hosting.

## UX included in the prototype

- Responsive, scrollable native Android screens for the home dashboard, unified search, downloads, and account management.
- Search dialog with keyboard search support, torrent/NZB result metadata, empty-result feedback, and per-result add-to-TorBox actions.
- TorBox and SceneNZBs linking and unlinking flows with clear linked-account status.
- Large bottom-navigation touch targets and content descriptions for assistive technology.

## Build an installable APK

1. Install Android Studio or the Android command-line SDK with Android SDK Platform 36.
2. Install Gradle 8.14.4 and set `ANDROID_HOME` to the SDK path.
3. Run `gradle assembleDebug`.
4. Install `app/build/outputs/apk/debug/app-debug.apk` on an Android device.

The included GitHub Actions workflow pins Gradle 8.14.4, builds the app, and uploads `fetch-debug-apk` as an installable artifact on pushes and pull requests. The Gradle wrapper JAR is intentionally not committed because this repository must remain text-only for PR upload compatibility.

The prototype stores linked API tokens in Android app-private preferences and disables Android backups. A production release should replace that storage layer with Android Keystore-backed encryption and connect the TorBox and SceneNZBs API clients.
