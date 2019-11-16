package jpaConnect;

import jpaEntities.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

public class FundracingManager {
    private EntityManagerFactory factory = null;
    private FundracingEntityManager fem = null;
    

    private void setup() 
    {
        try
        {
            factory = Persistence.createEntityManagerFactory("esercizio1");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        fem = new FundracingEntityManager(factory);
        
    }

    private <T> T select(Class<T> classTable, int id){
         if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }
        T ret = fem.read(classTable, id);

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;

    }

    private <T> T update(Class<T> classTable, int id, T newEntity){
        T ret = select(classTable, id);
        if(ret == null)
            return null;

        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        ret = fem.update(newEntity);
        
        if (ret == null)
            System.out.print("Elemento non trovato nel database");
        return ret;
    }

    private <T> T delete(Class<T> classTable, int id){
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        T ret = fem.delete(classTable, id);

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    private <T> T create(T entity){
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        T ret = fem.create(entity);

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;

    }

    
    public FundracingManager(){
        this.setup();
    }

    public Boolean isSetup()
    {
        if(factory == null||fem==null)
            return false;
        else
            return true;
    }

    public <T> List<T> query(Class<T> tableClass, String sql ){
    	if(factory == null||fem==null)
            return null;

        List<T> res = fem.query( tableClass, sql);

        return res;
    }

    public int executeUpdateQuery( String sql ){
    	if(factory == null||fem==null)
            return -1;
        
        int res = fem.queryExecuteQuery( sql);

        if( res < 0 )
            System.out.print("DELETE OR UPDATE FAILED \n");

        return res;
    }

    public <T> T singleReturnQuery(Class<T> tableClass, String sql ){
    	if(factory == null||fem==null)
            return null;

        T res = fem.singleResultQuery( tableClass, sql );

        return res;
    }

    public AziendaEntity createAgency(String nomeAzienda,
                        String urlLogo,
                        String urlSito,
                        String indirizzo,
                        Integer cap,
                        String password )
    {

        AziendaEntity newAzienda = new AziendaEntity();
        newAzienda.setNomeAzienda(nomeAzienda);
        newAzienda.setUrlLogo(urlLogo);
        newAzienda.setUrlSito(urlSito);
        newAzienda.setIndirizzo(indirizzo);
        newAzienda.setCap(cap);
        newAzienda.setPassword(password);

       if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        AziendaEntity ret = fem.create(newAzienda);

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    public AziendaEntity deleteAgency(String nomeAzienda){
    	if(factory == null||fem==null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        AziendaEntity ret = fem.delete(AziendaEntity.class, nomeAzienda);

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    public AziendaEntity selectAgency(String nomeAzienda){
    	if(factory == null||fem==null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        AziendaEntity ret = fem.read(AziendaEntity.class, nomeAzienda);

       
        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    public AziendaEntity updateAgency(String nomeAzienda, AziendaEntity newAzienda){
        AziendaEntity ret = selectAgency(nomeAzienda);
        if(ret == null)
            return null;

        if(factory == null||fem==null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        ret = fem.update(newAzienda);
       
        if (ret == null)
            System.out.print("Elemento non trovato nel database");
        return ret;
    }

    public FinanziamentoEntity createFinanziamento(
                                                   Integer budget,
                                                   AziendaEntity azienda,
                                                   ProgettoEntity progetto){

        FinanziamentoEntity newFinanziamento = new FinanziamentoEntity();

        newFinanziamento.setBudget(budget);
        newFinanziamento.setAzienda(azienda);
        newFinanziamento.setProgetto(progetto);

        return create(newFinanziamento);
    }

    public FinanziamentoEntity deleteFinanziamento(int id){
        return delete(FinanziamentoEntity.class, id);
    }

    public FinanziamentoEntity updateFinanziamento(int id, FinanziamentoEntity newFinanziamento){
        return update(FinanziamentoEntity.class, id, newFinanziamento);
    }

    public FinanziamentoEntity selectFinanziamento(int id){
        return select(FinanziamentoEntity.class, id);
    }


    public MessaggioEntity createMessage(String _testo,
                                         int _stake,
                                         Date _data,
                                         AziendaEntity _mittente,
                                         AziendaEntity _destinatario,
                                         ProgettoEntity _progetto){

        MessaggioEntity newMessage = new MessaggioEntity();
        newMessage.setTesto(_testo);
        newMessage.setStake(_stake);
        newMessage.setData(_data);
        newMessage.setMittente(_mittente);
        newMessage.setDestinatario(_destinatario);
        newMessage.setProgetto(_progetto);

        return create(newMessage);
    }

    public MessaggioEntity deleteMessaggio(int id){
        return delete(MessaggioEntity.class, id);
    }

    public MessaggioEntity updateMessaggio(int id, MessaggioEntity newMessaggio){
        return update(MessaggioEntity.class, id, newMessaggio);
    }

    public MessaggioEntity selectMessaggio(int id){
        return select(MessaggioEntity.class, id);
    }

    public ProgettoEntity createProject(String nome,
                                        Integer budget,
                                        String descrizione,
                                        AziendaEntity azienda){

        ProgettoEntity newProject = new ProgettoEntity();
        newProject.setNome(nome);
        newProject.setBudget(budget);
        newProject.setDescrizione(descrizione);
        newProject.setAzienda(azienda);

        return create(newProject);
    }

    public ProgettoEntity deleteProject(int id){
        return delete(ProgettoEntity.class, id);
    }

    public ProgettoEntity updateProject(int id, ProgettoEntity newProgetto){
        return update(ProgettoEntity.class, id, newProgetto);
    }

    public ProgettoEntity selectProject(int id){
        return select(ProgettoEntity.class, id);
    }

    public void exit(){
    	if(fem.isSetup())
    		fem.close();
        if (factory == null){
            System.out.print("Tentativo di exit su factory non costruita \n");
            return;
        }
        factory.close();
    }

}
