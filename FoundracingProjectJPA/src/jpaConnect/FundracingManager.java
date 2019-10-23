package jpaConnect;

import application.Fundracing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FundracingManager {
    private EntityManagerFactory factory = null;
    private EntityManager entityManager = null;

    private void setup(){
        factory = Persistence.createEntityManagerFactory("esercizio1");
    }

    public FundracingManager(){
        this.setup();
    }

    public void exit(){
        if (factory == null){
            System.out.print("Tentativo di exit su factory non costruita \n");
            return;
        }

        factory.close();
    }

}
