package lvDbConnect;

import applicationMiddle.RowTableMessage;
import applicationMiddle.RowTableProjects;
import applicationMiddle.wrapperDbs;
import jpaConnect.DepositoDati;
import jpaConnect.FundracingManager;
import jpaEntities.MessaggioEntity;
import jpaEntities.ProgettoEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class DepositoDatiLevelDb extends DepositoDati implements wrapperDbs {
    private Connect conn = new Connect();
    private Vector<String> agencyAtt = new Vector<String>();
    private Vector<String> projectsAtt = new Vector<String>();
    private Vector<String> messageAtt = new Vector<String>();
    private Vector<String> finanziamentoAtt = new Vector<String>();
    private Vector<String> deleteAtt=new Vector<String>();
    private Boolean aggiornamentoFatto=new Boolean(false);
    private AtomicBoolean lock = new AtomicBoolean(false);
    private int counterNewProjects=1000000;
    private int counterNewMessages=1000000;
    
    
    private Vector<String> getEntityFromLevelDb(String EntityName, String entityID ){
        List<Vector<String>> agencyList = conn.readEntity("read", EntityName, entityID);

        // se l'ho trovato
        if (agencyList != null && !agencyList.isEmpty())
            return agencyList.get(0);

        return null;
    }

    
    public DepositoDatiLevelDb(){
    	
    	agencyAtt.add("nomeAzienda");
        agencyAtt.add( "urlLogo");
        agencyAtt.add( "urlSito");
        agencyAtt.add( "indirizzo");
        agencyAtt.add( "cap" );
        agencyAtt.add( "password" );

        projectsAtt.add("id");
        projectsAtt.add("descrizione");
        projectsAtt.add("nome");
        projectsAtt.add("budget");
        projectsAtt.add("azienda");
        projectsAtt.add("Progress");
        projectsAtt.add("stake");

        messageAtt.add("id");
        messageAtt.add("idProgetto");
        messageAtt.add("testo");
        messageAtt.add("aziendaDestinataria");
        messageAtt.add("aziendaMittente");
        messageAtt.add("data");
        messageAtt.add("stake");
        
        finanziamentoAtt.add("id_finanziamento");
        finanziamentoAtt.add("azienda");
        finanziamentoAtt.add("stake");
        
        deleteAtt.add("id");
        deleteAtt.add("stake");
        
    }
    
    public void close() {
    	conn.close();
    	super.close();
    }
    
    
    public Vector<String> getAgency(String agencyName) {
        Vector<String> agency = getEntityFromLevelDb( "AziendaEntity", agencyName );
        if( agency == null || agency.size() < 6 )
            return new Vector<String>();

        Vector<String>ret = new Vector<String>();
        ret.add(agency.get(0));
        ret.add(agency.get(4));
        ret.add(agency.get(5));
        ret.add(agency.get(2));
        ret.add(agency.get(1));
        
        return ret;
    }
    public Vector<String> getAgency(String agencyName,String password) {
    	Vector<String> agency = getEntityFromLevelDb( "AziendaEntity", agencyName );
    	if( agency == null || agency.size() < 6 || !agency.get(3).equals(password))
            return new Vector<String>();
        
    	Vector<String>ret = new Vector<String>();
        ret.add(agency.get(0));
        ret.add(agency.get(4));
        ret.add(agency.get(5));
        ret.add(agency.get(2));
        ret.add(agency.get(1));
         
        return ret;
    }
    
    public boolean getAggiornamentoFatto() {
    	return this.aggiornamentoFatto;
    }
    
    public void setAggiornamentoFatto(boolean aggiornamentoFatto) {
    	this.aggiornamentoFatto=aggiornamentoFatto;
    }
    

    public void getLock()
    {
    	
    	//qui prendo il lock sulla istanza di classe su cui il metodo viene chiamato
    	synchronized(lock) 
    	{
    		if(lock.get()==true)
    			try 
    		{
    			lock.wait();
    		}catch(Exception e) {e.printStackTrace();}
    		else
    			lock.set(true);
    	}
    }

    public void freeLock()
    {
    	//rilascio il lock sulla istanza di classe,risvegliando chi attende
    	synchronized(lock)
    	{
    		lock.set(false);
    		lock.notifyAll();
    	}
    }
    
    
    public void insertAgency(Vector<String> val)
    {
     
        Vector<String> att = new Vector<String>();

        super.insertAgency(val);	//Inserisco l'azienda nel database
        
        conn.writeEntity("read", "AziendaEntity", agencyAtt, val);
        //testare
    }
    
    public List<String> getListAgency(){
        List<Vector<String>> allAgency = conn.readAllEntity("read", "AziendaEntity");	//legge da readCache tutte le aziende
    	List<String> agenciesName = new ArrayList<String>();
    	
    	if(allAgency == null || allAgency.isEmpty())
    		return agenciesName;
    	
    	for(int i = 0; i < allAgency.size(); i++) 
    	{
    		Vector<String> vett = allAgency.get(i);
    		
    		if(vett == null || vett.isEmpty() || vett.size() < 6)
    			continue;
    		else
    			agenciesName.add(vett.get(0));
    	}
    	
    	return agenciesName;
    }
    
    public List<RowTableProjects> getProjects(String agencyName)
    {
    	List<RowTableProjects>ret=new ArrayList<RowTableProjects>();
    	List<Vector<String>> progetti=conn.readAllEntity("read","ProgettoEntity");
    	
    	if(progetti==null||progetti.isEmpty()||progetti.get(0).size()<6)
    		return ret;
    	
    	for(int i=0;i<progetti.size();i++)
    	{
    		
    		Vector<String>progetto=progetti.get(i);
    		if(progetto.size()==7)
    		{
    			RowTableProjects rtp=new RowTableProjects(Integer.parseInt(progetto.get(0)),
        				progetto.get(5),
        				progetto.get(1),
        				Integer.parseInt(progetto.get(3)),
        				Integer.parseInt(progetto.get(6)),
        				progetto.get(2));
        		ret.add(rtp);
    		}
    	}
    	return ret;
    	
    }  
    
    
    public List<RowTableProjects> getProjectsWithoutStake() 
    {
    	List<RowTableProjects>ret=new ArrayList<RowTableProjects>();
    	List<Vector<String>> progetti=conn.readAllEntity("read","ProgettoEntity");
    	for(int i=0;i<progetti.size();i++)
    	{
    		
    		Vector<String>progetto=progetti.get(i);
    		if(progetto.size()==7)
    		{
    			RowTableProjects rtp=new RowTableProjects(Integer.parseInt(progetto.get(0)),
        				progetto.get(5),
        				progetto.get(1),
        				Integer.parseInt(progetto.get(3)),
        				Integer.parseInt(progetto.get(6)),
        				progetto.get(2));
    			
    				ret.add(rtp);
    		}
    	}
    	
    	return ret;
    }
    
    public List<Vector<String>> getProject(int idProgetto)
    {
        String descrizione=conn.readSingleValue("read","ProgettoEntity", Integer.toString(idProgetto), "descrizione");
        if(descrizione.equals(""))
        	return new ArrayList<Vector<String>>();
        else
        {
        	Vector<String> values=new Vector<String>();
        	values.add(descrizione);
            List<Vector<String>> ret = new ArrayList<Vector<String>>();
            ret.add(values);
            return ret;
        }
    }

    public String getDescriptionProject(int id_project) 
    {
    	String ret = conn.readSingleValue("read","ProgettoEntity", Integer.toString(id_project),"descrizione");
    	if(ret == null)
    		return new String();
    	else
    		return ret;
    }
    
    public boolean iAmOwner(int projectId,String agencyName) 
    {
    	
    	String owner = conn.readSingleValue("read","ProgettoEntity", Integer.toString(projectId), "azienda");
    	
    	if(owner == null || !owner.equals(agencyName))
    		return false;
    	else
    		return true;
    }
    

    
    public int getSommaStakes(int selectedProjectID){ //Deve prendere il progress e il total budget del progetto e da questi calcolare la somma degli stake
    	return Integer.parseInt(conn.readSingleValue("read","ProgettoEntity", Integer.toString(selectedProjectID), "stake"));
    }

    public List<RowTableMessage> getMessages(String agencyName){
    	List<RowTableMessage>ret=new ArrayList<RowTableMessage>();
    	List<Vector<String>> messaggi=conn.readAllEntity("read","MessaggioEntity");
    	if(messaggi==null||messaggi.isEmpty()||messaggi.get(0).size()<6)
    		return ret;
    	for(int i=0;i<messaggi.size();i++)
    	{
    		
    		Vector<String>messaggio=messaggi.get(i);
    		if(messaggio.get(1).equals(agencyName))
    		{
    			RowTableMessage rtp=new RowTableMessage(Integer.parseInt(messaggio.get(0)),
        				Integer.parseInt(messaggio.get(4)),
        				messaggio.get(3),
        				messaggio.get(2),
        				messaggio.get(1),
        				messaggio.get(6),
        				Integer.parseInt(messaggio.get(5)));
    			ret.add(rtp);
    		}
    		
    	}
    	return ret;
    	
        
    }
    public String getDescriptionMessage(int id_message) {
    	String ret = conn.readSingleValue("read","MessaggioEntity", Integer.toString(id_message), "testo");
    	if(ret == null)
    		return new String();
    	else
    		return ret;
    }
    
    public void deleteMessage(int messageId) {
    	conn.deleteSingleEntity("read","MessaggioEntity", Integer.toString(messageId));
    	Vector<String> val = new Vector<String>();
    	val.add(Integer.toString(messageId));
    	val.add("0");
    	if(messageId<1000000)
    		conn.writeEntity("delete","MessaggioEntity", deleteAtt, val); 
    	else
    		conn.deleteSingleEntity("insert","MessaggioEntity",Integer.toString(messageId));
    }
    
    //Inserimento del progetto in cache
    public void insertProject(Vector<String>vector) {
    	Vector<String> val = new Vector<String>();
    	int newLast=counterNewProjects;
    	val.add(0, Integer.toString(newLast)); //id
    	val.add(1, vector.get(2));//descrizione
    	val.add(2, vector.get(0));//nome
    	val.add(3,vector.get(1));//budget
    	val.add(4, vector.get(3));//azienda
    	val.add(5, vector.get(4)); //progress 
    	val.add(6, vector.get(5));//stake
    	conn.writeEntity("read","ProgettoEntity", projectsAtt, val);
    	conn.writeEntity("insert","ProgettoEntity", projectsAtt, val);
    	counterNewProjects++;
    	
    }
    
    public void insertMessage(Vector<String>vector) {
    	
    	Vector<String>val=new Vector<String>(); //riordino i valori
    	
    	int newLast=counterNewMessages;
    	val.add( Integer.toString(newLast)); //idMessaggio
        val.add(vector.get(2));//idProgetto
        val.add(vector.get(3));//testo
        val.add( vector.get(1));//destinatario
        val.add(vector.get(0));//mittente
        val.add( LocalDate.now().toString());//data
        val.add(vector.get(4)); //stake
        conn.writeEntity("read","MessaggioEntity", messageAtt, val);
        conn.writeEntity("insert","MessaggioEntity", messageAtt, val);
        counterNewMessages++;
    }
    
    public void updateStake(int selectedStake, String agencyName, int selectedProjectId, boolean add) 
    {
    	
    	String progressString=conn.readSingleValue("read","ProgettoEntity", Integer.toString(selectedProjectId), "Progress");
    	String totBudgetStakeString=conn.readSingleValue("read","ProgettoEntity", Integer.toString(selectedProjectId), "budget");
    	String sommaStakeString=conn.readSingleValue("read","ProgettoEntity", Integer.toString(selectedProjectId), "stake");
    	
    	if(progressString.equals("")||totBudgetStakeString.equals("")||sommaStakeString.equals(""))
    		return ;
    	
    	Double progress = Double.parseDouble(progressString);
    	Double tot_budget = Double.parseDouble(totBudgetStakeString);    	
    	Double somma_stake = (progress*tot_budget)/100;
    	
    	int myStake=Integer.parseInt(sommaStakeString);
    	int sumStakes=somma_stake.intValue();
    	
    	double newProgress=0.0;
    	
    	if(add) 
    	{
    		
    		int newMyStake=(myStake+selectedStake);
    		newProgress=(double)(((sumStakes-myStake+newMyStake)/tot_budget)*100);
    		if(newProgress<0)
    			newProgress=0.0;
    		conn.updateSingleValue("read","ProgettoEntity",Integer.toString(selectedProjectId), "stake", Integer.toString(newMyStake));
    		if(conn.readSingleValue("update", "FinanziamentoEntity", Integer.toString(selectedProjectId), "stake").equals(""))
    		{
    			Vector<String> valFin=new Vector<String>();
    	    	valFin.add(0,Integer.toString(selectedProjectId));
    	    	valFin.add(1,agencyName);
    	    	valFin.add(2,Integer.toString(0));
    	    	conn.writeEntity("update","FinanziamentoEntity", finanziamentoAtt, valFin);
    	   }
    		conn.updateSingleValue("update","FinanziamentoEntity", Integer.toString(selectedProjectId), "stake", Integer.toString(newMyStake));
    	}
    	else 
    	{
    		newProgress=(double)(((sumStakes-myStake+selectedStake)/tot_budget)*100);
    		if(newProgress<0)
    			newProgress=0.0;
    		conn.updateSingleValue("read","ProgettoEntity",Integer.toString(selectedProjectId), "stake", Integer.toString(selectedStake));
    		if(conn.readSingleValue("update","FinanziamentoEntity", Integer.toString(selectedProjectId), "stake").equals(""))
    		{
    			Vector<String> valFin=new Vector<String>();
    	    	valFin.add(0,Integer.toString(selectedProjectId));
    	    	valFin.add(1,agencyName);
    	    	valFin.add(2,Integer.toString(0));
    	    	conn.writeEntity("update","FinanziamentoEntity", finanziamentoAtt, valFin);
    		}
    		conn.updateSingleValue("update","FinanziamentoEntity", Integer.toString(selectedProjectId), "stake", Integer.toString(selectedStake));
    	}
    	conn.updateSingleValue("read","ProgettoEntity", Integer.toString(selectedProjectId), "Progress", Double.toString(newProgress));
    }
    
    public void deleteMyStake(int selectedProjectId, String agencyName) {
    	
    	Vector<String> projectList = getEntityFromLevelDb("ProgettoEntity", Integer.toString(selectedProjectId));
    	double newProgress = 0.0;
    	
    	if( projectList == null || projectList.isEmpty() || projectList.firstElement().isEmpty() )
            return;
    	
    	Double progress = Double.parseDouble(projectList.get(1));
    	Double tot_budget = Double.parseDouble(projectList.get(3));    	
    	Double somma_stake = (progress*tot_budget)/100;
    	
    	int myStake=Integer.parseInt(projectList.get(6));
    	
    	newProgress = ((somma_stake-myStake)/tot_budget)*100;
    	
    	if(newProgress<0)
    		newProgress=0.0;
    	
    	conn.updateSingleValue("read","ProgettoEntity",Integer.toString(selectedProjectId),"stake", Integer.toString(0));
    	conn.updateSingleValue("read","ProgettoEntity",Integer.toString(selectedProjectId),"Progress", Double.toString(newProgress));
    	if(conn.readSingleValue("update","FinanziamentoEntity", Integer.toString(selectedProjectId),"stake").equals(""))
    	{
    		Vector<String> valFin=new Vector<String>();
	    	valFin.add(0,Integer.toString(selectedProjectId));
	    	valFin.add(1,agencyName);
	    	valFin.add(2,Integer.toString(0));
	    	conn.writeEntity("update","FinanziamentoEntity", finanziamentoAtt, valFin);
    	}
    	else	
    		conn.updateSingleValue("update", "FinanziamentoEntity", Integer.toString(selectedProjectId),"stake",Integer.toString(0));
    }
    
    public Boolean isMyStake(String agencyName, int selectedProjectId) {
    	
    	Double stake = Double.parseDouble(conn.readSingleValue("read","ProgettoEntity", Integer.toString(selectedProjectId), "stake"));
    	
    	if(stake > 0)
    		return true;
    	else
    		return false;
    }
    
    public void deleteProject(int selectedProjectId) 
    {
    	conn.deleteSingleEntity("read","ProgettoEntity", Integer.toString(selectedProjectId));
    	conn.deleteSingleEntity("update", "FinanziamentoEntity", Integer.toString(selectedProjectId));
    	List<Vector<String>>messagesRead=conn.readAllEntity("read", "MessagggioEntity");
    	List<Vector<String>>messagesInserted=conn.readAllEntity("insert", "MessaggioEntity");
    	for(int i=0;i<messagesRead.size();i++)
    	{
    		Vector<String>message=messagesRead.get(i);
    		if(message.size()==7&&Integer.parseInt(message.get(4))==selectedProjectId)
    		{
    			this.deleteMessage(Integer.parseInt(message.get(0)));
    		}
    	}
    	for(int i=0;i<messagesInserted.size();i++)
    	{
    		Vector<String>message=messagesRead.get(i);
    		if(message.size()==7&&Integer.parseInt(message.get(4))==selectedProjectId)
    		{
    			conn.deleteSingleEntity("insert", "MessaggioEntity", message.get(0));
    		}
    	}
    	Vector<String> val = new Vector<String>();
    	val.add(Integer.toString(selectedProjectId));
    	val.add("0");
    	if(selectedProjectId<1000000)
    		conn.writeEntity("delete","ProgettoEntity", deleteAtt, val);
    	else
    		conn.deleteSingleEntity("insert", "FinanziamentoEntity", Integer.toString(selectedProjectId));
    	
    	
    }
    
    public void clearCache(String cacheName) {
    	
    	if(cacheName.equals("read")) 
    	{
	    	conn.clearEntity("read","AziendaEntity");
	    	conn.clearEntity("read","ProgettoEntity");
	    	conn.clearEntity("read","MessaggioEntity");
	    }
    	else if(cacheName.equals("delete")) 
    	{
    		conn.clearEntity("delete", "ProgettoEntity");
    		conn.clearEntity("delete", "MessaggioEntity");
    	}
    	else if(cacheName.equals("update")) 
    	{
    		conn.clearEntity("update", "FinanziamentoEntity");
    	}
    	else if(cacheName.equals("insert")) 
    	{
    		conn.clearEntity("insert", "ProgettoEntity");
    		conn.clearEntity("insert", "MessaggioEntity");
    		counterNewProjects=1000000;
    		counterNewMessages=1000000;
    	}
    	else
    		System.out.println("Operazione non permessa!");

    }
    
    public void readProgettoFromMySql(String agencyName)
    {
    	List<ProgettoEntity> projects = super.getProjectEntities();
    	for(int i = 0; i < projects.size(); i++) 
    	{
    		Vector<String> vett = new Vector<String>();
    		vett.add(Integer.toString(projects.get(i).getId()));
    		vett.add(projects.get(i).getDescrizione());
    		vett.add(projects.get(i).getNome());
    		vett.add(Integer.toString(projects.get(i).getBudget()));
    		vett.add(projects.get(i).getAzienda().getNomeAzienda());
    		vett.add(Double.toString((double)((double)super.getSommaStakes(projects.get(i).getId())/(double)projects.get(i).getBudget()*100))); //problema di conversione,per questo progress=0
    		vett.add(Integer.toString(super.myStake(agencyName,projects.get(i).getId()))); //solo i miei,non mi interessa la somma stakes
    		conn.writeEntity("read",  "ProgettoEntity", projectsAtt, vett);
    	}
    }
    
    public void readMessaggioFromMySql(String agencyName) {
    	List<MessaggioEntity> messages = getMessageEntities(agencyName);
    	for(int i = 0; i < messages.size(); i++) {
    		Vector<String> vett = new Vector<String>();
    		vett.add(Integer.toString(messages.get(i).getId()));
    		vett.add(Integer.toString(messages.get(i).getProgetto().getId()));
    		vett.add(messages.get(i).getTesto());
    		vett.add(messages.get(i).getDestinatario().getNomeAzienda());
    		vett.add(messages.get(i).getMittente().getNomeAzienda());
    		vett.add(messages.get(i).getData().toString());
    		vett.add(Integer.toString(messages.get(i).getStake()));
    		conn.writeEntity("read","MessaggioEntity", messageAtt, vett);
    	}
    }
    
    public void readAziendaFromMySql()
    {
    	 List<Vector<String>> allAgency = super.getAgencyEntities(); //Prende la lista delle aziende dal db
         if( allAgency == null )
             return;

         List<String> list = new ArrayList<String>();

         for( Vector<String> agency : allAgency)
             list.add(agency.get(0));
         
         for( Vector<String> agency : allAgency )
         	conn.writeEntity("read","AziendaEntity", agencyAtt, agency);
    }
    
    /*Inizio procedura*/
    
    //I progetti con ID >= 100000 sono quelli da copiare nel database
    public List<Vector<String>> getProjectsWrites()
    {
    	
    	
    	return conn.readAllEntity("read","ProgettoEntity");
    }
    
  //I messaggi con ID >= 100000 sono quelli da copiare nel database
    public List<Vector<String>> getMessagesWrites()
    {
    	List<Vector<String>> messages = conn.readAllEntity("insert","MessaggioEntity");
    	List<Vector<String>> ret = new ArrayList<Vector<String>>();
    	
    	if(messages==null||messages.isEmpty())
    		return null;
    	
		for(int i = 0; i < messages.size(); i++) 
		{
					if(messages.get(i).size()<7)
						;
					else
					{
						//Verifico se l'id del progetto è >= 100000
						if(Integer.parseInt(messages.get(i).get(0)) >= 100000)
		    			ret.add(messages.get(i));
					}	
		}
    	
    	return ret;
    }
    
    //Funzione che copia i progetti dalla cache al database
    public void copyProjectAndFundsInDB(List<Vector<String>> projects,String agencyName) {
    	
    	List<Vector<String>> messagesOnProjects = conn.readAllEntity("insert","MessaggioEntity");
    	
    	if(projects==null||projects.isEmpty()||projects.get(0).size()<7)
    		return ;
    	
    	//Per ogni progetto all'interno della lista ordino gli attributi come richiesto dalla insertProject 
    	//di jpaConnect(nome, budget, descrizione, azienda)
    	for(int i = 0; i < projects.size(); i++)
    	{
    		/*RowTableProjects rtp=new RowTableProjects(Integer.parseInt(progetto.get(0)),
    				progetto.get(5),
    				progetto.get(1),
    				Integer.parseInt(progetto.get(3)),
    				Integer.parseInt(progetto.get(6)),
    				progetto.get(2));*/
    		
    		int oldID = Integer.parseInt(projects.get(i).get(0));//id del progetto in cache
    		if(oldID>=1000000)
    		{
    			
    			Vector<String> vett = new Vector<String>();
    			vett.add(projects.get(i).get(5));	//nome
        		vett.add(projects.get(i).get(3));	//budget
        		vett.add(projects.get(i).get(4));	//descrizione
        		vett.add(projects.get(i).get(2));	//azienda owner
        		//Inserisco il progetto nel db
        		super.insertProject(vett);
        		
        		//Prendo tutti i progetti nel database
        		List<ProgettoEntity> projectsInDb =  getProjectEntities();
        		
        		//Prendo l'id dell'ultimo progetto presente nel database
        		ProgettoEntity lastProject = projectsInDb.get(projectsInDb.size()-1);
        		
        		//Prendo ultimo id inserito nel database
        		int lastID = lastProject.getId();
        		
        		String stakeString=conn.readSingleValue("update","FinanziamentoEntity", Integer.toString(oldID), "stake");
        		
        		if(stakeString.equals(""))
        			;
        		else 
        		{
        			int stake=Integer.parseInt(stakeString);
        			super.updateStake(stake, agencyName, lastID, false);
        		}
        		
        		if(messagesOnProjects == null || messagesOnProjects.isEmpty())
        			;
        		else {
        			//Rimappatura dei messaggi a cui cambio l'id del progetto
        			for(int j = 0; j < messagesOnProjects.size(); j++) {
        				
        				Vector<String> message = messagesOnProjects.get(j);
        				if(message == null || message.isEmpty() || message.size() < 7)
        					;
        				else if(Integer.parseInt(message.get(4)) == oldID)
        					conn.updateSingleValue("insert","MessaggioEntity", message.get(0), "idProgetto", Integer.toString(lastID));
        				
        			}
        		}
        		
    		}
    		else 
    		{
    			
    			String stakeString=conn.readSingleValue("update","FinanziamentoEntity", Integer.toString(oldID), "stake");
        		if(stakeString.equals(""))
        			;
        		else 
        		{
        			int stake=Integer.parseInt(stakeString);
        			super.updateStake(stake, agencyName, oldID, false);
        		}
    		}
    	}
    		
    		
    }
    
    //Funzione che copia i messaggi dalla cache al database
    public void copyMessagesInDB(List<Vector<String>> messages) {
    	
    	
    	//Per ogni messaggio all'interno della lista ordino gli attributi come richiesto dalla insertMessage
    	//di jpaConnect(testo, stake, data, mittente, destinatario, progetto)
    	for(int i = 0; i < messages.size(); i++) {
    		Vector<String> vett = new Vector<String>();
    		
    		vett.add(messages.get(i).get(2));	//mittente
    		vett.add(messages.get(i).get(1));	//destinatario
    		vett.add(messages.get(i).get(4)); 	//progetto
    		vett.add(messages.get(i).get(6)); 	//testo
    		vett.add(messages.get(i).get(5)); 	//stake
    		
    		//1.dest 0.mitt 2.prog 3.testo 4.stake
    		
    		//inserisco il messaggio nel database
    		super.insertMessage(vett);
    	}
    }
    
    public void deleteEntitiesInMysql() 
    {
    	
    	List<Vector<String>> agencies = conn.readAllEntity("read","AziendaEntity");
    	List<Vector<String>> projectsToBeEliminated=conn.readAllEntity("delete","ProgettoEntity");
    	List<Vector<String>> messagesToBeEliminated=conn.readAllEntity("delete","MessaggioEntity");
    	
    	for(int i=0;i<projectsToBeEliminated.size();i++)
    	{
    		Vector<String>row=projectsToBeEliminated.get(i);
    		
    		if(row==null||row.isEmpty()||row.size()<2)
    			;
    		else
    		{
    		
    			/*for(int j = 0; j < agencies.size(); j++)
    				super.deleteMyStake(Integer.parseInt(row.get(0)), agencies.get(j).get(0));*/
    				
    				super.deleteProject(Integer.parseInt(row.get(0)));
    				
    		}
    	}
    	
    	for(int i=0;i<messagesToBeEliminated.size();i++)
    	{
    		
    		Vector<String>row=messagesToBeEliminated.get(i);
    		
    		if(row==null||row.isEmpty()||row.size()<2)
    			;
    		else
    		{
    		
    			super.deleteMessage(Integer.parseInt(row.get(0)));
    		
    		}
    	}
    }
    
}
