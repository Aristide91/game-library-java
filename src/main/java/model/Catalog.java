package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Catalog {
    private List<Game> games;

    public Catalog() {
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public Game getGame(UUID id) {
        for (Game g : games) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }

    public void displayAll() {
        if (games.isEmpty()) {
            System.out.println("Le catalogue est vide.");
            return;
        }
        System.out.println("=== CATALOGUE DES JEUX ===");
        for (Game g : games) {
            g.displayDetails();
            System.out.println("-------------------------");
        }
    }

    public List<Game> getGamesList() {
        return games;
    }
}