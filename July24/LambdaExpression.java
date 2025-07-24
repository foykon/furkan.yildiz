package July24;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaExpression {
    public static void run(){
        List<Integer> list = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            list.add(i);
        }

        list.forEach(System.out::println);

        list.stream()
                .filter(e -> e * e % 2 == 0)
                .collect(Collectors.toMap(
                        i -> i,
                        i -> i * i
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() % 6 == 0)
                .forEach(System.out::println);



    }
}
