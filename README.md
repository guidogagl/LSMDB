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

Le classi sono state strutturate usando il linguaggio Java 

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/class_diagram1.png)

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

The TableMessage class has the purpose of constructing the table that will be shown on the application interface.

Private attributes:

.messagesList: ObservableList<RowTableMessage>
This attribute represents a list containing all the rows present in the table. 

Public methods:

.TableMessage() - class constructor, inizializes the table columns and then adds them to the table
.updateMessages(List<RowTableMessage> messages):void - Thisfunctionclears all the content of the table and replace it with
the informations stored in the List passed as argument.


## Obbiettivi del branch Matteo

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/class_diagramGestoreMessaggi.png)

The class GestoreMessaggi has the purpose of creating a "scheduler" that will update the messages'table every 5 seconds

Private attributes:

- **d : DepositoDati** 
- **tm : TableMessage**
- **agencyName : String**
- **timer : Timer = null**
- **task : TimerTask = null**

Public methods

- **GestoreMessaggi(DepositoDati,TableMessage,String):void** -Initialize all the private fields of GestoreMessaggi inside the constructor
- **startAggiornamentoTabella():void** -create,using the constructs Timer and TimerTask, a "scheduler" that will call every 5 seconds the function aggiornaTabellaMessaggi()
- **endAggiornamentoTabella():void** -stop the "scheduler" previously created

Private methods

- **aggiornaTabellaMessaggi():void** -retrieve from the database the messages related to the agency, that previously logged in, and show them in the related table 

------

Update use case diagram

![archetture diagram](https://github.com/guidogagl/LSMDB/blob/master/usecase.png)


