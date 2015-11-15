package com.java.multithreading;

public class RacingCondition {
	static Counter counter = new Counter();

	public static class Counter {
		private int counter = 0;

		public void add(int value) {
			synchronized (this) {
				counter += value;
				print();
			}

		}

		public void print() {
			System.out.println("counter: " + counter);
		}

	}
	
	
	public static class MyRunnable implements Runnable {

		int value = 0;

		public MyRunnable(int value) {
			this.value = value;
		}

		public void run() {
			counter.add(this.value);
		}

	}

	public static void main(String[] args) {
		Thread th1 = new Thread(new MyRunnable(2), "Th1");
		Thread th2 = new Thread(new MyRunnable(3), "Th2");

		th1.start();
		th2.start();

	}

}
