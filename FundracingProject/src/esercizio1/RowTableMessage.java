package esercizio1;

import java.awt.Component;

import javafx.beans.property.*;
import javafx.scene.image.*;

public class RowTableMessage extends Component{
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty data;
	private SimpleStringProperty mittente;
	private SimpleStringProperty messaggio;
	private SimpleIntegerProperty stake;
	
	public RowTableMessage(int id, String data, String mittente, String messaggio,
			int stake) {
		
		this.id = new SimpleIntegerProperty(id);
		this.data = new SimpleStringProperty(data);
		this.mittente = new SimpleStringProperty(mittente);
		this.messaggio = new SimpleStringProperty(messaggio);
		this.stake = new SimpleIntegerProperty(stake);
	}
	
	
	public int getId() {return id.get();}
	public String getData() {return data.get();}
	public String getMittente() {return mittente.get();}
	public String getMessaggio() {return messaggio.get();}
	public int getStake() {return stake.get();}

}
