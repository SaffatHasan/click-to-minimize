# Project Memory

## Environment & Build Setup
- **Java Version:** The plugin requires Java 11.
- **Local JDK:** We downloaded and extracted OpenJDK 11 locally. The `justfile` is hardcoded to use this local JDK path.
- **Build Tool:** Gradle is used to build the plugin (`.\gradlew.bat jar`).
- **Dependencies:** The standard RuneLite example template does **not** include Mockito by default. If unit testing is required, Mockito must be manually added to `build.gradle`, or we can stick to testing manually via `just run`.

## Deployment & Execution
- **Justfile:** We are using `just` as a command runner with `powershell` set as the default shell.
- **Commands:**
  - `just deploy`: Builds the `.jar` and copies it to the RuneLite sideloaded plugins directory (`%USERPROFILE%\.runelite\plugins`). This requires a manual restart of the official client.
  - `just run`: Launches the developer client directly with the plugin loaded. For this to work, the plugin test class (e.g. `Click2MinimizePluginTest.java`) must contain a `public static void main(String[] args)` method that calls `ExternalPluginManager.loadBuiltin(YourPlugin.class)` and `RuneLite.main(args)`.

## Jagex Accounts in Developer Client
- To log in using a Jagex account while testing via `just run`:
  1. Open Windows Start Menu -> **RuneLite (configure)**.
  2. Add `--insecure-write-credentials` to the **Client arguments**.
  3. Launch the game normally via the official Jagex Launcher. This saves your session token to `%USERPROFILE%\.runelite\credentials.properties`.
  4. Once generated, remove the flag from the configurator. Future `just run` executions will seamlessly pick up the token!

## RuneLite API Insights & Best Practices
- **Event Timing:** `MenuOptionClicked` fires instantly upon clicking, *before* the game actually processes the action or sends any chat messages. 
- **Handling Rejections:** If you want to perform an action (like minimizing the window) but need to cancel it if the game rejects it (e.g., "inventory is too full"), use a tick delay. Set a flag `ticksToWait = 1` on click, decrement it in `@Subscribe public void onGameTick(GameTick event)`, and execute your logic when it hits 0. If a `ChatMessage` rejection appears in the meantime, cancel the pending action.

## Multiple Plugins Architecture (Next Session Goal)
- **Plugin Hub Standard:** If you intend to publish to the RuneLite Plugin Hub, **each plugin must have its own separate GitHub repository**. The hub relies on pointing to a repo with a root `build.gradle` file.
- **Local Development:** While you technically *could* put multiple `@PluginDescriptor` classes in the same repository to compile a single `.jar` for your personal use, it's highly recommended to keep each plugin in its own completely separate folder/repository. This keeps dependencies, configurations, and versioning clean, and makes it trivial to publish later. 

## Next Session Goal
- **Objective:** Create a `One Click` plugin that adds configurable menu options (e.g., modifying the inventory to support `use knife -> log` for accessibility).
- **Instructions:** Start a fresh repository/folder for this new plugin to adhere to standard RuneLite architecture.
