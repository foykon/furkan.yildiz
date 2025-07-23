package July23SelfLearning;

import java.util.*;

public class ListSetMap {
    public List<String> arrayList;
    public List<Integer> linkedList; // do not accept primitive types
    public Set<String> hashSet;
    public Set<Integer> treeSet;
    public Map<Integer, String> hashMap; // same as dictionary in c#
    public Map<Integer, Integer> treeMap;


    public void run(){
        hashMap();
    }

    // faster in storing and accessing data
    public void arrayList(){
        arrayList = new ArrayList<>(); // inheritance from List
        while(true){
            arrayList.add("a");

            System.out.println(arrayList.size());
        }

    }

    // faster in manipulation of data
    public void linkedList(){
        linkedList = new LinkedList<>(); // inheritance from List
        List<Integer> linkedList2 = new LinkedList<>();
        for (int i = 0; i < 10; i++){
            linkedList.add(i);
            linkedList2.add(i);
        }
        System.out.println(linkedList2.equals(linkedList));
        System.out.println(linkedList.contains(5));
        linkedList.remove(5);
        System.out.println(linkedList.contains(5));
        System.out.println(linkedList.get(4).equals(4));
        System.out.println(linkedList.size());

        System.out.println(linkedList.stream().max(Integer::compare).get());


        linkedList.replaceAll(integer -> integer * 2);
        linkedList.forEach(System.out::println);

        System.out.println(linkedList.removeIf(integer -> integer % 3 == 0));
        linkedList.forEach(System.out::println);


    }

    // unordinary and doesnt add same
    public void hashSet(){
        hashSet = new HashSet<>();
        for (int i = 0; i < 10; i++){
            hashSet.add("furkan");
            if(i%3 == 0){
                hashSet.add("yıldız" +i);
            }
        }
        hashSet.stream().peek(System.out::println).findAny(); // random taking. with peek() takes top
        hashSet.forEach(System.out::println);
        hashSet.stream().sorted(String::compareTo).forEach(System.out::println); // sort func. "String::compareTo" is optional

        hashSet.forEach(x-> System.out.println(x.hashCode()));
        var li = hashSet.stream().toList(); // liste dönüştü

    }

    /*
     * false ->
     * ordinary set but it comes like 1 10 11 12 2 3 4 .... Doesnt sort min to max. Sorted alphabetic.
     * true ->
     * Iy s sorted by its type. If use set<String> goes as alphabetic but if use set<Integer> goes numeric.
     * another way ->
     * TreeSet<String> treeSet = new TreeSet<>(Comparator.comparingInt(Integer::parseInt));
     */
    public void treeSet(){
        treeSet = new TreeSet<>();
        for (int i = 12; i > 0; i--){
            treeSet.add((i));
        }
        treeSet.forEach(System.out::println);

    }

    public void hashMap(){
        hashMap = new HashMap<>();
        for (int i = 0; i < 10; i++){
            hashMap.put(i, "furkan"+ i);
        }
        hashMap.forEach((x,y) -> System.out.println(x + y )); // usage of lambda

    }
}
