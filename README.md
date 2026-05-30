# 🎮 JavaF - Pro Du Jeu (Next Gen)

Une application desktop de gestion de bibliothèque de jeux inspirée de Steam, développée en **Java** avec une interface utilisateur moderne et personnalisée.

## ✨ Points forts du projet

- **UI Moderne (Custom Swing)** : Développement d'une interface utilisateur élégante utilisant des composants Swing personnalisés (`ModernButton`, `ModernPanel`) avec gestion du survol, des arrondis et des thèmes sombres.
- **Gestion de Catalogue** : Système complet d'achat de jeux, de gestion de bibliothèque et d'historique de transactions.
- **Persistence des données** : Sauvegarde et chargement des données (utilisateurs, catalogue, bibliothèque) via un stockage **JSON**.
- **Gestion du Portefeuille** : Système de monnaie virtuelle pour simuler l'achat de jeux.
- **Architecture Propre** : Utilisation de Design Patterns (Singleton, Factory) et séparation nette entre le modèle, la vue (GUI) et les services.
- **Export PDF** : Intégration de la génération de documents PDF pour les reçus ou rapports (via iText).

## 🛠️ Stack Technique

- **Langage** : Java 17+
- **Interface Graphique** : Java Swing (Custom Components)
- **Gestion de projet** : Maven
- **Données** : JSON (GSON / Jackson)
- **Bibliothèques tierces** : iText (pour l'export PDF)

## 🚀 Lancement

1. **Prérequis** : Avoir un JDK 17+ installé.
2. **Compilation** :
   ```bash
   mvn clean install
   ```
3. **Exécution** :
   Lancer la classe principale `app.Main`.

---
*Ce projet démontre une maîtrise avancée de Java Desktop et de la création d'interfaces graphiques ergonomiques.*
