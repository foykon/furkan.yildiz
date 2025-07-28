package FirstWeek.July24Homework.SimpleDatabase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FileDataStore implements DataStore {
    private static final String FILE_PATH = "FirstWeek/data_store.txt";
    private static final String SEPARATOR = "=>";
    private static final String TTL_SEPARATOR = ";TTL=";

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public FileDataStore() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing data store file: " + e.getMessage());
        }
        // ileri tarihe planlayıcı
        scheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void put(String key, String value) {
        put(key, value, -1);
    }

    @Override
    public void put(String key, String value, int ttl) {
        if (key == null || value == null || key.contains(SEPARATOR) || value.contains(SEPARATOR)) {
            System.err.println("Key or value is invalid.");
            return;
        }

        List<String> lines = readAllLines();
        lines.removeIf(line -> line.split(SEPARATOR)[0].equals(key));

        long expiryTime = (ttl > 0) ? System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ttl) : -1;
        String newLine = key + SEPARATOR + value + TTL_SEPARATOR + expiryTime;
        lines.add(newLine);

        writeAllLines(lines);
}

    @Override
    public String getByKey(String key) {
        if (key == null) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length > 0 && parts[0].equals(key)) {
                    String[] valueParts = parts[1].split(TTL_SEPARATOR);
                    String value = valueParts[0];
                    long expiryTime = Long.parseLong(valueParts[1]);

                    if (expiryTime == -1 || System.currentTimeMillis() < expiryTime) {
                        return value;
                    } else {
                        System.out.println("TTL expired for key: " + key + ". Entry will be removed.");
                        delete(key);
                        return null;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from data store: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(String key) {
        if (key == null) return;
        List<String> lines = readAllLines();
        lines.removeIf(line -> line.split(SEPARATOR)[0].equals(key));
        writeAllLines(lines);
    }

    @Override
    public List<String> getAll() {
        List<String> values = new ArrayList<>();
        List<String> allLines = readAllLines();
        long currentTime = System.currentTimeMillis();

        for (String line : allLines) {
            String[] parts = line.split(SEPARATOR);
            if (parts.length > 1) {
                String[] valueParts = parts[1].split(TTL_SEPARATOR);
                String value = valueParts[0];
                long expiryTime = Long.parseLong(valueParts[1]);

                if (expiryTime == -1 || currentTime < expiryTime) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    /**
     * If values expire time arrives. Value has to be deleted with this func.
     * Splits which values still valid and writes valid ones again.
     */
    private void cleanupExpiredEntries() {
        System.out.println("Running scheduled cleanup for expired entries...");
        List<String> lines = readAllLines();
        long currentTime = System.currentTimeMillis();

        List<String> validLines = lines.stream().filter(line -> {
            String[] parts = line.split(SEPARATOR);
            if (parts.length > 1) {
                long expiryTime = Long.parseLong(parts[1].split(TTL_SEPARATOR)[1]);
                return expiryTime == -1 || currentTime < expiryTime;
            }
            return false;
        }).collect(Collectors.toList());

        if(validLines.size() < lines.size()){
            System.out.println((lines.size() - validLines.size()) + " expired entries removed.");
            writeAllLines(validLines);
        } else {
            System.out.println("No expired entries found to clean up.");
        }
    }


    private List<String> readAllLines() {
        try {
            return new ArrayList<>(Files.readAllLines(Paths.get(FILE_PATH)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Writes all lines to the file. Using when something s deleted from txt
     * @param lines
     */
    private void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) { // false -> overwrite
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}