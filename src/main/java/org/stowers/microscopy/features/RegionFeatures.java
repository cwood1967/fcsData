package org.stowers.microscopy.features;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;

import org.stowers.microscopy.segmentation.YeastSrm;

/**
 * This class will calculate features from a single plane image
 * 
 * @author cjw
 * 
 */

public class RegionFeatures {

	final ImagePlus imp;
	final ImageProcessor ip;
	int stacksize;
	int width;
	int height;

	public RegionFeatures(ImagePlus imp) {
		// TODO Auto-generated constructor stub
		this.imp = imp;
		this.ip = imp.getProcessor();
		stacksize = imp.getStackSize();
		width = imp.getWidth();
		height = imp.getHeight();

	}

	public HashMap<Byte, ArrayList<Integer>> findSrmRegions(float Q) {
		
		YeastSrm srm = new YeastSrm(imp);
		ImagePlus srmimp = srm.ijSRM(false);

		byte[] pixels = (byte[])srmimp.getProcessor().getPixels();
		ArrayList<Integer> regions = new ArrayList<Integer>();
		
		HashMap<Byte, ArrayList<Integer>> regionMap = 
					new HashMap<Byte, ArrayList<Integer>>();
		
		for (int i=0; i < pixels.length; i++) {
			byte key = pixels[i];
			if (regionMap.containsKey(key)) {
				regionMap.get(key).add(i);
			} else {
				ArrayList a = new ArrayList<Integer>();
				a.add(i);
				regionMap.put(pixels[i], a); 
			}
		}
		
		System.out.println(regionMap.size());
		for (byte b : regionMap.keySet()) {
			System.out.println(b + " " + regionMap.get(b).size());
		}
		
		return regionMap;
		
	}
	
	public double[]  calcRegionCOM(ArrayList<Integer> region) {

		double xsum = 0;
		double ysum = 0;

		for (int i : region) {
			int xi = i % width;
			int yi = i / width;
			
			xsum += xi;
			ysum += yi;
		}
		
		double xcom = xsum/region.size();

		double ycom = ysum/region.size();
		System.out.println(xcom  + " " + ycom);
		return new double[] {xcom, ycom};
	}
}

