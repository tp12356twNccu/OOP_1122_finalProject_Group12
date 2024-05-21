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

    public void rent(int CustomerID, int UtensilID){
        try {
            Connection conn = connectToDB();
            // check if the utensil is available
            PreparedStatement checkUtensil = conn.prepareStatement("SELECT COUNT(Utensil_ID) FROM RENTS WHERE Utensil_ID = ? AND Returned = FALSE;");
            checkUtensil.setInt(1, UtensilID);
            ResultSet checkResult = checkUtensil.executeQuery();
            if(checkResult.getInt(1) != 0){
                System.out.println("The utensil is not available.");
                return;
            }

            // check if the user is suspended
            PreparedStatement checkSuspention = conn.prepareStatement("SELECT Suspended FROM CUSTOMER WHERE User_ID = ?;");
            checkSuspention.setInt(1, CustomerID);
            ResultSet checkSuspentionResult = checkSuspention.executeQuery();
            if(checkSuspentionResult.getInt(1) == 1){
                System.out.println("You are suspended.");
                return;
            }

            // check if the user has reached the set limit
            PreparedStatement checkLimit = conn.prepareStatement("SELECT Set_Limit FROM CUSTOMER WHERE User_ID = ?;");
            checkLimit.setInt(1, CustomerID);
            ResultSet checkLimitResult = checkLimit.executeQuery();
            int setLimit = checkLimitResult.getInt(1);
            PreparedStatement checkRent = conn.prepareStatement("SELECT COUNT(Rent_ID) FROM RENTS WHERE User_ID = ? AND Returned = FALSE;");
            checkRent.setInt(1, CustomerID);
            ResultSet checkRentResult = checkRent.executeQuery();
            if(checkRentResult.getInt(1) >= setLimit){
                System.out.println("You have reached the set limit.");
                return;
            }

            // rent the utensil
            PreparedStatement rent = conn.prepareStatement("INSERT INTO RENTS(Customer_ID, Utensil_ID, Returned) VALUES(?, ?, FALSE);");
            rent.setInt(1, CustomerID);
            rent.setInt(2, UtensilID);
            rent.executeUpdate();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }






}
