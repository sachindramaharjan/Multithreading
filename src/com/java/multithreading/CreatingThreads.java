package com.java.multithreading;

public class CreatingThreads {

	public static class MyRunnable implements Runnable {

		public void run() {
			System.out.println("Hello");
			Thread thread = Thread.currentThread();
			String threadName = thread.getName();
			System.out.println("I am " + threadName);

		}
	}

	public static class MyThread extends Thread {
		public void run() {
			System.out.println("Hello");
			Thread thread = Thread.currentThread();
			String threadName = thread.getName();
			System.out.println("I am " + threadName);
		}

	}

	public static void main(String[] args) {

		Thread myRunnableThread = new Thread(new MyRunnable(), "MyRunnable");
		myRunnableThread.start();

		new Thread("MyThread") {
			public void run() {
				System.out.println("Hi this is a test thread");
				System.out.println("My name is " + getName());
			}
		}.start();

	}

}
