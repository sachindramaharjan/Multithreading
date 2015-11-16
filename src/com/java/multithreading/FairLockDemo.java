package com.java.multithreading;

import java.util.ArrayList;
import java.util.List;

public class FairLockDemo {
	public static void main(String[] args) {
		FairLockDemo fld = new FairLockDemo();

		Thread th1 = new Thread(fld.new Synchronizer(), "Thread 1");
		Thread th2 = new Thread(fld.new Synchronizer(), "Thread 2");
		Thread th3 = new Thread(fld.new Synchronizer(), "Thread 3");
		Thread th4 = new Thread(fld.new Synchronizer(), "Thread 4");
		th4.start();
		th3.start();
		th2.start();
		th1.start();
	}

	class Synchronizer implements Runnable {

		private FairLock lock = new FairLock();

		public void doSynchronize() throws InterruptedException {
			this.lock.lock();
			System.out.println("Thread: " + Thread.currentThread().getName());
			this.lock.unlock();
		}

		public void run() {
			int count = 0;
			while (count < 6) {
				count++;
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
}

class FairLock {

	private boolean isLocked = false;
	private Thread lockingThread = null;
	private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();

	public void lock() throws InterruptedException {
		QueueObject obj = new QueueObject();
		boolean isLockedForThisThread = true;

		synchronized (this) {
			waitingThreads.add(obj);
		}

		while (isLockedForThisThread) {
			synchronized (this) {
				isLockedForThisThread = this.isLocked || (waitingThreads.get(0) != obj);
				while (!isLockedForThisThread) {
					isLocked = true;
					waitingThreads.remove(0);
					lockingThread = Thread.currentThread();
					return;
				}
			}

			try {
				obj.doWait();
			} catch (InterruptedException ex) {
				waitingThreads.remove(obj);
				throw ex;
			}
		}

	}

	public synchronized void unlock() throws InterruptedException {
		if (this.lockingThread != Thread.currentThread()) {
			throw new InterruptedException("The current thread is not locked this lock");
		}

		isLocked = false;
		lockingThread = null;
		if (waitingThreads.size() > 0) {
			waitingThreads.get(0).doNotify();
		}

	}

}

class QueueObject {
	private boolean isNotified = false;

	public synchronized void doWait() throws InterruptedException {
		while (!isNotified) {
			this.wait();
		}
		isNotified = false;
	}

	public synchronized void doNotify() {
		isNotified = true;
		this.notify();
	}
}
