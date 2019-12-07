package applicationMiddle;

import java.util.List;
import java.util.Vector;


public abstract interface wrapperDbs {
	
	
	public abstract List<RowTableProjects> getProjectsWithoutStake();
	
	public abstract Vector<String> getAgency(String agencyName,String password);
	
	public abstract void getLock();
	
	public abstract void freeLock();
	
	public abstract void setAggiornamentoFatto(boolean b);
	
	
	public abstract boolean getAggiornamentoFatto();
	
	public abstract void deleteMessage(int i);
	
	public abstract List<RowTableMessage> getMessages(String agencyName);
	
	public abstract void updateStake(int selectedStake, String agencyName, int selectedProjectId, boolean add) ;
	
	public abstract List<RowTableProjects> getProjects(String agencyName);
	
	public abstract void insertProject(Vector<String>vector);
	
	public abstract boolean iAmOwner(int selectedProjectId, String agencyName);
	
	public abstract List<String> getListAgency();
	
	public abstract void deleteMyStake(int selectedProjectId, String agencyName);
	
	public abstract void deleteProject(int selectedProjectId);
	
	public abstract Boolean isMyStake(String agencyName, int selectedProjectId) ;
	
	public abstract int getSommaStakes(int selectedProjectID);
	
	public abstract List<Vector<String>> getProject(int idProgetto);
	
	public abstract void insertMessage(Vector<String>vector);
	
	public abstract void close();
	
	public abstract String getDescriptionProject(int selectedProjectID);
	
	public abstract String getDescriptionMessage(int selectedMessageID);
	
	public  abstract Vector<String> getAgency(String agencyName);
	
	public abstract void insertAgency(Vector<String> val);
	
	public abstract void clearCache(String name);
	
	public abstract void deleteEntitiesInMysql();
	
	public abstract void copyMessagesInDB(List<Vector<String>>a);
	
	public abstract List<Vector<String>> getMessagesWrites();
	
	public abstract  List<Vector<String>> getProjectsWrites();
	
	public abstract void copyProjectAndFundsInDB(List<Vector<String>> projects,String agencyName);
	
	public abstract  void readProgettoFromMySql(String agencyName);
	
	public abstract void readMessaggioFromMySql(String agencyName);
	
	public abstract void readAziendaFromMySql();
	
}
