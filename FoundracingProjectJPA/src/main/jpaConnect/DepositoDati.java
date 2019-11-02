package jpaConnect;

import application.*;
import jpaEntities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DepositoDati {
    private FundracingManager fm = null;

    protected List<RowTableProjects> getRowTableProjects(List<ProgettoEntity> list, String agencyName, Boolean withStake) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

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

        fm.exit();
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

        fm = new FundracingManager();
        if (!fm.isSetup()) {
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<AziendaEntity> agencies = fm.query(AziendaEntity.class, sql);

        fm.exit();

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
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, "SELECT p FROM ProgettoEntity p");
        fm.exit();

        return projects;
    }
    protected List<MessaggioEntity> getMessageEntities(String agencyName){
        String sql = "SELECT m FROM MessaggioEntity m WHERE destinatario = '" + agencyName + "'";

        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return null;
        }

        List<MessaggioEntity> messages = fm.query(MessaggioEntity.class, sql);

        fm.exit();

        return messages;
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
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return new Vector<>();
        }

        AziendaEntity ae = fm.selectAgency(agencyName);

        fm.exit();

        if( ae ==null){
            System.out.print("Agency not present \n");
            return new Vector<>();
        }

        Vector<String> vett = new Vector<String>();
        vett.add(ae.getNomeAzienda());
        return vett;
        }

    public void insertAgency(Vector<String> val){
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
        }
        fm.createAgency(val.get(0), val.get(1), val.get(2), val.get(3), Integer.parseInt(val.get(4)), val.get(5));
        fm.exit();
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
        String sql = "SELECT sum(f.budget) FROM FinanziamentoEntity f  WHERE f.progetto= " + selectedProjectID ;


        fm = new FundracingManager();
        if(!fm.isSetup())
            return -1;

        Long stake = fm.singleReturnQuery(Long.class, sql);

        fm.exit();

        if(stake == null)
            return 0;

        return Integer.parseInt(stake.toString());
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
        List<ProgettoEntity> projects = fm.query(ProgettoEntity.class, "SELECT p FROM ProgettoEntity p");

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
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return false;
        }


        ProgettoEntity progetto = fm.singleReturnQuery(ProgettoEntity.class, "SELECT p FROM ProgettoEntity p WHERE p.azienda = '" + agencyName + "' AND p.id = " + projectId );

        fm.exit();
        if(progetto == null)
            return false;

        return true;
    }

    public int myStake(String agencyName, int id_project){
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return -1;
        }

        Long stake = fm.singleReturnQuery(Long.class, "SELECT SUM(f.budget) FROM FinanziamentoEntity f WHERE f.progetto = " + id_project + "AND f.azienda = '" + agencyName + "'");
        if (stake == null)
            stake = new Long( 0);

        fm.exit();

        return Integer.parseInt(stake.toString());
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
        String sql = "DELETE FROM FinanziamentoEntity WHERE progetto = "+ projectId +" and azienda = '"+ agencyName + "'";

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
        fm = new FundracingManager();

        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        FinanziamentoEntity finanziamento = fm.singleReturnQuery(FinanziamentoEntity.class, "SELECT f FROM FinanziamentoEntity f WHERE f.progetto =" + idProgetto + "AND f.azienda = '" + agencyName + "'");

        if(finanziamento == null){
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

        fm.exit();

    }

    public List<Vector<String>> getProject(int idProgetto){
        String sql = "SELECT p FROM ProgettoEntity p WHERE id =" + idProgetto ;

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
            vett.add(p.getAzienda().getNomeAzienda());

            list.add(vett);
        }

        return list;
    }


    public void insertMessage(Vector<String>val) {
        fm = new FundracingManager();
        if( !fm.isSetup() ){
            System.out.print("Impossibile creare il manager del database \n");
            return;
        }

        AziendaEntity aeDest = fm.selectAgency( val.get(1) );
        if(aeDest == null){
            System.out.print( "Impossibile inviare un messaggio a un'azienda non registrata \n");
            return;
        }

        AziendaEntity aeMitt = fm.selectAgency( val.get(0) );
        if(aeMitt == null){
            System.out.print( "Impossibile inviare un messaggio a un'azienda non registrata \n");
            return;
        }
        System.out.println("Questo � l'id "+Integer.parseInt(val.get(2)));
        ProgettoEntity pe = fm.selectProject(Integer.parseInt( val.get(2) ));
        if(pe == null){
            System.out.print( "Impossibile inviare un messaggio a su di un progetto inesistente \n");
            return;
        }
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());

        MessaggioEntity me = fm.createMessage( val.get(3), Integer.parseInt(val.get(4)), date, aeMitt, aeDest, pe );

        fm.exit();

        if(me == null){
            System.out.print("MESSAGE SEND FAILED \n");
        }

    }

    
}
