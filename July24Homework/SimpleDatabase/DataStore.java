package July24Homework.SimpleDatabase;

import java.util.List;

public interface DataStore {
    void put(String key, String value);

    void put(String key, String value, int ttl);

    String getByKey(String key);

    void delete(String key);

    List<String> getAll();

}
