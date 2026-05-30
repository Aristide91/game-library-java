package model;

import java.io.Serial;
import java.util.UUID;

public final class MultiplayerGame extends Game {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int maxPlayers;
    private final boolean isOnline;

    public MultiplayerGame(final String title, final String studio,
                           final double price, final String genre,
                           final String datesortie, final int maxPlayers,
                           final boolean isOnline) {
        super(title, studio, price, genre, datesortie);
        this.maxPlayers = maxPlayers;
        this.isOnline = isOnline;
    }

    public MultiplayerGame(final UUID id, final String title,
                           final String studio, final double price,
                           final String genre, final String datesortie,
                           final int maxPlayers, final boolean isOnline) {
        super(id, title, studio, price, genre, datesortie);
        this.maxPlayers = maxPlayers;
        this.isOnline = isOnline;
    }

    @Override
    public void displayDetails() {
        System.out.println(
                "Jeu Multijoueur: " + getTitle() + " (" + getStudio() + ")");
        System.out.println(" - Prix: " + getPrice() + "€");
        System.out.println(" - Genre: " + getGenre());
        System.out.println(" - Joueurs max: " + maxPlayers);
        System.out.println(" - Mode: " + (isOnline ? "En ligne" : "Local"));
        System.out.println(" - Date: " + getDatesortie());
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isOnline() {
        return isOnline;
    }
}