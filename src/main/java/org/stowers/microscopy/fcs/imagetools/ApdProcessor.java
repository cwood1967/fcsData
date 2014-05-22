package org.stowers.microscopy.fcs.imagetools;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import ij.process.ShortProcessor;

public class ApdProcessor extends ShortProcessor {

	public ApdProcessor(BufferedImage bi) {
		super(bi);
		// TODO Auto-generated constructor stub
	}

	public ApdProcessor(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public ApdProcessor(int width, int height, boolean unsigned) {
		super(width, height, unsigned);
		// TODO Auto-generated constructor stub
	}

	public ApdProcessor(int width, int height, short[] pixels, ColorModel cm) {
		super(width, height, pixels, cm);
		// TODO Auto-generated constructor stub
	}
	
	public int countAboveThreshold(int threshold) {
		
		int count = 0;
		short[] pixels = (short[])getPixels();
		for (int i =0; i < width*height; i++) {
			if (pixels[i] > threshold) {
				count++;
			}
		}
		return count;
	}
	
}
