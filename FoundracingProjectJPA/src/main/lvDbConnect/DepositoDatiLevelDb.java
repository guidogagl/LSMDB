package lvDbConnect;

import application.RowTableMessage;
import application.RowTableProjects;

import jpaConnect.DepositoDati;

import jpaEntities.MessaggioEntity;
import jpaEntities.ProgettoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DepositoDatiLevelDb extends DepositoDati{
    private Connect conn = new Connect();
    private Vector<String> agencyAtt = new Vector<String>();
    private Vector<String> projectsAtt = new Vector<String>();
    private Vector<String> messageAtt = new Vector<String>();
    
    
    private Boolean createConnection(){
        if (conn == null){
            System.out.print("Impossibile create la connessione con il levelDb \n");
            return false;
        }
        return true;
    }  

    private Vector<String> getEntityFromLevelDb(String EntityName, String entityID ){
        if( !createConnection() )
            return null;
        List<Vector<String>> agencyList = conn.readEntity(EntityName, entityID);

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
        projectsAtt.add("Stake");

        messageAtt.add("id");
        messageAtt.add("testo");
    }

    public Vector<String> getAgency(String agencyName) {
        Vector<String> agency = getEntityFromLevelDb( "AziendaEntity", agencyName );
        if( agency == null )
            agency = new Vector<String>();

        return agency;
    }
    public Vector<String> getAgency(String agencyName,String password) {
        Vector<String> res = this.getAgency(agencyName);
        if( res.isEmpty() || !res.get(3).equals(password))
            return new Vector<String>();
        return res;
    }
    
    public void insertAgency(Vector<String> val){
        super.insertAgency(val); //Inserimento in db ricordare per documentazione

       /* Vector<String> att = new Vector<String>();

        if(!createConnection())
            return;

        conn.writeEntity( "AziendaEntity", agencyAtt, val);*/

    }
    public List<String> getListAgency(){
        List<Vector<String>> allAgency = super.getAgencyEntities();
        if( allAgency == null )
            return null;

        List<String> list = new ArrayList<String>();

        for( Vector<String> agency : allAgency)
            list.add(agency.get(0));

        if(!createConnection() || list.isEmpty())
            return list;
       conn.clearEntity("AziendaEntity");
        // aggiorno la cache 
        for( Vector<String> agency : allAgency )
        	conn.writeEntity("AziendaEntity", agencyAtt, agency);
        return list;
    }
    
    public List<RowTableProjects> getProjects(String agencyName){
        List<ProgettoEntity> projects = super.getProjectEntities();
        List<RowTableProjects> ret = super.getRowTableProjects( projects, agencyName, true );

        if(!createConnection())
            return ret;
       	conn.clearEntity("ProgettoEntity");
        int i = 0;
        for(ProgettoEntity p : projects){
          
        	Vector<String> val = new Vector<String>();
            val.add( Integer.toString(p.getId()) );
            val.add( p.getDescrizione() );
            val.add( p.getNome());
            val.add( p.getBudget().toString() );
            val.add( p.getAzienda().getNomeAzienda() );
            val.add( ret.get(i).getProgress());
            val.add( ret.get(i).getStake().toString() );
            conn.writeEntity("ProgettoEntity", projectsAtt, val);
            i++;
        }
        

        return ret;
    }
    public List<RowTableProjects> getProjectsWithoutStake() {
        List<ProgettoEntity> projects = super.getProjectEntities();
        List<RowTableProjects> ret = super.getRowTableProjects(projects, null, false);

        if (!createConnection())
            return ret;

        int i = 0;
        for (ProgettoEntity p : projects) {
            Vector<String> val = new Vector<String>();
            val.add(Integer.toString(p.getId()));
            val.add(p.getDescrizione());
            val.add(p.getNome());
            val.add(p.getBudget().toString());
            val.add(p.getAzienda().getNomeAzienda());

            val.add(ret.get(i).getProgress());
            val.add( "0" );

            conn.writeEntity("ProgettoEntity", projectsAtt, val);
            i++;
        }

        return ret;
    }
    public List<Vector<String>> getProject(int idProgetto){
        Vector<String> prog = getEntityFromLevelDb("ProgettoEntity", Integer.toString(idProgetto));

        if( prog!= null ) {
            List<Vector<String>> ret = new ArrayList<Vector<String>>();
            ret.add(prog);
            return ret;
        }

        return new ArrayList<Vector<String>>();
    }

    public double getProgress(int id_progetto) {
    	Double somma_stake = super.getProgress(id_progetto);
        List<Vector<String>> prog = super.getProject( id_progetto );

        
        if( !prog.isEmpty() ) {
        	Double total_budget = Double.parseDouble(prog.get(0).get(2));
            return (somma_stake/total_budget)*100;
        }
        
        return 0;
    }

    public String getDescriptionProject(int id_project) {
       Vector<String> prog = getEntityFromLevelDb("ProgettoEntity", Integer.toString(id_project));
       if( prog!= null && !prog.isEmpty() && prog.firstElement() != null ) {
    	   return prog.get(5) ;
       } 
       
        return new String();
    }
    
    public boolean iAmOwner(int projectId,String agencyName) {
    	List<Vector<String>> agency = super.getProject( projectId );

        if(  agency.isEmpty() || !agency.get(0).get(4).equals(agencyName))
            return false;
        return true;
    }
    public int getSommaStakes(int selectedProjectID){
        Vector<String> stakes = getEntityFromLevelDb("ProgettoEntity" , Integer.toString( selectedProjectID ));
        if( stakes == null || stakes.isEmpty() || stakes.firstElement().isEmpty() )
            return 0;
        return Integer.parseInt(stakes.firstElement());
    }

    public List<RowTableMessage> getMessages(String agencyName){
        List<MessaggioEntity> messages = super.getMessageEntities( agencyName );
        List<RowTableMessage> ret = super.getRowTableMessage(messages);

        if(!createConnection())
            return ret;
       	conn.clearEntity("MessaggioEntity");
       	for(MessaggioEntity m : messages){
       		Vector<String> val = new Vector<String>();
            val.add( Integer.toString(m.getId()) );
            val.add( m.getTesto() );
            conn.writeEntity("MessaggioEntity", messageAtt, val);
        }
        
        return ret;
    }
    public String getDescriptionMessage(int id_message) {
        Vector<String> prog = getEntityFromLevelDb("MessaggioEntity",Integer.toString(id_message));

        if( prog!= null && !prog.isEmpty() && prog.firstElement() != null ) {
            return prog.get(1);
        }

        return new String();
    }
    public Vector<String> getAgencyBasic(String agencyName, String password, boolean withPassword){
    	if(withPassword)
    		return super.getAgency(agencyName, password);
    	else
    		return super.getAgency(agencyName);
    }
}
