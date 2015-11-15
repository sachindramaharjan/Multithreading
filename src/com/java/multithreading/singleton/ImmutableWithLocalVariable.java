package com.java.multithreading.singleton;

/**
 * Double Locking with Local Variable and Helper class
 * 
 * @author Internet
 *
 */
public class ImmutableWithLocalVariable implements Runnable {
	private Helper helper;
	int count = 0;

	public Helper getHelper(int n) {
		Helper h = helper; // unsynchronized read on helper

		if (h == null) {
			synchronized (this) {
				h = helper; // synchronized read of helper
				if (h == null) {
					h = new Helper(count);
					helper = h;
				}
			}
		}

		return h;
	}

	public void run() {
		while (true) {
			Helper h = getHelper(++count);
			System.out.println("value: "  + h.get() + " >>> " + Thread.currentThread().getName() + " Count: " + count);
		}
	}

	public static void main(String[] args) {
		ImmutableWithLocalVariable obj1 = new ImmutableWithLocalVariable();

		Thread th1 = new Thread(obj1, "Thread 1");
		Thread th2 = new Thread(obj1, "Thread 2");
		Thread th3 = new Thread(obj1, "Thread 3");
		th1.start();
		th2.start();
		th3.start();
	}
}

final class Helper {
	private final int n;

	public Helper(int n) {
		this.n = n;
	}

	public int get() {
		return n;
	}
}