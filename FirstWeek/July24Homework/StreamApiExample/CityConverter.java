package FirstWeek.July24Homework.StreamApiExample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CityConverter {
    private List<City> cities;

    public CityConverter() {
        setValues();
    }
    public void run(){
        System.out.println("*************** Listed as a List *************");
        cities.stream().forEach(city -> {
            System.out.println(city.getName()  + "\t"+ city.getPopulation());
        });

        System.out.println("*************** Listed as a Map ****************");
        convert().forEach((o, o2) -> System.out.println(o + "\t" + o2));
    }

    private void setValues(){
        cities = new ArrayList<City>();
        cities.add(new City("Istanbul", 15519267));
        cities.add(new City("Ankara", 5445000));
        cities.add(new City("Izmir", 4320519));
        cities.add(new City("Bursa", 3056120));
        cities.add(new City("Antalya", 2426356));
        cities.add(new City("Adana", 2237945));
        cities.add(new City("Konya", 2235275));
        cities.add(new City("Gaziantep", 2103497));
        cities.add(new City("Mersin", 1897633));
        cities.add(new City("Kayseri", 1421305));
        cities.add(new City("Eskisehir", 887475));
        cities.add(new City("Diyarbakir", 1637197));
        cities.add(new City("Samsun", 1407043));
        cities.add(new City("Denizli", 1082725));
        cities.add(new City("Sanliurfa", 2029630));
        cities.add(new City("Malatya", 806156));
        cities.add(new City("Trabzon", 808974));
        cities.add(new City("Hatay", 1637774));
        cities.add(new City("Aydın", 1109727));
        cities.add(new City("Tekirdağ", 1104840));
        cities.add(new City("Kahramanmaraş", 1102100));
        cities.add(new City("Manisa", 1407305));
        cities.add(new City("Balıkesir", 1196547));
        cities.add(new City("Ordu", 750454));
        cities.add(new City("Sakarya", 1043046));

    }

    private Map convert(){
        return   cities.stream()
                .collect(Collectors.toMap(
                        City::getName,
                        City::getPopulation
                ));
    }
}

