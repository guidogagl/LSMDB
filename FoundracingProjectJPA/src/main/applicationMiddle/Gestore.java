package applicationMiddle;

import jpaConnect.*;
import lvDbConnect.DepositoDatiLevelDb;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import applicationFront.TableMessage;
import applicationFront.TableProjects;
import java.util.Vector;
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
	public Gestore( DepositoDatiLevelDb d,TableProjects tp,TableMessage tm,ChoiceBox cb,String agencyName)
	{
		this.d=d;
		this.tp=tp;
		this.tm=tm;
		this.cb=cb;
		this.agencyName=agencyName;
		
		
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
	
	private void aggiorna() {
		
		tp.updateProjects(d.getProjects(agencyName));
		List<RowTableMessage> messaggiDaAggiungere = d.getMessages(agencyName);
		tm.updateMessages(messaggiDaAggiungere);
		if(cb.getValue()==null||cb.getValue().toString().equals("")) //posso aggiornare solo se non è stato selezionato nulla nel choicebox
			cb.setItems(FXCollections.observableArrayList(d.getListAgency()));
	}
	
	
	public void endAggiornamento() {
		timer.cancel();
		timer.purge();
	}
        
        public void routine(){
            if(false) // controlla se la connessione è possibile
                return;
            List<Vector<String>> projects= d.getProjectWrites();
            List<Vector<String>> messaggi= d.getMessageWrites();
            for(Vector<String> project: projects){
                d.insertProject(project);
            }
            
            
        }
}

