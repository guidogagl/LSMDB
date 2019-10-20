package esercizio1;

import java.awt.Component;

import javafx.beans.property.*;
import javafx.scene.image.*;

public class RowTableMessage extends Component{
	
	private SimpleIntegerProperty id; //id messaggio
	private SimpleIntegerProperty id_project;
	private SimpleStringProperty data;
	private SimpleStringProperty mittente;
	private SimpleStringProperty destinatario;
	//private SimpleStringProperty logo_mittente;
	private SimpleStringProperty messaggio;
	private SimpleIntegerProperty stake;
	
	public RowTableMessage(int id, int id_project, String data, String mittente,String destinatario, /*String logo_mittente,*/ String messaggio,
			int stake) {
		
		this.id = new SimpleIntegerProperty(id);
		this.id_project = new SimpleIntegerProperty(id_project);
		this.data = new SimpleStringProperty(data);
		this.mittente = new SimpleStringProperty(mittente);
		this.destinatario = new SimpleStringProperty(destinatario);
		//this.logo_mittente = new SimpleStringProperty(logo_mittente);
		this.messaggio = new SimpleStringProperty(messaggio);
		this.stake = new SimpleIntegerProperty(stake);
	}
	
	
	public int getId() {return id.get();}
	public int getId_project() {return id_project.get();}
	public String getData() {return data.get();}
	public String getMittente() {return mittente.get();}
	public String getDestinatario() {return destinatario.get();}
	//public String getLogo_mittente() {return logo_mittente.get();}
	public String getMessaggio() {return messaggio.get();}
	public int getStake() {return stake.get();}

}
