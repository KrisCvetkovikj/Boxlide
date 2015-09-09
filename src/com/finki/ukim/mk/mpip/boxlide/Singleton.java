package com.finki.ukim.mk.mpip.boxlide;

public class Singleton {
	
	public int highscore = Integer.MAX_VALUE;

	private static Singleton mInstance = null;

	protected Singleton() {
	}

	public static synchronized Singleton getInstance() {
		if (null == mInstance) {
			mInstance = new Singleton();
		}
		return mInstance;
	}
}