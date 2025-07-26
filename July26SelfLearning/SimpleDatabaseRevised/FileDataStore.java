package July26SelfLearning.SimpleDatabaseRevised;

import java.io.*;
import java.util.Map;

public class FileDataStore<K, V> implements DataStore<K, V> {

    private InMemoryDataStore<K, V> inMemoryDataStore;
    private String filePath;

    public FileDataStore(String filePath){
        inMemoryDataStore = new InMemoryDataStore<>();
        this.filePath = filePath;

        loadFromFile();
    }


    @Override
    public void put(K key, V value) {
        inMemoryDataStore.put(key, value);
        saveToFile();

    }

    @Override
    public void put(K key, V value, int ttl) {
        inMemoryDataStore.put(key, value, ttl);
        saveToFile();
    }

    @Override
    public V get(K key) {
        return inMemoryDataStore.get(key);
    }

    private void putHelper(K key, V value, int ttl){
        inMemoryDataStore.put(key,value, ttl);
    }


    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(inMemoryDataStore.getAll());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            var loadedData = (Map<K, DataEntry<V>>) ois.readObject();
            loadedData.forEach((key, dataEntry) ->
                    inMemoryDataStore.put(key, dataEntry.getValue(), dataEntry.getTtl())
            );

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
