package com.screen.stu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.screen.utils.Constant;

import net.coobird.thumbnailator.Thumbnails;

public class StudentUI extends JFrame implements ActionListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private JLabel jLabel;
	private JFrame jFrame;
	private JPanel glassPane;
	private boolean isFull = false;
	private JButton jButton;
	private int top;
	private int left;

	public StudentUI(JFrame jFrame, int top, int left) {
		this.jFrame = jFrame;
		this.setTitle("学生端");
		this.top = top;
		this.left = left;
		this.setSize(Constant.WINDOWS_DIMENSION.width / 2 + 2 * left,
				Constant.WINDOWS_DIMENSION.height / 2 + top + left);
		this.setLocationRelativeTo(null);
		init();
		this.setVisible(true);
	}

	private void init() {
		jButton = new JButton("全屏");
		jButton.setActionCommand("full");
		jButton.addActionListener(this);
		glassPane = new JPanel();
		glassPane.add(jButton);
		glassPane.setOpaque(false);
		this.setGlassPane(glassPane);
		glassPane.setVisible(true);
		jLabel = new JLabel();
		this.add(jLabel);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jFrame.setVisible(true);
				StudentUI.this.dispose();
			}
		});
	}

	private ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public void updateScreens(byte[] bs) {
		bos.reset();
		try {
			Thumbnails.of(new ByteArrayInputStream(bs)).size(jLabel.getWidth(), jLabel.getHeight()).toOutputStream(bos);
			bs = bos.toByteArray();
			ImageIcon icon = new ImageIcon(bs);
			jLabel.setIcon(icon);
			bs=null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("full")) {
			if (isFull) {
				this.removeMouseMotionListener(this);
				isFull = !isFull;
				jButton.setText("全屏");
				this.dispose();
				this.setSize(Constant.WINDOWS_DIMENSION.width / 2 + 2 * left,
						Constant.WINDOWS_DIMENSION.height / 2 + top + left);
				glassPane.setVisible(true);
				this.setUndecorated(false);
				this.setLocationRelativeTo(null);
				this.setVisible(true);
			} else {
				this.addMouseMotionListener(this);
				isFull = !isFull;
				jButton.setText("退出全屏");
				this.dispose();
				this.setSize(Constant.WINDOWS_DIMENSION);
				glassPane.setVisible(false);
				this.setUndecorated(true);
				this.setLocation(0, 0);
				this.setVisible(true);
			}

		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getY() < 50) {
			glassPane.setVisible(true);
		} else {
			glassPane.setVisible(false);
		}
	}
}
