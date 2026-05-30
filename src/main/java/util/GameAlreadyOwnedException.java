package util;

public class GameAlreadyOwnedException extends GameException {
    public GameAlreadyOwnedException(String message) {
        super(message);
    }
}