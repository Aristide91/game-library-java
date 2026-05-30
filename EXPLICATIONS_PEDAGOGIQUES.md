# Explications : Validation des Piliers Techniques (POO)

Ce document met en évidence la manière dont le projet répond strictement aux exigences académiques concernant la Programmation Orientée Objet (POO).

## 1. Architecture et Structure

*   **Abstraction :** 
    La classe `Game` est déclarée comme `public abstract class Game`. Il est impossible d'instancier un jeu "générique". Cela force l'utilisation d'une classe enfant concrète. La méthode `displayDetails()` est abstraite, forçant les enfants à l'implémenter.
*   **Héritage :** 
    Une hiérarchie claire est établie. `SoloGame`, `MultiplayerGame`, et `SubscriptionGame` héritent de la classe parente `Game`. Ils réutilisent le code commun (titre, prix, datesortie) et ajoutent leurs propriétés (ex: `monthlyFee` pour l'abonnement).
*   **Interfaces (Contrats transversaux) :** 
    L'interface `Exportable` est créée (`void export(User user)`). La classe `PdfService` implémente cette interface. Cela démontre une capacité à définir des comportements indépendants de l'héritage de base (un vrai "plus" pédagogique).
*   **Polymorphisme :** 
    *   Le catalogue stocke tous les jeux dans une `List<Game>`. Lorsqu'on itère dessus, on appelle `g.displayDetails()` et la JVM résout dynamiquement la bonne méthode selon le type réel (Solo, Multi).
    *   Dans l'interface graphique, l'export PDF est appelé via l'interface : `Exportable exporter = new PdfService(); exporter.export(user);`.

## 2. Rigueur de codage

*   **Encapsulation (Modificateurs d'accès) :** 
    Aucun attribut n'est `public`. Tous les champs sont `private` ou `protected` (pour l'héritage dans `Game`). L'accès aux données se fait strictement via des `getters` et `setters`.
*   **Mot-clé `final` :** 
    *   Il est utilisé sur les classes enfants (`public final class SoloGame`) pour empêcher une sous-classe indésirable.
    *   Il est utilisé sur les attributs constants (`private static final double INITIAL_WALLET = 100.0;`).
    *   Il sécurise les paramètres des méthodes (`final String password`) pour éviter leur altération pendant l'exécution de la méthode.

## 3. Logique et Flexibilité

*   **Surcharge des méthodes (Overloading) :** 
    Dans la classe `User`, plusieurs méthodes sont surchargées :
    *   `buyGame(Game game)` : Achat standard au prix fort.
    *   `buyGame(Game game, double discount)` : Achat avec une réduction (utilisé par le système de codes promo).
    *   `addMoney(double amount)` et `addMoney(String amountStr)` : Gèrent différents types de paramètres entrants.
*   **Gestion des Exceptions :** 
    Le projet crée ses propres exceptions métiers étendant `Exception` : 
    *   `GameException` (Classe parente des exceptions du projet).
    *   `InsufficientFundsException`.
    *   `GameAlreadyOwnedException`.
    Elles sont levées (`throw`) dans la logique métier et attrapées (`try-catch`) dans la couche graphique pour afficher des messages d'erreur propres à l'utilisateur, évitant tout crash de l'application.

## 4. Organisation des données

*   **Framework Collections :** 
    L'utilisation de tableaux natifs statiques (`[]`) a été proscrite au profit des Collections :
    *   `List<Game>` et `List<GamePurchase>` (implémentées via `ArrayList`) pour la gestion dynamique des catalogues et bibliothèques.
    *   `Map<String, User>` (implémentée via `HashMap`) dans le `PlatformManager` pour stocker et retrouver les utilisateurs instantanément grâce à leur pseudo (clé).