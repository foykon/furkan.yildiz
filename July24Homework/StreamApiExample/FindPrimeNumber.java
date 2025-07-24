package July24Homework.StreamApiExample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindPrimeNumber {
    private List<Integer> numbers;

    public FindPrimeNumber(int length) {
        setValues(length);
    }

    private void setValues(int length){
        numbers = new ArrayList<Integer>();
        for(int i=1; i<=length; i++){
            numbers.add(i);
        }
    }

    public void printPrimeNumbers(){
        System.out.println("Prime numbers: ");
        getPrimeNumbers().forEach(System.out::println);
    }

    private List<Integer> getPrimeNumbers() {
        return numbers.stream().filter(f -> {
            if(f == 1) return false;
            if(f == 2) return true;
            if(f % 2 == 0) return false;
            for(int i = 3; i <= Math.sqrt(f); i++){
                if(f % i == 0) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

}
