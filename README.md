# 🎮 Game Library (Java Swing)

🇫🇷 [Version française](README.fr.md)

A desktop app that simulates a small game store and library, in the spirit of Steam. Built in **Java 17** with a custom **Swing** UI and a layered object-oriented design.

## Features

- **Accounts**: sign up and log in (passwords stored as SHA-256 hashes).
- **Store and library**: browse the catalogue, top up a virtual wallet, buy games, view purchase history and request refunds (within 14 days and under 2 hours played).
- **Game types**: solo, multiplayer and subscription games, modelled with inheritance (`Game` → `SoloGame`, `MultiplayerGame`, `SubscriptionGame`).
- **Custom UI**: dark theme with hand-made Swing components (`ModernButton`, `ModernPanel`) with rounded corners and hover effects.
- **Persistence**: users and libraries are saved to a JSON file with **Gson**, behind a generic `IStorage` interface.
- **PDF export**: export a user's library to PDF with **OpenPDF**.
- **Error handling and logging**: custom exceptions (`InsufficientFundsException`, `GameAlreadyOwnedException`) and a `java.util.logging` logger configured in `config.properties`.

## Tech stack

Java 17 · Swing · Maven · Gson · OpenPDF

## Project structure

```
src/main/java/
├── app/       # entry points: GuiMain (desktop UI), Main (console version)
├── gui/       # Swing UI (SteamLikeGui)
├── model/     # Game, User, Library, Catalog, GamePurchase...
├── service/   # PlatformManager: business logic
└── util/      # JSON storage, PDF export, logger, password hashing, exceptions
```

## Run it

Requirements: JDK 17+ and Maven.

```bash
mvn clean package
java -cp target/javaf-1.0-SNAPSHOT.jar app.GuiMain   # desktop UI
java -jar target/javaf-1.0-SNAPSHOT.jar              # console version
```

The app resets `users.json` on each start so the demo always begins from a clean state.

> The UI text is in French.
