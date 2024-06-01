package group12;

import java.sql.*;
import java.awt.*;
import javax.swing.*;



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
            System.out.println("Connection success");
            LoginFrame1 loginFrame = new LoginFrame1();
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setVisible(true);
            
           






           
        }catch(SQLException e){
            System.out.println("Connection failed");
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    conn.close();
                    System.out.println("Connection closed");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });




        
    }

}


