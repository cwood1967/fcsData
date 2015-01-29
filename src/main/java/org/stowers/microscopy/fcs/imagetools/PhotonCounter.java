package org.stowers.microscopy.fcs.imagetools;

import java.util.concurrent.Callable;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class PhotonCounter implements Callable<Long> {

	int image_id;

	public PhotonCounter(int image_id) {
		// TODO Auto-generated constructor stub
		this.image_id = image_id;
	}

	@Override
	public Long call()  throws Exception {
		// TODO Auto-generated method stub
		
		OmeImage image = new OmeImage(image_id);
		image.setPixelInfoFromImageId();
		if (image.getServerId() < 0) {
			return -1L;
		}
		image.fetchPixels();
		image.createImagePlus();
		ImagePlus imp = image.getImage();
		ImageProcessor ip = imp.getProcessor();
		
		ApdProcessor amp = new ApdProcessor(image.width, image.height);
		ip.setSliceNumber(0);
		float[] pixels = (float[]) ip.getPixels();
		amp.setPixels(pixels);

		long res = 0;
//		res = amp.countAboveThreshold(0);
		res = amp.getPhotonCount();
//		System.out.println(Thread.currentThread().getName() + " " + res);
		return res;
	}

}
