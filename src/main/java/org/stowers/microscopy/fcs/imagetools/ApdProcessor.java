package org.stowers.microscopy.fcs.imagetools;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;

import ij.process.FloatProcessor;

public class ApdProcessor extends FloatProcessor {

	int[] apdHistogram = null;
	long photonCount = 0;
	

	public ApdProcessor(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	
	public int countAboveThreshold(float threshold) {
		
		int count = 0;
		float[] pixels = (float[])getPixels();
		for (int i =0; i < width*height; i++) {
			if (pixels[i] > threshold) {
				count++;
			}
		}
		return count;
	}
	
	private int[] calcApdHistogram() {
		int[] rawhist = new int[256];
		TreeMap<Float, Integer> histmap = new TreeMap<Float, Integer>();
		float[] pixels = (float[])getPixels();
		for (int i=0; i < width*height; i++) {
			float key= pixels[i];
			if (histmap.containsKey(key)) {
				int v = histmap.get(key) + 1;
				histmap.put(key, v);
			} else {
				histmap.put(key,  1);
			}
		}
	
		
		Float[] keys = histmap.keySet().toArray(new Float[histmap.size()]);

		float[] dels = new float[keys.length -1];

		for (int i=0;i < dels.length; i++) {
			dels[i] = keys[i+1] - keys[i];
		}
		float del = (dels[1]+dels[0])/2.f;
		
		float maxint = keys[keys.length -1];
		int nbins = Math.round(maxint/del) + 1;
//		System.out.println(maxint + " " + nbins);
		apdHistogram = new int[nbins];
		for (int i = 0; i < keys.length; i++) {
			int akey = Math.round(keys[i]/del);
//			System.out.println("********* " + akey + " " + keys.length + " " + keys[i]);
			apdHistogram[akey] += histmap.get(keys[i]);
		}

		long count = 0;
		for (int k = 0; k < apdHistogram.length; k++) {
			count += k*apdHistogram[k];
		}
		photonCount = count;
		return apdHistogram;
	}
	public long getPhotonCount() {
		if (apdHistogram == null) {
			calcApdHistogram();
		}
		return photonCount;
	}
}
