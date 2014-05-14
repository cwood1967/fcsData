package queryYeast;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;

public class yeastdata {

	Connection db;
	String url = "jdbc:postgresql://ome:5432/ome";
	String username = "cjw";
	String password = "rdf59gt2";
	String [] columnNames;
	int [] columnTypes;
	String [] rsData;
	
	public yeastdata() {
		//set up the database connection
		
		try {
            Class.forName("org.postgresql.Driver");
		    db = DriverManager.getConnection(url, username, password);
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Could not open connection");
		}
        catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not open connection");
       }
	}
	
public String [] getColumnNames() {
		return columnNames;
	}


public int [] getColumnTypes() {
		return columnTypes;
	}

	public String [] queryOverviewForOrfname(String query) {
		String [] result = null;
		
		try {
			
			
			Statement storf = db.createStatement();
			
			ResultSet orfresults = storf.executeQuery(query);
					
			List<String> orfnames = new ArrayList<String>();
			
			if (orfresults != null)
			{
				while (orfresults.next())
				{
					String orf = orfresults.getString("orfname");
					orfnames.add(orf);
				}
			} 
			
			int np = orfnames.size();
			
			result = orfnames.toArray(new String[np]);
			
			orfresults.close();
			}
			
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			return result;
		
	}
	// the method will return an array of strings for the entire 
	//result set
	// the format is [column names, column types, data]
	//the data in a long one-dimensional array
	
	
		
	
	
	public String[] settest(String query) {

			ArrayList<String> res = new ArrayList<String>();;
			ArrayList<String> colnames = new ArrayList<String>();;
			ArrayList<Integer> types = new ArrayList<Integer>();;
			try {
			
			//String query = "select * from yeastdata.overview where goodcells " +
			//	"> 9";
				
			String resultCol = "difftime";
			Statement storf = db.createStatement();
			
			
			ResultSet rset = storf.executeQuery(query);
			ResultSetMetaData md =  rset.getMetaData();
			int cols = md.getColumnCount();
			
			for (int i =1; i < cols+1; i++)
			{
				
				colnames.add(md.getColumnName(i));
				types.add(md.getColumnType(i));
				//res.add(((Integer)md.getColumnType(i)).toString());
				
			}
			
		
			columnNames = colnames.toArray(new String[cols]);
			Integer [] itmp = types.toArray(new Integer[cols]);
			
			columnTypes = new int[types.size()];
			for (int i =0; i < cols; i++)
			{
				columnTypes[i] =  types.get(i).intValue();
				
			}
			
			//res = new ArrayList<Object>();
			
			if (rset != null)
			{
				while (rset.next())
				{
					
					for (int i =1; i <= cols; i++)
					{
						res.add(rset.getString(i));
					}
					//String tmp = rset.getString(resultCol);
				
				}
			} 
			
			//System.out.println((double)res.size());
			
			int np = res.size();
			
			//result = res.toArray(new String[np]);
			
			rset.close();
			}
			
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			int nb = res.size();
			String []  result = res.toArray(new String[nb]);
			return result;
	}
	
	public int updateDB(String query)
	{
		try {
			
			Statement storf = db.createStatement();
			storf.executeUpdate(query);
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return 1;
	}
	
	
	
	public String [] getStringArray(String query, String resultCol) {
		String [] result = null;
		
		try {
			
			
			Statement storf = db.createStatement();
			
			ResultSet rset = storf.executeQuery(query);
					
			List<String> res = new ArrayList<String>();
			
			if (rset != null)
			{
				while (rset.next())
				{
					String tmp = rset.getString(resultCol);
					res.add(tmp);
				}
			} 
			
			int np = res.size();
			
			result = res.toArray(new String[np]);
			
			rset.close();
			}
			
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			return result;
		
	}
	
	public String [] OrfnameFromLocalization(String localization) {
		
		
		
		String[] result = null;
		try {
		String sq = "Select orfname from yeastdata.ucsforfdata where " +
			"localization like \'" + localization + "\'";
		
		Statement storf = db.createStatement();
		
		//System.out.println(sq);
		ResultSet orfresults = storf.executeQuery(sq);
		
				
		List<String> orfnames = new ArrayList<String>();
		
		if (orfresults != null)
		{
			while (orfresults.next())
			{
				String orf = orfresults.getString("orfname");
				orfnames.add(orf);
			}
		} 
		
		int np = orfnames.size();
		
		result = orfnames.toArray(new String[np]);
		
		orfresults.close();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public CachedRowSetImpl queryDiffusionTime(double minval, double maxval) {
		
		
		try {
			CachedRowSetImpl result = new CachedRowSetImpl();
			String sq = "Select * from yeastdata.overview where "
					+ "difftime > " + ((Double) minval).toString() + " AND "
					+ "difftime < " + ((Double) maxval).toString();

			Statement storf = db.createStatement();

//			System.out.println(sq);
			ResultSet orfresults = storf.executeQuery(sq);
//			while (orfresults.next()) {
//				
//				System.out.println(orfresults.getString(1));
//				
//			}
			result.populate(orfresults);
			
			return result;
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
			CachedRowSetImpl result = null;
			return result;
		}
		
		
		

	}
		
	
	public void dispose() {
		try {
			db.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
		
}
	
	

