package com.java.multithreading;

public class ThreadSignalling {

	public static void main(String[] args) {
		Queue q = new Queue();
		new Producer(q);
		new Consumer(q);
		System.out.println("Press Control-C to stop.");
	}
}


class Queue {
	private int n;
	boolean isDataReady = false;

	public synchronized int get() {

		// wait until data is not ready
		while (!isDataReady) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		isDataReady = false;
		notify();
		System.out.println("Get: " + n);
		return n;
	}

	public synchronized void put() {
		// wait until there is no new data available
		while (isDataReady) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		n++;
		isDataReady = true;
		notify();
		System.out.println("Put: " + n);
	}

}

class Producer implements Runnable {

	Queue queue;

	public Producer(Queue queue) {
		this.queue = queue;
		new Thread(this, "Producer").start();
	}

	public void run() {
		while (true) {
			queue.put();
		}
	}
}

class Consumer implements Runnable {
	Queue queue;

	public Consumer(Queue queue) {
		this.queue = queue;
		new Thread(this, "Consumer").start();
		;
	}

	public void run() {
		while (true) {
			this.queue.get();
		}

	}
}

/*
 class MessageQueue {
	int n;

	public MessageQueue() {
	}

	public MessageQueue(int n) {
		this.n = n;
	}

	public int get() {
		return n;
	}

	public void set(int n) {
		this.n = n;
	}
}

class MyNotifyModel {
	MessageQueue msgQueue;
	boolean signal = false;

	public MyNotifyModel(MessageQueue msgQueue) {
		this.msgQueue = msgQueue;
	}

	public void doWait() {
		// monitor on MessageQueue class
		synchronized (msgQueue) {
			while (!signal) {
				try {
					msgQueue.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// clear signal and continue running
			signal = false;
		}

	}

	public void doNotify() {
		synchronized (msgQueue) {
			signal = true;
			msgQueue.notify();
		}
	}

}
 
 */
