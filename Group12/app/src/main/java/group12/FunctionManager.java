package group12;
import java.sql.*;

import org.checkerframework.checker.units.qual.C;

import com.mysql.cj.xdevapi.Result;

public class FunctionManager {

    public static final int UTENSIL_UNAVAILABLE = -1;
    public static final int USER_SUSPENDED = -2;
    public static final int USER_REACHED_LIMIT = -3;
    public static final int SQL_EXCEPTION = -4;

    private Connection conn;
    public FunctionManager(){
        conn = connectToDB();
    }


    public Connection connectToDB(){
        return Main.getConn();
            
    }

    public void registration(String name, String password) throws SQLException{
        // check if the name is already used
        String checkNameStat = "SELECT COUNT(User_ID) FROM CUSTOMER WHERE Name = ?;";
        try {
            
            
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

    public boolean login(String name, String password) throws SQLException{
        String checkStat = "SELECT COUNT(User_ID) FROM CUSTOMER WHERE Name = ? AND Password = ?;";
        try {
            
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

    public int rent(int CustomerID, int UtensilID) throws SQLException{
        try {
            
            // check if the utensil is available
            PreparedStatement checkUtensil = conn.prepareStatement("SELECT COUNT(Utensil_ID) FROM RENTS WHERE Utensil_ID = ? AND Returned = FALSE;");
            checkUtensil.setInt(1, UtensilID);
            ResultSet checkResult = checkUtensil.executeQuery();
            if(checkResult.getInt(1) != 0){
                System.out.println("The utensil is not available.");
                return UTENSIL_UNAVAILABLE;
            }

            // check if the user is suspended
            PreparedStatement checkSuspention = conn.prepareStatement("SELECT Suspended FROM CUSTOMER WHERE User_ID = ?;");
            checkSuspention.setInt(1, CustomerID);
            ResultSet checkSuspentionResult = checkSuspention.executeQuery();
            if(checkSuspentionResult.getInt(1) == 1){
                System.out.println("You are suspended.");
                return USER_SUSPENDED;
            }

            // check if the user has reached the set limit
                // check which type of utensil is being rented
            PreparedStatement checkType = conn.prepareStatement("SELECT Utensil_Type FROM UTENSIL WHERE Utensil_ID = ?;");
            checkType.setInt(1, UtensilID);
            ResultSet checkTypeResult = checkType.executeQuery();
            char type = checkTypeResult.getString(1).charAt(0);
                // check the limit

            PreparedStatement getLimit = conn.prepareStatement("SELECT LunchSet_Limit, Cup_Limit FROM CUSTOMER WHERE User_ID = ?;");
            getLimit.setInt(1, CustomerID);
            ResultSet getLimitResult = getLimit.executeQuery();
            int setLimit = getLimitResult.getInt(1);
            

            PreparedStatement checkRented = conn.prepareStatement("SELECT COUNT(UTENSIL.Utensil_ID) FROM UTENSIL, RENTS\r\n" + //
                                "WHERE Customer_ID = ?\r\n" + //
                                "    AND Returned = FALSE\r\n" + //
                                "    AND UTENSIL.Utensil_ID = RENTS.Utensil_ID\r\n" + //
                                "    AND Utensil_Type = '?';\r\n" + //
                                "");
            checkRented.setInt(1, CustomerID);
            checkRented.setString(2, String.valueOf(type));
            ResultSet checkRentedResult = checkRented.executeQuery();
            if(checkRentedResult.getInt(1) >= setLimit){
                System.out.println("You have reached the limit.");
                return USER_REACHED_LIMIT;
            }

            

            // rent the utensil
            PreparedStatement rent = conn.prepareStatement("INSERT INTO RENTS(Customer_ID, Utensil_ID, Returned) VALUES(?, ?, FALSE);");
            rent.setInt(1, CustomerID);
            rent.setInt(2, UtensilID);
            rent.executeUpdate();

            // get the rent ID
            PreparedStatement getRentID = conn.prepareStatement("SELECT Rent_ID FROM RENTS WHERE Customer_ID = ? AND Utensil_ID = ? AND Returned = FALSE ORDER BY Rent_ID DESC LIMIT 1;");
            getRentID.setInt(1, CustomerID);
            getRentID.setInt(2, UtensilID);
            ResultSet getRentIDResult = getRentID.executeQuery();
            return getRentIDResult.getInt(1);

        } catch (SQLException e) {
            
            e.printStackTrace();
            return SQL_EXCEPTION;
        }

    }

    public ResultSet getRentedUtensil(int Customer_ID) throws SQLException{
        try {
            
            PreparedStatement getRentedItem = conn.prepareStatement("SELECT Renting_ID, RENTS.Utensil_ID, Utensil_Type FROM UTENSIL, RENTS " +
            "WHERE Customer_ID = ? " +
            "AND Returned = 0 " +
            "AND UTENSIL.Utensil_ID = RENTS.Utensil_ID;");
            getRentedItem.setInt(1, Customer_ID);
            ResultSet getRentedItemResult = getRentedItem.executeQuery();
            return getRentedItemResult;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
            // TODO: handle exception
        }
    }

    public void turnBack(int RentID) throws SQLException{   
        try{
            
            PreparedStatement turnBack = conn.prepareStatement("UPDATE RENTS SET Returned = TRUE WHERE Rent_ID = ?");
            turnBack.setInt(1, RentID);
            turnBack.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void setLimit(int CustomerID, int limit) throws SQLException{
        try{
            
            PreparedStatement setLimit = conn.prepareStatement("UPDATE CUSTOMER SET LunchSet_Limit = ? WHERE User_ID = ?");
            setLimit.setInt(1, limit);
            setLimit.setInt(2, CustomerID);
            setLimit.executeUpdate();
            PreparedStatement setCupLimit = conn.prepareStatement("UPDATE CUSTOMER SET Cup_Limit = ? WHERE User_ID = ?");
            setCupLimit.setInt(1, limit);
            setCupLimit.setInt(2, CustomerID);
            setCupLimit.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void giveReward(int CustomerID, int RewardID) throws SQLException{ 
        try {
            
            PreparedStatement giveReward = conn.prepareStatement("INSERT INTO ACHIEVEMENT(Customer, Reward) VALUES(?, ?)");
            giveReward.setInt(1, CustomerID);
            giveReward.setInt(2, RewardID);
            giveReward.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void showQueryResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Print column names
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();

        // Print rows
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }
    }



}
