# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class org.jetbrains.compose.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class io.ktor.** { *; }

# Keep our application classes
-keep class org.example.project.** { *; }

# Don't warn about missing annotations
-dontwarn org.jetbrains.annotations.**