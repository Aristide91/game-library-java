package model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import java.time.temporal.ChronoUnit;

public final class GamePurchase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int MAX_PROGRESSION = 100;
    private static final int REFUND_MAX_DAYS = 14;
    private static final int REFUND_MAX_HOURS = 2;
    private final UUID gameId;
    private final String purchaseDate;
    private transient Game game;
    private int playTime;
    private int progression;
    private boolean isRefunded;
    private double pricePaid;

    public GamePurchase(final Game game, double pricePaid) {
        this.game = game;
        this.gameId = game.getId();
        this.purchaseDate = LocalDate.now().toString();
        this.playTime = 0;
        this.progression = 0;
        this.isRefunded = false;
        this.pricePaid = pricePaid;
    }

    public GamePurchase(final Game game) {
        this(game, game.getPrice());
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public LocalDate getPurchaseDate() {
        return LocalDate.parse(purchaseDate);
    }

    public int getProgression() {
        return progression;
    }

    public int getPlayTime() {
        return playTime;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void addPlayTime(final int hours) {
        if (hours > 0) {
            this.playTime += hours;
            updateProgression();
        }
    }

    private void updateProgression() {

        if (game instanceof SoloGame) {
            SoloGame solo = (SoloGame) game;
            if (solo.getEstimatedDuration() > 0) {
                double ratio = (double) playTime / solo.getEstimatedDuration();
                this.progression = (int) (ratio * 100);

                if (this.progression > 100) {
                    this.progression = 100;
                }
            }
        } else {
            this.progression = 0;
        }
    }

    public boolean canBeRefunded() {
        if (isRefunded) {
            return false;
        }

        LocalDate dateOfPurchase = LocalDate.parse(this.purchaseDate);
        long daysOwned =
                ChronoUnit.DAYS.between(dateOfPurchase, LocalDate.now());

        return daysOwned <= 14 && this.playTime < 2;
    }

    public void refund() {
        if (!isRefunded) {
            this.isRefunded = true;
        }
    }
}