package July26SelfLearning.SimpleDatabaseRevised;

import java.util.Map;

public class InMemoryDataStore<K,V> implements DataStore<K, V> {
    private final DataStoreGenericArray<K, V> dataStoreGenericArray;

    public InMemoryDataStore(){
        dataStoreGenericArray = new DataStoreGenericArray<>();
    }

    public DataStoreGenericArray<K, V> getDataStoreGenericArray() {
        return dataStoreGenericArray;
    }

    @Override
    public void put(K key, V value) {
        putHelper(key, value, -1);
    }

    @Override
    public void put(K key, V value, int ttl) {
        putHelper(key, value, ttl);
    }

    @Override
    public V get(K key) {
        return dataStoreGenericArray.get(key).getValue();
    }

    private void putHelper(K key, V value, int ttl){
        dataStoreGenericArray.add(key,value, ttl);
    }

    public Map<K, DataEntry<V>> getAll(){
        return dataStoreGenericArray.getAll();
    }




}
