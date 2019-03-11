package com.screen.stu;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import com.screen.utils.Constant;

public class SoundClient extends Thread {

	@Override
	public void run() {
		try (MulticastSocket ds = new MulticastSocket(Constant.PORT_SOUND);){
			ds.joinGroup(InetAddress.getByName(Constant.IP_SOUND));
			startSoundReceive(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void startSoundReceive(MulticastSocket ds) throws Exception {
		
		
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4, 44100F, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();
		FloatControl fc = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
		double value = 2;
		float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
		fc.setValue(dB);
		final int bufSize = 4096;
		byte[] buffer = new byte[bufSize];
		DatagramPacket dp = new DatagramPacket(buffer, bufSize);
		while (true) {
			ds.receive(dp);
			int len = dp.getLength();
			sourceDataLine.write(buffer, 0, len);
		}
	}
}
