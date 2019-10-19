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

import com.mysql.cj.util.StringUtils;


public class Connect {
	private static String password = "root";
	private static String username = "root";
	private static String ssl = "&requireSSL=true";
	
	private String connStr = "jdbc:mysql://localhost:3306/esercizio1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=";
	private Connection conn = null;

	private List<Vector<String>> fetchRows(ResultSet result, int num) throws SQLException {
		ArrayList<Vector<String>> list = new ArrayList<Vector<String>>();
		
		if(!result.next())
			return null;
		
		while(result.next()) {
            Vector<String> v = new Vector<String>();
			
            for(int i=1; i <= num; i++)
            	if(result.getObject(i) == null)
            		v.add("0");
            	else
            		v.add(result.getObject(i).toString());
            
            list.add(v);
        }
		
		return list;
	}
	
	public Connect(){
		connStr = connStr + username + "&password=" + password + ssl;
		try {
			conn = DriverManager.getConnection(connStr);

		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage() );
			System.out.println("SQLState: " + ex.getSQLState() );
			System.out.println("VendorError: " + ex.getErrorCode() );	
		}
	}

	public void close() {
		try {
			conn.close();
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block	
			e.printStackTrace();
		}
	}

	public List<Vector<String>> query(String queryString, int numColumns) {
		System.out.print("in esecuzione la query: \n" + queryString + "\n");		
		
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(queryString);
			ResultSet rs = stmt.getResultSet();
					
			if( numColumns == 0 ) {
				stmt.close();
				return null;
			}
			
			List<Vector<String>> fetchedRows =  fetchRows(rs, numColumns);
			
			stmt.close();
			
			System.out.print("Query eseguita con successo \n");
			
			return fetchedRows;
        }
		catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return null;		
	}
	
	public List<Vector<String>> query(String queryString, int numColumns, Vector<String> data) {
		System.out.print("in esecuzione la query: \n" + queryString + "\n");
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(queryString);
			System.out.println("Vector capacity: " + data.capacity());

			for(int i=0, j = 1; i < data.size() ; i++, j++) 
				if(StringUtils.isStrictlyNumeric(data.get(i)))
					pstmt.setInt(j, Integer.parseInt( data.get(i) ) );
				else
					pstmt.setString(j, data.get(i));	

			pstmt.execute();
			
			if( numColumns == 0 ) {
				pstmt.close();
				return null;
			}
			
			ResultSet rs = pstmt.getResultSet();
			
			if( rs == null) {
				pstmt.close();
				return null;
			}
			
			List<Vector<String>> fetchedRows =  fetchRows(rs, numColumns);

			pstmt.close();		
			
			System.out.print("Query eseguita con successo \n");
			return fetchedRows;		

		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		
		return null;
	}

}