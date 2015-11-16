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

/*
 First you might notice that the lock() method is no longer declared synchronized. Instead only the blocks necessary to synchronize are nested inside synchronized blocks.

FairLock creates a new instance of QueueObject and enqueue it for each thread calling lock(). The thread calling unlock() will take the top QueueObject in the queue and call doNotify() on it, to awaken the thread waiting on that object. This way only one waiting thread is awakened at a time, rather than all waiting threads. This part is what governs the fairness of the FairLock.

Notice how the state of the lock is still tested and set within the same synchronized block to avoid slipped conditions.

Also notice that the QueueObject is really a semaphore. The doWait() and doNotify() methods store the signal internally in the QueueObject. This is done to avoid missed signals caused by a thread being preempted just before calling queueObject.doWait(), by another thread which calls unlock() and thereby queueObject.doNotify(). The queueObject.doWait() call is placed outside the synchronized(this) block to avoid nested monitor lockout, so another thread can actually call unlock() when no thread is executing inside the synchronized(this) block in lock() method.

Finally, notice how the queueObject.doWait() is called inside a try - catch block. In case an InterruptedException is thrown the thread leaves the lock() method, and we need to dequeue it.
*/

class FairLock {

	private boolean isLocked = false;
	private Thread lockingThread = null;
	private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();

	public void lock() throws InterruptedException {
		QueueObject obj = new QueueObject();
		boolean mustWait = true;

		synchronized (this) {
			waitingThreads.add(obj);
		}

		while (mustWait) {
			synchronized (this) {
				mustWait = this.isLocked || (waitingThreads.get(0) != obj);
				while (!mustWait) {
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
