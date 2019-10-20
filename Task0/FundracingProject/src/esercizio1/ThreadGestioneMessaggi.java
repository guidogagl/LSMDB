package esercizio1;

import java.util.ArrayList;
import java.util.List;

public class ThreadGestioneMessaggi extends Thread{
	private DepositoDati d;
	private TableMessage tm;
	private String agencyName;
	private static List<RowTableMessage>listaMessaggi;
	static {
		
		listaMessaggi=new ArrayList<RowTableMessage>();
	}
	ThreadGestioneMessaggi(DepositoDati d,TableMessage tm,String agencyName)
	{
		this.d=d;
		this.tm=tm;
		this.agencyName=agencyName;
	}
	public void run() {
		synchronized(listaMessaggi) 
		{ //prendo il lock su listaMessaggi; ci aggiungo quelli che mi riguardano
			List<RowTableMessage>messaggiDaAggiungere=d.getMessages(agencyName);
			for(int i=0;i<messaggiDaAggiungere.size();i++)
				listaMessaggi.add(messaggiDaAggiungere.get(i));
			System.out.println("Thread taking");
			listaMessaggi.notifyAll();
		}
		try {
			this.sleep(1000);
		}catch(Exception e) {System.out.println(e.getMessage());}
		synchronized(listaMessaggi)
		{	//prendo il lock su listaMessaggi; se il messaggio che trovo scorrendo è mio,lo levo dalla lista comune e lo uso per aggiornare
			//la mia tabella
			List<RowTableMessage>listaPersonale=new ArrayList<RowTableMessage>();
			for(int i=0;i<listaMessaggi.size();i++)
			{
					if(listaMessaggi.get(i).getDestinatario().equals(agencyName))
						listaPersonale.add(listaMessaggi.get(i));
			}
			for(int i=0;i<listaMessaggi.size();i++)
			{
					if(listaMessaggi.get(i).getDestinatario().equals(agencyName))
						listaMessaggi.remove(i);
			}
			tm.updateMessages(listaPersonale);
			System.out.println("Thread giving");
			listaMessaggi.notifyAll();
		}
	}
}
