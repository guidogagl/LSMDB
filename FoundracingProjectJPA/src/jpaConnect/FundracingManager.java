package jpaConnect;

import application.Fundracing;
import jpaEntities.AziendaEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GenerationType;
import javax.persistence.Persistence;

public class FundracingManager {
    private EntityManagerFactory factory = null;
    private FundracingEntityManager fem = null;

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

    public void exit(){
        if (factory == null){
            System.out.print("Tentativo di exit su factory non costruita \n");
            return;
        }

        factory.close();
    }

}
