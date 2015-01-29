package org.stowers.microscopy.fcs;

import java.util.ArrayList;

import org.jtransforms.fft.DoubleFFT_1D;

public class Correlator {

	double[] times = null;
	double[] a1 = null;
	double[] a2 = null;
	double[] c1 = null;
	double[] c2 = null;
	int nx;
	double padfactor = 1;

	public Correlator() {
		// TODO Auto-generated constructor stub
	}

	public double[][] correlate(double a1[], double[] a2, double[] times) {
		this.a1 = a1;
		this.a2 = a2;
		this.times = times;

		double mean1 = this.mean(a1);
		double mean2 = this.mean(a2);
		System.out.println("Mean " + mean2);
		double[] av1 = arraySubract(a1, mean1);
		double[] av2 = arraySubract(a2, mean2);

		double[] v1 = pad(av1, 0);
		double[] v2 = pad(av2, 0);
		double[] f1 = realToComplex(v1);
		double[] f2 = realToComplex(v2);

		this.nx = v1.length;

		DoubleFFT_1D fft = new DoubleFFT_1D(nx);

		/*
		 * the result of the fft will be symmetric because the input is real
		 */
//		fft.realForward(v1);
//		fft.realForward(v2);
		fft.complexForward(f1);
		fft.complexForward(f2);

		// correlate by multiplying f1 and *f2
		double[] cf2 = conjugate(f2);
//		double[] cf2 = conjugate(v2);
		
//		the imaginary parts should all be zero 
		double[] fcor = multiplyComplex(f1, cf2);

		double check = fcor[2];
		double check2 = fcor[3];
		System.out.println("XXXX: " + check + " " + check2);

		DoubleFFT_1D ifft = new DoubleFFT_1D(nx);
		ifft.complexInverse(fcor, true);
		double[] rcor = meanNormalize(real(fcor), mean1, mean2);
		System.out.println(rcor[0] + " " + rcor[1] + " " + 2. * fcor[0] + " "
				+ fcor.length);
		return binCorrelation(rcor);
//		return answer(rcor);
	}

	private double[] realToComplex(double[] v) {
		int n = 2 * v.length;
		double[] c = new double[n];

		int index = 0;
		for (int i = 0; i < v.length; i++) {
			c[index] = v[i];
			index++;
			c[index] = 0;
			index++;
		}

		return c;
	}

	private double[] real(double[] v) {
		double[] res = new double[v.length / 2];
		int index = 0;
		for (int i = 0; i < v.length; i += 2) {
			res[index] = v[i];
			index++;
		}
		return res;
	}

	private double[] magnitude(double[] v) {
		double[] res = new double[v.length / 2];
		int index = 0;
		for (int i = 0; i < v.length; i += 2) {
			res[index] = Math.sqrt(v[i] * v[i] + v[i + 1] * v[i + 1]);
			index++;
		}
		return res;
	}

	private double[][] answer(double[] v) {
		double[][] res = new double[2][a1.length / 2 - 1];
		for (int i = 0; i < res[0].length; i++) {
			res[1][i] = v[i + 1];
			res[0][i] = times[i + 1];
		}
		return res;
	}

	private double[] conjugate(double[] v) {
		double[] c = new double[v.length];
		for (int i = 0; i < v.length; i++) {
			if (i % 2 == 0) {
				c[i] = v[i];
			} else {
				c[i] = -v[i];
			}
		}
		return c;
	}

	private double[] multiplyComplex(double[] v1, double[] v2) {

		int n = v1.length;
		double[] res = new double[n];
		for (int i = 0; i < n / 2; i += 2) {
			double a = v1[i];
			double b = v1[i + 1];
			double c = v2[i];
			double d = v2[i + 1];

			res[i] = a * c - b * d;
			res[i + 1] = a * d + c * b;
		}
		return res;
	}

	private double mean(double[] v) {

		double total = 0;
		for (int i = 0; i < v.length; i++) {
			total += v[i];
		}

		return total / v.length;
	}

	private double[] arraySubract(double[] v, double mv) {
		double[] res = new double[v.length];
		for (int i = 0; i < v.length; i++) {
			res[i] = v[i] - mv;
		}
		return res;
	}

	private double[] meanNormalize(double[] v, double mv1, double mv2) {
		double[] nv = new double[v.length];
		for (int i = 0; i < v.length; i++) {
			// use the class variable nx, because v could be from complex
			double b = mv1 * mv2 * nx;
			double fudge = 2.; //use this to multiply by 2 if needed
			nv[i] = fudge * padfactor * v[i] / b;
		}
		return nv;
	}

	private double[] pad(double[] v, double c) {

		double n2 = Math.floor((Math.log(v.length) / Math.log(2.))) + 0;
		n2++;
		int need = (int) Math.pow(2, n2);

		int padlength = need - nx;
		double[] pad = new double[need];
		for (int i = 0; i < v.length; i++) {
			pad[i] = v[i];
		}
		for (int i = v.length; i < padlength; i++) {
			pad[i] = c;
		}

		padfactor = (double) pad.length / (double) v.length;
		System.out.println("PADFACTOR: " + padfactor);
		return pad;
	}

	private double[][] binCorrelation(double[] v) {

		double[] bintimes = createBins(400);


		int ibin = 0;
		int counter = 0;
		
		ArrayList<Double> aclist = new ArrayList<Double>();
		ArrayList<Double> timelist = new ArrayList<Double>();
		
		int index = 1;
		for (int i =0; i < bintimes.length; i++) {
			double total = 0.;
			double norm = 0.;
			while (times[index] <= bintimes[i]) {
				total += v[index];
				index++;
				norm += 1.0;
			}
			
			if (norm > 0) {
				aclist.add(total/norm);
				timelist.add(bintimes[i]);
			}
					
		}
		
		double[][] res = new double[2][aclist.size()];
		for (int i = 0; i < aclist.size(); i++) {
			res[1][i] = aclist.get(i);
			res[0][i] = timelist.get(i);
		}
		
		return res;
	}

	private double[] createBins(int nbins) {
		double mintime = times[0];
		double maxtime = times[times.length / 2];

		double logmin = Math.floor(Math.log10(mintime));
		double logmax = Math.ceil(Math.log10(maxtime));
		double binmax = (Math.ceil(logmax - logmin));

		double del = binmax / nbins;

		double y = logmin + del;
		
		Double a = mintime;
		ArrayList<Double> list = new ArrayList<Double>();
		while (a < maxtime) {
			a = Math.pow(10, y);
			if (a > mintime) {
				list.add(a);
			}
			y += del;
		}
		
		double[] res = new double[list.size()];
		
		for (int i = 0; i < res.length; i++) {
			res[i] = list.get(i);
		}
		return res;
	}

	public double[] bruteForceAutoCorrelation() {
		double[] ac = new double[a1.length];
		int n = a1.length;
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < n; i++) {
				ac[j] += a1[i] * a1[(n + i - j) % n];
			}
		}
		return ac;
	}

	public double[] getTimes() {
		return times;
	}

}
