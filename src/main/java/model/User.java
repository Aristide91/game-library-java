package model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import util.GameException;
import util.GameAlreadyOwnedException;
import util.InsufficientFundsException;
import util.SecurityUtils;

public final class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final double INITIAL_WALLET = 100.0;

    private final UUID id;
    private final String nickname;
    private final String email;
    private final String password;
    private final String joinDate;
    private final Library library;
    private double wallet;

    public User(final String nickname, final String email,
                final String password) {
        this.id = UUID.randomUUID();
        this.nickname = nickname;
        this.email = email;

        this.password = SecurityUtils.hashPassword(password);
        util.AppLogger.logInfo("Création du compte '" + nickname + "' avec mot de passe hashé.");

        this.wallet = INITIAL_WALLET;
        this.joinDate = LocalDate.now().toString();
        this.library = new Library();
    }

    public void buyGameWithExternalPayment(final Game game,
                                           final String paymentMethod) throws GameException {
        if (hasGame(game)) {
            throw new GameAlreadyOwnedException("Vous possédez déjà ce jeu : " + game.getTitle() + " !");
        }
        GamePurchase purchase = new GamePurchase(game);
        library.addPurchase(purchase);
        util.AppLogger.logInfo("Achat réussi via " + paymentMethod + " : " + game.getTitle() + " par " + nickname);
    }

    public void buyGame(final Game game) throws GameException {
        buyGame(game, 0.0);
    }

    public void buyGame(final Game game, final double discount) throws GameException {
        if (hasGame(game)) {
            throw new GameAlreadyOwnedException("Vous possédez déjà ce jeu : " + game.getTitle() + " !");
        }

        double finalPrice = game.getPrice() * (1 - discount);

        if (wallet >= finalPrice) {
            wallet -= finalPrice;
            GamePurchase purchase = new GamePurchase(game, finalPrice);
            library.addPurchase(purchase);
            util.AppLogger.logInfo("Transaction validée : " + game.getTitle() + " acheté par " + nickname + " pour " + String.format("%.2f", finalPrice) + "€");
        } else {
            throw new InsufficientFundsException("Fonds insuffisants (Solde: " +
                    String.format("%.2f", wallet) + "€, Requis: " + String.format("%.2f", finalPrice) + "€)");
        }
    }

    public boolean hasGame(final Game game) {
        for (GamePurchase p : library.getPurchases()) {
            if (p.getGameId().equals(game.getId()) && !p.isRefunded()) {
                return true; 
            }
        }
        return false; 
    }

    public boolean validatePassword(final String inputPassword) {
        String inputHash = util.SecurityUtils.hashPassword(inputPassword);
        boolean isValid = this.password.equals(inputHash);
        util.AppLogger.logInfo("Tentative de connexion pour '" + this.nickname + "' : " + (isValid ? "RÉUSSIE" : "ÉCHOUÉE"));
        return isValid;
    }

    // Getters
    public String getNickname() {
        return nickname;
    }

    public Library getLibrary() {
        return library;
    }

    public UUID getId() {
        return id;
    }

    public double getWallet() {
        return wallet;
    }

    public LocalDate getJoinDate() {
        return LocalDate.parse(joinDate);
    }

    public void addMoney(final double amount) {
        if (amount > 0) {
            this.wallet += amount;
            System.out.println(
                    "Compte crédité de " + String.format("%.2f", amount) + "€");
        }
    }

    public void addMoney(final String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            addMoney(amount);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Le montant '" + amountStr + "' n'est pas un nombre valide.");
        }
    }

    public void requestRefund(final GamePurchase purchase) throws GameException {
        if (!library.getPurchases().contains(purchase)) {
            throw new GameException("Ce jeu ne vous appartient pas.");
        }

        if (purchase.isRefunded()) {
            throw new GameException("Ce jeu a déjà été remboursé.");
        }

        if (purchase.canBeRefunded()) {
            double amount = purchase.getPricePaid();
            this.wallet += amount;
            purchase.refund();

            System.out.println(
                    "Succès : Remboursement de " + amount + "€ accepté.");
            System.out.println(
                    "Nouveau solde : " + String.format("%.2f", wallet) + "€");
        } else {
            throw new GameException(
                    "Le jeu n'est plus éligible au remboursement.\n(Conditions : Achat < 14 jours et Temps de jeu < 2h)");
        }
    }
}