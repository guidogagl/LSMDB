# LSMDB
Ciao questa è la wiki del progetto di LSMDB

## Obiettivi del branch amaryllis (20/10)
- It was implemented the registration mechanism, by modyfing the structure of Fundracing class as to include an inner class, RegistrationForm, existing only in the scope of the application instance and used for allowing the user to insert its data in a new frame. Both Fundracing and RegistrationForm are front-end classes which interact with the database through DepositoDati and Connect.
Why is RegistrationFrom an inner class? Because its scope is within the cicle of life of Fundracing and because, as of Java documentation, the use of inner or nested class is recomended for enabling encapsulation and improving code readibility. A *Singleton* implementation of the registration mechanism is currently _under consideration_.
- (discarded, as already solved in Lucia's branch, _code available upon need and request_) the implementation of a choice menu to select the receiver of the stake request. Class Choice from java.awt was used in the discarded solution, implementing a callback mechanism (applying an Observer pull methodology) in order to keep it up-to-date with the registration of new companies to the network.
- (to be done, as not clear if it can be simplified by exploiting Guido's work) a Listener thread to store and retrieve messages from a shared UnifiedQueue<String>.

### Implementation of _Fundracing_

## Schema della classe

## descrizione delle modifiche
E' stato aggiunto un evento al bottone register: Button tale per cui, quando viene cliccato, viene inizializzato il campo privato form:RegistrationForm. Tramite un meccanismo di callback, Fundracing si occupa di inizializzare l'attributo privato interface:Interfaccia della propria istanza di RegistrationForm.

```
form= new RegistrationForm();
form.getInterface()=new Interface(form);
form.setVisible(true);
```
#attributi privati nuovi
- form:RegistrationForm
- register:Button


### Implementation of _Registration Form_
## main idea
La classe RegistrationForm ha come compito principale il reperimento delle informazioni sulle aziende che intendono registrarsi al network. Trasmette poi queste informazioni, se corrette, al database. Estende la classe _Frame_ della libreria _java.awt_.
## Schema della Classe

# attributi privati
I campi statici:


- nameAgency:JLabel
- address:JLabel
- ZIP: JLabel 
- insertPassword:JLabel
- urlLogo:JLabel


I seguiti campi di inserimento dati:


- insertUrl:JTextField


I due campi per l'inserimento delle password:


- password:JPasswordField
- confirm_password: JPasswordField


I due bottoni:


- submit: JButton
- discard: JButton


# metodi pubblici

- Metodi getter per gli attributi sovramenzionati





 
 
 
 




