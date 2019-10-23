# LSMDB
Ciao questa è la wiki del progetto di LSMDB

## Obbiettivi del branch Guido1
 - [x] Creazione della classe Connect e ristrutturazione di DepositoDati per la realizzazione di Middle-Layer e Back-end con un database Mysql per il progetto MyFundrasing Project (Task1).
 - [ ] Implementazione e utilizzo di JPA (Task2).

# Task1
## Software Architecture
La realizzazione della classe Connect e il seguente restauro della classe DepositoDati è avvenuto per dividere logicamente Middle-Layer e Back-end come in figura.

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/img/arch_diagram.png)

## Database Modeling
Lo schema ER in figura mostra la struttura relazionare del database implementato dal Mysql server dell'applicazione
![ER diagram](https://github.com/guidogagl/LSMDB/blob/master/img/ER.png)

Tale struttura è implementata mediante l'utilizzo di quattro tabelle 
- **Azienda**
- **Progetto**
- **Finanziamento**
- **Messaggio**

illustrate in dettaglio nel modello sottostante
![ER diagram](https://github.com/guidogagl/LSMDB/blob/master/img/er_details.png)

In seguito sono riportate quattro tabelle con la spiegazione nel dettaglio dei loro campi.

### Azienda
- **nomeAzienda** - chiave primaria, si suppone che ogni azienda abbia un nome diverso.
- **urlLogo** - rappresenta il riferimento ( relativo o assoluto ) al logo dell'a-zione che viene visualizzato nell'applicazione.
- **urlSito** - rappresenta il riferimento al sito web dell'azienda.
- **indirizzo** 
- **cap**
- **password** 

### Progetto
- **id** - chiave primaria, identifica univocamente il progetto nel database
- **nome** - nome del progetto assegnato dall'azienda che lo crea ( possono esistere più progetti con lo stesso nome )
- **budget** - somma di denaro che l'azienda che crea il progetto vuole ottenere attraverso l'applicazione per finanziare interamente il progetto.
- **descrizione** - descrizione descrittiva del progetto
- **azienda** - nome (nomeAzienda) dell'azienda che ha creato il progetto.

### Finanziamento
- **id** - chiave primaria, identifica univocamente il finanziamento nel database.
- **budget** - somma di denaro che l'azienda finanziatrice ha deciso di investire nel progetto finanziato.
- **azienda** - nome ( nomeAzienda ) dell'azienda che ha creato il finanziamento.
- **progetto** - identificativo ( id ) del progetto che si intende finanziare

### Messaggio
- **id** - chiave primaria, identifica univocamente il messaggio nel database.
- **mittente** - nome dell'azienda ( nomeAzienda ) che scrive (crea) il messaggio.
- **destinatario** - nome dell'azienda ( nomeAzienda ) che riceve il messaggio.
- **progetto** - identificativo ( id ) del progetto per il quale si intende chiedere un finanziamento attraverso il messaggio.
- **stake** - somma di denaro che si richiede per finanziare il progetto oggetto del messaggio.
- **data** - data di invio del messaggio.



## Class Modeling 

Le classi sono state strutturate usando il linguaggio Java 

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/img/class_diagram1.png)

Il meccanismo software è stato implementato così per nascondere del tutto a DepositoDati qualsiasi informazione relativa al database e nascondergli il back-end che è lasciato così unicamente a Connect.

Nello specifico DepositoDati fornisce una serie di metodi application-dependent per interrogare il database e restituire al front-end le informazioni di cui ha bisogno.

Il metodo classico con il quale DepositoDati interroga il database consiste nello scrivere la query desiderata dall'utente, costruire un'istanza di Connect, chiamare un metodo di Connect e chiudere la connessione:

```
 String sql = " SELECT * FROM ANYWHERE ; ";
 Connect conn = new Connect();
 
 List<Vector<String>> result = conn.query(sql, numeroColonneTabellaResult);
 
 conn.close();
```
Le istanze di Connect si costruiscono creando una connessione con il database, che viene effettuata con parametri statici password, username e SSL. Per garantire una connessione sicura con la base di dati la variabile SSL è settata con la restrizione più forte "require=TRUE".

Connect realizza da sola tutto il livello di back-end dell'applicazine. Per questo vede unicamente la base di dati ed è completamente svincolata da tutto il resto.

La classe fornisce sostazialmente due metodi pubblici in overload per effettuare query sul database. Il primo effettua query classiche attraverso la classe Statement, il secondo prepara le query per rendere rendere efficienti le chiamate multiple attraverso la classe Prepared Statement.

Il modo classico di effettuare una query per Connect è quello di creare lo statement, eseguire la query, effettuare un fetch del risultato e chiudere lo statement.

```
Statement stmt = conn.createStatement();
stmt.execute(queryString);
ResultSet rs = stmt.getResultSet();

if( numColumns == 0 ) {
	stmt.close();
	return null;
}

List<Vector<String>> fetchedRows =  fetchRows(rs, numColumns);

stmt.close();
return fetchedRows;
```
Nel caso in cui la tabella risultante fosse priva di colonne la funzione prevede un'ottimizzazione evitando di effettuare il fetch del risultato.

Il fetch del risultato è inserito in una lista di vettori di stringhe prima di chiudere lo stmt perchè dopo aver fatto questo si perderebbero i ResultSet. L'unica alternativa a effettuare il fetch dei risultati sarebbe mischiare middle-layer e back-end creando gli stmt al livello di DepositoDati. Per evitare questo si è optato per questa implementazione.
			   
```
PreparedStatement pstmt = conn.prepareStatement(queryString);
for(int i=0, j = 1; i < data.size() ; i++, j++) 
	if(StringUtils.isStrictlyNumeric(data.get(i)))
		pstmt.setInt(j, Integer.parseInt( data.get(i) ) );
	else
		pstmt.setString(j, data.get(i));	
pstmt.execute();
if( numColumns == 0 ) {
	pstmt.close();
	return null;
}
ResultSet rs = pstmt.getResultSet();
if( rs == null) {
	pstmt.close();
	return null;
}
List<Vector<String>> fetchedRows =  fetchRows(rs, numColumns);
pstmt.close();		
return fetchedRows;		
```
Nel caso in cui si voglia effettuare una chiamata con statement di tipo PreparedStatement, Connection riconosce il tipo della variabile da settare attraverso la direttiva StringUtils.isStriclyNumeric(), per questo motivo in tutta l'applicazione devono essere effettuati dei controlli sui tipi per evitare che compilando campi corrispondenti a stringhe, l'utente inserisca valori numerici, che verrebbero riconosciuti erroneamente da Connection e provocherebbero errori nel database.

## Code Explanation
### Connect
Attributi privati
- **username : String = root** 
- **password : String = root**
- **SSL : String =  &requireSSL=true**
- **connStr: String**
- **conn : Connection = null**

Metodi privati
- **fetchRows(Result, int) : List<Vector<String>>** - prende il result set di una query insieme al numero di colonne della tabella di result ed esegue il fetch del risultato su una lista che restituisce 

Metodi pubblici
- **Connect()** - costruttore, crea una connessione di tipo Connection con il database che assegna all'attributo privato conn
- **close()** - chiude la connessione istanziata dall'attributo privato conn
- **query(String, int): List<Vector<String>>** - esegue una query passata per argomento e restituisce il result set in una lista. Il numero di colonne della tabella del result è passato come argomento.
- **query(String, int, Vector<String>): List<Vector<String>>** - metodo in overload con il precedente che esegue una query attraverso un preparedStatement. I dati da assegnare al prepared statement sono passati come argomento.


### DepositoDati
Attributi privati
- **conn : Connect = null**

Metodi privati
- **getRowTableProjects(String, Vector<string>):List<RowTableProjects>** 
- **getRowTableMessage(String, Vector<string>):List<RowTableMessage>**

Metodi pubblici
- getProjects(String, Vector<string>):List<RowTableProjects>
- getMessage(String, Vector<string>):List<RowTableMessage>
- getDescriptionMessage(int):String
- getSommaStakes(int):int
- getProgress(int):double
- getProjectsWithoutStake:List<RowTableProjects>
- insertProject(Vector<String> val): void
- iAmOwner(int, String): boolean
- myStake(String, int): Boolean
- deleteProject(int):void
- getAgency(String, String): Vector<String>
- getDescriptionProject(int): String
- deleteMyStake(int, String): void
- updateStake(int, String, int): void


---



## Obbiettivi del branch Lucia

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/img/ClassiTableMessage.png)

### TableMessage
The TableMessage class has the purpose of constructing the table that will be shown on the application interface.

Private attributes:

- **messagesList: ObservableList\<RowTableMessage>**
This attribute represents a list containing all the rows present in the table. 

Public methods:

- **TableMessage()** - class constructor, inizializes the table columns and then adds them to the table
- **updateMessages(List\<RowTableMessage> messages):void** - Thisfunctionclears all the content of the table and replace it with
the informations stored in the List passed as argument.

```
public void updateMessages(List<RowTableMessage> messages) {
	messagesList.clear();
	messagesList.addAll(messages);
}

```
	
### RowTableMessage
This class is the data representation of the rows in the message table.

Public attributes:

- **id:SimpleIntegerProperty**
- **id_project:SimpleIntegerProperty**
- **data:SimpleStringProperty**
- **mittente:SimpleStringProperty**
- **destinatario:SimpleStringProperty**
- **messaggio:SimpleStringProperty**
- **stake:SimpleIntegerProperty**

Each attribute is a column in the table.

Public methods:

- **RowTableMessage(int id, int id_project, String data, String mittente, String destinatario, String messaggio,
	int stake)** - class constructor, it converts the attribute passed as argument in SimpleItsProperty, where Its is the type of the argument
	
```
public RowTableMessage(int id, int id_project, String data, String mittente, String destinatario, String messaggio,
			int stake) {
		
	this.id = new SimpleIntegerProperty(id);
	this.id_project = new SimpleIntegerProperty(id_project);
	this.data = new SimpleStringProperty(data);
	this.mittente = new SimpleStringProperty(mittente);
	this.destinatario = new SimpleStringProperty(destinatario);
	this.messaggio = new SimpleStringProperty(messaggio);
	this.stake = new SimpleIntegerProperty(stake);
}

```
	
- **getId():int** - returns the private field 'id' of the class
- **getId_project():int** - returns the private field 'id_project' of the class
- **getData():String** - returns the private field 'data' of the class
- **getMittente():String** - returns the private field 'mittente' of the class
- **getDestinatario():String** - returns the private field 'destinatario' of the class
- **getMessaggio():String** - returns the private field 'messaggio' of the class
- **getStake():int** - returns the private field 'stake' of the class


### Fundracing

The following changes have been made to this class.

Private attributes:

- **accept:Button**
- **refuse:Button**
- **l_description_message:Label**
- **description_message:TextArea**
- **stake_message:TextField**
- **l_stake_message:Label**
- **project_message:TextField**
- **l_project_message:Label**
- **send:Button**
- **choice_agency:ChoiceBox**
- **message_receiver:Label**
- **gm:GestoreMessaggi**
- **l_password:Label**
- **l_agencyName:Label**
- **messages_received:Label**
- **l_stake:Label**
- **l_description:Label**
- **l_project_name:Label**
- **l_total_budget:Label**

Public methods:

- **selectTableRow():void** - the function uses a handle, able to detect the selection of a row table, in order to return the information
 contained in that row
- **selectTableMessages():void** - the function uses a handle, able to detect the selection of a row table, in order to return the information
 contained in that row
- **inizializeChoiceBox():void** - the function initializes the drop-down menu with the list of companies in the database


---

## Use Case

At the opening, the application shows the interface represented in the Fig. 1.


!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/StartingInterface.PNG)
*Fig. 1: The picture represents the interface tha will appear to the user at the opening
of the application*


The user can access to its private informations by inserting its credentials (username and password) and clicking on the 'Login' button.


!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/Login.PNG)
*Fig. 2: The picture represents an example of login, using the credentials of the Tesla agency*


After a successful login, the text fields and buttons become active and within the 'NetworkProjects' and 'MessagesReceived' tables the user can see its own information. In particular, the first table shows the various information relating to the projects in the network: the total budget, required to start the project; the progress, which highlights the funding received over the total budget; the name of the project; the name of the project owner and the stake invested in that project by the company that logged in. 
The second table shows all the messages received by the agency that logged in. (Fig. 3)

!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/AfterLogin.PNG)
*Fig. 3: The picture represents an example of how the interface will appear after a successfull login*

 
If the user wants to insert a new project in the network, it must specify the description, the project name and the total budget, and then click the 'Insert' button, as shown in the Fig. 4.


!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/BeforeInserting.PNG)
*Fig. 4: The picture represents an example of insertion of a new project*


After the insertion of a new project, it will appear in the NetworkProjects table, as shown in the figure Fig. 5.

!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/AfterInsertion.PNG)
*Fig. 5: The picture represents how the interface will appear after inserting a new project*


When the user wants to delete one of its own project, it has to click on the corresponding row of the 'NetworkProjects' table and then click the 'Delete' button as in the figure below. If the user tries to delete a project of which he is not the owner, but for which he had made a financing, the click of the 'Delete' button will only cause the reset of his stake for that project. If the user wants to delete a project that is not his own and for which he has not made any financing, an alert window will be displayed.


!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/DeleteProject.PNG)
*Fig. 6: The picture represents how the interface will appear before deleting a project*


If the user wants to update its stake for a project, it has to click on the corresponding row of the table, inserts the new stake in its field, and then clicks the 'Update' button, as in Fig. 7.


!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/DeleteProject.PNG)
*Fig. 7: The picture represents how the interface will appear before updating a stake*


For both messages and projects, clicking on the corresponding row in the table, the description will appear in the 'Description' text field below.
As shown in the Fig. 7.


Finally, a user can accept or reject a message received from another company and can send a new one. 
By selecting the row of the table 'MessagesReceived' and pressing the button 'Accept', the user agrees to make a new financing equal to the value of stake expressed in the message. The images below show the application interface before and after pressing the 'Accept' button.


!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/BeforeAccepting.PNG)
*Fig. 8: The picture represents how the interface will appear before accepting a received message*



!["dominating_sets_example2"](https://github.com/guidogagl/LSMDB/blob/master/img/UseCaseImages/AfterAccept.PNG)
*Fig. 8: The picture represents how the interface will appear after accepting a received message*


The user can decide to refuse a received message by selecting the corresponding row of the table and pressing the 'Refuse' button.

Finally, the user can send a new message to a company by entering a text in the description field, entering the stake, the id of the project to which the message refers and selecting the company receiving the message.


## Obbiettivi del branch Matteo

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/img/class_diagramGestoreMessaggi.png)

The class GestoreMessaggi has the purpose of creating a "scheduler" that will update the messages'table every 5 seconds

Private attributes:

- **d : DepositoDati** 
- **tm : TableMessage**
- **agencyName : String**
- **timer : Timer = null**
- **task : TimerTask = null**

Public methods

- **GestoreMessaggi(DepositoDati,TableMessage,String):void** -Initialize all the private fields of GestoreMessaggi inside the constructor

- **startAggiornamentoTabella():void** -create, using the constructs Timer and TimerTask, a "scheduler" that will call every 5 seconds the function aggiornaTabellaMessaggi()

```
timer = new Timer();
task = new TimerTask() 
{
	public void run() 
	{
					
		aggiornaTabellaMessaggi();
					
	}
};
timer.schedule(task, 0, 5000);

```

- **endAggiornamentoTabella():void** -stop the "scheduler" previously created

```

{
	timer.cancel();
	timer.purge();
}

```

Private methods

- **aggiornaTabellaMessaggi():void** -retrieve from the database the messages related to the agency, that previously logged in, and show them in the related table 

```

{
	List<RowTableMessage> messaggiDaAggiungere = d.getMessages(agencyName);
	tm.updateMessages(messaggiDaAggiungere);
}

```

------

Update use case diagram

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/img/usecase.png)


The main actor of the application is an agency chief. The application provides to the user six different main functionalities:

**1)Login** The user can, by inserting the username and password, confirm its identity and access to private informations

**2)Registration** By filling in a predeterminated form, the user can register its agency inside the network and from now on have an active part in financing other projects and receive founds for its own

**3)View Projects List** The chief agency will see,in a table, all the projects previously insered by the companies part of the network. If it's interested, by clicking a row, it will automatically receive additional informations, namely 
the description and the stake insered until then, and also it can update the stake of a project or delete the projects itself,if it's the owner of it, or just the investment made, if it's not.

**4)Add Project** The user can insert a new project requiring founds to start, and it will automatically be seen in the table.

**5)Add Request** The user can ask to other agency chiefs present in the network to fund one of its projects. That request will be automatically be seen by the receiver.

**6)View Messages** The chief agency will see, in a table, all the requests made to it. If it's interested, by clicking a row, it will automatically receive the additional information of the description, and also it can accept or decline
 the request.


