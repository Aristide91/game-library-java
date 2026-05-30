package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Library implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<GamePurchase> purchases;

    public Library() {
        this.purchases = new ArrayList<>();
    }

    public void addPurchase(GamePurchase purchase) {
        this.purchases.add(purchase);
    }

    public List<GamePurchase> getPurchases() {
        return purchases;
    }


    public GamePurchase getPurchaseByGameId(UUID gameId) {
        for (GamePurchase p : purchases) {
            if (p.getGame().getId().equals(gameId)) {
                return p;
            }
        }
        return null;
    }


    public String generateHtmlRecap() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Ma Bibliothèque</h1>");
        for (GamePurchase p : purchases) {
            sb.append("<p>").append(p.getGame().getTitle())
                    .append(" - Progression: ").append(p.getProgression())
                    .append("%</p>");
        }
        return sb.toString();
    }
}