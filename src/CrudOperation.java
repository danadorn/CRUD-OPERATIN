import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CrudOperation {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_test";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234";

    private static final Scanner scanner = new Scanner(System.in);

    public void createUser() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.print("Enter user id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter user name: ");
        String name = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        String sql = """
                INSERT INTO users (id, name, age) VALUES (?, ?, ?)
                """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setInt(3, age);

        int rowAffected = ps.executeUpdate();

        if (rowAffected > 0) {
            System.out.println("Insert successfully!");
        }else {
            System.out.println("Failed to insert!");
        }

    }

    public static void main(String[] args) throws SQLException {
        CrudOperation crudOperation = new CrudOperation();
        crudOperation.createUser();
    }
}
