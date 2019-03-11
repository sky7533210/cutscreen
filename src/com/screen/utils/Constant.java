package com.screen.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Constant {
	public static final int PORT_IMG=12590;
	public static final int PORT_SOUND=12591;
	public static final String IP_IMG="226.5.7.7";
	public static final String IP_SOUND="226.5.7.8";
	public static final Dimension WINDOWS_DIMENSION;
	static {
		WINDOWS_DIMENSION= Toolkit.getDefaultToolkit().getScreenSize();
	}
}
