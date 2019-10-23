package jpaConnect;

import application.RowTableMessage;
import application.RowTableProjects;
import jpaEntities.AziendaEntity;
import jpaEntities.FinanziamentoEntity;
import jpaEntities.MessaggioEntity;
import jpaEntities.ProgettoEntity;
import mysqlConnect.Connect;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;

public class DepositoDati {
    private FundracingManager fm = null;

    private List<RowTableProjects> getRowTableProjects(List<ProgettoEntity> list, String agencyName, Boolean withStake) {
        List<RowTableProjects> rows = new ArrayList<RowTableProjects>();
        for(ProgettoEntity p : list){
            int projectId = p.getId();

            Integer progress = fm.singleReturnQuery(int.class, "SELECT SUM(f.budget) FROM finanziamento f WHERE f.progetto = " + projectId );
            if( progress == null)
                progress = 0;

            progress = 100 * progress / p.getBudget();

            Integer stake = 0;

            if(withStake) {
                stake = fm.singleReturnQuery(int.class, "SELECT SUM(f.budget) FROM finanziamento f WHERE f.progetto = " + projectId + "AND f.azienda = '" + agencyName + "'");
                if (stake == null)
                    stake = 0;
            }

            RowTableProjects rtp = new RowTableProjects(projectId, p.getNome(), progress.toString(), p.getBudget(), stake, p.getOwner());
            rows.add( rtp );
        }

        return rows;
    }

    private List<RowTableMessage> getRowTableMessage(List<MessaggioEntity> me_list) {
        if(me_list == null)
            return new ArrayList<RowTableMessage>();

        List<RowTableMessage> rows = new ArrayList<RowTableMessage>();

        for( MessaggioEntity me : me_list){

            RowTableMessage row = new RowTableMessage(me.getId(), me.getProject(), me.getData().toString(),
                    " ", me.getDestinatario(), me.getTesto(), me.getStake());

            rows.add(row);
        }

        return rows;
    }

    public List<RowTableProjects> getProjects(String agencyName){
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, "SELECT p FROM progetto p");

        fm.exit();

        List<RowTableProjects> ret = getRowTableProjects( projects, agencyName, true );

        return ret;
    }

    public List<RowTableMessage> getMessages(String agencyName){
        /* String sqlStr="select	m.id, m.progetto as id_project, m.data, m.mittente, m.destinatario, m.testo as messaggio, m.stake\n" +
                "from	messaggio m\n" +
                "		inner join\n" +
                "        azienda a\n" +
                "        on m.mittente = a.nomeAzienda\n" +
                "where 	m.destinatario =  (?) ;";
        */
        String sql = "SELECT * FROM messaggio WHERE destinatario = '" + agencyName + "'";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<MessaggioEntity> messages = fm.query(MessaggioEntity.class, sql);

        fm.exit();

        List<RowTableMessage> ret = getRowTableMessage(messages);

        return ret;
    }

    public String getDescriptionMessage(int id_message) {
        fm = new FundracingManager();
        if(!fm.isSetup())
            return " ";

        MessaggioEntity msg = fm.selectMessaggio(id_message);

        fm.exit();

        if(msg == null)
            return " ";

        return msg.getTesto();
    }

    public int getSommaStakes(int selectedProjectID){
        String sql = "SELECT sum(f.budget) as somma "
                    + "FROM finanziamento f  "
                    + " WHERE f.progetto="+ selectedProjectID +";";


        fm = new FundracingManager();
        if(!fm.isSetup())
            return -1;

        Integer stake = fm.singleReturnQuery(int.class, sql);

        fm.exit();

        if(stake == null)
            return 0;

        return stake;
    }

    public double getProgress(int id_progetto) {
        fm = new FundracingManager();
        if(!fm.isSetup())
            return 0;

        Integer progress = fm.singleReturnQuery(int.class, "SELECT SUM(f.budget) FROM finanziamento f WHERE f.progetto = " + id_progetto );
        if( progress == null)
            progress = 0;

        fm.exit();

        return progress;
    }

    public List<RowTableProjects> getProjectsWithoutStake(){
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }
        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, "SELECT p FROM progetto p");

        List<RowTableProjects> ret = getRowTableProjects( projects, null, false );

        fm.exit();
        return ret;
        }

    public void insertProject(Vector<String>val) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }
        AziendaEntity ae = fm.selectAgency(val.get(3));
        if(ae == null){
            System.out.print("Impossibile creare progetto di un'azienda non registrata \n");
            return;
        }

        fm.createProject(val.get(0), Integer.parseInt(val.get(1)), val.get(2), ae );

        fm.exit();

    }

    public boolean iAmOwner(int projectId,String agencyName) {
        String sql="SELECT COUNT(*) as conta FROM progetto WHERE id="+ projectId +" and azienda='"+ agencyName +"';";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return false;
        }
        Integer occurrency = fm.singleReturnQuery(int.class, sql);

        fm.exit();
        if(occurrency == null || occurrency <= 0)
            return false;

        return true;
    }

    public int myStake(String agencyName, int id_project){
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return -1;
        }

        Integer stake = fm.singleReturnQuery(int.class, "SELECT SUM(f.budget) FROM finanziamento f WHERE f.progetto = " + id_project + "AND f.azienda = '" + agencyName + "'");
        if (stake == null)
            stake = 0;

        fm.exit();

        return stake;
    }

    public Boolean isMyStake(String agencyName, int id_project) {
        if( myStake(agencyName, id_project) > 0 )
            return true;
        return false;
    }

    public void deleteProject(int projectId) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        fm.deleteProject(projectId);

        fm.exit();

    }

    public void deleteMessage(int messageId) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        fm.deleteMessaggio(messageId);

        fm.exit();

    }

    public Vector<String> getAgency(String agencyName,String password) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        AziendaEntity ae = fm.selectAgency(agencyName);

        fm.exit();

        Vector<String> vett = new Vector<String>();

        if(ae == null || password != ae.getPassword())
            return vett;

        vett.add(ae.getNomeAzienda());
        vett.add( ae.getUrlLogo());
        vett.add( ae.getUrlSito());
        vett.add( ae.getIndirizzo());
        vett.add( ae.getCap().toString());

        return vett;
    }

    public String getDescriptionProject(int id_project) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        ProgettoEntity pe = fm.selectProject(id_project);

        fm.exit();

        if(pe == null)
            return null;

        return pe.getDescrizione();
    }

    public void deleteMyStake(int projectId,String agencyName) {
        String sql = "DELETE FROM finanziamento WHERE progetto = "+ projectId +" and azienda = '"+ agencyName + "';";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        int occurrency = fm.executeUpdateQuery(sql);

        fm.exit();

        if( occurrency < 1 )
            System.out.print("FAILED TO DELETE STAKE \n");
    }

    public void updateStake(int stakeBudget,String agencyName,int idProgetto, boolean add) {
        Boolean stakePresent = isMyStake(agencyName, idProgetto);

        if(stakePresent == null )
            return;

        String stakeBudgetStr = Integer.toString(stakeBudget);
        String idProgettoStr = Integer.toString(idProgetto);

        String sql = "";
        if(!stakePresent)
            sql = "INSERT INTO finanziamento (budget, azienda, progetto) values('"+ stakeBudgetStr +"', '"+ agencyName +"', '"+idProgettoStr +"');";
        else if (!add)
            sql = "UPDATE finanziamento SET budget = '"+ stakeBudgetStr +"' WHERE azienda = '"+agencyName+"' and progetto = '"+idProgettoStr+"';";
        else
            sql = "UPDATE finanziamento SET budget = budget + '"+ stakeBudgetStr +"' WHERE azienda = '"+agencyName+"' and progetto = '"+idProgettoStr+"';";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        int occurrency = fm.executeUpdateQuery(sql);

        fm.exit();

        if( occurrency < 1 )
            System.out.print("FAILED TO UPDATE STAKE \n");
    }

    public List<Vector<String>> getProject(int idProgetto){
        String sql = "SELECT * FROM progetto WHERE id =" + idProgetto + ";";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, sql);

        fm.exit();
        if(projects == null)
            return new ArrayList<Vector<String>>();


        List<Vector<String>> list = new ArrayList<Vector<String>>();

        for(ProgettoEntity p : projects){
            Vector<String> vett = new Vector<String>();
            vett.add(Integer.toString(p.getId()));
            vett.add(p.getNome());
            vett.add(p.getBudget().toString());
            vett.add(p.getDescrizione());
            vett.add(p.getOwner());

            list.add(vett);
        }

        return list;
    }

    public List<String> getListAgency(){
        String sql = "select *	\n" +
                "from	azienda;";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<AziendaEntity> agencies = fm.query(AziendaEntity.class, sql);

        fm.exit();

        if(agencies == null)
            return new ArrayList<String>();

        List<String> list = new ArrayList<String>();

        for(AziendaEntity a : agencies)
            list.add(a.getNomeAzienda());

        return list;
    }

    public void insertMessage(Vector<String>val) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        AziendaEntity ae = fm.selectAgency( val.get(1) );
        if(ae == null){
            System.out.print( "Impossibile inviare un messaggio a un'azienda non registrata \n");
            return;
        }

        ProgettoEntity pe = fm.selectProject(Integer.parseInt( val.get(2) ));
        if(pe == null){
            System.out.print( "Impossibile inviare un messaggio a su di un progetto inesistente \n");
            return;
        }
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());

        MessaggioEntity me = fm.createMessage(  val.get(3), Integer.parseInt(val.get(4)), date, ae, pe );

        fm.exit();

        if(me == null){
            System.out.print("MESSAGE SEND FAILED \n");
        }

    }


}
