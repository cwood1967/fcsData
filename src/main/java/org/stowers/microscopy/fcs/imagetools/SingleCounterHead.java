package org.stowers.microscopy.fcs.imagetools;

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public class SingleCounterHead {

	public SingleCounterHead() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		for (int i = 0; i < 100; i++) {
			int id = i + 22000;
			PhotonCounter counter = new PhotonCounter(id);
			try {
				long c = counter.call();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done");
	}
}
