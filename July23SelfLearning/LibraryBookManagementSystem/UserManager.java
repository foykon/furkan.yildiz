package July23SelfLearning.LibraryBookManagementSystem;

import java.util.List;
import java.util.Set;

public class UserManager {
    private List<User> users;

    public UserManager(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public int howManyUserHasThisBook(Book book) {
        int count=0;
        for (User user : users) {
            if(user.getBooks().contains(book)){
                count++;
            }
        }
        return count;
    }

    public User getUserById(int id){

        return users.stream().filter(user -> user.getId() == id).findFirst().get();
    }

    public void addBookToUser(User user, Book book){
        user.getBooks().add(book);
        users.set(users.indexOf(user), user);
    }

    public List<User> getUsers() {
        return users;
    }
    public Set<Book> showUserBook(int index){

        return getUserById(index).getBooks();
    }


}
