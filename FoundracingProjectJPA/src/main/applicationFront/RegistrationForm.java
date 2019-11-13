
package applicationFront;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;

import javafx.scene.control.Alert;
import jpaConnect.DepositoDati;
import lvDbConnect.DepositoDatiLevelDb;


public class RegistrationForm extends JFrame {

         public JLabel nameAgency = new JLabel("Agency Name:");
         public JLabel address = new JLabel("Address:");
         public JLabel ZIP = new JLabel("ZIP code:");
         public JPasswordField password = new JPasswordField(16);
         public JPasswordField confirm_password = new JPasswordField(16);
         public JTextField name_field = new JTextField();
         public JTextField address_field = new JTextField();
         public JTextField ZIP_field = new JTextField();
         public JTextField url = new JTextField();
         public JLabel insertPassword = new JLabel("Password:");
         public JLabel insertUrl = new JLabel("Web Site:");
         public JLabel urlLogo = new JLabel("Logo: ");
         public JTextField urlLogo_field = new JTextField();
         public JButton submit = new JButton("Submit");
         public JButton reset = new JButton("Reset");
         //public DepositoDatiLevelDb deposito=null;
         public DepositoDati deposito=null;
         private Boolean Mysql_active = true;

        public RegistrationForm(DepositoDatiLevelDb db) {
       // public RegistrationForm(DepositoDati db) {

        	deposito=db;
        	reset.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent e) {
                   resetFields();
                }

            });
        	
        	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        	
            submit.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) 
                {
                	
                	if(!Mysql_active) {
                		JOptionPane.showMessageDialog(null, "Non puoi rigistrarti perché manca la connessione al server.");
    					return;
                	}else 
                	{
                	
                    if (name_field.getText().isEmpty() || address_field.getText().isEmpty() || ZIP_field.getText().isEmpty()
                                    || password.getPassword()==null || confirm_password.getPassword()==null|| url.getText().isEmpty() || urlLogo_field.getText().isEmpty()) 
                    {  
                                JOptionPane.showMessageDialog(null, "Attenzione! Alcuni campi sono ancora vuoti!");
                    }
                    else if(!ZIP_field.getText().matches("[0-9]+")) 
                    {
                    	JOptionPane.showMessageDialog(null, "Attenzione! Il cap deve essere numerico!");
                    	ZIP_field.setText("");
                    }
                    else
                    {
                        if (!deposito.getAgency(name_field.getText()).isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Questa azienda è già presente!");
                            resetFields();
                        }
                        else 
                        {
                            if(password.getPassword()==null)
                                JOptionPane.showMessageDialog(null, "Password non inserita!");
                            else if (!Arrays.equals(password.getPassword(), confirm_password.getPassword())) 
                            {
                            	JOptionPane.showMessageDialog(null, "Le due password non coincidono!");
                                password.setText("");;
                                confirm_password.setText("");;
                            } 
                            else 
                            {
                                Vector<String> val = new Vector<String>();
                                val.add(name_field.getText());
                                val.add(urlLogo_field.getText());
                                val.add(url.getText());
                                val.add(address_field.getText());
                                val.add(ZIP_field.getText());
                                //password passata in chiaro!!!
                                val.add(new String(password.getPassword()));
                                if(Mysql_active==true) 
                                {
                                	deposito.insertAgency(val);	//Inserisce direttamente l'agenzia nel database
                                	dispose();
                                	JOptionPane.showMessageDialog(null, "Inserimento realizzato con successo!");
                                }
                                else
                                {
                                	dispose();
                                	JOptionPane.showMessageDialog(null, "Inserimento non realizzabile,connessione con il database assente!");
                                }
                            }
                        }

                    }

                }
            }
                
                //Chiamo il gestore che copia le informazioni da database a cache
                
            }
            );
          
    }
        
        private void resetFields() {
        	ZIP_field.setText("");
            password.setText("");
            confirm_password.setText("");
            name_field.setText("");
            address_field.setText("");
            ZIP_field.setText("");
            url.setText("");
            urlLogo_field.setText("");
        }
    
  }
  