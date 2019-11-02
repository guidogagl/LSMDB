
package application;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;
import jpaConnect.DepositoDati;


class RegistrationForm extends JFrame {

         JLabel nameAgency = new JLabel("Agency Name:");
         JLabel address = new JLabel("Address:");
         JLabel ZIP = new JLabel("ZIP code:");
         JPasswordField password = new JPasswordField(16);
         JPasswordField confirm_password = new JPasswordField(16);
         JTextField name_field = new JTextField();
         JTextField address_field = new JTextField();
         JTextField ZIP_field = new JTextField();
         JTextField url = new JTextField();
         JLabel insertPassword = new JLabel("Password:");
         JLabel insertUrl = new JLabel("Web Site:");
         JLabel urlLogo = new JLabel("Logo: ");
         JTextField urlLogo_field = new JTextField();
        private JButton submit = new JButton("Submit");
        private JButton discard = new JButton("Reset");
        public Interface interfaccia;
        private DepositoDati deposito=new DepositoDati();

        public RegistrationForm() {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           discard.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent e) {
                    nullify();
                }

            });           
            submit.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (name_field.getText().isEmpty() || address_field.getText().isEmpty() || ZIP_field.getText().isEmpty()
                                    || password==null || confirm_password.getPassword()==null|| url.getText().isEmpty() || urlLogo_field.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Beware! The registration form is incomplete");
                    } else {
                        if (deposito.getAgency(name_field.getText())!=null) {
                            JOptionPane.showMessageDialog(null, "This agency is already present!");
                            
                        } else {
                            if(password.getPassword().length==0)
                                JOptionPane.showMessageDialog(null, "Password not inserted!");
                            if (!Arrays.equals(password.getPassword(), confirm_password.getPassword())) {
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
                                val.addElement(new String(password.getPassword()));
                                deposito.insertAgency(val);
                                
                            }
                        }

                    }

                }
            }
            );
          
    }
        public void nullify(){
            name_field.setText("");
            address_field.setText("");
            ZIP_field.setText("");
            
            url.setText("");
            urlLogo_field.setText("");
            
        }

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
        public JLabel getUrlLogo(){return urlLogo;}
        public JTextField geturlLogo_field(){return urlLogo_field;}
        public JButton submit(){return submit;}
        public JButton discard(){return discard;}

  }
  