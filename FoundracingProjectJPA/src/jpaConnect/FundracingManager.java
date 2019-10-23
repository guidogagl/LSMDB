package jpaConnect;

import application.Fundracing;
import jpaEntities.AziendaEntity;
import jpaEntities.FinanziamentoEntity;
import jpaEntities.MessaggioEntity;
import jpaEntities.ProgettoEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GenerationType;
import javax.persistence.Persistence;
import java.sql.Date;

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

    private <T> T update(Class<T> classTable, int id){
        T ret = select(classTable, id);
        if(ret == null)
            return null;

        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        ret = fem.update(ret);
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

    private void setup(){
        factory = Persistence.createEntityManagerFactory("esercizio1");
    }

    public FundracingManager(){
        this.setup();
    }

    public AziendaEntity createAgency(String nomeAzienda,
                        String urlLogo,
                        String urlSito,
                        String indirizzo,
                        Integer cap,
                        String password ){
        AziendaEntity newAzienda = new AziendaEntity(   nomeAzienda,
                                                    urlLogo,
                                                    urlSito,
                                                    indirizzo,
                                                    cap,
                                                    password);
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

    public AziendaEntity updateAgency(String nomeAzienda){
        AziendaEntity ret = selectAgency(nomeAzienda);
        if(ret == null)
            return null;

        fem = new FundracingEntityManager(factory);
        if(fem == null){
            System.out.print("Impossibile costruire connessione con il database");
            return null;
        }

        ret = fem.update(ret);
        fem.close();

        if (ret == null)
            System.out.print("Elemento non trovato nel database");
        return ret;
    }

    public FinanziamentoEntity createFinanziamento(int id,
                                                   Integer budget,
                                                   AziendaEntity azienda,
                                                   ProgettoEntity progetto){
        FinanziamentoEntity newFinanziamento = new FinanziamentoEntity(id, budget, azienda, progetto);
        return create(newFinanziamento);
    }

    public FinanziamentoEntity deleteFinanziamento(int id){
        return delete(FinanziamentoEntity.class, id);
    }

    public FinanziamentoEntity updateFinanziamento(int id){
        return update(FinanziamentoEntity.class, id);
    }

    public FinanziamentoEntity selectFinanziamento(int id){
        return select(FinanziamentoEntity.class, id);
    }


    public MessaggioEntity createMessage(int _id,
                                         String _testo,
                                         int _stake,
                                         Date _data,
                                         AziendaEntity _azienda,
                                         ProgettoEntity _progetto){
        MessaggioEntity newMessage = new MessaggioEntity(_id,
                                                     _testo,
                                                     _stake,
                                                     _data,
                                                     _azienda,
                                                     _progetto);
        return create(newMessage);
    }

    public MessaggioEntity deleteMessaggio(int id){
        return delete(MessaggioEntity.class, id);
    }

    public MessaggioEntity updateMessaggio(int id){
        return update(MessaggioEntity.class, id);
    }

    public MessaggioEntity selectMessaggio(int id){
        return select(MessaggioEntity.class, id);
    }

    public ProgettoEntity createProject(int id,
                                        String nome,
                                        Integer budget,
                                        String descrizione,
                                        AziendaEntity aziendaOwner){
        ProgettoEntity newProject = new ProgettoEntity(id,
                                                        nome,
                                                        budget,
                                                        descrizione,
                                                        aziendaOwner);
        return create(newProject);
    }

    public ProgettoEntity deleteProject(int id){
        return delete(ProgettoEntity.class, id);
    }

    public ProgettoEntity updateProject(int id){
        return update(ProgettoEntity.class, id);
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
