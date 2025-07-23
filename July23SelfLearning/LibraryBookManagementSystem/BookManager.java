package July23SelfLearning.LibraryBookManagementSystem;

import java.util.List;

public class BookManager {
    private List<Book> books;

    public BookManager(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Book getBookById(int bookIdInput) {
        return books.stream().filter(b -> b.getId() == bookIdInput).findFirst().get();
    }
}
