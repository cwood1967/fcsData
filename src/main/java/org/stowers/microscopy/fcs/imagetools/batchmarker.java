package  org.stowers.microscopy.fcs.imagetools;

import java.io.File;

import ij.CompositeImage;
import ij.io.FileSaver;

import java.sql.*;


public class batchmarker {

	static String dburl = "jdbc:postgresql://ome/ome?user=cjw&password=rdf59gt2";
	static String urlpre = "http://ome/fcsmarker/";
	String schema;
	String plate;
	String part;
	String select;
	String outdir;
	
	Connection conn = null;
	
	public batchmarker(String schema, String outdir) {
		this.schema = schema;
		this.outdir = outdir;
		select = "select p.image_id, image_server_id, size_x, size_y, size_c, x.xpos, x.ypos "
				+ "from image_pixels p "
				+ "left join " + schema + ".fcsposition x on  p.image_id = x.image_id ";
	}
	
	public batchmarker(String schema, String outdir, String selectwhere) {
		this.schema = schema;
		this.outdir = outdir;
		select = "select p.image_id, image_server_id, size_x, size_y, size_c, x.xpos, x.ypos "
				+ "from image_pixels p "
				+ "left join " + schema + ".fcsposition x on  p.image_id = x.image_id ";
		
		select = select + " " + selectwhere;
		System.out.println(select);
	}
	
	public void runbatch() {
		try {
			conn = DriverManager.getConnection(dburl);
			conn.setAutoCommit(false);
			
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
												ResultSet.CONCUR_UPDATABLE);
		
			st.setFetchSize(100);
			ResultSet rs = st.executeQuery(select);
		
			//rs.last();
			//int size = rs.getRow();
			//System.out.println(size);
			//rs.beforeFirst();
			int count = 0;
			double  t0 = System.nanoTime()*1e-6;
			double t1 = t0;
			while (rs.next()) {
				int image_id = rs.getInt("image_id");
				OmeImage x = new OmeImage(image_id, schema);
				int server_id = rs.getInt("image_server_id");
				int width = rs.getInt("size_x");
				int height = rs.getInt("size_y");
				int channels = rs.getInt("size_c");
				float xpos = rs.getFloat("xpos");
				float ypos = rs.getFloat("ypos");
				
				x.setPixelInfo(server_id, width, height, channels, xpos, ypos);
				x.fetchPixels();
				x.createImagePlus();
				CompositeImage ci = x.makeComposite();
				x.drawmark(ci);
				saveMarkedImage(x);
				//x.showfinalimage();
				if (count % 100 == 0) {
					t0 = t1;
					t1 = System.nanoTime()*1e-6;
					System.out.print(count + " : ");
					System.out.print(t1 -t0);
					System.out.println(" " +  image_id);
					
					
				}
				
//				if (count > 10) {
//					break;
//				}
				count++;
			}
			rs.close();
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveMarkedImage(OmeImage x) {
		
		int id = x.getImageId();
		int dnum = id/1000;
		String sdnum = String.format("%d", dnum);
		String path = outdir + "/" + sdnum;
		File d = new File(path);
		boolean res;
		if (d.exists()) {
			res = true;
		}
		else {
			res = d.mkdirs();
		}
		
		String filename = path + "/" + Integer.toString(id) + ".jpg";
		System.out.println(filename);
		x.savemarkedimage(filename);
		
		String url = urlpre + sdnum + "/" + Integer.toString(id) + ".jpg";
		int ru = insertRecord(id, url);
		return res;
	}
	
	public int insertRecord(int id, String url) {
		
		String insert = "insert into " + schema + ".markedimage values "
						+ "(?,?)";
		
		String update = "update " + schema + ".markedimage "
						+ "set url=(?) "
						+ "where image_id=(?)";
		int ru = 0 ;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(insert);
			ps.setInt(1, id);
			ps.setString(2, url);
			ru = ps.executeUpdate();
			ru = 1;
			conn.commit();
			System.out.println("Inserting " + id + " " + url);
		}
		catch (SQLException e){
			ru = 0;
			
			//e.printStackTrace();
		}
		
		if (ru == 0) {
			try {
				conn.rollback();
				ps = conn.prepareStatement(update);
				ps.setString(1, url);
				ps.setInt(2, id);
				ru = ps.executeUpdate();
				ru = 1;
				conn.commit();
				System.out.println("Updating " + id + " " + url);
			}
			catch(SQLException e) {
				e.printStackTrace();
				ru = -1;
			}
		}
		
		return ru;
	}

}
