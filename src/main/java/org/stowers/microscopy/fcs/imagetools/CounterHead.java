package org.stowers.microscopy.fcs.imagetools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

public class CounterHead {

	public CounterHead() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		int num = 2;
		if (args.length > 0) {
			num = Integer.parseInt(args[0]);
		}
		// ExecutorService executor = Executors.newFixedThreadPool(num);
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<Integer>> countlist = new ArrayList<Future<Integer>>();

		for (int i = 0; i < 200; i++) {
			int id = i + 22000;
			Callable counter = new PhotonCounter(id);
			Future<Integer> future = executor.submit(counter);
			countlist.add(future);
		}

		float total = 0.f;
		for (Future<Integer> f : countlist) {
			try {
				total += f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(total + " " + total / countlist.size());
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Done");
	}
}
