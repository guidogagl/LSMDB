package jpaConnect;

import jpaEntities.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

public class FundracingManager {
    private EntityManagerFactory factory = null;
    private FundracingEntityManager fem = null;

    private <T> T select(Class<T> classTable, int id){
        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        T ret = fem.read(classTable, id);

        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;

    }

    private <T> T update(Class<T> classTable, int id, T newEntity){
        T ret = select(classTable, id);
        if(ret == null)
            return null;

        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        ret = fem.update(newEntity);
        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");
        return ret;
    }

    private <T> T delete(Class<T> classTable, int id){
        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        T ret = fem.delete(classTable, id);

        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    private <T> T create(T entity){
        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        T ret = fem.create(entity);

        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;

    }

    private void setup() {
        try{
            factory = Persistence.createEntityManagerFactory("esercizio1");
        }
        catch(Exception e){
        e.printStackTrace();
        }
    }

    
    public FundracingManager(){
        this.setup();
    }

    public Boolean isSetup(){
        if(factory == null)
            return false;
        else
            return true;
    }

    public <T> List<T> query(Class<T> tableClass, String sql ){
        fem = new FundracingEntityManager(factory);
        if(!fem.isSetup())
            return null;

        List<T> res = fem.query( tableClass, sql);

        fem.close();

        return res;
    }

    public int executeUpdateQuery( String sql ){
        fem = new FundracingEntityManager(factory);
        if(!fem.isSetup())
            return -1;
        
        int res = fem.queryExecuteQuery( sql);

        fem.close();

        if( res < 0 )
            System.out.print("DELETE OR UPDATE FAILED \n");

        return res;
    }

    public <T> T singleReturnQuery(Class<T> tableClass, String sql ){
        fem = new FundracingEntityManager(factory);
        if(!fem.isSetup())
            return null;

        T res = fem.singleResultQuery( tableClass, sql );

        fem.close();

        return res;
    }

    public AziendaEntity createAgency(String nomeAzienda,
                        String urlLogo,
                        String urlSito,
                        String indirizzo,
                        Integer cap,
                        String password ){

        AziendaEntity newAzienda = new AziendaEntity();
        newAzienda.setNomeAzienda(nomeAzienda);
        newAzienda.setUrlLogo(urlLogo);
        newAzienda.setUrlSito(urlSito);
        newAzienda.setIndirizzo(indirizzo);
        newAzienda.setCap(cap);
        newAzienda.setPassword(password);

        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        AziendaEntity ret = fem.create(newAzienda);

        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    public AziendaEntity deleteAgency(String nomeAzienda){
        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        AziendaEntity ret = fem.delete(AziendaEntity.class, nomeAzienda);

        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    public AziendaEntity selectAgency(String nomeAzienda){
        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        AziendaEntity ret = fem.read(AziendaEntity.class, nomeAzienda);

        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");

        return ret;
    }

    public AziendaEntity updateAgency(String nomeAzienda, AziendaEntity newAzienda){
        AziendaEntity ret = selectAgency(nomeAzienda);
        if(ret == null)
            return null;

        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        ret = fem.update(newAzienda);
        fem.close();

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
        if (factory == null){
            System.out.print("Tentativo di exit su factory non costruita \n");
            return;
        }

        factory.close();
    }

}
