package org.stowers.microscopy.fcs.imagetools;

import java.util.concurrent.Callable;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class PhotonCounter implements Callable<Integer> {

	int image_id;

	public PhotonCounter(int image_id) {
		// TODO Auto-generated constructor stub
		this.image_id = image_id;
	}

	@Override
	public Integer call()  throws Exception {
		// TODO Auto-generated method stub
		
		OmeImage image = new OmeImage(image_id);
		image.setPixelInfoFromImageId();
		image.fetchPixels();
		image.createImagePlus();
		ImagePlus imp = image.getImage();
		ImageProcessor ip = imp.getProcessor();
		ip = ip.convertToShortProcessor();
		ApdProcessor amp = new ApdProcessor(image.width, image.height);
		ip.setSliceNumber(0);
		short[] pixels = (short[]) ip.getPixels();
		amp.setPixels(pixels);

		int res = 0;
		res = amp.countAboveThreshold(0);
//		System.out.println(Thread.currentThread().getName() + " " + res);
		return res;
	}

}
