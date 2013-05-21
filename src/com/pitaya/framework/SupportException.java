package com.pitaya.framework;

public class SupportException extends Exception{
	private static final long serialVersionUID = 1L;
	private String mExtra;

	public SupportException(String message) {
		super(message);
	}

	public SupportException(String message, String extra) {
		super(message);
		mExtra = extra;
	}

	public String getExtra() {
		return mExtra;
	}
}
