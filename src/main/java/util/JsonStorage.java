package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage<T> implements IStorage<T> {

    private final String filePath;
    private final Gson gson;

    public JsonStorage(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void save(List<T> data) throws GameException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
            AppLogger.logInfo("Données sauvegardées avec succès dans " + filePath);
        } catch (IOException e) {
            AppLogger.logError("Erreur sauvegarde JSON", e);
            throw new GameException(
                    "Impossible de sauvegarder : " + e.getMessage());
        }
    }

    @Override
    public List<T> load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<T>>() {
            }.getType();
            List<T> data = gson.fromJson(reader, listType);

            if (data == null) {
                return new ArrayList<>();
            }
            return data;

        } catch (IOException e) {
            AppLogger.logError("Erreur chargement JSON", e);
            return new ArrayList<>();
        }
    }

    public List<T> load(Type type) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}