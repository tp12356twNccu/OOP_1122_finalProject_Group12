package group12;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class InfoFrame extends JFrame{
    private Customer customer;
    

    JButton btn;
    JLabel label=new JLabel("you rent sth");
    public InfoFrame(Customer customer) {
        this.customer = customer;

        setTitle("Info Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        label.setFont(new Font("TimesRoman", Font.PLAIN, 60));

        JPanel helloPanel = new JPanel();
        createBtn();
        helloPanel.add(label);
        helloPanel.setPreferredSize(new Dimension(500, 100));

        setLayout(new BorderLayout(20, 60));
        getContentPane().add(new JPanel(), BorderLayout.NORTH);
        getContentPane().add(helloPanel, BorderLayout.CENTER);
        getContentPane().add(btn, BorderLayout.SOUTH);
    }
    public void createBtn(){
        btn=new JButton("continue");
        btn.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			HomeFrame homeFrame=new HomeFrame(customer);
                homeFrame.setVisible(true);
    		}

        });
    }
    public void setRentLabel(String s) {
    	
    	label.setText("You rent for utensil ID :"+s+" !");
    	
    }
    public void setTurnbackLabel(String s) {
    	
    	label.setText("You turned back utensil ID :"+s+" !");
    	
    }
}
