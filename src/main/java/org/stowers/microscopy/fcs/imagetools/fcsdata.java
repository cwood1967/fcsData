package fcs.imagetools;

import java.sql.*;

public class fcsdata {

	Connection conn = null;
	static String dburl = "jdbc:postgresql://ome/ome?user=spotfire&password=spotfire";
	String schema;
	int image_id;
	int server_id;
	int width;
	int height;
	int channels;
	float xpos;
	float ypos;
	public Exception excep = null;
	
	public fcsdata(int image_id, String schema) {
		this.setup(image_id, schema);
	}
	
	public fcsdata(int image_id) {
		this.setup(image_id, "");
	}
	
	private void setup(int image_id, String schema) {
		this.image_id = image_id;
		this.schema = schema;
		this.server_id = 0;
		
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
	
	public int getServerId() {
		return server_id;
	}
	
	public int getImageId() {
		return image_id;
	}
	
	public void setPixelInfo(int server_id, int width, int height, int channels,
							float xpos, float ypos) {
		
		this.server_id = server_id;
		this.width = width;
		this.height = height;
		this.channels = channels;
		this.xpos = xpos;
		this.ypos = ypos;
		
	}
	
	public void setPixelInfoFromImageId() {
		
		String s1;
		if (schema == "") {
			s1 = "select image_server_id, size_x, size_y, size_c "
						+ "from image_pixels p "
						+ "where p.image_id=";
		}
		else {
			s1 = "select image_server_id, size_x, size_y, size_c, x.xpos, x.ypos "
					+ "from image_pixels p "
					+ "join " + schema + ".fcsposition x on  p.image_id = x.image_id "
					+ "where p.image_id=";
		}
		String select = s1 + Integer.toString(image_id);
		//System.out.println(select);
		
		try {
			//Class.forName("org.postgresql.Driver");
			//Connection conn = DriverManager.getConnection(dburl);
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
												ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = st.executeQuery(select);
		
			rs.last();
			int size = rs.getRow();
			System.out.println(size);
			rs.beforeFirst();
			if (size > 0) {
				rs.next();
				server_id = rs.getInt(1);
				System.out.println("Server id " + server_id);
				width = rs.getInt(2);
				height = rs.getInt(3);
				channels = rs.getInt(4);
				if (schema != "") {
					xpos = rs.getFloat(5);
					ypos = rs.getFloat(6);
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			excep = e;
			server_id = -2;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			excep =e;
			server_id = -1;
		}
	}
}
