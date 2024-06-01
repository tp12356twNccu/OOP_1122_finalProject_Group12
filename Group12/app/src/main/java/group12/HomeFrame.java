package group12;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class HomeFrame extends JFrame {
    private FunctionManager fm=new FunctionManager();
    private JButton btnRent, btnTurnback;
    private JPanel panel = (JPanel) this.getContentPane();
    private JRadioButton radio;
    private Customer customer;
    private JPanel btnPanel = new JPanel();

    public HomeFrame(Customer customer) {
        setTitle("HOME");
        setSize(500, 300);    
        createBtn();
        createLayout();
        this.customer = customer;
    }
    
    private void createLayout() {
        btnPanel.add(btnRent);
        btnPanel.add(btnTurnback);
        setLayout(new BorderLayout(20, 40));
        getContentPane().add(new JPanel(), BorderLayout.NORTH);
        getContentPane().add(btnPanel, BorderLayout.CENTER);
        getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    }
   
    private void createBtn() {

    	btnRent= new JButton("Rent");
    	btnTurnback= new JButton("Turn back");
        
    	btnRent.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			RentFrame rentFrame=new RentFrame(customer);
                rentFrame.setVisible(true);
                setVisible(false);
    		}
    	});
    	
    	btnTurnback.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent w) {
                TurnbackFrame turnbackFrame=new TurnbackFrame(customer);
                turnbackFrame.setVisible(true);
                setVisible(false);
            }
    	});
    }
    
    
}