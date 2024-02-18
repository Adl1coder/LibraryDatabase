import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDatabase {
    private Connection connection;

    public LibraryDatabase(String dbName) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "isbn TEXT NOT NULL" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBook(String title, String author, String isbn) {
        String insertQuery = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        String deleteQuery = "DELETE FROM books WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(int id, String title, String author, String isbn) {
        String updateQuery = "UPDATE books SET title = ?, author = ?, isbn = ? WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, isbn);
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String selectQuery = "SELECT * FROM books;";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                books.add(new Book(id, title, author, isbn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static void main(String[] args) {
        LibraryDatabase library = new LibraryDatabase("library.db");

        // Kitap ekleme
        library.insertBook("Java Programming", "John Doe", "1234567890");
        library.insertBook("Python Basics", "Jane Smith", "0987654321");

        // Tüm kitapları listeleme
        List<Book> books = library.getAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }

        // Kitap güncelleme
        library.updateBook(1, "New Java Programming", "John Doe", "1111111111");

        // Kitap silme
        library.deleteBook(2);
    }
}

class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;

    public Book(int id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
