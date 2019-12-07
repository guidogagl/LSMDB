package lvDbConnect;

import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

public class Connect {
    private Options options = new Options();
    private DB db = null;

    private Boolean isSetup(){
        if(db == null)
            return false;
        return true;
    }
    
    private void createConnection(String str) {
    	options.createIfMissing(true);
        try {
            db =  factory.open( new File(str), options);
        } catch (IOException e) {
            e.printStackTrace();
            db = null;
        }
    }

    public Connect(){
    }
    
    public void close(){
        if(!isSetup()){
            System.out.print("Tentativo di chiusura su un database già chiuso \n");
            return;
        }
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void changeConnection(String str) {
    	this.close();
    	if(str.equals("read"))
    		 createConnection("levelDbStore");
    	else if(str.equals("delete")) 
    		createConnection("levelDbDeleteStore");
    	else if(str.equals("insert"))
    		createConnection("levelDbInsertStore");
    	else if(str.equals("update"))
    		createConnection("levelDbUpdateStore");
    	else
    		System.out.println("Operazione non permessa!");
    }
   
    public void clearEntity(String cache, String entityName) {
    	
    	this.changeConnection(cache);
    	
    	DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName));
        try {
	        while (keyIterator.hasNext()) { 
	        	byte[]row=keyIterator.peekNext().getKey();
	        	String stored_key = asString(row); // key arrangement : employee:$employee_id:$attribute_name = $value
	            String[] keySplit = stored_key.split(":"); // split the key
	            if( keySplit[0].equals(entityName) ){
	                db.delete(row);
	                keyIterator.next();
	            }else
	            	break;
	        }
	        this.close();
        }catch(Exception e ) {
        	 e.printStackTrace();
        }
    }
    
    
	

    public void writeEntity(String cache, String entityName, Vector<String> attributes, Vector<String> values){

    	this.changeConnection(cache);
    	
        WriteBatch batch = db.createWriteBatch();
        String primaryKey = entityName + ":" + values.get(0);

        try {
            for( int i = 1; i < attributes.size() ; i++){
                String key = primaryKey + ":" + attributes.get(i);
                batch.put( bytes(key), bytes(values.get(i)));
            }
            db.write(batch);
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
        	try {
        		batch.close();
        		this.close();
        	}catch(IOException e) {
        		e.printStackTrace();
        	}
        }
    }
    
    public void deleteSingleEntity(String cache, String entityName,String primaryKey) {
    	this.changeConnection(cache);
    	DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName+":"+primaryKey));
        try {
	        while (keyIterator.hasNext()) { 
	        	byte[]row=keyIterator.peekNext().getKey();
	        	String stored_key = asString(row); // key arrangement : employee:$employee_id:$attribute_name = $value
	            String[] keySplit = stored_key.split(":"); // split the key
	            if( keySplit[0].equals(entityName) && keySplit[1].equals(primaryKey) ){
	                db.delete(row);
	                keyIterator.next();
	            }else
	            	break;
	        }
	        this.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public void updateSingleValue(String cache, String entityName,String primaryKey,String attribute,String newValue) {
    	this.changeConnection(cache);
    	DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName+":"+primaryKey+":"+attribute));
        try {
	        while (keyIterator.hasNext()) { 
	        	byte[]row=keyIterator.peekNext().getKey();
	        	String stored_key = asString(row); // key arrangement : employee:$employee_id:$attribute_name = $value
	            String[] keySplit = stored_key.split(":"); // split the key
	            if( keySplit[0].equals(entityName) && keySplit[1].equals(primaryKey) && keySplit[2].equals(attribute)){
	                db.delete(row);
	                db.put(bytes( entityName+":"+primaryKey+":"+attribute),bytes(newValue));
	                keyIterator.next();
	            }else
	            	break;
	        }
	        this.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public String readSingleValue(String cache, String entityName,String primaryKey,String attribute) {
    	this.changeConnection(cache);
    	DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName+":"+primaryKey+":"+attribute));
        try {
	        while (keyIterator.hasNext()) { 
	        	byte[]row=keyIterator.peekNext().getKey();
	        	String stored_key = asString(row); // key arrangement : employee:$employee_id:$attribute_name = $value
	            String[] keySplit = stored_key.split(":"); // split the key
	            if( keySplit[0].equals(entityName) && keySplit[1].equals(primaryKey) && keySplit[2].equals(attribute)){
	            	return asString( db.get( bytes( stored_key )));
	            }else
	            	break;
	        }
	        this.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return new String();
    }

    public List<Vector<String>> readEntity(String cache, String entityName, String primaryKey){
        // effettuiamo uno snapshot per evitare letture inconsistenti a causa dei thread di update
        // del database
    	this.changeConnection(cache);
        ReadOptions ro = new ReadOptions();
        ro.snapshot(db.getSnapshot());

        DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName + ":" + primaryKey ));

        Vector<String> entity = new Vector<String>();
        if( primaryKey != null )
            entity.add(primaryKey); //primo elemento del vector Ã¨ la chiave

        List<Vector<String>> resultSet = new ArrayList<Vector<String>>();
        try {
            while (keyIterator.hasNext()) {
                String stored_key = asString(keyIterator.peekNext().getKey()); // key arrangement : employee:$employee_id:$attribute_name = $value
                String[] keySplit = stored_key.split(":"); // split the key

                if( !keySplit[0].equals(entityName) ){
                    
                    break;
                }

                if( keySplit[1].equals( primaryKey )  )
                {
                    entity.add( asString( db.get( bytes( stored_key ) ) ) );
                    keyIterator.next();
                } 
                else //secondo me qui c'è un problema,se siamo sulla entità corretta ma id sbagliato,usciamo senza inseririre nulla -->
                {
                	resultSet.add(entity);
                	break;
                }

            }
            resultSet.add(entity);

            if( resultSet.isEmpty() && (entity.size() > 1) ) //--> e qui abbiamo resultSet empty e entityVuoto->in questo caso ritorno un resultSet vuoto
                resultSet.add(entity);

        }catch(Exception e) {
        	e.printStackTrace();
        }
    	finally {
    		try {
	            keyIterator.close();
	            ro.snapshot().close();
	            this.close();
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
        }

        return resultSet;
    }
    
    
    public List<Vector<String>> readAllEntity(String cache, String entityName){
    	this.changeConnection(cache);
    	ReadOptions ro = new ReadOptions();
        ro.snapshot(db.getSnapshot());

        DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName ));
        Vector<String> entity=new Vector<String>();
        String primaryKey="";
        List<Vector<String>> resultSet = new ArrayList<Vector<String>>();
        try {
            while (keyIterator.hasNext()) 
            {
            	String stored_key = asString(keyIterator.peekNext().getKey()); // key arrangement : employee:$employee_id:$attribute_name = $value
                String[] keySplit = stored_key.split(":"); // split the key
                if( !keySplit[0].equals(entityName) ) //se non hai la classe che cerchi
                {
                    break;
                }
               
                else if(keySplit[0].equals(entityName)&&!keySplit[1].equals(primaryKey)) { //siamo su un nuova chiave
                	if(!entity.isEmpty())
                		resultSet.add(entity);
                	entity=new Vector<String>();
                	primaryKey=keySplit[1];
                	entity.add(primaryKey);
                	entity.add( asString( db.get( bytes( stored_key ) ) ) );
                }
                else { //stessa chiave,nuovo valore
                	entity.add( asString( db.get( bytes( stored_key ) ) ) );
                
                }
                keyIterator.next(); 	
            }
            resultSet.add(entity);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    	finally {
    		try {
	            keyIterator.close();
	            ro.snapshot().close();
	            this.close();
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
        }

        return resultSet;
    } 
    



}
