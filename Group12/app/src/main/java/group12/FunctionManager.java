package group12;
import java.sql.*;

public class FunctionManager {

    public Connection connectToDB(){
        String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "111306095";
        String url = server + database + "?useSSL=false";
        String username = "111306095";
        String password = "068zd";
        Connection conn = null;

        try{
            conn = DriverManager.getConnection(url, username, password);
            return conn;

        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
            
    }

    public void registration(String name, String password){
        // check if the name is already used
        String checkNameStat = "SELECT Name FROM CUSTOMER;";
        try {
            Connection conn = connectToDB();
            Statement checkName = conn.createStatement();
            ResultSet checkNameResult
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }




}
