package util;

import java.util.List;

public interface IStorage<T> {
    void save(List<T> data) throws GameException;

    List<T> load() throws GameException;
}