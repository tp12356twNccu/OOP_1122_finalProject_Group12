package group12;
import java.util.ArrayList;
import java.sql.*;

public class Customer {
    private int id;
    private String name;
    private String password;
    private boolean suspended = false;
    private FunctionManager fm = new FunctionManager();



    public void turnBack(){
        // show all rented utensils
        try {
            

        } catch (Exception e) {
            // TODO: handle exception
        }
    }



    public void increaseLimit(){
        // check if eligible
        if(suspended){
            System.out.println("You are suspended.");
            return;
        }

        try {
            Connection conn = fm.connectToDB();
            PreparedStatement checkUseTime = conn.prepareStatement("SELECT COUNT(Renting_ID) FROM RENTS WHERE Customer_ID = ? AND Returned = TRUE; ");
            checkUseTime.setInt(1, id);
            ResultSet checkUseTimeResult = checkUseTime.executeQuery();
            if(checkUseTimeResult.getInt(1) < 10){
                System.out.println("You have not rented enough utensils.");
                return;
            }
            else{
                fm.setLimit(id, 2);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }



    }


    
    

}
