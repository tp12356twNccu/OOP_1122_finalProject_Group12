package group12;
import java.util.ArrayList;
import java.sql.*;
import java.util.Scanner;

public class Customer {
    private int id;
    private String name;
    private String password;
    private boolean suspended = false;
    private FunctionManager fm = new FunctionManager();



    public void turnBack(){
        // show all rented utensils
        try {
            ArrayList<Utensil> rentedUtensils = new ArrayList<>();
            ResultSet UtensilRented = fm.getRentedUtensil(id);
            while(UtensilRented.next()){
                if(UtensilRented.getString("Type").equals("Cup")){
                    Cup cup = new Cup(UtensilRented.getInt("Utensil_ID"));
                    rentedUtensils.add(cup);
                }
                else if(UtensilRented.getString("Type").equals("LunchSet")){
                    LunchSet lunchSet = new LunchSet(UtensilRented.getInt("Utensil_ID"));
                    rentedUtensils.add(lunchSet);
                }
            }
            // show all rented utensils
            for(Utensil utensil : rentedUtensils){
                System.out.println(utensil);
            }

            // ask user to choose which one to turn back
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter the ID of the utensil you want to turn back: ");
            int utensilID = sc.nextInt();

            // check if the utensil is rented by the user
            for(int i = 0; i < rentedUtensils.size(); i++){
                if(rentedUtensils.get(i).getID() == utensilID){
                    rentedUtensils.get(i).turnBack();
                    return;
                }
            }

            
            


            sc.close();

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
