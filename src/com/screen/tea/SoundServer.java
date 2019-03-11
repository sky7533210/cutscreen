package com.screen.tea;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.screen.utils.Constant;

public class SoundServer extends Thread{
	@Override
	public void run() {
		
		try (DatagramSocket ds = new DatagramSocket();){
			startSoundSend(ds);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	private void startSoundSend(DatagramSocket ds) throws Exception {
		
			AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4, 44100F, true);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
			TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			
			int nByte = 0;
			final int bufSize = 4096;
			byte[] buffer = new byte[bufSize];
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(Constant.IP_SOUND), Constant.PORT_SOUND);
			while (nByte != -1) {
				nByte = targetDataLine.read(buffer, 0, bufSize);
				dp.setData(buffer, 0, nByte);
				ds.send(dp);
			}
	}
}
