package org.stowers.microscopy;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;


import fcs.imagetools.OmeImage;

public class Image_from_ome implements PlugIn {

	int image_id;
	int server_id;
	int width;
	int height;
	int channels;
	
	OmeImage omeimage = null;
	ImagePlus image = null;
	
	public static void main(String args[]) {
		Image_from_ome x = new Image_from_ome();
		x.run("sdsd");
	}
	
	
	public void setImageId(int id) {
		image_id = id;
	}

    public ImagePlus getImage() {
        if (image == null) {
            fetchImage();
        }
        return image;
    }

	public void run(String args) {
		
		if (!showDialog()) {
			return;
		}
		//image_id = 64567;
		fetchImage();
		image.show();
		float[] pixels = (float[])image.getStack().getPixels(2);
		System.out.println(pixels.length);
		System.out.println(pixels[0] + " " +  pixels[200000]);
	}
	
	public void fetchandshow() {
		fetchImage();
		image.show();
	}
	public boolean  showDialog() {
		GenericDialog d = new GenericDialog("Fetch an Image from OME",
											IJ.getInstance());
		
		int iid = 0;
		d.addNumericField("Image Id" , iid, 0);
		d.showDialog();
		if (d.wasCanceled()) {
			return false;
		}
		
		if (d.invalidNumber()) {
			IJ.showMessage("Error", "Invalid Input");
			return false;
		}
		
		image_id = (int)d.getNextNumber();
		return true;
	}
	
//	public showFxDialog() {
//		prim
//	}
	public void fetchImage() {
		omeimage = new OmeImage(image_id);
		omeimage.setPixelInfoFromImageId();
		omeimage.fetchPixels();
		if (omeimage.excep != null) {
			IJ.log(omeimage.excep.getMessage());
		}
		omeimage.createImagePlus();
		image = omeimage.getImage();
		
	}
}
