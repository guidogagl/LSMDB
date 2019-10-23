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

    public <T> T create( T entity ){
        if( entity == null || em == null)
            return null;

        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T update(T entity){
        if( entity == null || em == null )
            return null;

        try{
            em.getTransaction().begin();
            T result  = em.merge(entity);
            em.getTransaction().commit();
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T read(Class<T> tableClass, String id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            T result  =  em.find(tableClass, id);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T read(Class<T> tableClass, int id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            T result  =  em.find(tableClass, id);
            return null;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T delete(Class<T> type, int id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            T old = em.getReference(type, id);
            em.remove( old );
            em.getTransaction().commit();
            return old;
        }catch(EntityNotFoundException ex){
            return null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T delete(Class<T> type, String id) {
        if (em == null)
            return null;
        try {
            em.getTransaction().begin();
            T old = em.getReference(type, id);
            em.remove( old );
            em.getTransaction().commit();
            return old;
        }
        catch(EntityNotFoundException ex){
            return null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
