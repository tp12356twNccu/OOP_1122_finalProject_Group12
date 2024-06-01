package group12;
import java.sql.*;

public class Cup implements Utensil{

    private int cupID;
    private FunctionManager fm = new FunctionManager();

    public Cup(int cupID){
        this.cupID = cupID;
    }

    public void rent(){
        
        
    }

    public void turnBack(){
        
        Connection conn = Main.getConn();
        try {
            //get renting ID;
            PreparedStatement getRentingID = conn.prepareStatement("SELECT Renting_ID FROM RENTS WHERE Utensil_ID = ? AND Returned = FALSE;");
            getRentingID.setInt(1, cupID);
            ResultSet getRentingIDResult = getRentingID.executeQuery();
            getRentingIDResult.next();
            int rentingID = getRentingIDResult.getInt(1);

            fm.turnBack(rentingID);

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public int getID(){
        return cupID;
    }


}
