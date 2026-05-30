package app;

import service.PlatformManager;

public final class Main {
    private Main() {

    }

    public static void main(final String[] args) {
        PlatformManager app = new PlatformManager();
        app.run();
    }
}