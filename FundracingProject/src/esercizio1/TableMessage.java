package esercizio1;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;

public class TableMessage extends TableView<RowTableMessage> {

	private ObservableList<RowTableMessage> messagesList;
	//rivate ObservableList<LogoImage> logoList;
	
	public TableMessage() {
		
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		/*TableColumn Image = new TableColumn<>("Image");
		Image.setCellValueFactory(new PropertyValueFactory<>("logo_mittente"));
		Image.setStyle("-fx-alignment: CENTER;");*/
    	
    	TableColumn Mittente = new TableColumn("Azienda Mittente"); 
    	Mittente.setCellValueFactory(new PropertyValueFactory<>("mittente")); 
    	Mittente.setStyle("-fx-alignment: CENTER;");
    	
    	TableColumn Data = new TableColumn("Data"); 
    	Data.setCellValueFactory(new PropertyValueFactory<>("data")); 
    	Data.setStyle("-fx-alignment: CENTER;");
    	
    	TableColumn Messaggio = new TableColumn("Messaggio"); 
    	Messaggio.setCellValueFactory(new PropertyValueFactory<>("messaggio")); 
    	Messaggio.setStyle("-fx-alignment: CENTER;");
    	
    	TableColumn Stake = new TableColumn("Stake"); 
    	Stake.setCellValueFactory(new PropertyValueFactory<>("stake")); 
    	Stake.setStyle("-fx-alignment: CENTER;");
    	
    	messagesList = FXCollections.observableArrayList();
    	setItems(messagesList);
        getColumns().addAll(Mittente, Data, Messaggio, Stake);
	}
	
	public void updateMessages(List<RowTableMessage> messages) {
		messagesList.clear();
		messagesList.addAll(messages);
    	/*messagessList = FXCollections.observableArrayList(projects);
    	messagessList.addAll(projects);*/
    }
}
