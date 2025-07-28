package FirstWeek.July23;

public class StringExample {
    public static String[] words;
    public static int countS1 =0;
    public static int countS2 =0;

    public static boolean sameFrequency(String sentence, String s1, String s2) {
        words = sentence.split(" ");
        for(var word : words) {
            if(word.contains(s1)){
                countS1++;
            }
            if(word.contains(s2)){
                countS2++;
            }

        }

        return (countS1==countS2);

    }
}
