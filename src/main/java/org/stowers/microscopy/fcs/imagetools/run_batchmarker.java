package  org.stowers.microscopy.fcs.imagetools;

public class run_batchmarker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int nargs = args.length;
		System.out.println(args[nargs-2] + "   " +  args[nargs-1]);
		System.out.println("nargs " + nargs);
		batchmarker b = null;
		String schema = args[0];
		String outdir = args[1];
		String where="";
 		if (nargs == 2) {
			b = new batchmarker(schema, outdir);
		}
		else if (nargs ==3) {
			where = args[2];
			System.out.println(where);
			b = new batchmarker(schema, outdir, where);
		}
 		
		b.runbatch();
	}

}
