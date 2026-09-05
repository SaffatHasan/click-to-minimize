# Project Memory

## Environment & Build Setup
- **Java Version:** The plugin requires Java 11.
- **Local JDK:** We downloaded and extracted OpenJDK 11 locally. The `justfile` is hardcoded to use this local JDK path (`c:\Users\Nayan\Desktop\runelite exploration\HelloWorldPlugin\jdk-11\jdk-11.0.24+8`).
- **Build Tool:** Gradle is used to build the plugin (`.\gradlew.bat jar`).

## Deployment
- **Justfile:** We are using `just` as a command runner with `powershell` set as the default shell.
- **Commands:**
  - `just deploy`: Builds the `.jar` using the local JDK 11 and copies it to the RuneLite sideloaded plugins directory (`%USERPROFILE%\.runelite\plugins`).

## Next Session Goal
- **Objective:** Create a `Click2Minimize` plugin.
- **Instructions for Next Agent:** Use this setup to build and test the new `Click2Minimize` plugin. You can rely on the existing `justfile` targets to quickly iterate and test changes locally.
