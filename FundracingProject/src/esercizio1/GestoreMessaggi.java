package esercizio1;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GestoreMessaggi {
	private DepositoDati d;
	private TableMessage tm;
	private String agencyName;
	private Timer timer = null;
	private TimerTask task = null;
	public GestoreMessaggi( DepositoDati d,TableMessage tm,String agencyName)
	{
		this.d=d;
		this.tm=tm;
		this.agencyName=agencyName;
		
	}
	public void startAggiornamentoTabella() {
			timer = new Timer();
			task = new TimerTask() {
				public void run() {
					aggiornaTabellaMessaggi();
				}
			};
			timer.schedule(task, 0, 5000);
			
							
	}
	
	private void aggiornaTabellaMessaggi() {
		List<RowTableMessage> messaggiDaAggiungere = d.getMessages(agencyName);
		tm.updateMessages(messaggiDaAggiungere);
	}
	
	
	public void endAggiornamentoTabella() {
		timer.cancel();
		timer.purge();
	}
}
