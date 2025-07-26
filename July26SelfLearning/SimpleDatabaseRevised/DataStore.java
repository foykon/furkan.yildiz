package July26SelfLearning.SimpleDatabaseRevised;

public interface DataStore <K, V> {

    void put(K key, V value);
    void put(K key, V value, int ttl);
    V get(K key);

}
