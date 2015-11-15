package com.java.multithreading.singleton;

/**
 * Multi-threaded Singleton using Double locking idiom
 * 
 * The above approach also has some issues. The initialization of an object and
 * the creation of instance of that class are 2 different operations. Once the
 * instance of the class has been created that instance is then used to
 * initialize the values of its state either with default values or some user
 * defined values. Now between these 2 operations another thread might invoke
 * the method to get the instance and sees that the instance is already not null
 * and then proceeds with using that instance. Though its the correct instance
 * we wanted the thread to use but that instance hasn’t been initialized yet.
 * This issue is called as Unsafe Publication.
 * 
 * @author Internet
 *
 */
public final class MultiThreadedSingleton {

	/*
	 * Using volatile keyword, when a thread initializes the Object, a
	 * happens-before-relationship is established between current thread and
	 * other threads
	 * 
	 */
	public volatile static MultiThreadedSingleton instance = null;

	private MultiThreadedSingleton() {
	}

	public static MultiThreadedSingleton getInstance() {
		if (instance == null) {
			synchronized (MultiThreadedSingleton.class) {
				if (instance == null) {
					instance = new MultiThreadedSingleton();
				}
			}
		}

		return instance;
	}
}
