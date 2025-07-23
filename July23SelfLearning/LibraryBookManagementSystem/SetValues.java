package July23SelfLearning.LibraryBookManagementSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SetValues {
    public static List<Book> setBookValues(){
        List<Book> books = new ArrayList<>();
        for(int i = 0; i<10; i++){
            books.add(new Book(i,"Title" + i, "Author" +i ));
        }
        return books;
    }

    public static List<User> setUserValues(){
        List<User> users = new ArrayList<>();
        for(int i = 0; i<3; i++){
                users.add(new User(i, "User" + i, new HashSet<>()));
        }
        return users;
    }
}
