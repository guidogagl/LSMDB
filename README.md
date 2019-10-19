# LSMDB


Ciao questa è la wiki del progetto di LSMDB

The TableMessage class has the purpose of constructing the table that will be shown on the application interface.

Private attributes:

.messagesList: ObservableList<RowTableMessage>
This attribute represents a list containing all the rows present in the table. 

Public methods:

.TableMessage() - class constructor, inizializes the table columns and then adds them to the table
.updateMessages(List<RowTableMessage> messages):void - Thisfunctionclears all the content of the table and replace it with
the informations stored in the List passed as argument.

