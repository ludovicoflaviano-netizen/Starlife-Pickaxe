# Starlife Pickaxe

A simple custom item plugin for Paper 1.21.11.

## Starlife Pickaxe

The Starlife Pickaxe is a custom-named Netherite Pickaxe identified with persistent item data.

### Recipe

The recipe uses three Nether Stars across the top row and two sticks down the center:

```text
Nether Star | Nether Star | Nether Star
     Air    |    Stick    |     Air
     Air    |    Stick    |     Air
```

The Nether Stars are the material used for the pickaxe head, while sticks form the handle.

## Build

Use Java 21 and Gradle:

```bash
./gradlew build
```

The plugin JAR is generated in `build/libs/`.

## Server

Place the generated `Starlife-Pickaxe-1.0.0.jar` in the server's `plugins` folder and restart the Paper 1.21.11 server.
