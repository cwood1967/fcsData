package org.stowers.microscopy.features;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.stowers.microscopy.fcs.imagetools.OmeImage;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ShortProcessor;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import org.stowers.microscopy.segmentation.SRM;

public class Extractor {

	ImagePlus imp = null;
	public Extractor(ImagePlus imp) {
		// TODO Auto-generated constructor stub
//		imp.show();
		this.imp = imp;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		OmeImage image = new OmeImage(38256);
		image.setPixelInfoFromImageId();
		image.fetchPixels();
		image.createImagePlus();
	
		ImagePlus mimp = image.getImage();
		ImageStack stack = mimp.getImageStack();
		ImageProcessor ip = stack.getProcessor(stack.getSize());
		ImagePlus imp2 = new ImagePlus("TR image",  ip);
		Extractor ex = new Extractor(imp2);
		
		RegionFeatures r = new RegionFeatures(imp2);
		HashMap<Byte, ArrayList<Integer>>  rm = r.findSrmRegions(15.f);
		double[] com = r.calcRegionCOM(rm.get((byte)6));
	
		double xpos = com[0];
		double ypos = com[1];
		ImageProcessor p = ex.imp.getProcessor();
		Color color = new Color(255,255,255,125);
		p.setColor(color);
		p.setLineWidth(2);
		int delta = 12;
		int xstart = (int)xpos - delta;
		int xstop = (int)xpos + delta;
		int ystart = (int)ypos - delta;
		int ystop = (int)ypos + delta;
		p.moveTo(xstart, (int)ypos);
		p.lineTo(xstop, (int)ypos);
		p.moveTo((int)xpos, ystart);
		p.lineTo((int)xpos, ystop);
		ex.imp.show();
	}	

}
