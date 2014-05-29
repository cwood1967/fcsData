package org.stowers.microscopy.fcs.imagetools;


//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

public class CounterHead_TJ {

	public CounterHead_TJ() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		int num = 2;
		int ids = 9000;
		if (args.length > 0) {
			num = Integer.parseInt(args[0]);
			ids = Integer.parseInt(args[1]);
		}
		Double startTime = 0.001*(new java.util.Date().getTime());
		ForkJoinPool pool = new ForkJoinPool();
		List<Future<Long>> countlist = new ArrayList<Future<Long>>();

		for (int i = 0; i < num; i++) {
			int id = i + ids;
			PhotonCounterTask counter = new PhotonCounterTask(id);
			countlist.add(pool.submit(counter));
		}

		float total = 0.f;
		for (Future<Long> f : countlist) {
			try {
				System.out.println(f.get());
				total += f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Double runTime =  0.001*(new java.util.Date().getTime())- startTime;
		System.out.println(total + " " + total / countlist.size());
		System.out.println(runTime + "--  Done");
	}
}
