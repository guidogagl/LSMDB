package esercizio1;
import java.awt.event.*; 
import java.awt.*; 
import java.util.Arrays;
import javax.swing.*;


public class registerPanel extends JFrame 
{   private static Boolean filled= false;
    private JLabel nameAgency= new JLabel("Agency Name:");
    private JLabel address= new JLabel ("Address:");
    private JLabel ZIP= new JLabel("ZIP code:");
    private static JPasswordField password= new JPasswordField(16);
    private static JPasswordField confirm_password= new JPasswordField(16);
    private JTextField name_field= new JTextField();
    private JTextField address_field= new JTextField();
    private JTextField ZIP_field= new JTextField();
    private JTextField url=new JTextField();
    private JLabel insertPassword=new JLabel("Password:");
    private JLabel insertUrl=new JLabel("Web Site:");

   private  JButton submit=new JButton("Submit");
     private   JButton discard=new JButton("Discard");
     public static Boolean getFilled(){return filled;}
    public String getnameAgency(){return this.name_field.getText();}
    public String getaddress(){return this.address_field.getText();}
    public static String getAgencyPassword(){return new String(password.getPassword());}
    public String getAgencyField(){return ZIP_field.getText();}
    public String getAgencyWebSite(){return url.getText();}
    public registerPanel(){
        setVisible(true);
        setTitle("Network Registration Form");
        setSize(700, 700);
        setLayout(null);
        this.nameAgency.setBounds(100, 30, 400, 30);
        this.name_field.setBounds(250, 30, 350, 30);
        this.address.setBounds(100, 70, 400, 30);
        this.address_field.setBounds(250, 70, 350, 30);
        this.ZIP.setBounds(100, 110, 400, 30);
        this.ZIP_field.setBounds(250, 110, 350, 30);
        this.insertPassword.setBounds(100, 150, 400, 30);
        this.password.setBounds(250, 150, 350, 30);
        JLabel confirmPasswordLabel=new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(100, 190, 400, 30);
        this.confirm_password.setBounds(250, 190, 350, 30);
        this.insertUrl.setBounds(100, 230, 350, 30);
        this.url.setBounds(250, 230, 350, 30);
        submit.setBounds(200, 300, 140, 30);
        discard.setBounds(400, 300, 140, 30);
        add(nameAgency);
        add(name_field);
        add(address);
        add(address_field);
        add(ZIP);
        add(ZIP_field);
        add(insertPassword);
        add(password);
        add(confirmPasswordLabel);
        add(confirm_password);
        add(url);
        add(insertUrl);
        add(submit);
        add(discard);
        discard.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
      {     
          setVisible(false);
            }
      
    });
      submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
      {   if(Arrays.equals(password.getPassword(), confirm_password.getPassword())){
          filled=true;
          setVisible(false);
            }
      else{
         JOptionPane.showMessageDialog(null, "The two passwords do not coincide!");
         password=new JPasswordField();
         confirm_password=new JPasswordField();
         
      }
      
      }
      }
            );}
           
    
              }