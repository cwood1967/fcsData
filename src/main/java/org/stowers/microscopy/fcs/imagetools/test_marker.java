package  org.stowers.microscopy.fcs.imagetools;

public class test_marker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		batchmarker b = new batchmarker("ccdata", "/tmp/testmarker",
				"where p.image_id = 96404");
		b.runbatch();
	}

}
