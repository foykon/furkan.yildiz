package July24Homework.SimpleDatabase;

import java.util.List;
import java.util.Scanner;

public class DatabaseUI {
    private Scanner scanner;
    private DataStore dataStore;

    public DatabaseUI() {
        scanner = ScannerHelper.getScannerInstance();
    }

    public void run(){
        System.out.println("Welcome DatabaseUI");
        pickDatabaseType();
    }

    public void pickDatabaseType(){
        while(true){
            System.out.println("Enter database type: ");
            System.out.println("1 - InMemoryDataStore");
            System.out.println("2 - FileDataStore");
            System.out.println("0 - Exit");
            switch (scanner.nextInt()){
                case 1:
                    handleDatabase(DatabaseType.InMemoryDataStore);
                    break;
                case 2:
                    handleDatabase(DatabaseType.FileDataStore);
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    break;
            }
        }
    }

    private  void handleDatabase(DatabaseType type){
        switch (type){
            case InMemoryDataStore:
                dataStore = new InMemoryDataStore();
                break;
            case FileDataStore:
                dataStore = new FileDataStore();
                break;
            default:
                break;

        }

        pickDatabaseOperation();

    }

    public void pickDatabaseOperation(){
        while(true){
            System.out.println("Enter database operation: ");
            System.out.println("1 - Put value");
            System.out.println("2 - Put value with real time");
            System.out.println("3 - Delete value");
            System.out.println("4 - Read value by ID");
            System.out.println("5 - Read values");
            System.out.println("0 - Exit");
            switch (scanner.nextInt()){
                case 1:
                    putInput();
                    break;
                case 2:
                    putInputWithTime();
                    break;
                case 3:
                    deleteByKey();
                    break;
                case 4:
                    getValueByKey();
                    break;
                case 5:
                    getAllValues();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    break;

            }
        }
    }

    public void putInput(){
        System.out.println("Enter key");
        String key = scanner.next();
        System.out.println("Enter value");
        String value = scanner.next();
        dataStore.put(key, value);
    }

    public void putInputWithTime(){
        System.out.println("Enter key");
        String key = scanner.next();
        System.out.println("Enter value");
        String value = scanner.next();
        System.out.println("Enter time");
        int sec = scanner.nextInt();
        dataStore.put(key, value, sec);
    }

    public void getAllValues(){
        System.out.println("All values: ");
         List<String> values = dataStore.getAll();
         values.forEach(v -> System.out.println("Value: " + v));
    }

    public void getValueByKey(){
        System.out.println("Enter key");
        String value = dataStore.getByKey(scanner.next());
        System.out.println("Value is: " + value);
    }

    public void deleteByKey(){
        System.out.println("Enter key");
        dataStore.delete(scanner.next());
    }
}
