package service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import model.Catalog;
import model.Game;
import model.GamePurchase;
import model.MultiplayerGame;
import model.SoloGame;
import model.SubscriptionGame;
import model.User;
import util.AppLogger;
import util.GameException;
import util.IStorage;
import util.JsonStorage;

public final class PlatformManager {
    private final Map<String, User> users;
    private final Catalog catalog;
    private final IStorage<User> userStorage;
    private final Scanner scanner;
    private User currentUser;

    public PlatformManager() {
        // Reset automatique au démarrage pour une démo toujours propre
        File f = new File("users.json");
        if (f.exists()) {
            f.delete();
        }

        this.users = new HashMap<>();
        this.catalog = new Catalog();
        this.scanner = new Scanner(System.in);
        this.userStorage = new JsonStorage<>("users.json");

        loadData();
        initCatalog();
    }

    private void initCatalog() {

        UUID idWitcher =
                UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID idFortnite =
                UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID idMinecraft =
                UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID idWow = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID idRdr = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID idForza = UUID.fromString("66666666-6666-6666-6666-666666666666");


        catalog.addGame(
                new SoloGame(idWitcher, "The Witcher 3", "CD Projekt", 29.99,
                        "RPG", "19/05/2015", 100));
        catalog.addGame(
                new SoloGame(idRdr, "Red Dead Redemption II", "Rockstar Games",
                        60.0, "Aventure", "26/10/2018", 200));
        catalog.addGame(
                new MultiplayerGame(idFortnite, "Fortnite", "Epic Games", 0.0,
                        "Battle Royale", "21/07/2017", 100, true));
        catalog.addGame(new SoloGame(idMinecraft, "Minecraft", "Mojang", 20.0,
                "Sandbox", "18/11/2011", 999));
        catalog.addGame(
                new SubscriptionGame(idWow, "World of Warcraft", "Blizzard",
                        40.0, "MMORPG", "23/11/2004", 12.99));
        catalog.addGame(new MultiplayerGame(idForza, "Forza Horizon 5",
                "Playground Games", 20.0, "Course de voitures", "5/11/2011",
                100, true));
    }

    public void run() {
        System.out.println("Bienvenue sur PRO DU JEU !");
        System.out.println("Tapez --h pour voir les commandes.");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];

            try {
                switch (command) {
                    case "--c":
                        register();
                        break;

                    case "--i":
                        login();
                        break;

                    case "--d":
                        logout();
                        break;

                    case "--a":
                        displayMyLibrary();
                        break;

                    case "--g":
                        catalog.displayAll();
                        break;

                    case "--p":
                        updateProgression();
                        break;

                    case "--b":
                        buyGame();
                        break;

                    case "--s":
                        manageWallet();
                        break;

                    case "--r":
                        processRefund();
                        break;

                    case "--h":
                        showHelp();
                        break;

                    case "--q":
                        running = false;
                        saveData();
                        System.out.println("Au revoir !");
                        break;

                    default:
                        System.out.println(
                                "Commande inconnue. Tapez --h pour l'aide.");
                }
            } catch (Exception e) {
                AppLogger.logError("Erreur de logs.", e);
                System.out.println(
                        "Une erreur est survenue : " + e.getMessage());
            }
        }
    }

    private void register() {

        if (currentUser != null) {
            System.out.println(
                    "Erreur : Veuillez vous déconnecter (--d) avant de créer un nouveau compte.");
            return;
        }

        System.out.println("Création de compte.");
        System.out.print("Pseudo : ");
        String nick = scanner.nextLine();

        if (nick.isEmpty()) {
            System.out.println("Erreur : veuillez entrer un pseudo");
            return;
        }

        if (users.containsKey(nick)) {
            System.out.println("Erreur : Ce pseudo existe déjà.");
            return;
        }

        System.out.print("Email : ");
        String email = scanner.nextLine();

        if (email.isEmpty()) {
            System.out.println("Erreur : veuillez entrer un email");
            return;
        }

        System.out.print("Mot de passe : ");
        String pass = scanner.nextLine();


        if (pass.isEmpty()) {
            System.out.println("Erreur : veuillez entrer un mot de passe");
            return;
        }

        User newUser = new User(nick, email, pass);
        users.put(nick, newUser);
        System.out.println("Compte créé avec succès ! Connectez-vous avec --i");
        AppLogger.logInfo("Nouvel utilisateur inscrit : " + nick);
    }

    private void login() {
        if (currentUser != null) {
            System.out.println("Erreur : Une session est déjà active (" +
                    currentUser.getNickname() +
                    "). Veuillez vous déconnecter (--d).");
            return;
        }

        System.out.print("Pseudo : ");
        String nick = scanner.nextLine();

        User user = users.get(nick);
        if (user == null) {
            System.out.println("Erreur : Utilisateur inconnu.");
            return;
        }


        System.out.print("Mot de passe : ");
        String pass = scanner.nextLine();


        if (user.validatePassword(pass)) {
            currentUser = user;
            System.out.println(
                    "Connexion réussie ! Bienvenue " + user.getNickname());
            AppLogger.logInfo("Connexion utilisateur : " + nick);
        } else {
            System.out.println("Erreur : Mot de passe incorrect.");
        }
    }

    private void logout() {
        if (currentUser == null) {
            System.out.println("Erreur : Vous n'êtes pas connecté.");
            return;
        }

        System.out.println("Au revoir " + currentUser.getNickname() + " !");
        AppLogger.logInfo(
                "Déconnexion de l'utilisateur : " + currentUser.getNickname());
        currentUser = null;
        System.out.println("Déconnexion réussie.");
    }

    private void displayMyLibrary() {
        if (currentUser == null) {
            System.out.println("Erreur : Vous devez être connecté (--i).");
            return;
        }
        System.out.println(
                "=== BIBLIOTHÈQUE DE " + currentUser.getNickname() + " ===");
        List<GamePurchase> purchases = currentUser.getLibrary().getPurchases();

        boolean hasGames = false;

        for (GamePurchase p : purchases) {
            if (!p.isRefunded()) {
                if (p.getGame() != null) {
                    System.out.println(
                            "- " + p.getGame().getTitle() + " (Acheté le " +
                                    p.getPurchaseDate() + ")");
                    hasGames = true;
                }
            }
        }
        if (!hasGames) {
            System.out.println(
                    "Vide (ou uniquement des jeux remboursés). Allez acheter des jeux !");
        }
    }

    private void buyGame() {
        if (currentUser == null) {
            System.out.println("Erreur : Connectez-vous d'abord.");
            return;
        }

        System.out.print("Entrez le titre EXACT du jeu à acheter : ");
        String gameTitle = scanner.nextLine();

        Game foundGame = null;
        for (Game g : catalog.getGamesList()) {
            if (g.getTitle().equalsIgnoreCase(gameTitle)) {
                foundGame = g;
                break;
            }
        }

        if (foundGame == null) {
            System.out.println("Jeu introuvable dans le catalogue.");
            return;
        }

        if (currentUser.hasGame(foundGame)) {
            System.out.println("Vous possédez déjà ce jeu !");
            return;
        }

        System.out.println("--- PAIEMENT ---");
        System.out.println("Jeu : " + foundGame.getTitle() + " | Prix : " +
                foundGame.getPrice() + "€");
        System.out.println("Comment souhaitez-vous payer ?");
        System.out.println("1. Porte-monnaie (Solde actuel : " +
                String.format("%.2f", currentUser.getWallet()) + "€)");
        System.out.println("2. Carte Bancaire (Simulation)");
        System.out.print("Votre choix : ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            try {
                currentUser.buyGame(foundGame);
                saveData();
            } catch (GameException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        } else if (choice.equals("2")) {
            System.out.print("Numéro de carte fictif : ");
            String cardNum = scanner.nextLine();
            if (!cardNum.isEmpty()) {
                System.out.println("Vérification bancaire en cours...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                try {
                    currentUser.buyGameWithExternalPayment(foundGame,
                            "Carte Bancaire");
                    saveData();
                } catch (GameException e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
            } else {
                System.out.println("Annulation : Numéro de carte invalide.");
            }
        } else {
            System.out.println("Choix invalide. Achat annulé.");
        }
    }

    private void manageWallet() {
        if (currentUser == null) {
            System.out.println("Erreur : Connectez-vous d'abord (--i).");
            return;
        }

        System.out.println("=== PORTE-MONNAIE ===");
        System.out.println("Solde actuel : " +
                String.format("%.2f", currentUser.getWallet()) + "€");
        System.out.println("1. Ajouter du solde (Carte Bancaire)");
        System.out.println("2. Retour");
        System.out.print("Votre choix : ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("Montant à créditer (€) : ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println(
                            "Erreur : le montant doit être positif.");
                    return;
                }

                System.out.print("Numéro de carte bancaire : ");
                String card = scanner.nextLine();
                if (card.isEmpty()) {
                    System.out.println("Annulation.");
                    return;
                }

                System.out.println("Validation bancaire en cours...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                currentUser.addMoney(amount);
                saveData();
                System.out.println("Nouveau solde : " +
                        String.format("%.2f", currentUser.getWallet()) + "€");

            } catch (NumberFormatException e) {
                System.out.println("Erreur : montant invalide.");
            }
        }
    }

    private void updateProgression() {
        if (currentUser == null) {
            System.out.println("Erreur : connectez-vous d'abord (--i).");
            return;
        }

        List<GamePurchase> activePurchases = new ArrayList<>();
        for (GamePurchase p : currentUser.getLibrary().getPurchases()) {
            if (!p.isRefunded()) {
                activePurchases.add(p);
            }
        }
        if (activePurchases.isEmpty()) {
            System.out.println("Vous ne possédez aucun jeu actif.");
            return;
        }
        System.out.println("=== BIBLIOTHÈQUE ET PROGRESSION ===");
        for (int i = 0; i < activePurchases.size(); i++) {
            GamePurchase p = activePurchases.get(i);
            String progInfo = (p.getGame() instanceof SoloGame) ?
                    " | Progression: " + p.getProgression() + "%" : "";

            System.out.println(
                    (i + 1) + ". " + p.getGame().getTitle() + " (Joué: " +
                            p.getPlayTime() + "h" + progInfo + ")");
        }

        System.out.println("-----------------------------------");
        System.out.print(
                "Numéro du jeu à mettre à jour (ou Entrée pour retour) : ");
        String choiceStr = scanner.nextLine();

        if (choiceStr.isEmpty()) {
            return;
        }

        try {
            int choice = Integer.parseInt(choiceStr);
            if (choice < 1 || choice > activePurchases.size()) {
                System.out.println("Numéro invalide.");
                return;
            }

            GamePurchase selected = activePurchases.get(choice - 1);

            System.out.println(">> Jeu : " + selected.getGame().getTitle());
            System.out.print(
                    "Temps joué à AJOUTER en heures (Entrée pour annuler) : ");
            String hoursStr = scanner.nextLine();

            if (!hoursStr.isEmpty()) {
                int hours = Integer.parseInt(hoursStr);
                selected.addPlayTime(
                        hours);
                /*
                  Modifie l'objet réel (c'est une référence)
                 */
                System.out.println(
                        "Succès ! Nouveau total : " + selected.getPlayTime() +
                                "h");
                if (selected.getGame() instanceof SoloGame) {
                    System.out.println("Nouvelle progression : " +
                            selected.getProgression() + "%");
                }
                saveData();
            }

        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide.");
        }
    }

    private void showHelp() {
        System.out.println("Commandes disponibles :");
        System.out.println(" --c : Création de compte");
        System.out.println(" --i : Connexion");
        System.out.println(" --a : Ma bibliothèque");
        System.out.println(" --g : Voir le catalogue");
        System.out.println(" --p : Mettre à jour la progression jeu");
        System.out.println(" --b : Acheter un jeu");
        System.out.println(" --s : Gérer mon solde (Voir / Ajouter)");
        System.out.println(" --r : Demander un remboursement");
        System.out.println(" --d : Se déconnecter");
        System.out.println(" --q : Quitter et sauvegarder");
    }

    private void saveData() {
        try {
            userStorage.save(new ArrayList<>(users.values()));
        } catch (GameException e) {
            System.out.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            File f = new File("users.json");
            if (!f.exists()) {
                return;
            }

            com.google.gson.reflect.TypeToken<ArrayList<User>> token =
                    new com.google.gson.reflect.TypeToken<ArrayList<User>>() {
                    };
            List<User> userList =
                    ((JsonStorage<User>) userStorage).load(token.getType());

            for (User u : userList) {
                for (GamePurchase p : u.getLibrary().getPurchases()) {
                    UUID idJeu = p.getGameId();
                    Game gameInCatalog = catalog.getGame(idJeu);

                    if (gameInCatalog != null) {
                        p.setGame(gameInCatalog);
                    } else {
                        System.out.println(
                                "Attention : Jeu introuvable pour l'achat ID " +
                                        idJeu);
                    }
                }
                users.put(u.getNickname(), u);
            }

            if (!userList.isEmpty()) {
                System.out.println(userList.size() +
                        " utilisateurs chargés depuis le JSON.");
            }
        } catch (Exception e) {
            System.out.println("Erreur chargement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processRefund() {
        if (currentUser == null) {
            System.out.println("Erreur : Connectez-vous d'abord.");
            return;
        }
        System.out.println("=== GESTION DES REMBOURSEMENTS ===");
        List<GamePurchase> purchases = currentUser.getLibrary().getPurchases();

        List<GamePurchase> eligibleCandidates = new ArrayList<>();
        System.out.println("\n--- Jeux éligibles au remboursement ---");

        int index = 1;
        for (GamePurchase p : purchases) {
            if (!p.isRefunded()) {
                eligibleCandidates.add(p);
                String status = p.canBeRefunded() ? "[ÉLIGIBLE]" :
                        "[NON ÉLIGIBLE (Délai/Temps dépassé)]";
                System.out.println(index + ". " + p.getGame().getTitle() +
                        " (Achat: " + p.getPurchaseDate() +
                        " | Joué: " + p.getPlayTime() + "h) " + status);
                index++;
            }
        }

        if (eligibleCandidates.isEmpty()) {
            System.out.println("(Aucun jeu actif à afficher)");
        }

        System.out.println("\n--- Historique des jeux remboursés ---");
        boolean hasRefunds = false;
        for (GamePurchase p : purchases) {
            if (p.isRefunded()) {
                System.out.println(" - [REMBOURSÉ] " + p.getGame().getTitle() +
                        " (Acheté le " + p.getPurchaseDate() + ")");
                hasRefunds = true;
            }
        }
        if (!hasRefunds) {
            System.out.println("(Aucun historique de remboursement)");
        }
        System.out.println("--------------------------------------");
        if (eligibleCandidates.isEmpty()) {
            return;
        }

        System.out.print(
                "Numéro du jeu à rembourser (ou Entrée pour quitter) : ");
        String choiceStr = scanner.nextLine();

        if (choiceStr.isEmpty()) {
            return;
        }

        try {
            int choice = Integer.parseInt(choiceStr);
            if (choice < 1 || choice > eligibleCandidates.size()) {
                System.out.println("Choix invalide.");
                return;
            }

            GamePurchase selected = eligibleCandidates.get(choice - 1);
            try {
                currentUser.requestRefund(selected);
                saveData();
                AppLogger.logInfo("Remboursement effectué : " +
                        selected.getGame().getTitle());
            } catch (GameException e) {
                System.out.println("Erreur : " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide.");
        }
    }

    private void exportLibraryHtml() {
        String filename = currentUser.getNickname() + "_bibliotheque.html";
        String content = currentUser.getLibrary().generateHtmlRecap();

        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write(content);
            System.out.println("Succès ! Fichier généré : " + filename);
            System.out.println(
                    "Chemin : " + new java.io.File(filename).getAbsolutePath());
        } catch (java.io.IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier HTML.");
        }
    }

    // Getters pour l'interface graphique
    public Map<String, User> getUsers() { return users; }
    public Catalog getCatalog() { return catalog; }
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }
    public void forceSaveData() { saveData(); }
}