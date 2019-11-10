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
            List<Vector<String>> projects= d.getProjectWrites();

            //controllare se connessione possibile
            if(false) //da settare
                return;
            List<Vector<String>> finances= d.getFinanziamentoWrites();
            List<Vector<String>> messaggi= d.getMessageWrites();
            
            for(Vector<String> project: projects){
                String id_old=project.get(0);
                d.insertProject(project);
                String id_new= Integer.toString(d.getId("ProgettoEntity"));
                if(id_new.equals("-1")){
                    System.out.println("Errore nel recupero dell'ultima entità inserita");
                    return;
                }
                for (Vector<String> mess: messaggi)
                    if(mess.get(4)==id_old)
                        mess.set(4, id_new);
                for (Vector<String> fin: finances)
                    if(fin.get(3)==id_old)
                        fin.set(3, id_new);
  
            }
            
            
        }    
        }


