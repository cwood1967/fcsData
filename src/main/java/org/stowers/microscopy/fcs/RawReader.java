package org.stowers.microscopy.fcs;

import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * The header is the first 128 bytes
 * Each piece of data is 4 bytes (32 bits), little endian
 */
public class RawReader {

	double FREQ = 20000000.0;
	double P = 1. / FREQ;
	String fcsurlname;
	URL fcsurl = null;

	byte[] header;
	byte[] rawdata = null;
	long[] arrivalCounts = null;
	double[] arrivalTimes = null;
	double minTime;
	double maxTime;

	public RawReader(String fcsurlname) {
		this.fcsurlname = fcsurlname;
		try {
			fcsurl = new URL(fcsurlname);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readUrl() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = fcsurl.openStream();
			byte[] chunk = new byte[4096 * 4];
			int n;

			n = is.read(chunk);
			while (n > 0) {
				baos.write(chunk, 0, n);
				n = is.read(chunk);
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		byte[] raw = baos.toByteArray();

		header = new byte[128];
		rawdata = new byte[raw.length - 128];
		System.out.println(rawdata.length);
		System.out.println(raw.length);
		for (int i = 0; i < 128; i++) {
			header[i] = raw[i];
		}

		for (int i = 0; i < (rawdata.length); i++) {
			rawdata[i] = raw[i + 128];
		}
		rawToArrival();
	}

	public double[][] getTrajectory(double binsize) {
		double res[][] = null;

		// bin to the round of maxTime
		// double last = Math.round(maxTime);

		int nbins = (int) (maxTime / binsize) + 1;
		double[] bins = new double[nbins + 1];
		res = new double[2][nbins];

		for (int i = 0; i <= nbins; i++) {
			bins[i] = i * binsize;
		}

		for (int i = 0; i < nbins; i++) {
			res[0][i] = (bins[i + 1] + bins[i]) / 2.;
		}

		int ntimes = arrivalTimes.length;
		int ib = 0;
		int kkkk= 0;
		for (int i = 0; i < ntimes; i++) {
			while (arrivalTimes[i] >= bins[ib + 1]) {
				ib++;
			}
			if (ib > (nbins - 1)) {
				System.out.println("****** " + nbins + " " + ib);
				break;
			}
			res[1][ib]++;
			kkkk = i;
		}
		System.out.print(kkkk + " kk " + " " + arrivalTimes[kkkk] + " " + minTime + " " + maxTime + " " + bins.length);
		System.out.println(" " + bins[nbins] + " " + res[1].length + 
				" " + res[0][0] + " " + res[1][0] + " " + ib + " " + nbins + " " + ntimes) ;
		return res;
	}

	private void rawToArrival() {
		int nphotons = rawdata.length / 4;
		arrivalCounts = new long[nphotons];
		arrivalTimes = new double[nphotons];
		int pnum = 0;
		long totalTicks = 0;
		for (int i = 0; i < nphotons; i++) {
			int mask = 0xFF;
			/*
			 * It is important to only use 0xFF (255) (1111 1111) as the mask
			 * otherwise strange things happen
			 */
			int x = ((rawdata[pnum + 3] & mask) << 24)
					+ ((rawdata[pnum + 2] & mask) << 16)
					+ ((rawdata[pnum + 1] & mask) << 8)
					+ ((rawdata[pnum]) & mask);

			totalTicks += x;
			arrivalCounts[i] = totalTicks;
			arrivalTimes[i] = P * totalTicks;
			pnum += 4;
		}

		minTime = arrivalTimes[0];
		maxTime = arrivalTimes[nphotons - 1];
		System.out.println(FREQ);
	}

	public double[] getArrivalTimes() {
		return arrivalTimes;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "file:///Users/cjw/YBR218C_fcs_Jan_10_2012_0_0_8_feba41fa4fbed13f616e3c9cbe8a54c3_R1_P1_K1_Ch2.raw";
//		String url = "file:///Volumes/projects/jjl/HCS-FCS/autocorrsubsetdata/Feb_09_2012/000_AutoScan.mdb/YBR115C_000_Well_33.mdb/YBR115C_000_fcs_Feb_09_2012_33_0_0_d3bb31c947f9c77e5014f6b01c4ee339_R1_P1_K1_Ch2.raw";
		RawReader r = new RawReader(url);
		
		r.readUrl();
		double[] c = r.getArrivalTimes();
		double[][] t = r.getTrajectory(1./50000.);
		
		double t1;
		double t2;
		
		t1 = System.nanoTime();
		Correlator aca = new Correlator();
		double[][] ya = aca.correlate(t[1], t[1], t[0]);
		t2 = System.nanoTime();
		System.out.println("Time 2: " + 1e-6 * (t2 - t1));
		
//		t1 = System.nanoTime();
//		CommonsCorrelator ac = new CommonsCorrelator();
//		double[] y = ac.correlate(t[1], t[1], t[0]);
//		t2 = System.nanoTime();
//		System.out.println("Time 1: " + 1e-6 * (t2 - t1));
		
		
//		TestFcsPlots p = new TestFcsPlots("Test", t[0], y);
//		p.getChart();
		TestFcsPlots pa = new TestFcsPlots("Test", ya[0], ya[1]);
		System.out.println("Points " + ya[0].length);
		pa.getChart();
		System.out.println("Done");
//		double[] bf = ac.bruteForceAutoCorrelation();
//		TestFcsPlots p1 = new TestFcsPlots("Test", t[0], bf);
//		p1.getChart();
//		System.out.println(bf[0] + " " +  bf[1] + " " +  bf[2]);

	}

}
