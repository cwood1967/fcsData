package org.stowers.microscopy;

import ij.CompositeImage;
import ij.ImageJ;
import org.stowers.microscopy.fcs.imagetools.OmeImage;

public class testtools {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long t0 = System.nanoTime();
		OmeImage x = new OmeImage(64567, "ccdata");
		x.setPixelInfoFromImageId();
		long t01 = System.nanoTime();
		System.out.println("Create object - db: "  + (t01 - t0)*1e-6);
		//x.setDimensions(512, 512, 3);
		try {
			if (x.getServerId() > 0) {
				long ta = System.nanoTime();
				x.fetchPixels();
				System.out.println("Fetching " + (System.nanoTime() - ta)/1000000.);
				if (!x.arePixelsNull()) {
					t01 = System.nanoTime();
					x.createImagePlus();
					long t11 = System.nanoTime();
					System.out.println("Create imageplus: "  + (t11 - t01)*1e-6);
					CompositeImage ci = x.makeComposite();
					long t12 = System.nanoTime();
					System.out.println("Create composite: "  + (t12 - t11)*1e-6);
					x.drawmark(ci);
					long t13 = System.nanoTime();
					System.out.println("drawmark: "  + (t13- t12)*1e-6);
				}
				else {
					System.out.println("Pixels are null");
				}
			}
			else {
				System.out.println("No server id");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//x.showimage(ci);
		//x.savePNG(ci);
		long t1 = System.nanoTime();
		System.out.println("dfdfdfdfdfdfdfdfdfd");
		System.out.println((t1 - t0)/1000000.0);
		//x.convertToRGB(ci);
//		x.createImage();
//		x.convertPixelsToUByte();
		
		
	}

}
