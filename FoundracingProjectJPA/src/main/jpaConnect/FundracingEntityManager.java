package jpaConnect;

import javax.persistence.*;
import javax.transaction.Transactional;

import java.util.List;
import java.util.logging.Level;

public class FundracingEntityManager {
    private EntityManager em = null;
    private EntityManagerFactory emf=null;


    public FundracingEntityManager(EntityManagerFactory emf){
        try{
        	// java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);//LEVEL.SEVERE altrimenti,o ON
            em = emf.createEntityManager();
            this.emf=emf;
        }
        catch(Exception ex){
            ex.printStackTrace();
            em = null;
       
        }
        
    }

    public Boolean isSetup(){
        return em.isOpen();
    }

    public void close(){
        if(!em.isOpen() ) 
        {
        	em.close();
        }
    }

    public <T> List<T>  query( Class<T> tableClass, String sql) {
        if (!em.isOpen()) {
        	em = emf.createEntityManager();
        }
        TypedQuery<T> query = em.createQuery( sql, tableClass );

        try {
            List<T> results = query.getResultList(); 
            em.close();
            return results;
        } catch(NoResultException e){
            return null;
        }
    }

    public <T> T  singleResultQuery( Class<T> tableClass, String sql) {
        if (!em.isOpen()) {
            em=emf.createEntityManager();
        }

        TypedQuery<T> query = em.createQuery( sql, tableClass );

        try 
        {
            T results = query.getSingleResult();
            em.close();
            return results;
        } catch(NoResultException e){
            return null;
        }
    }
    
    public int queryExecuteQuery(String sql){
        if (!em.isOpen()) {
            em=emf.createEntityManager();
        }
        
        int results = 0;
        
        try {
        	em.getTransaction().begin();
            results = em.createQuery( sql ).executeUpdate();
            
        } catch(NoResultException e){
            System.out.println("La query ha dato risultato vuoto");
        }finally {
        	em.getTransaction().commit();
        	em.close();
            return results;
        }
    }

    public <T> T create( T entity ){
        if( entity == null )
            return null;
        if(!em.isOpen())
        	em=emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();    
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }finally {
        	em.close();
        	return entity;
        }
    }

    public <T> T update(T entity){
        if( entity == null )
            return null;
        if(!em.isOpen())
        	em=emf.createEntityManager();
        try{
            em.getTransaction().begin();
            T result  = em.merge(entity);
            em.getTransaction().commit();
            em.close();
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public <T> T read(Class<T> tableClass, String id) {
       if(!em.isOpen())
    	   em=emf.createEntityManager();

        T result = null;

        try {
            em.getTransaction().begin();
            result  =  em.find(tableClass, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.getTransaction().commit();
            em.close();
            
        }
        return result;
    }

    public <T> T read(Class<T> tableClass, int id) {
        while(!em.isOpen())
        	em=emf.createEntityManager();
        
        T result = null;

        try {
            em.getTransaction().begin();
            result  =  em.find(tableClass, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.getTransaction().commit();
            em.close();
            return result;
        }
    }

    public <T> T delete(Class<T> type, int id) {
        if(!em.isOpen())
        	em=emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T old = em.getReference(type, id);
            em.remove( old );
            em.getTransaction().commit();
            em.close();
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
        if(!em.isOpen())
        	em=emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T old = em.getReference(type, id);
            em.remove( old );
            em.getTransaction().commit();
            em.close();
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
