package org.stowers.microscopy.dbutils;

import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.commons.beanutils.DynaBean;
public class OmeDBTest extends TestCase {

	public OmeDBTest() {
		// TODO Auto-generated constructor stub
	}

	public OmeDBTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void testMakeQuery() {
		String q = "select image_id, image_server_id from image_pixels where image_id > "
				+ "13000 limit 10";
		OmeDB ome = new OmeDB();
		List<DynaBean> rows = ome.makeQuery(q);
		
		for (DynaBean bean : rows) {
			System.out.println("** " + bean.get("image_server_id") + " " + bean.get("image_id"));
		}
		assertNotNull(rows);
	}

}
