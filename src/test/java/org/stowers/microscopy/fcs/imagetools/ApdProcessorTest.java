package org.stowers.microscopy.fcs.imagetools;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import junit.framework.TestCase;

public class ApdProcessorTest extends TestCase {

	int image_id = 53245;
	public ApdProcessorTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testCountAboveThreshold() throws Exception {
		OmeImage image = new OmeImage(image_id);
		image.setPixelInfoFromImageId();
		image.fetchPixels();
		image.createImagePlus();
		ImagePlus imp = image.getImage();
		ImageProcessor ip = imp.getProcessor();
		ip = ip.convertToShortProcessor();
		ApdProcessor amp = new ApdProcessor(image.width, image.height);
		ip.setSliceNumber(0);
		short[] pixels = (short[])ip.getPixels();
		amp.setPixels(pixels);

		int res = amp.countAboveThreshold(0);
		System.out.println("Count Above Threshold " + res);
		
		assertNotNull(res);
	}
}
