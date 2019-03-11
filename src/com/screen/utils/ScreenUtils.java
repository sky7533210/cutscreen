package com.screen.utils;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import com.webcam.capture.WebcamCapture;

import net.coobird.thumbnailator.Thumbnails;

public class ScreenUtils {
	private static Robot rb;
	private static Rectangle rectangle = new Rectangle(0, 0, Constant.WINDOWS_DIMENSION.width, Constant.WINDOWS_DIMENSION.height);
	private static ByteArrayOutputStream bos=new ByteArrayOutputStream(64*1024);
	static {
		try {
			rb = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	public static byte[] cutScreen() throws IOException{
		
		 BufferedImage img = rb.createScreenCapture(rectangle);
		 Graphics g=img.getGraphics();
		 BufferedImage cam=WebcamCapture.getCapture();
		 g.drawImage(cam, 0,0 , cam.getWidth(), cam.getHeight(), cam.getWidth(), 0, 0, cam.getHeight(), null);		 
		 g=null;
		 cam=null;
		 System.gc();
		 bos.reset();
		 //long time=System.currentTimeMillis();
		 Thumbnails.of(img).scale(1).outputQuality(1).outputFormat("jpeg").toOutputStream(bos);		
		 //System.out.println(System.currentTimeMillis()-time);
		 img=null;
		 System.gc();
		 byte[] datas=bos.toByteArray();
		 bos.reset();
		 GZIPOutputStream zos=new GZIPOutputStream(bos);
		 zos.write(datas);
		 zos.finish();
		 zos.close();
		 zos=null;
		
		 return bos.toByteArray();
	}
}
