import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public void createUser() throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

    System.out.print("Enter user id: ");
    int id = Integer.parseInt(scanner.nextLine());

    System.out.print("Enter user name: ");
    String name = scanner.nextLine();

    System.out.print("Enter age: ");
    int age = Integer.parseInt(scanner.nextLine());

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



