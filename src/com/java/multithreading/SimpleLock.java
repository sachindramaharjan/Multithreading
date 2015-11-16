package com.java.multithreading;

public class SimpleLock {

	public static void main(String[] args) {
		Thread th1 = new Thread(new Synchronizer(), "Thread 1");
		Thread th2 = new Thread(new Synchronizer(), "Thread 2");
		Thread th3 = new Thread(new Synchronizer(), "Thread 3");
		Thread th4 = new Thread(new Synchronizer(), "Thread 4");
		th4.start();
		th3.start();
		th2.start();
		th1.start();
	}

}

class Lock {
	private boolean isLocked = false;
	private Thread lockingThread = null;

	public synchronized void lock() throws InterruptedException {
		if (isLocked) {
			wait();
		}

		isLocked = true;
		lockingThread = Thread.currentThread();
	}

	public synchronized void unlock() throws InterruptedException {
		if (this.lockingThread != Thread.currentThread()) {
			throw new InterruptedException("Calling thread has not locked this thread");
		}

		isLocked = false;
		lockingThread = null;
		notify();
	}
}

class Synchronizer implements Runnable {

	private Lock lock = new Lock();

	public void doSynchronize() throws InterruptedException {
		this.lock.lock();
		System.out.println("Thread: " + Thread.currentThread().getName());
		this.lock.unlock();
	}

	public void run() {
		while (true) {
			try {
				doSynchronize();
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
