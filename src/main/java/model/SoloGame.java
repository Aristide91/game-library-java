package model;

import java.io.Serial;
import java.util.UUID;

public final class SoloGame extends Game {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int estimatedDuration;

    public SoloGame(final String title, final String studio, final double price,
                    final String genre, final String datesortie,
                    final int estimatedDuration) {
        super(title, studio, price, genre, datesortie);
        this.estimatedDuration = estimatedDuration;
    }

    public SoloGame(final UUID id, final String title, final String studio,
                    final double price, final String genre,
                    final String datesortie, final int estimatedDuration) {
        super(id, title, studio, price, genre, datesortie);
        this.estimatedDuration = estimatedDuration;
    }

    @Override
    public void displayDetails() {
        System.out.println(
                "Jeu Solo: " + getTitle() + " (" + getStudio() + ")");
        System.out.println(" - Prix: " + getPrice() + "€");
        System.out.println(" - Genre: " + getGenre());
        System.out.println(" - Durée estimée: " + estimatedDuration + "h");
        System.out.println(" - Date de sortie: " + getDatesortie());
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }
}