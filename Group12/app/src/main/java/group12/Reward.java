package group12;
import java.sql.*;

import javax.swing.JOptionPane;

public class Reward {
    
    private int Reward_ID;
    private String RName;
    private FunctionManager fm = new FunctionManager();

    public static void checkAndGiveReward(Customer customer){
        Reward reward = new Reward();
        reward.giveRentingKingReward(customer);
    }

    public void giveRentingKingReward(Customer customer){
        Connection conn = fm.connectToDB();
        try {
            // get customer's renting count
            PreparedStatement getRentingCount = conn.prepareStatement("SELECT COUNT(Renting_ID) \r\n" + //
                                "FROM RENTS \r\n" + //
                                "WHERE Customer_ID = ?\r\n" + //
                                "    AND Returned = 1;");
            getRentingCount.setInt(1, customer.getID());
            ResultSet rentingCountResult = getRentingCount.executeQuery();
            rentingCountResult.next();
            int rentingCount = rentingCountResult.getInt(1);

            // if the customer has rented 10 utensils, give him a reward
            if (rentingCount == 10) {
                PreparedStatement getRentingKingID = conn.prepareStatement("SELECT Reward_ID FROM REWARD\r\n" + //
                                        "WHERE RName = 'Renting King';");
                ResultSet rentingKingIDResult = getRentingKingID.executeQuery();
                rentingKingIDResult.next();
                Reward_ID = rentingKingIDResult.getInt(1);

                // insert record into ACHIEVEMENT
                fm.giveReward(customer.getID(), Reward_ID);
                JOptionPane.showMessageDialog(null, "You've got a reward \"Renting King\"!", "Reward", JOptionPane.PLAIN_MESSAGE);

                
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }



}
