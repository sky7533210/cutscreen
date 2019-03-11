package com.screen.mainui;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.screen.stu.SoundClient;
import com.screen.stu.Student;
import com.screen.stu.StudentUI;
import com.screen.tea.SoundServer;
import com.screen.tea.Teacher;
import com.screen.utils.Constant;

public class MainUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private ButtonGroup buttonGroup;
	private TrayIcon trayIcon;

	public MainUI() {
		this.setSize(Constant.WINDOWS_DIMENSION.width / 4, Constant.WINDOWS_DIMENSION.height / 2);
		JPanel jPanel1 = new JPanel();
		BoxLayout bly = new BoxLayout(jPanel1, BoxLayout.Y_AXIS);
		jPanel1.setLayout(bly);

		Font font = new Font("宋体", Font.PLAIN, 24);
		JRadioButton jbStu = new JRadioButton("学生端", true);
		jbStu.setActionCommand("stu");
		jbStu.setFont(font);
		JRadioButton jbtea = new JRadioButton("教师端");
		jbtea.setActionCommand("tea");
		jbtea.setFont(font);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(jbStu);
		buttonGroup.add(jbtea);

		JPanel jPanel2 = new JPanel();
		jPanel2.add(jbStu);
		jPanel2.add(jbtea);

		jPanel1.add(jPanel2);

		JPanel jPanel3 = new JPanel();
		JButton jbtnStart = new JButton("开始");
		jbtnStart.setActionCommand("start");
		jbtnStart.addActionListener(this);
		jbtnStart.setFont(font);
		jPanel3.add(jbtnStart);
		jPanel1.add(jPanel3);

		this.setLayout(null);

		jPanel1.setBounds((this.getWidth() - 300) / 2, (this.getHeight() - 120) / 2, 300, 120);

		this.add(jPanel1);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(trayIcon!=null) {
					SystemTray.getSystemTray().remove(trayIcon);
				}
				System.exit(0);
			}
		});
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new MainUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			if (buttonGroup.getSelection().getActionCommand().equals("stu")) {
				StudentUI ui = new StudentUI(this, getInsets().top, getInsets().left);
				Student stu = new Student(ui);
				stu.start();
				
				new SoundClient().start();
				
				this.setVisible(false);
			} else if (buttonGroup.getSelection().getActionCommand().equals("tea")) {
				this.setVisible(false);
				((JButton)e.getSource()).setEnabled(false);
				showTrayIcon();				
				new Teacher().start();					
				new SoundServer().start();	
			}
		}
	}
	private BufferedImage img1;
	private BufferedImage img2;
	public void showTrayIcon() {
		try {
			InputStream is= MainUI.class.getClassLoader().getResourceAsStream("icon.png");
			img1 = ImageIO.read(is);
			is= MainUI.class.getClassLoader().getResourceAsStream("icon2.png");
			img2 = ImageIO.read(is);
		} catch (IOException e2) {
			e2.printStackTrace();
		}			
		trayIcon = new TrayIcon(img1);
		trayIcon.setToolTip("正在录屏");
		trayIcon.setImageAutoSize(true);
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		PopupMenu pmenu = new PopupMenu();
		MenuItem open = new MenuItem("打开");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainUI.this.setVisible(true);
			}
		});
		pmenu.add(open);
		MenuItem exit = new MenuItem("关闭");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SystemTray.getSystemTray().remove(trayIcon);
				System.exit(0);
			}
		});
		pmenu.add(exit);
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					MainUI.this.setVisible(true);
				}
			}
		});
		trayIcon.setPopupMenu(pmenu);
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						trayIcon.setImage(img1);
						Thread.sleep(800);
						trayIcon.setImage(img2);
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
}
