package FirstWeek.July23SelfLearning.LibraryBookManagementSystem;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class LibraryManager {
    private UserManager userManager;
    private BookManager bookManager;
    private Scanner scanner;

    public LibraryManager(UserManager userManager, BookManager bookManager, Scanner scanner) {
        this.userManager = userManager;
        this.bookManager = bookManager;
        this.scanner = scanner;
    }

    public void runManager(){

        pickAction();




    }


    public void pickAction(){
        while (true) {
            System.out.println("\n----------------------------------------------------\n");
            System.out.println("1 -> Users book.");
            System.out.println("2 -> How many copy of this book are given.");
            System.out.println("3 -> Add book to a user.");
            System.out.println("4 -> Add book.");
            System.out.println("5 -> Add user.");
            System.out.println("0 -> Quit");

            switch (scanner.nextInt()) {
                case 1:
                    User currentUSer = pickUser();
                    userManager.showUserBook(currentUSer.getId()).forEach(System.out::println);
                    break;
                case 2:
                    howManyUsersHaveThisBook();
                    break;
                case 3:
                    addBookToUser();
                    break;
                case 4:
                    addBook();
                    break;
                case 5:
                    addUser();
                    break;
                case 0:
                    return;
                default:
                    break;
            }
        }
    }


    private void howManyUsersHaveThisBook(){
        Book currentBook = pickBook();
        System.out.println(userManager.howManyUserHasThisBook(currentBook));
    }

    private void addBookToUser(){
        User currentUser = pickUser();
        Book currentBook = pickBook();
        userManager.addBookToUser(currentUser, currentBook);
    }

    private void addUser(){
        System.out.println("Enter ID for new user: ");
        int userId = scanner.nextInt();
        System.out.println("Enter name for new user: ");
        String userName = scanner.next();
        userManager.addUser(new User(userId, userName, new HashSet<>()));
    }

    public void addBook(){
        System.out.println("Enter ID for new book: ");
        int bookId = scanner.nextInt();
        System.out.println("Enter title for new book: ");
        String bookTitle = scanner.next();
        System.out.println("Enter author for new book: ");
        String bookAuthor = scanner.next();
        bookManager.addBook(new Book(bookId, bookTitle, bookAuthor));
    }

    public Book pickBook(){
        List<Book> books = bookManager.getBooks();
        for(Book book : books){
            System.out.println(book.toString());
        }
        int bookIdInput = scanner.nextInt();

        //return books.get(bookIdInput);
        return bookManager.getBookById(bookIdInput);
    }

    public User pickUser(){
        List<User> users = userManager.getUsers();
        for(User user : users){
            System.out.println(user.toString());
        }
        int userIdInput = scanner.nextInt();

        // return users.get(userIdInput); farklı id değeri girildiğinde sıralama bouzlup patlıyor
        return userManager.getUserById(userIdInput);
    }

}
