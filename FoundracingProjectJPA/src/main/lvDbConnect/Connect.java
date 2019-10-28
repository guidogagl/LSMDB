package lvDbConnect;

import org.iq80.leveldb.*;

import javax.swing.text.html.parser.Entity;

import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Connect {
    private Options options = new Options();
    private DB db = null;

    public Boolean isSetup(){
        if(db == null)
            return false;
        return true;
    }

    public Connect(){
        options.createIfMissing(true);
        try {
            db =  factory.open( new File("levelDbStore"), options);
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


    public void writeEntity(String entityName, Vector<String> attributes, Vector<String> values){
        WriteBatch batch = db.createWriteBatch();
        // Primo vector<String>= EntityName:$entityNameID;
        // key arrangement EntityName:$entityNameId:NameAttribute = $value
<<<<<<< HEAD
        String primaryKey = key_value.get(1).firstElement() + ":" + key_value.get(1).lastElement() + ":";
        //String primaryKey= EntityName:$entityNameId
        Boolean next_step = true;
        try {
            for( Vector<String> kv : key_value ) {
                if(next_step){
                    next_step = false;
                    continue;
                }
                //NameAttribute=$value
                String key = primaryKey + kv.get(1);
                batch.put(bytes(key), bytes(kv.get(1)));
                
=======

        String primaryKey = entityName + ":" + attributes.get(0) + ":";

        Boolean next_step = true;
        try {
            for( int i = 1; i < attributes.size(); i++ ) {
                String key = primaryKey + attributes.get(i);
                batch.put(bytes(key), bytes(values.get(i)));
>>>>>>> 5e978378d2258437813b1e0feb4f5077dddcff08
            }

            db.write(batch);
        } finally {
            // Make sure you close the batch to avoid resource leaks.
            batch.close();
        }
    }

    public List<Vector<String>> readEntity(String entityName, String primaryKey){ //qui primaryKey=1

        // se primaryKey è null leggiamo tutte le entità con qualsiasi chiave
        if( primaryKey != null ) 
            primaryKey = ":" + primaryKey; //:1

        // effettuiamo uno snapshot per evitare letture inconsistenti a causa dei thread di update
        // del database
        ReadOptions ro = new ReadOptions();
        ro.snapshot(db.getSnapshot());

        DBIterator keyIterator = db.iterator();
        keyIterator.seek(bytes( entityName + primaryKey )); // moves the iterator to the keys starting with "employee:primaryKey"
        //employee:5->ma questo non c'�
        //employee
        Vector<String> entity = new Vector<String>();
        if( primaryKey != null )
            entity.add(primaryKey); //primo elemento entity=:primarykey

        List<Vector<String>> resultSet = new ArrayList<Vector<String>>();
        try {
            while (keyIterator.hasNext()) {

                String stored_key = asString(keyIterator.peekNext().getKey()); // key arrangement : employee:$employee_id:$attribute_name = $value
                String[] keySplit = stored_key.split(":"); // split the key
                //!keySplit[1].equals( primaryKey ) ?
                if( !keySplit[1].equals( primaryKey ) || !keySplit[0].equals(entityName) ){ // nuova entità da registrare nella lista
                    if( primaryKey != null ) // controllo non sia la prima iterazione
                        resultSet.add( entity ); // se non lo è allora ho registrato una nuova entità

                    // aggiorno la chiave primaria
                    primaryKey = keySplit[1];

                    entity = new Vector<String>();
                    entity.add( primaryKey );
                }

                if (!keySplit[0].equals(entityName))
                    break;

                entity.add( asString( db.get( bytes( stored_key ) ) ) ); // aggiungo il valore dell'attributo al vettore

                keyIterator.next();

                if(!keyIterator.hasNext()) // se non ho più next ho finito
                    resultSet.add(entity);
            }
        } finally {
            keyIterator.close();
            ro.snapshot().close();
        }

        return resultSet;
    }
    public List<Vector<String>> readEntities(String entityName){
        return readEntity(entityName, null);
    }


}
