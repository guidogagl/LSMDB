# LSMDB
Ciao questa è la wiki del progetto di LSMDB



## Obbiettivi del branch Guido1
 - [x] Creazione della classe Connect e ristrutturazione di DepositoDati per la realizzazione di Middle-Layer e Back-end con un database Mysql per il progetto MyFundrasing Project (Task1).
 - [ ] Implementazione e utilizzo di JPA (Task2).

# Task1
## Software Architecture
La realizzazione della classe Connect e il seguente restauro della classe DepositoDati è avvenuto per dividere logicamente Middle-Layer e Back-end come in figura.

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/arch_diagram.png)

## Class Modeling 
### Class Diagram 

---



## Obbiettivi del branch Lucia

The TableMessage class has the purpose of constructing the table that will be shown on the application interface.

Private attributes:

.messagesList: ObservableList<RowTableMessage>
This attribute represents a list containing all the rows present in the table. 

Public methods:

.TableMessage() - class constructor, inizializes the table columns and then adds them to the table
.updateMessages(List<RowTableMessage> messages):void - Thisfunctionclears all the content of the table and replace it with
the informations stored in the List passed as argument.
 
## Obbiettivi del branch amaryllis (da tradurre in inglese, ci penso io dopo)
 1) FATTO: aggiungere alla classe Fundracing la possibilità di registrare un nuovo utente, questo avviene tramite un evento sul bottone Register, che porta all'istanziazione di un riferimento alla classe interna RegisterForm. Se sono stati inseriti tutti i dati, password e confirm_password coincidono e l'azienda non è già presente, viene aggiunta al database (CONTROLLARE SE EFFETTIVAMENTE AVVIENE).
 TODO: passare cifrata la password, controllare che gli url inseriti siano validi.
 2) TO DO: Thread listener dell'oggetto condiviso.
 3) TO DO: menù a tendina che permetta di scegliere azienda destinatario
 
 Dunque in DepositoDati c'è nuovo metodo insertAgency() e agencyAlreadyPresent()
 In fundracing classe interna RegistrationForm()
