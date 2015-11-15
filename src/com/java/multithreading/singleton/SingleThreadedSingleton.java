package com.java.multithreading.singleton;

public class SingleThreadedSingleton {

	public static SingleThreadedSingleton instance;

	private SingleThreadedSingleton() {
	}

	public static SingleThreadedSingleton getInstance() {
		if (instance == null) {
			instance = new SingleThreadedSingleton();
		}
		return instance;
	}
}
