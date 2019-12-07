package jpaConnect;

import applicationMiddle.RowTableMessage;
import applicationMiddle.RowTableProjects;
import applicationMiddle.wrapperDbs;
import jpaEntities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class DepositoDati implements wrapperDbs {
    private FundracingManager fm = null;
    private AtomicBoolean aggiornamentoFatto=new AtomicBoolean(false);
    private AtomicBoolean routinesInExecution=new AtomicBoolean(false);
     
    
    public DepositoDati() {
    	fm = new FundracingManager();
    }
    
    protected List<RowTableProjects> getRowTableProjects(List<ProgettoEntity> list, String agencyName, Boolean withStake) {
        

        List<RowTableProjects> rows = new ArrayList<RowTableProjects>();
        for(ProgettoEntity p : list){
            int projectId = p.getId();

            Long progress = fm.singleReturnQuery(Long.class, "SELECT SUM(f.budget) FROM FinanziamentoEntity f WHERE f.progetto = " + projectId );
            if( progress == null)
                progress = new Long(0);

            double prog  = ((double) progress / p.getBudget() ) * 100 ;

            Long stake = new Long(0);

            if(withStake) {
                stake = fm.singleReturnQuery(Long.class, "SELECT SUM(f.budget) FROM FinanziamentoEntity f WHERE f.progetto = " + projectId + "AND f.azienda = '" + agencyName + "'");
                if (stake == null)
                    stake = new Long(0);
            }

            RowTableProjects rtp = new RowTableProjects(projectId, p.getNome(), Double.toString(prog), p.getBudget(), Integer.parseInt(stake.toString()), p.getAzienda().getNomeAzienda());
            rows.add( rtp );
        }

       return rows;
    }
    
    protected List<RowTableMessage> getRowTableMessage(List<MessaggioEntity> me_list) {
        if(me_list == null)
            return new ArrayList<RowTableMessage>();

        List<RowTableMessage> rows = new ArrayList<RowTableMessage>();

        for( MessaggioEntity me : me_list){

            RowTableMessage row = new RowTableMessage(me.getId(), me.getProgetto().getId(), me.getData().toString(),
                    me.getMittente().getNomeAzienda(), me.getDestinatario().getNomeAzienda(), me.getTesto(), me.getStake());

            rows.add(row);
        }

        return rows;
    }

    protected List<Vector<String>> getAgencyEntities() {
        String sql = "select a	from	AziendaEntity a ";

       if (!fm.isSetup()) {
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }

        List<AziendaEntity> agencies = fm.query(AziendaEntity.class, sql);

      
        List<Vector<String>> list = new ArrayList<>();

        if (agencies == null)
            return list;

        for (AziendaEntity a : agencies) {
            Vector<String> vett = new Vector<String>();
            vett.add(a.getNomeAzienda());
            vett.add( a.getUrlLogo() );
            vett.add( a.getUrlSito() );
            vett.add( a.getIndirizzo() );
            vett.add( a.getCap().toString() );
            vett.add( a.getPassword() );

            list.add(vett);
        }

        return list;
    }
    protected List<ProgettoEntity> getProjectEntities(){
       if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }

        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, "SELECT p FROM ProgettoEntity p");
      
        return projects;
    }
    protected List<MessaggioEntity> getMessageEntities(String agencyName){
        String sql = "SELECT m FROM MessaggioEntity m WHERE destinatario = '" + agencyName + "'";

        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }

        List<MessaggioEntity> messages = fm.query(MessaggioEntity.class, sql);
        
        return messages;
    }
    

    public boolean getAggiornamentoFatto() {
    	return this.aggiornamentoFatto.get();
    }
    
    public void setAggiornamentoFatto(boolean aggiornamentoFatto) {
    	this.aggiornamentoFatto.set(aggiornamentoFatto);
    }
    

    public boolean getRoutinesInExecution() {
    	return this.routinesInExecution.get();
    }
    
    public void setRoutinesInExecution(boolean routinesInExecution) {
    	this.routinesInExecution.set(routinesInExecution);
    	
    }
    
    public void close() {	//Sbagliata??? E' corretto scrivere if (fm.isSetup()) 
    	fm.exit();
    }
    
    
    public Vector<String> getAgency(String agencyName,String password) {
        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }

        AziendaEntity ae = fm.selectAgency(agencyName);

      
        Vector<String> vett = new Vector<String>();

        if(ae == null || !password.equals(ae.getPassword()) )
            return vett;

        vett.add(ae.getNomeAzienda());
        vett.add( ae.getUrlLogo());
        vett.add( ae.getUrlSito());
        vett.add( ae.getIndirizzo());
        vett.add( ae.getCap().toString());

        return vett;
    }
    public Vector<String> getAgency(String agencyName) {
       if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return new Vector<>();
        }

        AziendaEntity ae = fm.selectAgency(agencyName);

        if( ae ==null){
            System.out.print("Agency not present \n");
            return new Vector<>();
        }

        Vector<String> vett = new Vector<String>();
        vett.add(ae.getNomeAzienda());
        return vett;
        }
    
    
    public void insertAgency(Vector<String> val){
       if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
        }
        fm.createAgency(val.get(0), val.get(1), val.get(2), val.get(3), Integer.parseInt(val.get(4)), val.get(5));
       
    }
    
    
    public List<String> getListAgency(){

        List<Vector<String>> agencyList = getAgencyEntities();
        List<String> list = new ArrayList<>();

        for( Vector<String> a : agencyList )
            list.add(a.get(0));

        return list;
    }

    public List<RowTableProjects> getProjects(String agencyName){
        List<ProgettoEntity> projects = this.getProjectEntities();
        List<RowTableProjects> ret = getRowTableProjects( projects, agencyName, true );

        return ret;
    }

    public List<RowTableMessage> getMessages(String agencyName){
        List<MessaggioEntity> messages = getMessageEntities(agencyName);
        List<RowTableMessage> ret = getRowTableMessage(messages);

        return ret;
    }

    public String getDescriptionMessage(int id_message) {
        if(!fm.isSetup())
            return " ";

        MessaggioEntity msg = fm.selectMessaggio(id_message);

        if(msg == null)
            return " ";

        return msg.getTesto();
    }

    public int getSommaStakes(int selectedProjectID){
        String sql = "SELECT sum(f.budget) FROM FinanziamentoEntity f  WHERE f.progetto= " + selectedProjectID ;

 if(!fm.isSetup())
            return -1;

        Long stake = fm.singleReturnQuery(Long.class, sql);

       
        if(stake == null)
            return 0;

        return Integer.parseInt(stake.toString());
    }


    public List<RowTableProjects> getProjectsWithoutStake(){
       if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }
        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, "SELECT p FROM ProgettoEntity p");

        List<RowTableProjects> ret = getRowTableProjects( projects, null, false );

        
        return ret;
        }

    public void insertProject(Vector<String>val) {
       if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }
        AziendaEntity ae = fm.selectAgency(val.get(3));
        if(ae == null){
            System.out.print("Impossibile creare progetto di un'azienda non registrata \n");
            return;
        }

        fm.createProject(val.get(0), Integer.parseInt(val.get(1)), val.get(2), ae );	//nome, budget, descrizione, azienda


    }

    public boolean iAmOwner(int projectId,String agencyName) {
        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return false;
        }


        ProgettoEntity progetto = fm.singleReturnQuery(ProgettoEntity.class, "SELECT p FROM ProgettoEntity p WHERE p.azienda = '" + agencyName + "' AND p.id = " + projectId );

       if(progetto == null)
            return false;

        return true;
    }

    public int myStake(String agencyName, int id_project){
       if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return -1;
        }

        Long stake = fm.singleReturnQuery(Long.class, "SELECT SUM(f.budget) FROM FinanziamentoEntity f WHERE f.progetto = " + id_project + "AND f.azienda = '" + agencyName + "'");
        if (stake == null)
            stake = new Long( 0);

        return Integer.parseInt(stake.toString());
    }

    public Boolean isMyStake(String agencyName, int id_project) {
        if( myStake(agencyName, id_project) > 0 )
            return true;
        return false;
    }

    public void deleteProject(int projectId) { 
    	
    	//delete di tutti finanziamenti fatti da tutte le aziende sul progetto eliminato
			
    	this.deleteFinanziamenti(projectId);
    	
    	this.deleteMessages(projectId);
        
    	 if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }
        
        fm.deleteProject(projectId);


    }

    public void deleteMessage(int messageId) {
        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }

        fm.deleteMessaggio(messageId);

    }

    public String getDescriptionProject(int id_project) {
        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }

        ProgettoEntity pe = fm.selectProject(id_project);

        if(pe == null)
            return null;

        return pe.getDescrizione();
    }
    
    
    public void deleteMessages(int projectId) {
    	String sql = "DELETE FROM MessaggioEntity m WHERE m.progetto =" + projectId;
    	
    	  if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }
        
        int occurrency = fm.executeUpdateQuery(sql);
        /*
        if(occurrency<1) {
        	System.out.println("FAILED TO DELETE MESSAGES");
        }
		*/
        
    }
    
    public void deleteFinanziamenti(int projectId) {
    	String sql = "DELETE FROM FinanziamentoEntity f WHERE f.progetto =" + projectId;
    	
  	  if( !fm.isSetup() ){
          System.out.print("Non ho la connessione con il database \n");
          return;
      }
      
      int occurrency = fm.executeUpdateQuery(sql);
      /*
      if(occurrency<1) {
      	System.out.println("FAILED TO DELETE MESSAGES");
      }
		*/
      
  }
    

    public void deleteMyStake(int projectId,String agencyName) {
        String sql = "DELETE FROM FinanziamentoEntity WHERE progetto = "+ projectId +" and azienda = '"+ agencyName + "'";

        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }

        int occurrency = fm.executeUpdateQuery(sql);
        /*
        if( occurrency < 1 )
            System.out.print("FAILED TO DELETE STAKE \n"); */
            
    }

    public void updateStake(int stakeBudget,String agencyName,int idProgetto, boolean add) {
      
        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }

        FinanziamentoEntity finanziamento = fm.singleReturnQuery(FinanziamentoEntity.class, "SELECT f FROM FinanziamentoEntity f WHERE f.progetto =" + idProgetto + "AND f.azienda = '" + agencyName + "'");

        if(finanziamento == null)
        {
            AziendaEntity azienda = fm.selectAgency(agencyName);
            ProgettoEntity progetto = fm.selectProject(idProgetto);

            fm.createFinanziamento(stakeBudget, azienda, progetto);
        }
        else if(!add) {
            finanziamento.setBudget(stakeBudget);
            fm.updateFinanziamento(finanziamento.getId(), finanziamento );
        }else{
            finanziamento.setBudget(stakeBudget + finanziamento.getBudget());
            fm.updateFinanziamento(finanziamento.getId(), finanziamento);
        }


    }

    public List<Vector<String>> getProject(int idProgetto){
        String sql = "SELECT p FROM ProgettoEntity p WHERE id =" + idProgetto ;

        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return null;
        }

        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, sql);

        if(projects == null)
            return new ArrayList<Vector<String>>();


        List<Vector<String>> list = new ArrayList<Vector<String>>();

        for(ProgettoEntity p : projects){
            Vector<String> vett = new Vector<String>();
            vett.add(Integer.toString(p.getId()));
            vett.add(p.getNome());
            vett.add(p.getBudget().toString());
            vett.add(p.getDescrizione());
            vett.add(p.getAzienda().getNomeAzienda());

            list.add(vett);
        }

        return list;
    }


    public void insertMessage(Vector<String>val) {
    	
    	/*System.out.println("Sono in insertMessage");
    	for(int i = 0; i < val.size(); i++) {
        	System.out.println("insertMessage " + val.get(i));
        }*/
    	
        if( !fm.isSetup() ){
            System.out.print("Non ho la connessione con il database \n");
            return;
        }

        AziendaEntity aeDest = fm.selectAgency( val.get(1) );
        if(aeDest == null){
            System.out.print( "Impossibile inviare un messaggio a un'azienda non registrata \n");
            return;
        }
        AziendaEntity aeMitt = fm.selectAgency( val.get(0) );
        if(aeMitt == null){
            System.out.print( "Impossibile far inviare un messaggio a un'azienda non registrata \n");
            return;
        }
        ProgettoEntity pe = fm.selectProject(Integer.parseInt( val.get(2) ));
        if(pe == null){
            System.out.print( "Impossibile inviare un messaggio a su di un progetto inesistente \n");
            return;
        }
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());
        

        MessaggioEntity me = fm.createMessage( val.get(3), Integer.parseInt(val.get(4)), date, aeMitt, aeDest, pe );

        if(me == null){
            System.out.print("MESSAGE SEND FAILED \n");
        }

    }
    
    public void clearCache(String nome) {
    	
    }
    
    public void readProgettoFromMySql(String nome) {
    	
    }
    
    public void readMessaggioFromMySql(String nome) {
    	
    }
    
    public void readAziendaFromMySql() {
    	
    }
    
    public List<Vector<String>> getProjectsWrites() {
    	return null;
    }
    
    public void copyProjectAndFundsInDB(List<Vector<String>>a,String nome) {
    	
    }
    
    public List<Vector<String>>getMessagesWrites(){
    	return null;
    }
    
    public void copyMessagesInDB(List<Vector<String>>a) {
    	
    }
    
    public void deleteEntitiesInMysql() {
    	
    }
    
    public void freeLock() {
    	
    }
    
    public void getLock() {
    	
    }
    
}
