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
    private JLabel column1, column2;
    private JTextField text1, text2;
    private JButton commitButton;
    private Connection conn;
    private Statement stat;
    private Customer customer;

    


    public TurnbackFrame(Customer customer) {
        this.customer = customer;
        setTitle("Turnback");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    
        stat = conn.createStatement();

        //待確認query
        String query = "SELECT * FROM RENTS";
        
        boolean success = stat.execute(query);
        if(success){
        	ResultSet r=stat.getResultSet();
        	outputArea.append(fm.showQueryResult(r));
        	r.close();
        }
    }

    private void createLabel() {
        column1 = new JLabel("Customer ID");
        column2=new JLabel("Utensil ID:");
        //借用頁面只讓使用者填寫Utensil ID?
        
    }

    private void createTextField() {
        text1 = new JTextField(10);
        //text1應該要放UserName
        text2 = new JTextField(10);
    }

    private void createButton() throws SQLException {
        commitButton = new JButton("Commit");
        commitButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent event) {

                String query = "";
                String utensilID = text1.getText();
                int uIDint = Integer.parseInt(utensilID);
                
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

                    
                    
               }catch(SQLException e) {
            	   outputArea.append("Database error: " + e.getMessage() + "\n");
               }
               InfoFrame infoFrame=new InfoFrame(customer);
                    
                    infoFrame.setTurnbackLabel(utensilID);
                    infoFrame.setVisible(true);


            }
        });

       
    }
    private void createLayout() {
        flowPanel = new JPanel(new GridLayout(2, 1));
        operatePanel = new JPanel(new GridLayout(3, 1));
        
        JPanel labelPanel = new JPanel(new GridLayout(1, 2));
        labelPanel.add(column1);
        labelPanel.add(column2);
        
        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.add(text1);
        textPanel.add(text2);
        
        JPanel opePanel = new JPanel(new GridLayout(1, 1));
        
        opePanel.add(commitButton);
        
        operatePanel.add(labelPanel);
        operatePanel.add(textPanel);
        operatePanel.add(opePanel);
        
        flowPanel.add(new JScrollPane(outputArea));
        flowPanel.add(operatePanel);
        add(flowPanel);
    }

}