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


        public RegistrationForm(Interfaccia interface) {
           this.interfaccia=interfaccia;
          
           this.interfaccia.setRegistration(this);
           discard.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }

            });
            submit.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!Arrays.equals(password.getPassword(), confirm_password.getPassword())) {
                        JOptionPane.showMessageDialog(null, "The two passwords do not coincide!");
                        password = new JPasswordField();
                        confirm_password = new JPasswordField();
                    } else {
                        if (deposito.agencyAlreadyPresent(name_field.getText())) {
                            JOptionPane.showMessageDialog(null, "This agency is already present!");
                            setVisible(false);
                        } else {
                            if (name_field.getText().isEmpty() || address_field.getText().isEmpty() || ZIP_field.getText().isEmpty()
                                    || password.getPassword().length == 0 || confirm_password.getPassword().length == 0 || url.getText().isEmpty() || urlLogo_field.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Beware! The registration form is incomplete");
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
  }
  
