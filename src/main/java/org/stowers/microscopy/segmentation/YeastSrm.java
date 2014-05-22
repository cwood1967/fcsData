package org.stowers.microscopy.segmentation;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ShortProcessor;

public class YeastSrm extends SRM {

    int width;
    int height;
    byte[] imgArray = null;
    boolean averages;
    ByteProcessor ip = null;

    public YeastSrm(int width, int height, byte[] pixels) {
        this.width = width;
        this.height = height;
        this.imgArray = pixels;
        ip = new ByteProcessor(width, height, imgArray);
        ImagePlus imp = new ImagePlus("Original",  ip);
        setup("sdsdsd", imp);
    }  
    
    public YeastSrm(ImagePlus oimp) {
        this.width = oimp.getWidth();
        this.height = oimp.getHeight();

        this.imgArray = 
        		(byte[])oimp.getProcessor().convertToByteProcessor().getPixels(); 
        ip = new ByteProcessor(width, height, imgArray);
        ImagePlus imp = new ImagePlus("Original" , ip);
        setup("sdsdsd", imp);
    }  
    
    public void setQval(float q) {
        Q = q;
    }


    public float[] ijSRMavg() {
    /* this assumes that the imput is a byte array, because that is what the 
     * SRM algorithm implementation requires
     */
        averages = true;
        ImagePlus imp0 = new ImagePlus("Original", ip);
         
        ImagePlus imp = srm2D(ip, averages); 
        float[] resArray = (float[])imp.getProcessor().getPixels();
        return resArray;

    }

    public ImagePlus ijSRM(boolean averages) {
    	
    	ImagePlus resip = srm2D(ip, averages);
    	return resip;
    }
    public int[] ijSRMregions() {
    /* this assumes that the imput is a byte array, because that is what the 
     * SRM algorithm implementation requires
     */
        averages = false;
        ImagePlus imp0 = new ImagePlus("Original", ip);
         
        ImagePlus imp = srm2D(ip, averages); 
        ShortProcessor sip = imp.getProcessor().convertToShortProcessor();
        int[] resArray = new int[width*height];
        short[] temp = (short[])sip.getPixels();
        for (int i=0; i < temp.length; i++) {
            resArray[i] = (int)temp[i];
        }
        return resArray;

    }

}
