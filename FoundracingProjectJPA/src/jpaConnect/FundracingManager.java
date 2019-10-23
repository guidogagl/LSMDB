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

    public Boolean createAgency(String nomeAzienda,
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
        if(fem == null)
            return false;

        Boolean ret = fem.create(newAzienda);

        fem.close();

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
