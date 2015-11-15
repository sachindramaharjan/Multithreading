package com.java.multithreading;

public class ThreadSafeWithImmutableObject {

	static ImmutableClass immutable = new ImmutableClass(5);
	static Calculator mycalculator = new Calculator(immutable);

	public static void main(String[] args) {

		Thread th1 = new Thread(new MyRunnable(mycalculator, 5), "Thread 1");
		Thread th2 = new Thread(new MyRunnable(mycalculator, 3), "Thread 2");

		th1.start();
		th2.start();
	}

}

class MyRunnable implements Runnable {

	Calculator calculator = null;
	int value = 0;

	public MyRunnable(Calculator calculator, int newvalue) {
		this.calculator = calculator;
		this.value = newvalue;
	}

	public void run() {
		calculator.add(this.value);
	}
}

class ImmutableClass {

	int value = 0;

	public ImmutableClass(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/*
	 * Objects are thread safe but its references are not thread safe since add
	 * method can modify the value
	 * 
	 */
	public ImmutableClass add(int newvalue) {
		return new ImmutableClass(this.value + newvalue);
	}
}

/*
 * The Calculator class holds a reference to an ImmutableValue instance. Notice
 * how it is possible to change that reference through both the setValue() and
 * add() methods. Therefore, even if the Calculator class uses an immutable
 * object internally, it is not itself immutable, and therefore not thread safe.
 * In other words: The ImmutableValue class is thread safe, but the use of it is
 * not. This is something to keep in mind when trying to achieve thread safety
 * through immutability. To make the Calculator class thread safe you could have
 * declared the getValue(), setValue(), and add() methods synchronized. That
 * would have done the trick.
 */
class Calculator {
	ImmutableClass immutable = null;

	public Calculator(ImmutableClass immutable) {
		this.immutable = immutable;
	}

	public synchronized ImmutableClass getImmutableClass() {
		return immutable;
	}

	public synchronized void setImmutableClass(ImmutableClass immutable) {
		this.immutable = immutable;
	}

	public synchronized void add(int newvalue) {
		this.immutable = this.immutable.add(newvalue);
		System.out.println(this.immutable.getValue() + " >> " + Thread.currentThread().getName());
	}
}