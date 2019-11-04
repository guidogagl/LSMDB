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
    private File file=null;

    public Boolean isSetup(){
        if(db == null)
            return false;
        return true;
    }

    public Connect(){
    	options.createIfMissing(true);
        try {
        	file=new File("levelDbStore");
            db =  factory.open(file, options);
        } catch (IOException e) {
            e.printStackTrace();
            db = null;
        }
    }
    public void close(){
        if(!isSetup()){
            System.out.print("Tentativo di connessione su database chiuso \n");
            return;
        }
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void repairConnection() {
    	this.close();
    	options.createIfMissing(true);
        try {
        	file=new File("levelDbStore");
            db =  factory.open(file, options);
        } catch (IOException e) {
            e.printStackTrace();
            db = null;
        }
    }
    
    
   
    public void clearEntity(String entityName) {
    	//createConnectionIfMissing();
    	if(!file.canRead()||db.iterator()==null)
    		repairConnection();
    	DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName));
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
    }
	

    public void writeEntity(String entityName, Vector<String> attributes, Vector<String> values){
    	//createConnectionIfMissing();
    	if(!file.canWrite()||db.iterator()==null)
    		repairConnection();
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
        	}catch(IOException e) {
        		e.printStackTrace();
        	}
        }
    }

    public List<Vector<String>> readEntity(String entityName, String primaryKey){
        // effettuiamo uno snapshot per evitare letture inconsistenti a causa dei thread di update
        // del database
    	//createConnectionIfMissing();
    	if(!file.canRead()||db.iterator()==null)
    		repairConnection();
        ReadOptions ro = new ReadOptions();
        ro.snapshot(db.getSnapshot());

        DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName + ":" + primaryKey ));

        Vector<String> entity = new Vector<String>();
        if( primaryKey != null )
            entity.add(primaryKey); //primo elemento del vector è la chiave

        List<Vector<String>> resultSet = new ArrayList<Vector<String>>();
        try {
            while (keyIterator.hasNext()) {
                String stored_key = asString(keyIterator.peekNext().getKey()); // key arrangement : employee:$employee_id:$attribute_name = $value
                String[] keySplit = stored_key.split(":"); // split the key

                if( !keySplit[0].equals(entityName) ){
                    resultSet.add(entity);
                    break;
                }

                if( keySplit[1].equals( primaryKey )  ){
                    entity.add( asString( db.get( bytes( stored_key ) ) ) );
                    keyIterator.next();
                } else
                    break;

            }

            if( resultSet.isEmpty() && (entity.size() > 1) )
                resultSet.add(entity);

        }catch(Exception e) {
        	e.printStackTrace();
        }
    	finally {
    		try {
	            keyIterator.close();
	            ro.snapshot().close();
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
        }

        return resultSet;
    }


}
