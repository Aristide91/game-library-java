# Pro Du Jeu - Plateforme de Distribution de Jeux Vidéos

Bienvenue sur **Pro Du Jeu**, une plateforme de gestion et d'achat de jeux vidéos (façon Steam ou Epic Games Store). Ce projet a été développé en Java et propose une interface graphique moderne (GUI) ainsi qu'une logique métier robuste.

##  Fonctionnalités Principales

*   **Gestion des Utilisateurs :** Inscription, connexion sécurisée (hashage SHA-256), et gestion d'un porte-monnaie virtuel.
*   **Catalogue de Jeux :** Un magasin proposant différents types de jeux (Solo, Multijoueur, à Abonnement).
*   **Système d'Achat :** Achat de jeux avec gestion du solde, et possibilité d'utiliser des codes promotionnels (ex: `PRO20`).
*   **Bibliothèque Personnelle :** Suivi du temps de jeu et de la progression (pour les jeux solo).
*   **Remboursements :** Système automatisé vérifiant les conditions d'éligibilité (moins de 14 jours, moins de 2 heures jouées).
*   **Export PDF :** Génération d'un récapitulatif de la bibliothèque au format PDF.
*   **Recherche :** Barre de recherche en temps réel dans le catalogue.

##  Architecture

Le projet est divisé en plusieurs packages :
*   `model` : Contient les objets métiers (User, Game, Library, etc.).
*   `service` : Contient la logique d'orchestration (`PlatformManager`).
*   `gui` : Contient l'interface graphique (`SteamLikeGui`).
*   `util` : Utilitaires pour la sécurité, l'export (PDF), et la persistance (JSON).

##  Persistance des Données

Les données des utilisateurs (comptes, soldes, bibliothèques) sont sauvegardées localement au format JSON (`users.json`), garantissant qu'aucune progression n'est perdue entre deux sessions.

---
*Projet réalisé dans le cadre académique pour valider les concepts avancés de la Programmation Orientée Objet.*