package group12;
import java.sql.*;

// This class is for the devs to manipulate the database.
// Not part of product.



public class DBManipulator {

    public static void main(String[] args) {
        String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "111306095";
        String url = server + database + "?useSSL=false";
        String username = "111306095";
        String password = "068zd";
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Success!");

            String stm = "";
            PreparedStatement ps = conn.prepareStatement(stm);
            


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
