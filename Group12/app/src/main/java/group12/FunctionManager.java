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
        String checkNameStat = "SELECT COUNT(User_ID) FROM CUSTOMER WHERE Name = ?;";
        try {
            Connection conn = connectToDB();
            
            // check if the name is already used
            PreparedStatement checkName = conn.prepareStatement(checkNameStat);
            checkName.setString(1, name);
            ResultSet checkNameResult = checkName.executeQuery();
            if(checkNameResult.getInt(1) != 0){


                System.out.println("The name is already used.");
                return;
            }

            // insert the new user
            PreparedStatement insertUser = conn.prepareStatement("INSERT INTO CUSTOMER(Name, Password, Set_Limit, Suspended) VALUES(?, ?, 1, 0);");
            insertUser.setString(1, name);
            insertUser.setString(2, password);
            insertUser.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public boolean login(String name, String password){
        String checkStat = "SELECT COUNT(User_ID) FROM CUSTOMER WHERE Name = ? AND Password = ?;";
        try {
            Connection conn = connectToDB();
            PreparedStatement check = conn.prepareStatement(checkStat);
            check.setString(1, name);
            check.setString(2, password);
            ResultSet checkResult = check.executeQuery();
            if(checkResult.getInt(1) == 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    




}
