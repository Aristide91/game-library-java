package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AppLogger {
    private static final Logger logger = Logger.getLogger("GamePlatformLog");
    private static FileHandler fh;

    static {
        try {
            Properties config = new Properties();
            String logFile = "application.log";
            Level logLevel = Level.INFO;

            try (FileInputStream input = new FileInputStream(
                    "config.properties")) {
                config.load(input);
                String fileProp = config.getProperty("log.file");
                String levelProp = config.getProperty("log.level");

                if (fileProp != null && !fileProp.isEmpty()) {
                    logFile = fileProp;
                }
                if (levelProp != null && !levelProp.isEmpty()) {
                    try {
                        logLevel = Level.parse(levelProp);
                    } catch (IllegalArgumentException e) {
                        System.err.println(
                                "Niveau de log invalide dans config.properties, utilisation de INFO.");
                    }
                }
            } catch (IOException e) {
                System.out.println(
                        "Fichier config.properties non trouvé. Utilisation des valeurs par défaut.");
            }

            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setLevel(logLevel);

            logger.setUseParentHandlers(false);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private AppLogger() {
        throw new IllegalStateException("Utility class");
    }

    public static void logInfo(final String message) {
        logger.info(message);
    }

    public static void logError(final String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }
}