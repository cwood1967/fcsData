package fcs.imagetools.dbutils;

import java.util.HashMap;
import java.util.ArrayList;
import java.sql.*;

import fcs.imagetools.dbutils.ImagePlateWell;

public class DbQuery {

	static Connection conn = null;
	static String dburl = "jdbc:postgresql://ome/ome?user=spotfire&password=spotfire";
	static String schema;
	
	public Exception excep = null;
	
	public DbQuery(String schema) {
		//this.setup(schema);
	}
	
	public DbQuery() {
		//this.setup("");
	}
	
	private static void setup() {
		//this.schema = schema;
		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dburl);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<Integer, ImagePlateWell> imagesFromPlate(String schema, 
									String plate,
									String part) {
		
		setup();
		HashMap<Integer, ImagePlateWell> res = new HashMap<Integer, ImagePlateWell>();
		String q = "select image_id, plate, part, wellnumber, wellname from " + schema + ".imageplatewell "
					+ "where plate = '" + plate + "' and part = '" + part + "'"; 
		
		
		ResultSet rs = runquery(q);
		try {
			while (rs.next()) {
				int image_id = rs.getInt(1);
				String rsplate = rs.getString(2);
				String rspart = rs.getString(3);
				int wellnumber = rs.getInt(4);
				String wellname = rs.getString(5);
				ImagePlateWell ipw = new ImagePlateWell(image_id, rsplate, rspart, wellnumber, wellname);
				res.put(image_id, ipw);
			}
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			res = null;
		}
		return res;
	}
	
	private static ResultSet runquery(String query) {
		ResultSet rs = null;
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		
			rs = st.executeQuery(query);
		}
		catch (SQLException e) {
			rs = null;
		}
		return rs;
	}
}
