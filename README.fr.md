# 🎮 Bibliothèque de jeux (Java Swing)

🇬🇧 [English version](README.md)

Une application de bureau qui simule une petite boutique et une bibliothèque de jeux, dans l'esprit de Steam. Développée en **Java 17**, avec une interface **Swing** personnalisée et une conception orientée objet en couches.

## Fonctionnalités

- **Comptes** : inscription et connexion (mots de passe stockés sous forme de hash SHA-256).
- **Boutique et bibliothèque** : parcourir le catalogue, recharger un portefeuille virtuel, acheter des jeux, consulter l'historique d'achats et demander un remboursement (sous 14 jours et moins de 2 heures de jeu).
- **Types de jeux** : jeux solo, multijoueurs et par abonnement, modélisés par héritage (`Game` → `SoloGame`, `MultiplayerGame`, `SubscriptionGame`).
- **Interface personnalisée** : thème sombre avec des composants Swing faits main (`ModernButton`, `ModernPanel`), coins arrondis et effets de survol.
- **Persistance** : les utilisateurs et leurs bibliothèques sont enregistrés dans un fichier JSON avec **Gson**, derrière une interface générique `IStorage`.
- **Export PDF** : export de la bibliothèque d'un utilisateur en PDF avec **OpenPDF**.
- **Erreurs et journalisation** : exceptions dédiées (`InsufficientFundsException`, `GameAlreadyOwnedException`) et journal `java.util.logging` configuré dans `config.properties`.

## Stack technique

Java 17 · Swing · Maven · Gson · OpenPDF

## Structure du projet

```
src/main/java/
├── app/       # points d'entrée : GuiMain (interface graphique), Main (version console)
├── gui/       # interface Swing (SteamLikeGui)
├── model/     # Game, User, Library, Catalog, GamePurchase...
├── service/   # PlatformManager : logique métier
└── util/      # stockage JSON, export PDF, journal, hash des mots de passe, exceptions
```

## Lancer le projet

Prérequis : JDK 17+ et Maven.

```bash
mvn clean package
java -cp target/javaf-1.0-SNAPSHOT.jar app.GuiMain   # interface graphique
java -jar target/javaf-1.0-SNAPSHOT.jar              # version console
```

L'application réinitialise `users.json` à chaque démarrage, pour que la démo reparte toujours d'un état propre.
