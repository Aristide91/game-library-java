# Guide de Lancement et de Fonctionnement

Ce guide explique comment compiler, lancer et utiliser l'application **Pro Du Jeu**.

## 🚀 Prérequis

*   **Java Development Kit (JDK)** : Version 11 ou supérieure recommandée.
*   **IDE** : IntelliJ IDEA, Eclipse, ou VS Code.
*   *(Optionnel) Maven si vous souhaitez gérer les dépendances via le `pom.xml`.*

## ⚙️ Compilation et Lancement

Le projet possède deux points d'entrée (fichiers contenant une méthode `main`) :

### 1. Lancer l'Interface Graphique (Recommandé)
Le point d'entrée principal est la classe **`app.GuiMain`**.
*   Dans votre IDE, naviguez vers `src/main/java/app/GuiMain.java`.
*   Faites un clic droit et sélectionnez **"Run 'GuiMain.main()'"**.
*   L'interface moderne s'ouvrira.

### 2. Lancer la version Console (CLI)
Le point d'entrée alternatif est la classe **`app.Main`**.
*   Naviguez vers `src/main/java/app/Main.java`.
*   Faites un clic droit et exécutez.
*   L'application fonctionnera dans le terminal via un système de commandes (ex: `--c` pour créer un compte).

## 🎮 Comment tester les fonctionnalités ?

1.  **Créer un compte** : Depuis l'écran de connexion, cliquez sur "Créer un compte", entrez un pseudo et un mot de passe.
2.  **Ajouter des fonds** : Une fois connecté, cliquez sur le bouton `+` en haut à droite pour ajouter de l'argent virtuel.
3.  **Acheter un jeu** : 
    *   Allez dans l'onglet "Magasin".
    *   Utilisez la barre de recherche si besoin.
    *   Cliquez sur "Acheter". 
    *   *Astuce :* Entrez le code promo **`PRO20`** dans la boîte de dialogue pour obtenir 20% de réduction immédiate.
4.  **Jouer et Progression** : 
    *   Allez dans l'onglet "Bibliothèque".
    *   Cliquez sur "Jouer" pour simuler des heures de jeu et voir la progression augmenter.
5.  **Remboursement** :
    *   Si vous avez joué **moins de 2 heures**, vous pouvez cliquer sur "Rembourser" dans la bibliothèque. L'argent sera recrédité sur votre solde.
6.  **Export PDF** : Cliquez sur le bouton "Exporter PDF" dans la bibliothèque pour générer votre reçu. Le fichier sera créé à la racine du projet.