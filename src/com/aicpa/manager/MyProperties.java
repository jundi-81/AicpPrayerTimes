package com.aicpa.manager;

public class MyProperties {
	private static MyProperties mInstance = null;

	static int someValueIWantToKeep;

	protected MyProperties() {
	}

	public static synchronized MyProperties getInstance() {
		if (null == mInstance) {
			mInstance = new MyProperties();
		}
		return mInstance;
	}
}