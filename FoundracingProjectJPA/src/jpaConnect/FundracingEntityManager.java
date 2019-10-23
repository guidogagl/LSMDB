package jpaConnect;

import jpaEntities.AziendaEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

public class FundracingEntityManager {
    private EntityManager em = null;

    public FundracingEntityManager(EntityManagerFactory emf){
        try{
            em = emf.createEntityManager();
        }
        catch(Exception ex){
            ex.printStackTrace();
            em = null;
        }
    }

    public void close(){
        if( em != null ) em.close();
    }

    public Boolean create(Object o){
        if( o == null || em == null)
            return false;

        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Boolean update(Object o){
        if( o == null || em == null )
            return false;

        try{
            em.getTransaction().begin();
            em.merge(o);
            em.getTransaction().commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Object read(Class<?> type, String id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            Object o = em.find(type.getClass(), id);
            if (o == null)
                return new Object();
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Object read(Class<?> type, int id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            Object o = em.find(type.getClass(), id);
            if( o == null)
                return new Object();
            return o;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean delete(Class<?> type, int id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            Object old = em.getReference(type.getClass(), id);
            em.remove( old );
            em.getTransaction().commit();
            return true;
        }catch(EntityNotFoundException ex){
            return false;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean delete(Class<?> type, String id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            Object old = em.getReference(type.getClass(), id);
            em.remove( old );
            em.getTransaction().commit();
            return true;
        }
        catch(EntityNotFoundException ex){
            return false;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
