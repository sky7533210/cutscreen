package com.screen.stu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import com.screen.utils.Constant;
import com.screen.utils.DataChange;

public class Student extends Thread {

	private StudentUI ui;

	private MulticastSocket ms;

	public HashMap<Integer, HashMap<Integer, Message>> messageMap = new HashMap<Integer, HashMap<Integer, Message>>();

	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	private byte[] buff = new byte[1024 * 10];

	public Student(StudentUI ui) {
		try {

			this.ui = ui;
			ms = new MulticastSocket(Constant.PORT_IMG);
			ms.joinGroup(InetAddress.getByName(Constant.IP_IMG));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			byte[] buf = new byte[60 * 1024 + 6];
			DatagramPacket pack = new DatagramPacket(buf, buf.length);

			while (true) {
				ms.receive(pack);
				Message msg = parsePack(pack);
				putMessageIntoMap(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Message parsePack(DatagramPacket pack) {

		byte[] srcByte = pack.getData();
		int len = pack.getLength();
		Message msg = new Message();

		int flag = DataChange.bytes2Int(srcByte);

		msg.setFlag(flag);
		msg.setTotleFrame(srcByte[4]);
		msg.setCurFrameNum(srcByte[5]);

		byte[] data = new byte[len-6];
		System.arraycopy(srcByte, 6, data, 0, data.length);
		msg.setData(data);

		return msg;
	}

	private void putMessageIntoMap(Message msg) {
		int flag = msg.getFlag();
		int totleCount = msg.getTotleFrame();

		HashMap<Integer, Message> map;
		if (messageMap.containsKey(flag)) {
			map = messageMap.get(flag);
		}else {
			map = new HashMap<Integer, Message>();
		}
		map.put(msg.getCurFrameNum(), msg);
		messageMap.put(flag, map);

		if (map.size() >= totleCount) {
			Set<Integer> keySet = map.keySet();
			List<Integer> list = new ArrayList<Integer>(keySet.size());
			for (Integer i : keySet) {
				list.add(i);
			}

			Collections.sort(list);

			try {
				for (Integer i : list) {
					baos.write(map.get(i).getData());
				}
				byte[] bs = baos.toByteArray();
				baos.reset();			
				ByteArrayInputStream bis = new ByteArrayInputStream(bs);
				GZIPInputStream zis = new GZIPInputStream(bis);
				int length = 0;
				
				while ((length = zis.read(buff)) != -1) {
					baos.write(buff, 0, length);
				}
				ui.updateScreens(baos.toByteArray());
				baos.reset();
			} catch (Exception e) {
				e.printStackTrace();
				messageMap.remove(flag);
			}
			messageMap.remove(flag);
		}
	}
}
