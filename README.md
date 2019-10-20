# LSMDB


Ciao questa è la wiki del progetto di LSMDB

TableMessage

The TableMessage class has the purpose of constructing the table that will be shown on the application interface.

Private attributes:

.messagesList: ObservableList<RowTableMessage> - This attribute represents a list containing all the rows present in the table. 

Public methods:

.TableMessage() - class constructor, inizializes the table columns and then adds them to the table
.updateMessages(List<RowTableMessage> messages):void - This function clears all the content of the table and replace it with
the informations stored in the List passed as argument.


RowTableMessage

This class is the data representation of the rows in the message table.

Private attributes:

.id:SimpleIntegerProperty
.id_project:SimpleIntegerProperty
.data:SimpleStringProperty
.mittente:SimpleStringProperty
.destinatario:SimpleStringProperty
.messaggio:SimpleStringProperty
.stake:SimpleIntegerProperty

Each attribute is a column in the table.

Public methods:

.RowTableMessage(int id, int id_project, String data, String mittente, String destinatario, String messaggio,
	int stake) - class constructor, it converts the attribute passed as argument in SimpleItsProperty, where Its is the type of the argument
.getId():int - returns the private field 'id' of the class
.getId_project():int - returns the private field 'id_project' of the class
.getData():String - returns the private field 'data' of the class
.getMittente():String - returns the private field 'mittente' of the class
.getDestinatario():String - returns the private field 'destinatario' of the class
.getMessaggio():String - returns the private field 'messaggio' of the class
.getStake():int - returns the private field 'stake' of the class


Fundracing

The following changes have been made to this class

Private attributes:

.accept:Button
.refuse:Button
.l_description_message:Label
.description_message:TextArea
.stake_message:TextField
.l_stake_message:Label
.project_message:TextField
.l_project_message:Label
.send:Button
.choice_agency:ChoiceBox
.message_receiver:Label
.gm:GestoreMessaggi
.l_password:Label
.l_agencyName:Label
.messages_received:Label
.l_stake:Label
.l_description:Label
.l_project_name:Label
.l_total_budget:Label


Public methods:
.selectTableRow():void - the function uses a handle, able to detect the selection of a row table, in order to return the information
 contained in that row
.selectTableMessages():void - the function uses a handle, able to detect the selection of a row table, in order to return the information
 contained in that row
.inizializeChoiceBox():void - the function initializes the drop-down menu with the list of companies in the database











