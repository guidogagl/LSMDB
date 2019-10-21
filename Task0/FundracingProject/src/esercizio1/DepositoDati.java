package esercizio1;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DepositoDati {
	

	private Connect conn = null;
	
	private List<RowTableProjects> getRowTableProjects(String sql, Vector<String> v) {
		conn = new Connect();
		
		List<RowTableProjects> ret = new ArrayList<RowTableProjects>();
		
		List<Vector<String>> resultSet;
		
		if(v != null)
			resultSet = conn.query(sql, 6, v);
		else
			resultSet = conn.query(sql, 6);

		
		for(int i = 0; i < resultSet.size(); i++) {
			Vector<String> val = resultSet.get(i);
			
			RowTableProjects row = new RowTableProjects( Integer.parseInt(val.get(0)), 
														val.get(1), 
														val.get(2), 
														Integer.parseInt(val.get(3)), 
														Integer.parseInt(val.get(4)), 
														val.get(5)
														); 
			ret.add(row);
		}

		conn.close();
		
		System.out.print("Connessione con il database chiusa correttamente \n");
		
		return ret;		
	}
	
	private List<RowTableMessage> getRowTableMessage(String sql, Vector<String> v) {

		conn = new Connect();
		
		
		List<Vector<String>> resultSet;
		
		resultSet = conn.query(sql, 7, v);
		
		if(resultSet == null){
			conn.close();
			return new ArrayList<RowTableMessage>();
		}
		
		List<RowTableMessage> ret = new ArrayList<RowTableMessage>();
		
		for(int i = 0; i < resultSet.size(); i++) {
			Vector<String> val = resultSet.get(i);
			
			RowTableMessage row = new RowTableMessage( 	Integer.parseInt(val.get(0)), 
														Integer.parseInt(val.get(1)),
														val.get(2),
														val.get(3),
														val.get(4), 
														val.get(5),
														Integer.parseInt(val.get(6))
														); 
			ret.add(row);
		}

		conn.close();
		
		System.out.print("Connessione con il database chiusa correttamente \n");
		
		return ret;	
	}
	
		
	public List<RowTableProjects> getProjects(String agencyName){
		String sqlStr = "select f1.id_project, f1.nome,case when f2.progress is null then 0 else f2.progress end as progress, f1.budget, f1.stake, f1.azienda\r\n" + 
				"from (\r\n" + 
				"	select f1.id_project, f1.nome, f1.budget, max(f1.stake) as stake, f1.azienda\r\n" + 
				"	from  (\r\n" + 
				"	select p.id as id_project, p.nome, p.budget, case when f.azienda = (?) then f.budget else 0 end as stake, p.azienda\r\n" + 
				"	from finanziamento f right outer join progetto p on f.progetto = p.id\r\n" + 
				"	order by stake desc\r\n" + 
				"		) as f1 \r\n" + 
				"	group by f1.id_project\r\n" + 
				"	) as f1\r\n" + 
				"    left outer join \r\n" + 
				"    (\r\n" + 
				"		select  p.id, 100*sum(f.budget)/p.budget as progress\r\n" + 
				"        from progetto p inner join finanziamento f on p.id = f.progetto\r\n" + 
				"        group by p.id\r\n" + 
				"    ) as f2 on f1.id_project = f2.id\r\n" + 
				"    ;";
		
		Vector<String> d = new Vector<String>();		
		d.add(agencyName);
		
		List<RowTableProjects> ret = getRowTableProjects(sqlStr, d);
		
		return ret;
	}
	
	public List<RowTableMessage> getMessages(String agencyName){
		String sqlStr="select	m.id, m.progetto as id_project, m.data, m.mittente, m.destinatario, m.testo as messaggio, m.stake\n" + 
					"from	messaggio m\n" + 
					"		inner join\n" + 
					"        azienda a\n" + 
					"        on m.mittente = a.nomeAzienda\n" + 
					"where 	m.destinatario =  (?) ;";
		Vector<String> d = new Vector<String>();	
		d.add(agencyName);
		List<RowTableMessage> ret = getRowTableMessage(sqlStr, d);
		
		return ret;
	}
	
	public String getDescriptionMessage(int id_message) {
		
		String sql = "SELECT testo FROM messaggio WHERE id ="+ id_message +";";
		
		conn = new Connect();
		
		List<Vector<String>> val = conn.query(sql, 1);
		
		conn.close();
		
		return val.get(0).get(0).toString();
		}
	
	
	public int getSommaStakes(int selectedProjectID){		
		String sql="SELECT sum(f.budget) as somma "
		  		+ "FROM finanziamento f  "
				+" WHERE f.progetto="+ selectedProjectID +";"
				;
		
		conn = new Connect();
		
		List<Vector<String>> val = conn.query(sql, 1);
		
		conn.close();
		
		return Integer.parseInt( val.get(0).get(0) );
	}
	
	public double getProgress(int id_progetto)
	{
		String sql="SELECT (sum(f.budget)/p.budget)*100 as progresso "
		  		+ "FROM progetto p inner join finanziamento f on p.id=f.progetto "
				+" WHERE p.id="+ id_progetto +";"
				;
		conn = new Connect();
		
		List<Vector<String>> val = conn.query(sql, 1);
		
		conn.close();
		
		return Double.valueOf(val.get(0).get(0).toString());
		
	}
	
	public List<RowTableProjects> getProjectsWithoutStake(){
		String sqlStr =" select p.id as id_project, p.nome, case when d.id_project is null then 0 else d.progress end as progress, 0 as stake, p.budget,p.azienda\n" + 
				"from progetto p left outer join (\n" + 
				"select	p.id as id_project, p.nome as nome,(sum(f.budget)/p.budget)*100 as progress, p.budget, p.azienda\n" + 
				"				from	progetto as p \n" + 
				"						inner join \n" + 
				"				        finanziamento as f \n" + 
				"				        on p.id = f.progetto\n" + 
				"				group by f.progetto) as d on d.id_project=p.id;";
	
		return getRowTableProjects(sqlStr, null);
	}
	
	public void insertProject(Vector<String>val) {
	  String insertProject="INSERT INTO progetto (nome,budget,descrizione,azienda) values ((?),(?),(?),(?))";
	   
	  Connect conn = new Connect();
	  
	  conn.query(insertProject, 0, val);
	  
	  conn.close();
	}
	
	public boolean iAmOwner(int projectId,String agencyName) {
		String sql="SELECT COUNT(*) as conta FROM progetto WHERE id="+ projectId +" and azienda='"+ agencyName +"';";
		
		conn = new Connect();
		
		List<Vector<String>> val = conn.query(sql, 1);
		
		conn.close();
		
		int occurrency = Integer.parseInt( val.get(0).get(0) );
		
		if( occurrency > 0)
			return true;
		return false;
		
	}
	
	public Boolean myStake(String agencyName, int id_project) {
		String sql = "SELECT count(*) as conta FROM finanziamento WHERE progetto = "+ id_project + " and azienda = '"+ agencyName +"';";
		
		conn = new Connect();
		
		List<Vector<String>> val = conn.query(sql, 1);
		
		conn.close();
		
		int occurrency = 0;
		if(val != null)
			occurrency = Integer.parseInt(val.get(0).get(0));
		
		if( occurrency > 0)
			return true;
		
		return false;
	}	
	
	public void deleteProject(int projectId) {
		String sql="DELETE FROM progetto WHERE id = "+ projectId + ";";
		
		Connect conn = new Connect();
	  
		conn.query(sql, 0);
	  
		conn.close();
		
	}
	
	
	public void deleteMessage(int messageId) {
		String sql="DELETE FROM messaggio WHERE id = "+ messageId + ";";
		
		Connect conn = new Connect();
	  
		conn.query(sql, 0);
	  
		conn.close();
		
	}
	
	
	public Vector<String> getAgency(String agencyName,String password) {
		
		String sql = "SELECT * FROM azienda WHERE nomeAzienda = '"+ agencyName + "' AND password ='" + password + "';";
		
		Connect conn = new Connect();
		
		List<Vector<String>> res = conn.query(sql, 5);
		if(res == null) {
			conn.close();
			return new Vector<String>();
		}
		
		Vector<String> vett = res.get(0);
		
		conn.close();
		
		Vector<String> ret = new Vector<String>();
		for(int i = 0; i < 5; i++)
			ret.add(vett.get(i).toString());
		
		return ret;
	}
	
	public String getDescriptionProject(int id_project) {
		String sql = "SELECT descrizione FROM progetto WHERE id ="+ id_project +";";
		
		conn = new Connect();
		
		List<Vector<String>> val = conn.query(sql, 1);
		
		conn.close();
		
		return val.get(0).get(0).toString();
	}
		
	public void deleteMyStake(int projectId,String agencyName) {
		String sql = "DELETE FROM finanziamento WHERE progetto = "+ projectId +" and azienda = '"+ agencyName + "';";
		
		Connect conn = new Connect();
		  
		conn.query(sql, 0);
	  
		conn.close();
	}
	
	public void updateStake(int stakeBudget,String agencyName,int idProgetto) {
		
		boolean stakePresent = myStake(agencyName, idProgetto);
		
		Vector<String> val = new Vector<String>();
		val.add(Integer.toString(stakeBudget));
		val.add(agencyName);
		val.add(Integer.toString(idProgetto));
		
		conn = new Connect();
		String sql = "";
		
		//Se non esiste un finanziamento per il progetto selezionato da parte dell'azienda che ha fatto il login
		if(!stakePresent) 
			sql = "INSERT INTO finanziamento (budget, azienda, progetto) values((?), (?), (?));";
		else
			sql = "UPDATE finanziamento SET budget = (?) WHERE azienda = (?) and progetto = (?);";
		
		conn.query(sql, 0, val);
		
		conn.close();
	}

	public List<String> getListAgency(){
		
		List<String> val = new ArrayList<String>();
		
		
		String str = "select	nomeAzienda\n" + 
				"from	azienda;";
		
		conn = new Connect();
		
		List<Vector<String>> resultSet = conn.query(str, 1);
		
		for(int i = 0; i < resultSet.size(); i++) {
			
			val.add(resultSet.get(i).get(0));
			System.out.println(val.get(i));
		}
		
		conn.close();
		
		System.out.print("Connessione con il database chiusa correttamente \n");
		
		return val;
	}
	
	
	public void insertMessage(Vector<String>val) {
		
		  String insertProject="INSERT INTO messaggio (mittente,destinatario,progetto,testo,stake) values ((?),(?),(?),(?),(?))";
		   
		  Connect conn = new Connect();
		  
		  conn.query(insertProject, 0, val);
		  
		  conn.close();
		}
	
	
	
	
	
}
