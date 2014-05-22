package org.stowers.microscopy.dbutils;

import java.util.List;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.commons.beanutils.DynaBean;

/**
 * 
 * @author cjw
 *
 * This class creates a connection to the postgresql database on ome.sgc.loc
 * and has methods to perform querys.
 * 
 * 
 */
public class OmeDB {

	final String dburl = "jdbc:postgresql://ome/ome?user=cjw&password=rdf59gt2";
	Connection conn = null;
/**
 * The constructor for this class - it take no parameters. It creates the
 * connection to the database.
 */
	public OmeDB() {
		try {
			conn = DriverManager.getConnection(dburl);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("OK");
		}
	}

/**
 * Uses the apache commons beanutils to transform the result set into
 * beans. 
 * @param query A string of the sql query to perform
 * @return A List of DynaBean objectss
 */
	public List<DynaBean> makeQuery(String query) {

		List res = null;
		try {
			Statement st = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			
			ResultSet rs = st.executeQuery(query);
			RowSetDynaClass rows = new RowSetDynaClass(rs);
			res = rows.getRows();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		return res;
	}
}
