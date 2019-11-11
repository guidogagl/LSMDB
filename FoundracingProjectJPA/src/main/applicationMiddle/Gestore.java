package applicationMiddle;

import jpaConnect.*;
import lvDbConnect.DepositoDatiLevelDb;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import applicationFront.TableMessage;
import applicationFront.TableProjects;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;


public class Gestore {
	private DepositoDatiLevelDb d;
	private TableMessage tm;
	private TableProjects tp;
	private ChoiceBox cb;
	private String agencyName;
	private Timer timer = null;
	private TimerTask task = null;
	public Boolean Mysql_active;
	public Gestore( DepositoDatiLevelDb d,TableProjects tp,TableMessage tm,ChoiceBox cb,String agencyName)
	{
		this.d=d;
		this.tp=tp;
		this.tm=tm;
		this.cb=cb;
		this.agencyName=agencyName;
		this.Mysql_active = true;
		
	}
	
	public void SetStatus(Boolean value) {
		Mysql_active = value;
	}
	
	public void startAggiornamento() {
			timer = new Timer();
			task = new TimerTask() {
				public void run() {
					
					aggiorna();
					
				}
			};
			timer.schedule(task, 0, 15000);
			
							
	}
	
	private void aggiorna()
	{
		if(this.Mysql_active) 
		{
			d.setRoutinesInExecution(true);
			//routine da cache a database
			fromCacheToDB();
			//Ripulisco la cache
			d.clearCache("read");
			//Leggo dal database e aggiorno cache
			d.readProgettoFromMySql(agencyName);
			d.readMessaggioFromMySql(agencyName);
			d.readAziendaFromMySql();
			d.setRoutinesInExecution(false);
		}
		
		//Leggo in ogni caso dalla cache e aggiorno
			d.setAggiornamentoFatto(true);
		tp.updateProjects(d.getProjects(agencyName));
		List<RowTableMessage> messaggiDaAggiungere = d.getMessages(agencyName);
		tm.updateMessages(messaggiDaAggiungere);
		if(cb.getValue()==null||cb.getValue().toString().equals("")) //posso aggiornare solo se non è stato selezionato nulla nel choicebox
			cb.setItems(FXCollections.observableArrayList(d.getListAgency()));
	}
	
	
	private  void fromCacheToDB() {
		//Prendot tutti i progetti, finanziamenti e messaggi da copiare nel database
		List<Vector<String>> projects = d.getProjectsWrites();
		//List<Vector<String>> finanziamenti = d.getFinanziamentiWrites();
		
		
		//Copio i progetti e i finanziamenti nel database
		d.copyProjectAndFundsInDB(projects,agencyName);
		
		List<Vector<String>> messages = d.getMessagesWrites();		
		
		//Copio i messaggi nel database e aggiorno l'id nella cache
		d.copyMessagesInDB(messages);
		
		d.deleteEntitiesInMysql(agencyName);
		
		d.clearCache("insert");
		d.clearCache("update");
		d.clearCache("delete");
	}
	
	public void endAggiornamento() {
		timer.cancel();
		timer.purge();
	}
}

