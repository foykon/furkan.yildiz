package July26SelfLearning.SimpleDatabaseRevised;

import java.util.HashMap;
import java.util.Map;

public class DataStoreGenericArray<K, V> {
    private final Map<K, DataEntry<V>> data;

    public DataStoreGenericArray() {
        data = new HashMap<>();
    }

    public void add(K key, V value, int ttl){
        data.put(key, new DataEntry<>(value, ttl, System.currentTimeMillis()));
    }

    public DataEntry<V> get(K key){
        return data.get(key);
    }


}
