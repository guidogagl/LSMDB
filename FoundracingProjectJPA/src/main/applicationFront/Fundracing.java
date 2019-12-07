package applicationFront;


import java.util.*;
import java.util.logging.Level;

import javafx.collections.*;

import javax.swing.JLabel;

import applicationMiddle.Gestore;
import applicationMiddle.Interface;
import applicationMiddle.RowTableMessage;
import applicationMiddle.RowTableProjects;
import applicationMiddle.wrapperDbs;
import applicationMiddle.Interface;
import javafx.scene.image.*;

import javafx.application.*;
import static javafx.application.Application.launch;

import javafx.event.*;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import javafx.stage.*;
import javafx.util.*;
import jpaConnect.DepositoDati;
import jpaEntities.MessaggioEntity;
import lvDbConnect.DepositoDatiLevelDb;


public class Fundracing extends Application
{
	
	protected TextField tf_companyName = new TextField();
	protected Button login = new Button("Login");
	protected Label table_title = new Label("NetworkProjects");
	protected Label messages_received = new Label("Messages Received");
	protected Label l_password=new Label("Password");
	protected Label l_agencyName=new Label("Agency Name");
	protected TextArea description = new TextArea();
	protected TextField name_project = new TextField("");
	protected TextField total_budget = new TextField("");
	protected TextField stake = new TextField("");
	private Label l_stake = new Label("Stake");
	private Label l_description = new Label("Description");
	private Label l_project_name = new Label("Project Name");
	private Label l_total_budget = new Label("Total Budget");
	protected PasswordField tf_password=new PasswordField();
	protected Button update = new Button("Update");
	protected Button insert = new Button("Insert");
	protected Button delete = new Button("Delete");
	private Boolean logged = false;
	protected String agencyName = "";
	private TableProjects table = new TableProjects();
	private int selectedProjectId = 0;
	private int selectedMessagetId = 0;
	private int selectedStake=0;
	private int selectedTotalBudget=0;
	private int selectedMessageStake = 0;
	private int selectedProjectMessageId = 0;
	private Label name_agency = new Label("");
	private Label address_agency = new Label("");
	private Label site_agency = new Label("");
	private DepositoDati dbMySql = new DepositoDati();
	private DepositoDati dbKV=new DepositoDatiLevelDb();
	private wrapperDbs deposito=dbKV;
	//private  dep=new DepositoDati();
	//private wrapperDbs deposito=new wrapperDbs();
	
	//private DepositoDatiLevelDb deposito =(DepositoDatiLevelDb)depositoMySql;
	//private DepositoDati deposito=new DepositoDati();
	private JLabel label;
	private Image image;
	private ImageView iv1 = new ImageView();
	private TableMessage table_message = new TableMessage();
	
	private Button accept = new Button("Accept");
	private Button refuse = new Button("Refuse");
	private Label l_description_message = new Label("Description Message");
	private TextArea description_message = new TextArea();
	private TextField stake_message = new TextField();
	private Label l_stake_message = new Label("Stake");
	private TextField project_message = new TextField();
	private Label l_project_message = new Label("ID Project");
	private Button send = new Button("Send");
	private ChoiceBox choice_agency = new ChoiceBox(null);
	private Label message_receiver = new Label("Message receiver");
	private Gestore gm =new Gestore(deposito,table, table_message,choice_agency, agencyName);
    private Button register=new Button("Register");
	private RegistrationForm form=new RegistrationForm(deposito);
	private Button mysql=new Button();
	private Button keyValue=new Button();
	private Label mysqlText=new Label();
	private Label keyValueText=new Label();
	private boolean mysqlActive=true;
	private boolean KVActive=true;
	
	public void start(Stage stage) {
		
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE); //LEVEL.SEVERE altrimenti,o ON
		
		
		deposito.getLock();
		// inizio sezione protetta da variabile atomica

			table.updateProjects(deposito.getProjectsWithoutStake());
			
			selectTableRow();

			selectTableMessages();
			
			inizializeChoiceBox();
			
			deposito.readAziendaFromMySql();

		// fine sezione protetta da variabile atomica
		deposito.freeLock();
		
		
		login.setOnAction((ActionEvent ev1)->
		{
			String urlLogo = "";
			
			if(!tf_companyName.getText().equals("") || !tf_password.getText().equals("")) 
			{
				
				agencyName = tf_companyName.getText();
				String password=tf_password.getText();
				Vector<String> result = deposito.getAgency(agencyName,password); 
				if(!result.isEmpty()) 
				{
				//Se il nome dell'azienda � presente nel db e la password � corretta
					deposito.getLock();
				// inizio sezione protetta da variabile atomica

					//table.updateProjects(deposito.getProjects(agencyName));

					if(gm!=null) {
						gm.endAggiornamento();
					}
						gm.changeAgency(agencyName);
						gm.setStatusKV(true);
						gm.setStatusMySql(true);
						gm.startAggiornamento();
						logged = true;
						insert.setDisable(false);
						//delete.setDisable(false);
						description.setEditable(true);
						name_project.setEditable(true);
						total_budget.setEditable(true);
						stake.setEditable(true);
						register.setVisible(false);
						//update.setDisable(false);

						name_agency.setText(result.get(0)); 
						address_agency.setText(result.get(3) + ", " + result.get(4) );
						site_agency.setText(result.get(2));
						urlLogo = result.get(1);
						try 
						{
							image = new Image(urlLogo);
							iv1.setImage(image);
						}
						catch(IllegalArgumentException iae) 
						{
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setHeaderText("Siamo spiacienti,probabilmente l'url del tuo logo � sbagliato,l'immagine non pu� essere visualizzata!");
							alert.showAndWait();
						}
						catch(Exception e) 
						{
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setHeaderText("Siamo spiacienti,l'immagine non pu� essere visualizzata!");
							alert.showAndWait();
						}
						send.setDisable(false);
						description_message.setEditable(true);
						stake_message.setEditable(true);
						project_message.setEditable(true);
						deposito.freeLock();	
				} //Se il nome dell'azienda non � presente nel db
				else 
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Il nome dell'azienda � errato oppure la password � scorretta!");
					alert.showAndWait();
					return;
				}
			}else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Il nome o la password mancano!");
					alert.showAndWait();
					return;
			}
			
        });
		
		
		refuse.setOnAction((ActionEvent ev1)->{
			
			if( deposito.getAggiornamentoFatto()==true)
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Siamo spiacienti,abbiamo appena aggiornato la tabella perci� � necessario riselezionare la riga che ti interessa");
				alert.showAndWait();	
				return;
			}
			
			deposito.getLock();
			// inizio sezione protetta da variabile atomica
				
				deposito.deleteMessage(selectedMessagetId);
				table_message.updateMessages(deposito.getMessages(agencyName));
				refuse.setDisable(true);
				accept.setDisable(true);
				description_message.clear();
				
			// fine sezione protetta da variabile atomica
			deposito.freeLock();

		});

		
		
		accept.setOnAction((ActionEvent ev1)->{
			
			if( deposito.getAggiornamentoFatto()==true)
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Siamo spiacienti,abbiamo appena aggiornato la tabella perci� � necessario riselezionare la riga che ti interessa");
				alert.showAndWait();	
				return;
			}
			
			deposito.getLock();
			// inizio sezione protetta da variabile atomica


				deposito.updateStake(selectedMessageStake, agencyName, selectedProjectMessageId, true);
				deposito.deleteMessage(selectedMessagetId);//fatta
				accept.setDisable(true);
				refuse.setDisable(true);
				description_message.clear();
				table.updateProjects(deposito.getProjects(agencyName));
				table_message.updateMessages(deposito.getMessages(agencyName));
			
			// fine sezione protetta da variabile atomica
			deposito.freeLock();
		});
		
		
		Interface interfaccia = new Interface(login, tf_companyName, table_title, table, 
				description, name_project, total_budget, insert, delete, iv1, stake, update,
				name_agency, address_agency, site_agency,tf_password,l_agencyName,l_password,
				table_message, l_stake, l_description, l_total_budget, l_project_name, messages_received, accept, refuse,
				l_description_message, description_message, stake_message, project_message,
				l_stake_message, l_project_message, send, choice_agency, message_receiver, register,mysql,keyValue,mysqlText,keyValueText);
		
		
		insert.setOnAction((ActionEvent ev2)->{
			String budget = total_budget.getText();
			String desc = description.getText(); 
			String name = name_project.getText();
			
			if(!desc.equals("") && !name.equals("") && !budget.equals("")) {
				
				if(!budget.matches("[0-9]+")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Il budget deve essere numerico!");
					alert.showAndWait();
					return;
				}
				
				
				Vector<String> vector = new Vector<String>(6);
	
				vector.add(name);
				vector.add(budget);
				vector.add(desc);
				vector.add(agencyName);
				vector.add(Integer.toString(0));	//progress
				vector.add(Integer.toString(0));	//stake
				
				deposito.getLock();
					// inizio sezione protetta da variabile atomica
					deposito.insertProject(vector);
					table.updateProjects(deposito.getProjects(agencyName));
					// fine sezione protetta da variabile atomica
				deposito.freeLock();
				description.clear();
				name_project.clear();
				total_budget.clear();
				
				vector.clear();
				
				table.updateProjects(deposito.getProjects(agencyName));  
				}else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Uno dei campi non � stato inserito!");
				alert.showAndWait();
				return;
			}
			
        });
		
		
		delete.setOnAction((ActionEvent ev1)->{
			
			if( deposito.getAggiornamentoFatto()==true)
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Siamo spiacienti,abbiamo appena aggiornato la tabella perci� � necessario riselezionare la riga che ti interessa");
				alert.showAndWait();	
				return;
			}
			deposito.getLock();
			Boolean iAmOwner = deposito.iAmOwner(selectedProjectId, agencyName);
			
			//Se sono il proprietario
			if(iAmOwner)
			{
				
				if(selectedStake == 0) {
				
					/*List<String>agencies=deposito.getListAgency(); 
					
					for(String a : agencies) //delete di tutti finanziamenti fatti da tutte le aziende sul progetto eliminato
						deposito.deleteMyStake(selectedProjectId, a); */
					
					deposito.deleteProject(selectedProjectId);
				}else {
					deposito.deleteMyStake(selectedProjectId, agencyName); 
				}
				
				table.updateProjects(deposito.getProjects(agencyName));
				delete.setDisable(true);
				update.setDisable(true);
				description.clear();
			}//Se non sono il proprietario ma voglio levare il mio stake
			else if(iAmOwner==false &&
				deposito.isMyStake(agencyName, selectedProjectId) == true)  { 
				deposito.deleteMyStake(selectedProjectId, agencyName);
				table.updateProjects(deposito.getProjects(agencyName));
				delete.setDisable(true);
				update.setDisable(true);
				description.clear();
			}//Se cerco di eliminare il progetto o lo stake di un altro
			else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Puoi eliminare solo i tuoi progetti o finanziamenti!");
				alert.showAndWait();
				return;
			}
			deposito.freeLock();
			
		});
			
			
			
			
			register.setOnAction((ActionEvent ev2)->{
                           new Interface(form);
                            form.setVisible(true);
			}); 
			
			
			
			
		update.setOnAction((ActionEvent ev1)->{
				String string_stakeInsered = stake.getText();
				
				
				if( deposito.getAggiornamentoFatto()==true)
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Siamo spiacienti,abbiamo appena aggiornato la tabella perci� � necessario riselezionare la riga che ti interessa");
					alert.showAndWait();	
					return;
				}
				
				if(string_stakeInsered.equals("")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Manca il valore del campo stake!");
					alert.showAndWait();	
					return;
				}
				
				//Controllo se il valore inserito � un numero
				if(!string_stakeInsered.matches("[0-9]+")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Puoi inserire solo valori numerici nel campo stake!");
					alert.showAndWait();
					return;
				}
				deposito.getLock();
				int stakeInsered=Integer.parseInt(string_stakeInsered);
				int totalStakes=deposito.getSommaStakes(selectedProjectId); 
				//se ho gi� raggiunto l'obiettivo
				if(totalStakes>=selectedTotalBudget) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Ti ringraziamo per la tua generosit�, ma abbiamo gi� raggiunto l'obbiettivo prefissato!");
					alert.showAndWait();
				} //se non ho raggiunto l'obiettivo e voglio aggiungere soldi
				else  
				{
					int newStake=0;
					//se voglio mettere pi� soldi di quelli necessari,metto solo quelli che mi servono per raggiungere il budget prefisso
					if((totalStakes-selectedStake+stakeInsered)>selectedTotalBudget)
						newStake=(selectedStake+(selectedTotalBudget-totalStakes)); //quanto ho messo pi� quanto manca per il max
					else //altrimenti il nuovo stake sar� semplicemente quello inserito
						newStake=stakeInsered;
					deposito.updateStake(newStake,agencyName,selectedProjectId, false); 
					table.updateProjects(deposito.getProjects(agencyName));
					stake.setText("");
					update.setDisable(true);
					delete.setDisable(true);
					description.clear();
				}
				deposito.freeLock();
				
			});
			
			
			
		send.setOnAction((ActionEvent ev1)->{
			String description = description_message.getText();
			String stake = stake_message.getText();
			String project = project_message.getText();
			Object object_receiver = choice_agency.getValue();
			

			if(!description.equals("") && !stake.equals("") && !project.equals("") && !(object_receiver == null)) 
			{
			
				if(!stake.matches("[0-9]+") || !project.matches("[0-9]+")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Campo Stake e id progetto devono essere numerici!");
					alert.showAndWait();
					return;
				}
				deposito.getLock();
				String receiver = object_receiver.toString();
				
				if(deposito.getProject(Integer.parseInt(project)).isEmpty()) 
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Il progetto selezionato non esiste!");
					alert.showAndWait();
					return ;
				} else {
					Vector<String> vector = new Vector<String>();
					
					vector.add(agencyName);	//mittente
					vector.add(receiver);	//destinatario
					vector.add(project);	//progetto
					vector.add(description);	//testo
					vector.add(stake);	//stake
					deposito.insertMessage(vector); 
					table_message.updateMessages(deposito.getMessages(agencyName));
					description_message.clear();
					stake_message.clear();
					project_message.clear();
					choice_agency.setValue(null);
					
				}
				deposito.freeLock();
				
			} 
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Non hai compilato correttamente tutti i campi!");
				alert.showAndWait();
				return;
			}
		});
		
		mysql.setOnAction((ActionEvent ev1)->{
			if(mysqlActive==true) //mysql=green
			{
				if(KVActive==false)//kv=red
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Non puoi disattivare la connessione di keyValue finch� non attivi quella di KeyValue!");
					alert.showAndWait();
					return;
				}
				else
				{//kv=green
					mysqlActive=false;
					mysql.setStyle("-fx-background-color: red");
					gm.setStatusMySql(false);
					deposito=dbKV;
					gm.changeDB(deposito);
					form.changeStatusSql(false);
				}
				
				//switch connessione
			}
			else
			{//mysql=red->green
				mysqlActive=true;
				mysql.setStyle("-fx-background-color: green");
				gm.setStatusMySql(true);
				form.changeStatusSql(true);
			}
		});
		
		keyValue.setOnAction((ActionEvent ev1)->{
			if(KVActive==true) //kv=green
			{//sto passando da keyvalue verde a rosso
				if(mysqlActive==false) //sql=red
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Non puoi disattivare la connessione di keyValue finch� non attivi quella di mySQL!");
					alert.showAndWait();
					return;
				}
				else
				{	//sql=green
					KVActive=false;
					keyValue.setStyle("-fx-background-color: red");
					gm.setStatusKV(false);
					deposito=dbMySql;
					gm.changeDB(deposito);
				}
			}
			else 
			{	//kv=red
				KVActive=true;
				keyValue.setStyle("-fx-background-color: green");
				deposito=dbKV;
				deposito.readProgettoFromMySql(agencyName);
				deposito.readMessaggioFromMySql(agencyName);
				deposito.readAziendaFromMySql();
				gm.setStatusKV(true);
				gm.changeDB(deposito);
			}
			
		});
		
		stage.setOnCloseRequest((WindowEvent we)->{
			if(gm != null) {
				gm.endAggiornamento();
			dbMySql.close();
			dbKV.close();
			}
		});
		
		Group root = new Group(tf_companyName,tf_password, login, table_title, table, description,
				name_project, total_budget, insert, delete, iv1, stake, update, 
				name_agency, address_agency, site_agency,l_agencyName,l_password, table_message, l_stake, l_description, l_total_budget,
				l_project_name, messages_received, accept, refuse, l_description_message, description_message, stake_message, project_message,
				l_stake_message, l_project_message, send, choice_agency, message_receiver, register,mysql,keyValue,mysqlText,keyValueText);
		
		
	
		Scene scene = new Scene(root, 1300,650);
        stage.setTitle("My Fundracing Project");
        stage.setScene(scene);
        stage.show();
        
        
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	private void selectTableRow(){
        table.setRowFactory(new Callback<TableView<RowTableProjects>, TableRow<RowTableProjects>>() {  
        public TableRow<RowTableProjects> call(TableView<RowTableProjects> tableView2) {  
            final TableRow<RowTableProjects> row = new TableRow<>();  
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                
            public void handle(MouseEvent event) 
            {  
            	deposito.getLock();
                final int index = row.getIndex(); 
                RowTableProjects res = table.getItems().get(index);
                selectedProjectId = res.getId_project();
                selectedTotalBudget=res.getBudget();
                selectedStake=res.getStake();
                description.setText(deposito.getDescriptionProject(selectedProjectId));
                stake.setText(Integer.toString(res.getStake()));
                update.setDisable(false);
                delete.setDisable(false);
                deposito.setAggiornamentoFatto(false);
                deposito.freeLock();
            }  
         });  
            return row;  
        }  
        }); 
	}
	
	
	private void selectTableMessages(){
		
		table_message.setRowFactory(new Callback<TableView<RowTableMessage>, TableRow<RowTableMessage>>() {  
        public TableRow<RowTableMessage> call(TableView<RowTableMessage> tableView2) {  
            final TableRow<RowTableMessage> row = new TableRow<>();  
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                
            public void handle(MouseEvent event) 
            { 
            	deposito.getLock();
                final int index = row.getIndex(); 
                RowTableMessage res = table_message.getItems().get(index);
                selectedMessagetId = res.getId();
                selectedMessageStake=res.getStake();
                selectedProjectMessageId = res.getId_project();
                description_message.setText(deposito.getDescriptionMessage(selectedMessagetId));
                accept.setDisable(false);
                refuse.setDisable(false);
                deposito.setAggiornamentoFatto(false);
                deposito.freeLock();
            }  
         });  
            return row;  
        }  
        }); 
	}
	
	public void inizializeChoiceBox()
	{
		List<String> agencyList = deposito.getListAgency();
		
		choice_agency.setItems(FXCollections.observableArrayList(agencyList));
		
	}	
	
	
	
}









