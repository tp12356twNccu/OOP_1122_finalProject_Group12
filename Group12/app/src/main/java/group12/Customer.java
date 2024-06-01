package group12;
import java.util.ArrayList;
import java.sql.*;
import java.util.Scanner;

public class Customer {
    private int id;
     String name;
    private String password;
    private boolean suspended = false;
    private FunctionManager fm = new FunctionManager();

    public Customer(String name, String password){
        this.name = name;
        this.password = password;
        fetchData();
    }

    public int getID(){
        return id;
    }

    private void fetchData(){
        try {
            Connection conn = fm.connectToDB();
            PreparedStatement getID = conn.prepareStatement("SELECT User_ID FROM CUSTOMER WHERE Name = ?;");
            getID.setString(1, name);
            ResultSet idResult = getID.executeQuery();
            idResult.next();
            id = idResult.getInt(1);
            PreparedStatement getSuspended = conn.prepareStatement("SELECT Suspended FROM CUSTOMER WHERE Customer_ID = ?;");
            getSuspended.setInt(1, id);
            ResultSet suspendedResult = getSuspended.executeQuery();
            suspendedResult.next();
            suspended = suspendedResult.getBoolean(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public int rent(int UtensilID){
        try {
            return fm.rent(id, UtensilID);    
        } catch (Exception e) {
            return -1;
            // TODO: handle exception
        }
        
    }

    public void checkUserExist(String name) throws UserError {
        fm.checkUserExist(name);
    }

    public boolean login() throws SQLException{
        return fm.login(name, password);
    }

    public void turnBack(){
        // show all rented utensils
        Scanner sc = new Scanner(System.in);
        try {
            ArrayList<Utensil> rentedUtensils = new ArrayList<>();
            ArrayList<Integer> rentIDList = new ArrayList<>();
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
                rentIDList.add(UtensilRented.getInt("Renting_ID"));
            }
            // show all rented utensils
            fm.showQueryResult(UtensilRented);

            // ask user to choose which one to turn back
              // the following part will be implemented into the GUI, this is just a simple version
              // before the GUI is implemented

            System.out.println("Please enter the Renting_ID of the utensil you want to turn back: ");
            int utensilID = sc.nextInt();
            // check if the utensil is rented by the user
            Utensil utensiltoTurnBack = null;
            do {
                boolean rented = false;
                for(Utensil i : rentedUtensils){
                    if(i.getID() == utensilID){
                        rented = true;
                        utensiltoTurnBack = i;
                        break;
                    }
                   
                }
                if (rented) {
                    break;
                }
                else{
                    System.out.println("You have not rented this utensil.");
                    System.out.println("Please enter the Renting_ID of the utensil you want to turn back: ");
                    utensilID = sc.nextInt();
                }
            } while (true);

            if (utensiltoTurnBack != null) {
                utensiltoTurnBack.turnBack();
            }

            
            /* 
            System.out.println("Please enter the Renting_ID of the utensil you want to turn back: ");
            int rentID = sc.nextInt();
            // check if the utensil is rented by the user
            do {
                boolean rented = false;
                for(Integer i : rentIDList){
                    if(i == rentID){
                        rented = true;
                        break;
                    }
                   
                }
                if (rented) {
                    break;
                }
                else{
                    System.out.println("You have not rented this utensil.");
                    System.out.println("Please enter the Renting_ID of the utensil you want to turn back: ");
                    rentID = sc.nextInt();
                }


            } while (true);

            // turn back the utensil
            .turnBack(rentID);

            */


            sc.close();

        } catch (Exception e) {
            sc.close();
            e.printStackTrace();
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
                System.out.println("You have not done enought renting.\nPlease comeback after you've done 10 rentings.");
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
