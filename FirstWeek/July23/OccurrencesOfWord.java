package FirstWeek.July23;

import java.util.*;

public class OccurrencesOfWord {

    public void run(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Metni girin:");
        String input = scanner.nextLine();

        String[] words = input.toLowerCase().split("\\W+");

        Map<String, Integer> wordCount = new HashMap<>();

        for (String word : words) {
            if (word.isEmpty()) continue;
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCount.entrySet());

        entryList.sort(Comparator.comparing(Map.Entry::getKey));

        System.out.println("\nAlfabetik olarak sıralanmış kelime sayıları:");
        for (Map.Entry<String, Integer> entry : entryList) {
            System.out.printf("%s: %d\n", entry.getKey(), entry.getValue());
        }

        scanner.close();
    }
}
