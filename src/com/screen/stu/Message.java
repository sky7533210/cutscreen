package com.screen.stu;

public class Message {

	 
	private int flag;
	private int totleFrame;
	private int curFrameNum;
	private byte [] data;
	
	public Message() {
		super();
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getTotleFrame() {
		return totleFrame;
	}
	public void setTotleFrame(int totleFrame) {
		this.totleFrame = totleFrame;
	}
	public int getCurFrameNum() {
		return curFrameNum;
	}
	public void setCurFrameNum(int curFrameNum) {
		this.curFrameNum = curFrameNum;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

}
