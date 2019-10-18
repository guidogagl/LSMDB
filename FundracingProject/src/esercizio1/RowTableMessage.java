package esercizio1;

import javafx.beans.property.*;
import javafx.scene.image.*;

public class RowTableMessage {
	
	//private SimpleObjectProperty image;
	private SimpleStringProperty data;
	private SimpleStringProperty mittente;
	private SimpleStringProperty logo_mittente;
	private SimpleStringProperty messaggio;
	private SimpleIntegerProperty stake;
	
	public RowTableMessage(/*ImageView image, */String data, String mittente, String logo_mittente, String messaggio,
			int stake) {
		
		//this.image = new SimpleObjectProperty(image);
		this.data = new SimpleStringProperty(data);
		this.mittente = new SimpleStringProperty(mittente);
		this.logo_mittente = new SimpleStringProperty(logo_mittente);
		this.messaggio = new SimpleStringProperty(messaggio);
		this.stake = new SimpleIntegerProperty(stake);
	}
	
	
	//public ImageView getImage() {return (ImageView)image.get();}
	public String getData() {return data.get();}
	public String getMittente() {return mittente.get();}
	public String getLogo_mittente() {return logo_mittente.get();}
	public String getMessaggio() {return messaggio.get();}
	public int getStake() {return stake.get();}

}
