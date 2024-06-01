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

    public HomeFrame(Customer customer) {
        setTitle("HOME");
        setSize(500, 300);    
        createBtn();
        createLayout();
        this.customer = customer;
    }
    
    private void createLayout() {
        radio=new JRadioButton();
        radio.add(btnTurnback);
        radio.add(btnRent);
        setLayout(new BorderLayout(20, 40));
        getContentPane().add(new JPanel(), BorderLayout.NORTH);
        getContentPane().add(radio, BorderLayout.CENTER);
        getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    }
   
    private void createBtn() {

    	btnRent= new JButton("Lend ");
    	btnTurnback= new JButton("Turn back");
        
    	btnRent.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			RentFrame rentFrame=new RentFrame();
                rentFrame.setVisible(true);
    		}
    	});
    	
    	btnTurnback.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent w) {
                TurnbackFrame turnbackFrame=new TurnbackFrame();
                turnbackFrame.setVisible(true);
            }
    	});
    }
    
    
}