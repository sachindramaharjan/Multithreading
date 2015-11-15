package com.test.multithreading.singleton;

import org.junit.Test;

import com.java.multithreading.singleton.SingleThreadedSingleton;

import org.junit.Assert;

public class SingleThreadedSingletonTest {

	@Test
	public void getInstance() {

		SingleThreadedSingleton obj1 = SingleThreadedSingleton.getInstance();
		SingleThreadedSingleton obj2 = SingleThreadedSingleton.getInstance();

		Assert.assertEquals(true, obj1 == obj2);
		Assert.assertNotNull(obj1);
		Assert.assertNotNull(obj2);

	}

}
