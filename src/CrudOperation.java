import java.sql.*;
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

        if (existById(id)){
            System.out.println("User not found!");
        }

        User user = new User(id, name, age);

        String sql = """
                INSERT INTO users 
                VALUES (?, ?, ?)
                """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, user.getId());
        ps.setString(2, user.getName());
        ps.setInt(3, user.getAge());

        int rowAffected = ps.executeUpdate();

        if (rowAffected > 0) {
            System.out.println("Insert successfully!");
        }else {
            System.out.println("Failed to insert!");
        }
        conn.close();
    }

    public void readUserById() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.println("Enter user id: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (existById(id)){
            System.out.println("User not found!");
        }

        String sql = """
                SELECT * FROM users WHERE id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
            );
        }

    }

    public static boolean existById(int id) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        if (existById(id)){
            System.out.println("User not found!");
        }

        String sql = """
                SELECT 1 FROM users WHERE id = ?
                """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return !rs.next();
    };

    public void updateUser() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        System.out.print("Enter id to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (!existById(id)) {
            System.out.println("User not found");
        }
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new age: ");
        int newAge = Integer.parseInt(scanner.nextLine());

        String sql = """
                update users
                set name = ?, age = ?
                where id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newName);
        ps.setInt(2, newAge);
        ps.setInt(3, id);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Updated successful");
        } else {
            System.out.println("Failed to update");
        }
    }

    public void deleteUser() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.print("Enter user id: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = """
                DELETE FROM users WHERE id = ?
                """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int rowAffected = ps.executeUpdate();
        if (rowAffected > 0) {
            System.out.println("User not found!");
        }
    }

    public void readAllUsers() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        String sql = """
                SELECT * FROM users ORDER BY id
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        System.out.println("=".repeat(30));
        System.out.println("All users found!");
        System.out.println("=".repeat(30));

        boolean hasUsers = false;
        while (rs.next()) {
            hasUsers = true;
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
            );
            System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
        }

        if (hasUsers) {
            System.out.println("No users found in the database!");
        }
        conn.close();
    }

    public void exit() throws SQLException {
        System.out.println("Thank you for using CRUD Application!");
        scanner.close();
        System.exit(0);
    }

    public static void main(String[] args) throws SQLException {
        CrudOperation crudOperation = new CrudOperation();
//        crudOperation.readUserById();

        while (true) {
            System.out.println("""
                    1. Create a new user
                    2. Read users by id
                    3. Update user by id
                    4. Delete user by id
                    5. Read all
                    6. Exit
                    """);
            System.out.println("Enter an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 6) break;

            try {
                switch (option) {
                    case 1 -> crudOperation.createUser();
                    case 2 -> crudOperation.readUserById();
                    case 3 -> crudOperation.updateUser();
                    case 4 -> crudOperation.deleteUser();
                    case 5 -> crudOperation.readAllUsers();
                    case 6 -> crudOperation.exit();
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
