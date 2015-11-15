package com.java.multithreading;

public class DeadlockDemo {

	public static void main(String[] args) {
		NoDeadlock deadlock = new NoDeadlock();
		Thread th1 = new Thread(deadlock.new DeadlockRunnable1(), "Thread1");
		Thread th2 = new Thread(deadlock.new DeadlockRunnable2(), "Thread2");
		th1.start();
		th2.start();
	}

}

/*
 * Senario for deadlock
 * 
 * */
class Deadlock {
	public void method1() {

		synchronized (String.class) {
			System.out.println("String object locked >> " + Thread.currentThread().getName());
			System.out.println("Waiting for Integer object >> " + Thread.currentThread().getName());
			synchronized (Integer.class) {
				System.out.println("Integer object locked >> " + Thread.currentThread().getName());
			}
		}

	}

	public void method2() {
		synchronized (Integer.class) {
			System.out.println("Integer object locked >>" + Thread.currentThread().getName());
			System.out.println("Waiting for String object >>" + Thread.currentThread().getName());
			synchronized (String.class) {
				System.out.println("String object locked >>" + Thread.currentThread().getName());
			}
		}
	}

	class DeadlockRunnable1 implements Runnable {
		public void run() {
			while (true) {
				method1();
			}
		}
	}

	class DeadlockRunnable2 implements Runnable {
		public void run() {
			while (true) {
				method2();
			}
		}
	}
}

/*
 * Acquire lock on objects on the same order
 * 
 * */
class NoDeadlock {
	public void method1() {

		synchronized (String.class) {
			System.out.println("String object locked >> " + Thread.currentThread().getName());
			System.out.println("Waiting for Integer object >> " + Thread.currentThread().getName());
			synchronized (Integer.class) {
				System.out.println("Integer object locked >> " + Thread.currentThread().getName());
			}
		}

	}

	public void method2() {
		synchronized (String.class) {
			System.out.println("String object locked >>" + Thread.currentThread().getName());
			System.out.println("Waiting for String object >>" + Thread.currentThread().getName());
			synchronized (Integer.class) {
				System.out.println("Integer object locked >>" + Thread.currentThread().getName());
			}
		}
	}

	class DeadlockRunnable1 implements Runnable {
		public void run() {
			while (true) {
				method1();
			}
		}
	}

	class DeadlockRunnable2 implements Runnable {
		public void run() {
			while (true) {
				method2();
			}
		}
	}
}
