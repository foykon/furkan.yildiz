package July24Homework.SimpleDatabase;

import java.util.Scanner;

public class ScannerHelper {
    private static Scanner instance;

    public static Scanner getScannerInstance(){
        if(instance == null){
            instance = new Scanner(System.in);
        }
        return instance;
    }
}
