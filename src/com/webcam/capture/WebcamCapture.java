package com.webcam.capture;

import java.awt.image.BufferedImage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class WebcamCapture {
	private static Webcam webcam;
	static {
		webcam = Webcam.getDefault();		
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.open();
	}
	public static BufferedImage getCapture() {
		return  webcam.getImage();	
	}
}
 