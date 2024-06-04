package group12;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

//第73行
public class LoginFrame1 extends JFrame {

	private FunctionManager fm=new FunctionManager();
    private JButton btnEnroll, btnLogin;
    private JTextField tfUserName, tfPassword;

    private JPanel panel = (JPanel) this.getContentPane();


    public LoginFrame1() {
        setTitle("Login");
        setSize(500, 300);
        tfUserName = new JTextField(20);
        tfPassword = new JTextField(20);
        
        createBtn();
        createLayout();
    }
    public String getName() {
    	return tfUserName.getText();
    }
    private void createLayout() {
        JPanel uPanel = new JPanel();
        uPanel.add(new JLabel("Username:"));
        uPanel.add(tfUserName);
        uPanel.setPreferredSize(new Dimension(500, 40));

        JPanel pPanel = new JPanel();
        pPanel.add(new JLabel("Password:"));
        pPanel.add(tfPassword);
        pPanel.setPreferredSize(new Dimension(500, 40));

        JPanel bPanel = new JPanel();
        bPanel.add(btnEnroll);
        bPanel.add(btnLogin);
        bPanel.setPreferredSize(new Dimension(500, 40));

        JPanel allPanel = new JPanel();
        allPanel.setLayout(new GridLayout(3, 1, 0, 10));
        allPanel.add(uPanel);
        allPanel.add(pPanel);
        allPanel.add(bPanel);

        setLayout(new BorderLayout(20, 40));
        getContentPane().add(new JPanel(), BorderLayout.NORTH);
        getContentPane().add(allPanel, BorderLayout.CENTER);
        getContentPane().add(new JPanel(), BorderLayout.SOUTH);
    }

    private void createBtn() {
    	// lend=new JButton("Lend") ;
        // turnback=new JButton("Turn back");
        // limitationUpload=new JButton("Uplaod limitaion");
        btnEnroll = new JButton("Enroll");
        btnLogin = new JButton("Login");

        btnEnroll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = tfUserName.getText();
                String pw = tfPassword.getText();
                try {
                    fm.registration(name, pw);    
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                
                
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent w) {
                String name = tfUserName.getText();
                String pw = tfPassword.getText();
                try {
                    
                    Customer customer = new Customer(name, pw);
                    boolean loginSuccess = customer.login();
                    if(loginSuccess){
                        HomeFrame homeFrame = new HomeFrame(customer);
                        homeFrame.setVisible(true);
                        LoginFrame1.this.setVisible(false);
                    }
                    else{
                        JOptionPane.showMessageDialog(LoginFrame1.this, "Login failed: " + "Wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    }                  

                    

                    


                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginFrame1.this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });


    	
    	
    }
    
}