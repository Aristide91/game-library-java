package model;

import java.io.Serial;
import java.util.UUID;

public final class SubscriptionGame extends Game {
    @Serial
    private static final long serialVersionUID = 1L;
    private final double monthlyFee;

    public SubscriptionGame(final String title, final String studio,
                            final double price, final String genre,
                            final String datesortie, final double monthlyFee) {
        super(title, studio, price, genre, datesortie);
        this.monthlyFee = monthlyFee;
    }

    public SubscriptionGame(final UUID id, final String title,
                            final String studio, final double price,
                            final String genre, final String datesortie,
                            final double monthlyFee) {
        super(id, title, studio, price, genre, datesortie);
        this.monthlyFee = monthlyFee;
    }

    @Override
    public void displayDetails() {
        System.out.println(
                "Jeu à Abonnement: " + getTitle() + " (" + getStudio() + ")");
        System.out.println(" - Prix : " + getPrice() + "€");
        System.out.println(" - Genre: " + getGenre());
        System.out.println(" - Abonnement mensuel: " + monthlyFee + "€/mois");
        System.out.println(" - Date: " + getDatesortie());
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }
}