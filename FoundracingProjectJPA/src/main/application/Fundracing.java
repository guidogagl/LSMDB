package application;


import java.util.*;
import javafx.collections.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

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


public class Fundracing extends Application{
	
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
	private DepositoDati deposito = new DepositoDati();
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
	private Gestore gm = null;
        private Button register=new Button("Register");
	private RegistrationForm form=new RegistrationForm();
	
	public void start(Stage stage) {
		
		table.updateProjects(deposito.getProjectsWithoutStake());
		selectTableRow();
		
		selectTableMessages();
		
		inizializeChoiceBox();
		
login.setOnAction((ActionEvent ev1)->{
			
			String urlLogo = "";
			
			if(!tf_companyName.getText().equals("") || !tf_password.getText().equals("")) {
				
				agencyName = tf_companyName.getText();
				String password=tf_password.getText();
				Vector<String> result = deposito.getAgency(agencyName,password);
				
				//Se il nome dell'azienda ï¿½ presente nel db e la password ï¿½ corretta
				if(!result.isEmpty()) {
					
					table.updateProjects(deposito.getProjects(agencyName));
					
					if(gm!=null) {
						gm.endAggiornamentoTabella();
					}
					
					gm = new Gestore(deposito,table, table_message,choice_agency, agencyName);
					gm.startAggiornamentoTabella();
					logged = true;
					insert.setDisable(false);
					//delete.setDisable(false);
					description.setEditable(true);
					name_project.setEditable(true);
					total_budget.setEditable(true);
					stake.setEditable(true);
					//update.setDisable(false);
					name_agency.setText(result.get(0)); 
					address_agency.setText(result.get(3));
					site_agency.setText(result.get(4));
					urlLogo = result.get(1);
					image = new Image(urlLogo);
					iv1.setImage(image);
					//accept.setDisable(false);
					//refuse.setDisable(false);
					send.setDisable(false);
					description_message.setEditable(true);
					stake_message.setEditable(true);
					project_message.setEditable(true);
					
				} //Se il nome dell'azienda non è presente nel db
				else {
					JOptionPane.showMessageDialog(null, "Il nome dell'azienda è errato oppure la password è scorretta!");
					return;
				}
			}else {
					JOptionPane.showMessageDialog(null, "Il nome o la password mancano!");
					return;
			}
			
        });
		
		
		refuse.setOnAction((ActionEvent ev1)->{
			
			if(description_message.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Non hai selezionato nessun messaggio!");
				return;
			}
			deposito.deleteMessage(selectedMessagetId);
			table_message.updateMessages(deposito.getMessages(agencyName));
			refuse.setDisable(true);
			accept.setDisable(true);
			description_message.clear();
		});

		
		
		accept.setOnAction((ActionEvent ev1)->{
			
			if(description_message.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Non hai selezionato nessun messaggio!");
				return;
			}
			
			deposito.updateStake(selectedMessageStake, agencyName, selectedProjectMessageId, true);
			deposito.deleteMessage(selectedMessagetId);
			accept.setDisable(true);
			refuse.setDisable(true);
			description_message.clear();
			table.updateProjects(deposito.getProjects(agencyName));
			table_message.updateMessages(deposito.getMessages(agencyName));
		});
		
		
		Interface interfaccia = new Interface(login, tf_companyName, table_title, table, 
				description, name_project, total_budget, insert, delete, iv1, stake, update,
				name_agency, address_agency, site_agency,tf_password,l_agencyName,l_password,
				table_message, l_stake, l_description, l_total_budget, l_project_name, messages_received, accept, refuse,
				l_description_message, description_message, stake_message, project_message,
				l_stake_message, l_project_message, send, choice_agency, message_receiver, register);
		
		
		insert.setOnAction((ActionEvent ev2)->{
			
			String budget = total_budget.getText();
			String desc = description.getText(); 
			String name = name_project.getText();
			
			
			
			if(!desc.equals("") || !name.equals("") || !budget.equals("")) {
				
				if(!budget.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Il budget deve essere numerico!");
					return;
				}
				
				
				Vector<String> vector = new Vector<String>(4);
	
				vector.add(name);
				vector.add(budget);
				vector.add(desc);
				vector.add(agencyName);
				
				deposito.insertProject(vector);
				
				description.clear();
				name_project.clear();
				total_budget.clear();
				
				vector.clear();
				
				table.updateProjects(deposito.getProjects(agencyName));
			}else {
				JOptionPane.showMessageDialog(null, "Uno dei campi non è stato inserito!");
				return;
			}
        });
		
		
		delete.setOnAction((ActionEvent ev1)->{
			
			if(description.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Non hai selezionato nessun progetto!");
				return;
			}
			
			
			Boolean iAmOwner = deposito.iAmOwner(selectedProjectId, agencyName);
			
			//Se sono il proprietario
			if(iAmOwner) {
				deposito.deleteProject(selectedProjectId);
				table.updateProjects(deposito.getProjects(agencyName));
				delete.setDisable(true);
				update.setDisable(true);
				description.clear();
			}//Se non sono il proprietario ma voglio levare il mio stake
			else if(iAmOwner==false &&
				deposito.isMyStake(agencyName, selectedProjectId) == true) {
				deposito.deleteMyStake(selectedProjectId, agencyName);
				table.updateProjects(deposito.getProjects(agencyName));
				delete.setDisable(true);
				update.setDisable(true);
				description.clear();
			}//Se cerco di eliminare il progetto o lo stake di un altro
			else {
				JOptionPane.showMessageDialog(null, "Puoi eliminare solo i tuoi progetti o finanziamenti!");
				return;
			}
		});
			
			
			
			
			register.setOnAction((ActionEvent ev2)->{
                            form.interfaccia=new Interface(form);
                            form.setVisible(true);
                            
			}); 
			
			
			
			
		update.setOnAction((ActionEvent ev1)->{
				
				String string_stakeInsered = stake.getText();
				
				if(string_stakeInsered.equals("")) {
					JOptionPane.showMessageDialog(null, "Manca il valore del campo Stake!");	
					return;
				}
				
				//Controllo se il valore inserito è un numero
				if(!string_stakeInsered.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Puoi inserire solo valori numerici nel campo Stake!");	
					return;
				}
				
				int stakeInsered=Integer.parseInt(string_stakeInsered);
				int totalStakes=deposito.getSommaStakes(selectedProjectId);
				//se ho giï¿½ raggiunto l'obiettivo
				if(totalStakes>=selectedTotalBudget) {
					JOptionPane.showMessageDialog(null, "Ti ringraziamo per la tua generositï¿½, ma abbiamo giï¿½ raggiunto l'obiettivo prefissato!");	
				} //se non ho raggiunto l'obiettivo e voglio aggiungere soldi
				else  {
					int newStake=0;
					//se voglio mettere piï¿½ soldi di quelli necessari,metto solo quelli che mi servono per raggiungere il budget prefisso
					if((totalStakes-selectedStake+stakeInsered)>selectedTotalBudget)
						newStake=(selectedStake+(selectedTotalBudget-totalStakes)); //quanto ho messo piï¿½ quanto manca per il max
					else //altrimenti il nuovo stake sarï¿½ semplicemente quello inserito
						newStake=stakeInsered;
					deposito.updateStake(newStake,agencyName,selectedProjectId, false);
					table.updateProjects(deposito.getProjects(agencyName));
					stake.setText("");
					update.setDisable(true);
					delete.setDisable(true);
					description.clear();
				} 
			});
			
			
			
		send.setOnAction((ActionEvent ev1)->{
			String description = description_message.getText();
			String stake = stake_message.getText();
			String project = project_message.getText();
			Object object_receiver = choice_agency.getValue();

			if(!description.equals("") || !stake.equals("") || !project.equals("") || !(object_receiver == null)) {
			
				if(!stake.matches("[0-9]+") || !project.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Campo Stake e id progetto devono essere numerici!");
					return;
				}
				
				String receiver = object_receiver.toString();
				
				if(deposito.getProject(Integer.parseInt(project)).isEmpty()) {
					JOptionPane.showMessageDialog(null, "Il progetto selezionato non esiste");
					return ;
				} else {
					Vector<String> vector = new Vector<String>();
					
					vector.add(agencyName);	//mittente
					vector.add(receiver);	//destinatario
					vector.add(project);	//progetto
					vector.add(description);	//testo
					vector.add(stake);	//stake
					deposito.insertMessage(vector);
					
					description_message.clear();
					stake_message.clear();
					project_message.clear();
					choice_agency.setValue(null);
					
				}
			} else {
				JOptionPane.showMessageDialog(null, "Non hai compilato correttamente tutti i campi!");
				return;
			}
		});
		
		stage.setOnCloseRequest((WindowEvent we)->{
			if(gm != null) {
				gm.endAggiornamentoTabella();
			}
		});
		
		Group root = new Group(tf_companyName,tf_password, login, table_title, table, description,
				name_project, total_budget, insert, delete, iv1, stake, update, 
				name_agency, address_agency, site_agency,l_agencyName,l_password, table_message, l_stake, l_description, l_total_budget,
				l_project_name, messages_received, accept, refuse, l_description_message, description_message, stake_message, project_message,
				l_stake_message, l_project_message, send, choice_agency, message_receiver, register);
		
		
	
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
                
            public void handle(MouseEvent event) {  
                final int index = row.getIndex(); 
                RowTableProjects res = table.getItems().get(index);
                selectedProjectId = res.getId_project();
                selectedTotalBudget=res.getBudget();
                selectedStake=res.getStake();
                description.setText(deposito.getDescriptionProject(selectedProjectId));
                stake.setText(Integer.toString(res.getStake()));
                update.setDisable(false);
                delete.setDisable(false);
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
                
            public void handle(MouseEvent event) {  
                final int index = row.getIndex(); 
                RowTableMessage res = table_message.getItems().get(index);
                selectedMessagetId = res.getId();
                System.out.println("Message id: " + selectedMessagetId);
                //selectedTotalBudget=res.getBudget();
                selectedMessageStake=res.getStake();
                selectedProjectMessageId = res.getId_project();
                description_message.setText(deposito.getDescriptionMessage(selectedMessagetId));
                //stake.setText(Integer.toString(res.getStake()));
                accept.setDisable(false);
                refuse.setDisable(false);
            }  
         });  
            return row;  
        }  
        }); 
	}
	
	public void inizializeChoiceBox() {
		List<String> agencyList = deposito.getListAgency();
		
		choice_agency.setItems(FXCollections.observableArrayList(agencyList));
		
		for(int i = 0; i < agencyList.size(); i++) {
			System.out.println("agency " + i + " : " + agencyList.get(i));
		}
	}	
	
	
	
}









