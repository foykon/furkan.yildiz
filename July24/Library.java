package July24;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Library {

    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void add(Book book) {
        books.add(book);
    }

    public static List<Book> filterBooks(List<Book> books , LibraryFilter filter) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            boolean filtered = filter.filter(book);
            if (!filtered) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }


}
