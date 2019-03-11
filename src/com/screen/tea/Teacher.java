package com.screen.tea;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.screen.utils.Constant;
import com.screen.utils.DataChange;
import com.screen.utils.ScreenUtils;

public class Teacher extends Thread {
	private int curFrame = 0;

	@Override
	public void run() {
		try (DatagramSocket ds = new DatagramSocket();){
			startTeacher(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startTeacher(DatagramSocket ds) throws Exception {
		DatagramPacket dp = new DatagramPacket(new byte[] {}, 0, InetAddress.getByName(Constant.IP_IMG), Constant.PORT_IMG);
		while (true) {
			++curFrame;
			byte[] datas=ScreenUtils.cutScreen();		
			List<byte[]> blockList = creatBlockList(datas);
			for (byte[] bs : blockList) {
				dp.setData(bs);
				ds.send(dp);
			}
		}
	}

	public List<byte[]> creatBlockList(byte[] data) {
		List<byte[]> blockList = new ArrayList<byte[]>();

		int blockSize = 60 * 1024;
		int rest = data.length % blockSize;
		int blockCount = data.length / blockSize;
		if (rest != 0) {
			blockCount += 1;
		}

		for (int i = 0; i < blockCount; i++) {
			byte[] curFrameData;
			if (rest != 0 && i == (blockCount - 1)) {
				curFrameData = new byte[rest + 6];
			} else {
				curFrameData = new byte[blockSize + 6];
			}

			byte[] curFrameNumBytes = DataChange.int2Bytes(curFrame);

			System.arraycopy(curFrameNumBytes, 0, curFrameData, 0, 4);

			curFrameData[4] = (byte) blockCount;

			curFrameData[5] = (byte) i;

			System.arraycopy(data, i * blockSize, curFrameData, 6, curFrameData.length - 6);

			blockList.add(curFrameData);
		}
		return blockList;
	}

}
