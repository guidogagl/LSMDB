package application;

import jpaConnect.DepositoDati;

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
		//while(true) {
		
			timer = new Timer();
			task = new TimerTask() {
				public void run() {
					
					aggiornaTabellaMessaggi();
					
					/*ThreadGestioneMessaggi tgm=new ThreadGestioneMessaggi(d,tm,agencyName);
					tgm.start();
					try {
						tgm.join();
					}catch(Exception e) {
						e.printStackTrace();
					}*/
				}
			};
			timer.schedule(task, 0, 5000);
			
			/*try {
				tgm.join(2000);
			}catch(Exception e) {e.printStackTrace();}*/
		//}
							
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
