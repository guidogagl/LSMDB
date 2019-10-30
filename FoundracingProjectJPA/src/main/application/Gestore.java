package application;

import jpaConnect.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

public class Gestore {
	private DepositoDati d;
	private TableMessage tm;
	private TableProjects tp;
	private ChoiceBox cb;
	private String agencyName;
	private Timer timer = null;
	private TimerTask task = null;
	public Gestore( DepositoDati d,TableProjects tp,TableMessage tm,ChoiceBox cb,String agencyName)
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
			timer.schedule(task, 0, 5000);
			
							
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
}

