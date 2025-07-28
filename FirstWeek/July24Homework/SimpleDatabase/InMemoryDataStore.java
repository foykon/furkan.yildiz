package FirstWeek.July24Homework.SimpleDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InMemoryDataStore implements DataStore {
    private final HashMap<String, String> data;
    private final ScheduledExecutorService scheduler;
    public InMemoryDataStore() {
        this.data = new HashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

    }

    @Override
    public void put(String key, String value) {
        if(key == null || value == null){
            return;
        }

        if(data.containsKey(key)){
            data.replace(key, value);

        }else{
            data.put(key, value);

        }
    }

    @Override
    public void put(String key, String value, int ttl) {
        if (key == null || value == null) {
            return;

        }

        if(data.containsKey(key)){
            data.replace(key, value);

        }else{
            data.put(key, value);

        }

        scheduler.schedule(() -> { // inline func
            data.remove(key);
            System.out.println("TTL expired for key: " + key);
        }, ttl, TimeUnit.SECONDS);
    }

    @Override
    public String getByKey(String key) {
        return data.get(key);
    }

    @Override
    public void delete(String key) {
        data.remove(key);
    }

    @Override
    public List<String> getAll() {
        return data.entrySet()
                .stream()
                .map(e -> e.getValue())
                    .collect(Collectors.toList()
                );

    }


}
