
package esercizio1;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author Utente
 */

class RegistrationForm extends Frame {

        private JLabel nameAgency = new JLabel("Agency Name:");
        private JLabel address = new JLabel("Address:");
        private JLabel ZIP = new JLabel("ZIP code:");
        private JPasswordField password = new JPasswordField(16);
        private JPasswordField confirm_password = new JPasswordField(16);
        private JTextField name_field = new JTextField();
        private JTextField address_field = new JTextField();
        private JTextField ZIP_field = new JTextField();
        private JTextField url = new JTextField();
        private JLabel insertPassword = new JLabel("Password:");
        private JLabel insertUrl = new JLabel("Web Site:");
        private JLabel urlLogo = new JLabel("Logo: ");
        private JTextField urlLogo_field = new JTextField();
        private JButton submit = new JButton("Submit");
        private JButton discard = new JButton("Discard");
        private Interface interfaccia;
        private DepositoDati deposito=new DepositoDati();


        public RegistrationForm() {
           discard.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }

            });
            submit.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (name_field.getText().isEmpty() || address_field.getText().isEmpty() || ZIP_field.getText().isEmpty()
                                    || password.getPassword().isEmpty() || confirm_password.getPassword().isEmpty()|| url.getText().isEmpty() || urlLogo_field.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Beware! The registration form is incomplete");
                    } else {
                        if ((deposito.getAgency(name_field.getText()).isEmpty()) {
                            JOptionPane.showMessageDialog(null, "This agency is already present!");
                            setVisible(false);
                        } else {
                            if (password.getPassword().isEmpty||(!Arrays.equals(password.getPassword(), confirm_password.getPassword())) {
                        JOptionPane.showMessageDialog(null, "The two passwords do not coincide!");
                                password = new JPasswordField();
                                confirm_password = new JPasswordField();
                            } else {
                                Vector<String> val = new Vector<String>();
                                val.addElement(name_field.getText());
                                val.addElement(urlLogo_field.getText());
                                val.addElement(url.getText());
                                val.addElement(address_field.getText());
                                val.addElement(ZIP_field.getText());
                                //password passata in chiaro!!!
                                val.addElement(new String(password.getPassword()));
                                deposito.insertAgency(val);
                            }
                        }

                    }

                }
            }
            );
          
    }
    //getter methods
    

        public JTextField getname_field() {return name_field;}
        public JLabel getnameAgency() {return nameAgency;}
        public JTextField getddress_field() {return address_field;}
        public JLabel getAddress() {return address;}
        public JLabel getZIP() {return ZIP;}
        public JTextField getZIP_field() {return ZIP_field;}
        public JLabel getinsertPassword() {return insertPassword;}
        public JTextField getpassword(){return password;}
        public JTextField getconfirm_password(){return confirm_password;}
        public JLabel getinsertUrl(){return insertUrl;}
        public JTextField geturl(){return url;}
        public JLabel urlLogo(){return urlLogo;}
        public JTextField geturlLogo_field(){return urlLogo_field;}
        public JButton submit(){return submit;}
        public JButton discard(){return discard;}
        public Interface getInterfaccia(){return interfaccia;}

  }
  
