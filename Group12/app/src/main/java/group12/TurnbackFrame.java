package group12;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TurnbackFrame extends JFrame {
    private FunctionManager fm=new FunctionManager();
    private JPanel flowPanel, operatePanel;
    private JTextArea outputArea;
    private JLabel column2;
    private JTextField text2;
    private JButton commitButton, lastPageButton;
    private Connection conn;
    private Statement stat;
    private Customer customer;

    


    public TurnbackFrame(Customer customer) {
        this.customer = customer;
        setTitle("Turnback");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        try {
            conn = Main.getConn();
            createLabel();
            createTextField();
            createButton();
            createTextArea();
            createLayout();
        } catch (SQLException e) {
            outputArea.append("Database error: " + e.getMessage() + "\n");
        }
    }

    private void createTextArea() throws SQLException {
        outputArea = new JTextArea(1, 12);
        outputArea.setEditable(false);
    
        //stat = conn.createStatement();

        // PROBLEM: There's something wrong here that prevents the components from constructing properly.
        //String query = "SELECT * FROM RENTS;";
        String query = "SELECT Renting_ID, Utensil_ID FROM RENTS WHERE Customer_ID = ? AND Returned = 0;";
        PreparedStatement stat = conn.prepareStatement(query);
        stat.setInt(1, customer.getID());

        ResultSet r = stat.executeQuery();
        outputArea.append(fm.showQueryResult(r));
        r.close();
        
        /*
        boolean success = stat.execute(query);
        if(success){
        	ResultSet r=stat.getResultSet();
        	outputArea.append(fm.showQueryResult(r));
        	r.close();
        }
        */
    }

    private void createLabel() {
        //column1 = new JLabel("Customer ID");
        column2=new JLabel("Please enter the ID of the utensil you want to turn back:");
        //借用頁面只讓使用者填寫Utensil ID?
        
    }

    private void createTextField() {
       // text1 = new JTextField(10);
        //text1應該要放UserName
        text2 = new JTextField(10);
    }

    private void createButton() throws SQLException {
        commitButton = new JButton("Commit");
        commitButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent event) {

                String query = "";
                String utensilID = text2.getText();
                int uIDint = Integer.parseInt(utensilID);

                // check if the utensil is actually rented by the customer
                try {
                    PreparedStatement checkRent = conn.prepareStatement("SELECT * FROM RENTS WHERE Customer_ID = ? AND Utensil_ID = ? AND Returned = 0;");
                    checkRent.setInt(1, customer.getID());
                    checkRent.setInt(2, uIDint);
                    ResultSet checkRentResult = checkRent.executeQuery();
                    if (!checkRentResult.next()) {
                        JOptionPane.showMessageDialog(null, "You haven't rented this utensil!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException e) {
                    outputArea.append("Database error: " + e.getMessage() + "\n");
                }

                Utensil utensil = new Cup(uIDint); // default to be a cup
                try{
                    PreparedStatement checkType = conn.prepareStatement("SELECT Utensil_Type FROM UTENSILS WHERE Utensiil_ID = ?;");
                    checkType.setInt(1, uIDint);
                    ResultSet checkTypeResult = checkType.executeQuery();
                    checkTypeResult.next();
                    String type = checkTypeResult.getString(1);
                    if (type.equals("C")) {
                        utensil = new Cup(uIDint);
                    }
                    else{
                        utensil = new LunchSet(uIDint);
                    }

                }catch(SQLException e){
                    outputArea.append("Database error: " + e.getMessage() + "\n");
                }

                utensil.turnBack();
                Reward.checkAndGiveReward(customer);
                
                
                // fm.turnBack(customer.getID(),uIDint);

                
                
               try { 
                    stat = conn.createStatement();
                    query = String.format("???");
                    boolean success = stat.execute(query);
                    
                    //更新outputArea 現況
                    query = "SELECT * FROM RENTS";
                    success = stat.execute(query);
                    if (success) {
                        ResultSet r = stat.getResultSet();
                        if (r != null) {
                            outputArea.setText(fm.showQueryResult(r));
                            r.close();
                        }
	                   }

                    // check and give reward
                    

                       

                    
                    
               }catch(SQLException e) {
            	   outputArea.append("Database error: " + e.getMessage() + "\n");
               }
                InfoFrame infoFrame=new InfoFrame(customer);
                    
                setVisible(false);
                infoFrame.setTurnbackLabel(utensilID);
                infoFrame.setVisible(true);


            }
        });

        lastPageButton = new JButton("Back to Last Page");
        lastPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                HomeFrame homeFrame=new HomeFrame(customer);
                homeFrame.setVisible(true);
                setVisible(false);
            }
        });
       
    }
    private void createLayout() {
        flowPanel = new JPanel(new GridLayout(2, 1));
        operatePanel = new JPanel(new GridLayout(3, 1));
        
        JPanel labelPanel = new JPanel(new GridLayout(1, 2));
      //  labelPanel.add(column1);
        labelPanel.add(column2);
        
        JPanel textPanel = new JPanel(new GridLayout(1, 2));
      //  textPanel.add(text1);
        textPanel.add(text2);
        
        JPanel opePanel = new JPanel(new GridLayout(1, 1));
        
        opePanel.add(commitButton);
        opePanel.add(lastPageButton);
        
        operatePanel.add(labelPanel);
        operatePanel.add(textPanel);
        operatePanel.add(opePanel);
        
        flowPanel.add(new JScrollPane(outputArea));
        flowPanel.add(operatePanel);
        add(flowPanel);
    }

}