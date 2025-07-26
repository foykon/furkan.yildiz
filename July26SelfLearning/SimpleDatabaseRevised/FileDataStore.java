package July26SelfLearning.SimpleDatabaseRevised;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileDataStore<K, V> implements DataStore<K, V> {

    private InMemoryDataStore<K, V> inMemoryDataStore;
    private String filePath;

    public FileDataStore(String filePath){
        inMemoryDataStore = new InMemoryDataStore<>();
        this.filePath = filePath;
    }


    @Override
    public void put(K key, V value) {


    }

    @Override
    public void put(K key, V value, int ttl) {

    }

    @Override
    public V get(K key) {
        return null;
    }



}
