# LSMDB
Ciao questa è la wiki del progetto di LSMDB

## Obiettivi del branch amaryllis (20/10)
- It was implemented the registration mechanism, by modyfing the structure of Fundracing class as to include an inner class, RegistrationForm, existing only in the scope of the application instance and used for allowing the user to insert its data in a new frame. Both Fundracing and RegistrationForm are front-end classes which interact with the database through DepositoDati and Connect.
Why is RegistrationFrom an inner class? Because its scope is within the cicle of life of Fundracing and because, as of Java documentation, the use of inner or nested class is recomended for enabling encapsulation and improving code readibility. A *Singleton* implementation of the registration mechanism is currently _under consideration_.
- (discarded, as already solved in Lucia's branch, _code available upon need and request_) the implementation of a choice menu to select the receiver of the stake request. Class Choice from java.awt was used in the discarded solution, implementing a callback mechanism (applying an Observer pull methodology) in order to keep it up-to-date with the registration of new companies to the network.
- (to be done, as not clear if it can be simplified by exploiting Guido's work) a Listener thread to store and retrieve messages from a shared UnifiedQueue<String>.




The main aim of class RegistrationForm is to provide the user with a mechanism to insert all the information concerning its agency, in order to joint the foundraising scheme. Checks are performed on all the inserted data before allowing the insert, that is:


- The agency musn't already be enrolled with the same business name in the network.
- The entered information regarding the password, in fields _password_ and _confirm password_ of the form, must coincide. In return, the class will provide the user with a mechanism to insert the password without showing the single characters;
- The ZIP code, as for the specifi constraints given to attribute _cap_ in the scheme of table _Agenzia_, must be a numeric sequence.
- All fields must be filled before submitting the form.

Class Fundracing, through a **register:Button** button, offers the possibility to enroll new agencies in the network. It also takes the responsability to instantiate the interface of its reference **form:RegistrationForm** to class _RegistrationForm_. In order to do so neatly, the constructor of class _Interface_ was made polymorphic, offering different kinds of layout setting depending on the arguments passend. Let us note that the **interface:Interface** of class RegistrationForm and the  **interface:Interface** of class Fundraising are two different objects in memory, per default features of the implementation.
Here the call-back mechanism inside method **start()** of class Fundracing:
```
form= new RegistrationForm();
form.getInterface()=new Interface(form);
form.setVisible(true);
```


Throgh button _submit_ RegistrationForm performs a correctness check of the inserted data, following the criteria illustrated above. If the controls are successful, it delegates the insert of the agency in the database to its reference _deposito_ of class _DepositoDati_.


```
submit.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!Arrays.equals(password.getPassword(), confirm_password.getPassword())) {
                        JOptionPane.showMessageDialog(null, "The two passwords do not coincide!");
                        password = new JPasswordField();
                        confirm_password = new JPasswordField();
                    } 
                    else {
                        if ((deposito.getAgency(name_field.getText()).isEmpty()) {
                            JOptionPane.showMessageDialog(null, "This agency is already present!");
                            setVisible(false);
                        } else {
                            if (name_field.getText().isEmpty() || address_field.getText().isEmpty() || ZIP_field.getText().isEmpty()|| password.getPassword().length == 0 || confirm_password.getPassword().length == 0||url.getText().isEmpty() ||urlLogo_field.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Beware! The registration form is incomplete");
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
            });
            
```


# RegistrationForm
![Class RegistrationForm](https://github.com/guidogagl/LSMDB/blob/Amaryllis/Untitled%20Diagram.jpg)

Attributi privati


- **nameAgency:JLabel**
- **address:JLabel**
- **ZIP: JLabel**
- **insertPassword:JLabel**
- **urlLogo:JLabel**
- **insertUrl:JLabel**
- **url:JTextField**
- **name_field:JTextField**
- **address_field:JTextField**
- **ZIP_field:JTextField**
- **url:JTextField**
- **urlLogo_field:JTextField**
- **password:JPasswordField**
- **confirm_password: JPasswordField**
- **submit: JButton**
- **discard: JButton**
- **interface:Interface**
- **deposito:DepositoDati**

Metodi pubblici

-  **public RegistrationForm()** - the constructors sets the events associated with the _submit_ and _discard_ buttons and delegates the setting of its view to its instance of class _Interface_. 
- **getname_field(void) : JTextField** - the _getter_ method for the prospective agency's name.
- **getZIP_field(void) : JTextField** -  the _getter_ method for the prospective agency's ZIP code.
- **getaddress(void):JLabel**
- **getnameAgency(void):JLabel** 
- **getZIP(void):JLabel** 
- **getinsertUrl(void):JLabel**
- **getpassword(void):JPasswordField** 
- **getconfirm_password(void):JPasswordField** 
- **geturl:JTextField**
- **getname_field:JTextField**
- **getaddress_field:JTextField**
- **getZIP_field:JTextField**
- **geturl:JTextField**
- **geturlLogo_field:JTextField**
- **getsubmit:JButton**
- **getdiscard:JButton**
- **getInterface:Interface** - used for the call-back setup mechanism.

# Fundracing


Attributi privati nuovi
- **form:RegistrationForm**
- **reister:Button**
 
 




