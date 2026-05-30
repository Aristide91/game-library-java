package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String title;
    private final String studio;
    private final double price;
    private final String genre;
    private final String datesortie;

    protected Game(final String title, final String studio, final double price,
                   final String genre, final String datesortie) {
        this(UUID.randomUUID(), title, studio, price, genre, datesortie);
    }

    protected Game(final UUID id, final String title, final String studio,
                   final double price, final String genre,
                   final String datesortie) {
        this.id = id;
        this.title = title;
        this.studio = studio;
        this.price = price;
        this.genre = genre;
        this.datesortie = datesortie;
    }

    public abstract void displayDetails();

    public final UUID getId() {
        return id;
    }

    public final String getTitle() {
        return title;
    }

    public final double getPrice() {
        return price;
    }

    public final String getGenre() {
        return genre;
    }

    public final String getDatesortie() {
        return datesortie;
    }

    public final String getStudio() {
        return studio;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}