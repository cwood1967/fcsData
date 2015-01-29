package queryYeast;

import junit.framework.TestCase;

	public class yeastdataTest extends TestCase {
		// TODO Auto-generated constructor stub
		
	public void testQuery() {
		
		String query = "select count(image_id) from images";
		yeastdata y = new yeastdata();
		String [] result = y.settest(query);
		System.out.println(result[0]);
		assertNotNull(result);
	}

}
