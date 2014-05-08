package fcs.imagetools;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.LUT;
import ij.IJ;
import ij.ImageStack;
import ij.ImageJ;
import ij.plugin.CompositeConverter;
import ij.CompositeImage;
import ij.plugin.Converter;
import ij.process.ImageConverter;
import ij.io.FileSaver;
import ij.gui.Line;

import java.awt.Color;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

import java.util.ArrayList;
import java.util.Hashtable;


public class OmeImage extends fcsdata {

	short[] pixels;
	// UnsignedShortImage image = null;
	long[] dimensions;
	//int width;
	//int height;
	//int channels;
	ImageStack stack; 
	ImageStatistics[] imagestats = null;
	ImagePlus image = null;
	final LUT graylut = LUT.createLutFromColor(Color.GRAY);
	final LUT greenlut = LUT.createLutFromColor(Color.GREEN);
	final LUT redlut = LUT.createLutFromColor(Color.RED);
	ImagePlus finalimage = null;

	public boolean arePixelsNull() {
		if (pixels == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public ImagePlus getImage() {
		return image;
	}
	
	public OmeImage(int image_id, String schema) {
		super(image_id, schema);
		//setPixelInfoFromImageId();		
	}
	
	public OmeImage(int image_id) {
		super(image_id);
		//setPixelInfoFromImageId();		
	}
	public void savePNG(ImagePlus ci) {
		FileSaver saver = new FileSaver(ci);
		saver.saveAsPng("/tmp/my.png");
		saver.saveAsJpeg("/tmp/my.jpg");
	}
	
	public void savemarkedimage(String filename) {
		FileSaver saver = new FileSaver(finalimage);
		saver.saveAsJpeg(filename);
	}
	public void drawmark(ImagePlus img) {
//		Line line = new Line(234.4, 120.5, 240.4, 126.5);
//		line.drawPixels(img.getProcessor());
		ImagePlus flat = img.flatten();
		Color color = new Color(255,255,0,125);
		ImageProcessor p = flat.getProcessor();
		
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
		finalimage = flat;
		//p.fill();
		//flat.show();
		//long t1 =  System.nanoTime();
		//savePNG(flat);
		//long t2 = System.nanoTime();
		//System.out.println("Save time " + (t2-t1)*1e-6);
		
	}
	public void fetchPixels() {

		if (server_id <= 0){
			return;
		}
		try {
			//String urlstring = "http://ome/cgi-bin/omeis?Method=GetPixels&PixelsID=100226&BigEndian=1";
			String urlstring = "http://ome/cgi-bin/omeis?Method=GetPixels&PixelsID="
					+ Integer.toString(server_id) + "&BigEndian=1";
			
			//System.out.println(urlstring);
			URL url = new URL(urlstring);
			URLConnection urlconn = url.openConnection();

			String ctype = urlconn.getContentType();
			int clength = urlconn.getContentLength();
//			System.out.println(ctype);
//			System.out.println(clength);
			InputStream raw = urlconn.getInputStream();
			InputStream in = new BufferedInputStream(raw);

			int bytesread = 0;
			int totalbytes = 0;
			ArrayList<Byte> b = new ArrayList<Byte>();
			while (bytesread != -1) {
				byte[] data = new byte[width * height * channels *2];
				bytesread = in.read(data);
				if (bytesread == -1)
					break;
				for (int i = 0; i < bytesread; i++) {
					b.add(data[i]);
				}
				totalbytes += bytesread;
			}

			pixels = new short[b.size() / 2];
			int count = 0;
			for (int i = 0; i < b.size(); i += 2) {
				byte z1 = b.get(i);
				byte z2 = b.get(i + 1);
				pixels[count] = (short) (((z1 & 0xFF) << 8) + (z2 & 0xFF));
				count++;
			}
//			System.out.println(totalbytes);
//			System.out.println(b.size());
		} catch (IOException e) {
			e.printStackTrace();
			pixels = null;
			return;
		}

	}

	public void setDimensions(int width, int height, int channels) {
		dimensions = new long[] { width, height, channels };
		this.width = width;
		this.height = height;
		this.channels = channels;
	}

	public void createImagePlus() {
		stack = new ImageStack(width, height);

		if (pixels == null) {
			IJ.log("The pixels are null");
			return;
		}
//		FloatProcessor sp = new FloatProcessor(width, height);
		imagestats = new ImageStatistics[channels];
		for (int k = 0; k < channels; k++) {
			//System.out.println(k);
			float[] slicepixels = new float[width * height];

			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					int pnum = k * width * height + i * width + j;
					int spnum = i * width + j;
					slicepixels[spnum] = pixels[pnum];
				}
			}
			FloatProcessor tempfp = new FloatProcessor(width, height,
					slicepixels);
			imagestats[k] = tempfp.getStatistics();
			stack.addSlice(Integer.toString(k), tempfp.getPixels());
		}
		image = new ImagePlus();
		image.setStack(stack, channels, 1, 1);
		setDisplayRange();
		
	}

	public void setDisplayRange() {
		int nc = image.getNChannels();
		for (int i = 0; i < nc; i++) {
			ImageStatistics stats = imagestats[i];
			stack.getProcessor(i + 1).setMinAndMax(stats.histMin + 1,
					stats.histMax);

			
		}	
	}

	public void printminmax() {
		for (int i = 0; i < channels; i++) {
			double a = image.getStack().getProcessor(i + 1).getMin();
			double b = image.getStack().getProcessor(i + 1).getMax();
			System.out.println(i + "MINMAX " + a + " " + b);
		}
	}

	public void printminmax(int channel) {
		double a = image.getStack().getProcessor(channel).getMin();
		double b = image.getStack().getProcessor(channel).getMax();
		System.out.println("***" + channel + "MINMAX " + a + " " + b);
		System.out.println("-----");
	}

	public void printminmax(ImageStack stack) {
		for (int i = 0; i < channels; i++) {
			double a = stack.getProcessor(i + 1).getMin();
			double b = stack.getProcessor(i + 1).getMax();
			System.out.println(i + "MINMAX " + a + " " + b);
		}
	}

	public CompositeImage makeComposite() {
		CompositeImage compimage = new CompositeImage(image,
				CompositeImage.COMPOSITE);
		compimage.setChannelLut(graylut, channels);
		if (channels == 2) {
			compimage.setChannelLut(greenlut, 1);
		}
		if (channels == 3) {
			compimage.setChannelLut(redlut, 1);
			compimage.setChannelLut(greenlut, 2);
		}
		double min;
		double max;
		for (int i = 0; i < channels; i++) {
			compimage.setC(i + 1);
			if (i == channels - 1) {
				min = imagestats[i].min/2.;
				max = imagestats[i].mean + 2.*imagestats[i].stdDev;
			}
			else { 
				min = imagestats[i].histMin + 1;
				max = imagestats[i].histMax -1;
			}
			compimage.setDisplayRange(min, max);
		}
		return compimage;
	}

	public void showimage(ImagePlus imp) {
		imp.show();
	}

	public void showfinalimage() {
		finalimage.show();
	}
	
	public LUT makegrayLUT() {
		byte[] lutarray = new byte[256];
		for (byte i = 0; i < 256; i++) {
			lutarray[i] = i;
			System.out.println(i);
		}
		return new LUT(lutarray, lutarray, lutarray);
	}
	
	public LUT makegreenLUT() {
		byte[] red = new byte[256];
		byte[] green = new byte[256];
		byte[] blue = new byte[256];
		for (byte i = 0; i < 256; i++) {
			green[i] = i;
			red[i] = 0;
			blue[i] = 0;
			System.out.println(i);
		}
		return new LUT(red, green, blue);
	}
}
