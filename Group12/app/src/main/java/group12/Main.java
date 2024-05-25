package group12;
import java.sql.*;




public class Main {

    private static Connection conn;

    public static Connection getConn(){
        return conn;
    }




    public static void main(String[] args){
        String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "111306095";
        String url = server + database + "?useSSL=false";
        String username = "111306095";
        String password = "068zd";

        try{
           conn = DriverManager.getConnection(url, username, password); 

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
