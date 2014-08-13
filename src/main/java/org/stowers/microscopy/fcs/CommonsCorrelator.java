package org.stowers.microscopy.fcs;

import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformUtils;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;

public class CommonsCorrelator {

	double[] times;
	double padfactor;
	double[] a1 = null;
	double[] a2 = null;
	int nx;
	
	public CommonsCorrelator() {
		// TODO Auto-generated constructor stub	
	}

	public double[] correlate(double[] a1, double[] a2, double[] times) {
		this.a1 = a1;
		this.a2 = a2;
		this.times = times;
		
		double mean1 = this.mean(a1);
		double mean2 = this.mean(a2);
		System.out.println("Mean " + mean2);
		double[] av1 = arraySubract(a1, mean1);
		double[] av2 = arraySubract(a2, mean2);
		double[] res = null;
		double[] v1 = pad(av1, 0);
		double[] v2 = pad(av2, 0);
		Complex[] ca1 = realToComplex(v1);
		Complex[] ca2 = realToComplex(v2);
		
		FastFourierTransformer fft1 = 
				new FastFourierTransformer(DftNormalization.STANDARD);
		
//		FastFourierTransformer fft2 = 
//				new FastFourierTransformer(DftNormalization.STANDARD);
		
		Complex[] f1 = fft1.transform(ca1, TransformType.FORWARD);
		Complex[] f2 = fft1.transform(ca2, TransformType.FORWARD);
		Complex[] fac = new Complex[v1.length];

		for (int i= 0; i < v1.length; i++) {
			fac[i] = f1[i].multiply(f2[i].conjugate());
		}
		
		double acheck  = fac[1].getReal();
		double acheck2  = fac[1].getImaginary();
		System.out.println("AAAA: " + acheck + " " + acheck2); 
		
		this.nx = v1.length;
		Complex[] cres = fft1.transform(fac, TransformType.INVERSE);
		
		res = new double[a1.length/2];
		double check  = padfactor*cres[0].getReal()/mean1/mean2/v1.length;
		double check2  = cres[0].getReal();
		System.out.println(check + " " + cres[0]); 
		double b = padfactor/(mean1*mean2*v1.length);
		for (int i =1; i< a1.length/2; i++) {
//			res[i-1] = padfactor*cres[i].getReal()/mean1/mean2/v1.length;
			res[i-1] = b*cres[i].getReal();
//			res[i-1] = fac[i-1].getReal();
		}
		return res;
	}
	
	private Complex[] realToComplex(double[] v) {
//		int n = 2*v.length;
		Complex[] c = new Complex[v.length];
		
		int index = 0;
		for (int i = 0; i < v.length; i++) {
			c[i] = new Complex(v[i]);
		}
		
		return c;
	}
	
	private double mean(double[] v) {
		
		double total = 0;
		for (int i = 0; i < v.length; i++) {
			total += v[i];
		}
		
		return total/v.length;
	}
	
	private double[] arraySubract(double[] v, double mv) {
		double[] res = new double[v.length];
		for (int i = 0; i < v.length; i++) {
			res[i] = v[i] - mv;
		}
		return res;
	}
	
	private double[] pad(double[] v, double c) {
		
		double n2 = Math.floor((Math.log(v.length)/Math.log(2.))) + 0;
		n2++;
		int need = (int)Math.pow(2, n2);
		
		int padlength = need - nx;
		double[] pad = new double[need];
		for (int i=0; i < v.length; i++) {
			pad[i] = v[i];
		}
		for (int i = v.length; i < padlength; i++) {
			pad[i] = c;
		}
		
		padfactor = (double)pad.length/(double)v.length;
		System.out.println("PADFACTOR: " + padfactor);
		return pad;
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
