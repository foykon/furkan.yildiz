package FirstWeek.July23SelfLearning.LibraryBookManagementSystem;

import java.util.Set;

public class User {
    private int id;
    private String name;
    private Set<Book> books;

    public User(int id, String name, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
