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
    private Connect conn = null;
    private Boolean createConnection(){
        conn = new Connect();
        if (conn == null){
            System.out.print("Impossibile create la connessione con il levelDb \n");
            return false;
        }
        return true;
    }
    private Vector<String> agencyAtt = new Vector<String>();
    private Vector<String> projectsAtt = new Vector<String>();
    private Vector<String> messageAtt = new Vector<String>();

    private Vector<String> getEntityFromLevelDb(String EntityName, String entityID ){
        if( !createConnection() )
            return null;
        List<Vector<String>> agencyList = conn.readEntity(EntityName, entityID);

        // se l'ho trovato
        if (agencyList != null && !agencyList.isEmpty())
            return agencyList.get(1);
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
        if( res.isEmpty() || !res.get(5).equals(password))
            return null;
        return res;
    }
    public void insertAgency(Vector<String> val){
        this.insertAgency(val);

        Vector<String> att = new Vector<String>( );

        if(!createConnection())
            return;

        conn.writeEntity( "AziendaEntity", agencyAtt, val);
        conn.close();
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

        // aggiorno la caches
        for( Vector<String> agency : allAgency )
            conn.writeEntity("AziendaEntity", agencyAtt, agency);

        conn.close();
        return list;
    }

    public List<RowTableProjects> getProjects(String agencyName){
        List<ProgettoEntity> projects = super.getProjectEntities();
        List<RowTableProjects> ret = super.getRowTableProjects( projects, agencyName, true );

        if(!createConnection())
            return ret;
        int i = 1;
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

        conn.close();
        return ret;
    }
    public List<RowTableProjects> getProjectsWithoutStake() {
        List<ProgettoEntity> projects = super.getProjectEntities();
        List<RowTableProjects> ret = super.getRowTableProjects(projects, null, false);

        if (!createConnection())
            return ret;

        int i = 1;
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
        conn.close();
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
        Vector<String> prog = getEntityFromLevelDb("ProgettoEntity:" + id_progetto, "Progress");

        if( prog!= null && !prog.isEmpty() )
            return Double.parseDouble( prog.firstElement() );
        return 0;
    }
    public String getDescriptionProject(int id_project) {
        Vector<String> desc = getEntityFromLevelDb("ProgettoEntity:" + id_project, "descrizione");

        if( desc!= null && !desc.isEmpty() )
            return desc.firstElement() ;

        return new String();
    }
    public boolean iAmOwner(int projectId,String agencyName) {
        Vector<String> agency = getEntityFromLevelDb("ProgettoEntity:" + projectId , "nomeAzienda");

        if( agency == null || agency.isEmpty() || agency.firstElement() == null || !agency.firstElement().equals(agencyName))
            return false;
        return true;
    }
    public int getSommaStakes(int selectedProjectID){
        Vector<String> stakes = getEntityFromLevelDb("ProgettoEntity:" + selectedProjectID , "Stake");
        if( stakes == null || stakes.isEmpty() || stakes.firstElement().isEmpty() )
            return 0;
        return Integer.parseInt(stakes.firstElement());
    }

    public List<RowTableMessage> getMessages(String agencyName){
        List<MessaggioEntity> messages = super.getMessagesEntity( agencyName );
        List<RowTableMessage> ret = super.getRowTableMessage(messages);

        if(!createConnection())
            return ret;

        for(MessaggioEntity m : messages){
            Vector<String> val = new Vector<String>();
            val.add( Integer.toString(m.getId()) );
            val.add( m.getTesto() );

            conn.writeEntity("MessaggioEntity", messageAtt, val);
        }

        conn.close();
        return ret;
    }
    public String getDescriptionMessage(int id_message) {
        Vector<String> prog = getEntityFromLevelDb("MessaggioEntity:" +id_message, "testo" );

        if( prog!= null && !prog.isEmpty() && prog.firstElement() != null ) {
            return prog.firstElement();
        }

        return new String();
    }
}
