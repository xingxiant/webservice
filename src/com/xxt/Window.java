package com.xxt;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window {
	//标志
	static boolean isOpen=false;

	private static TrayIcon trayIcon = null;
	static SystemTray tray = SystemTray.getSystemTray();

	private static String port="";
	private static int iport;
	private static String file="";
	private volatile static HttpServer server;
	private static Thread thread;
	
	private static JButton buttonStart,buttonStop ;	//创建启动，结束按钮
	private static JTextField textFile;            //创建文本框对象
	private static JTextField textPort;            //创建文本框对象
	private static MenuItem start = new MenuItem("启动");
	private static MenuItem stop = new MenuItem("暂停");
	
	//创建并设置窗口
	static JFrame frame=new JFrame();
	/*
	 * 添加组件的方法：
	 */
	public static void addComponentdToPane(Container pane) {

		
		JLabel lable1,lable2;           //创建标签对象
		JPanel topPane=new JPanel(); 	//创建放置标签与文本框的面板
		//设置为网格包布距
		topPane.setLayout(new GridBagLayout()); 
		JPanel bottomPane=new JPanel(); //创建放置按钮的面板

		//创建流动布距对象
		FlowLayout flowLayout=new FlowLayout();
		flowLayout.setHgap(80);  		//组件间水平距离
		flowLayout.setVgap(20);			//组件间垂直距离
		bottomPane.setLayout(flowLayout);

		//标签一
		GridBagConstraints conLable1=new GridBagConstraints();  //创建约束对象
		conLable1.fill=GridBagConstraints.NONE;				  //组件保持自然大小
		lable1=new JLabel("端口号");
		conLable1.weightx=0.2;									//水平方向权重
		conLable1.gridx=0;                                       //第一列
		conLable1.gridy=0;										//第一行
		conLable1.anchor=GridBagConstraints.LINE_END;			//标签靠右对齐
		topPane.add(lable1,conLable1);

		//标签二
		GridBagConstraints conLable2=new GridBagConstraints();  //创建约束对象
		conLable2.fill=GridBagConstraints.NONE;				  //组件保持自然大小
		lable2=new JLabel("路径");

		conLable2.gridx=0;                                       //第一列
		conLable2.gridy=1;										//第一行
		conLable2.anchor=GridBagConstraints.LINE_END;			//标签靠右对齐
		topPane.add(lable2,conLable2);

		//port文本框
		GridBagConstraints conLablePort=new GridBagConstraints();  //创建约束对象
		conLablePort.fill=GridBagConstraints.HORIZONTAL;				  //组件水平扩展到整个单元格
		textPort=new JTextField();
		conLablePort.weighty=0.8;									//竖直方向权重
		conLablePort.weightx=0.5;									//水平方向权重
		conLablePort.gridx=1;                                       //第二列
		conLablePort.gridy=0;										//第二行
		conLablePort.insets=new Insets(10,0,10,20);
		topPane.add(textPort,conLablePort);

		//file文本框
		GridBagConstraints conLableText=new GridBagConstraints();  //创建约束对象
		conLableText.fill=GridBagConstraints.HORIZONTAL;				  //组件水平扩展到整个单元格
		textFile=new JTextField();
		conLableText.weighty=0.5;									//竖直方向权重
		conLableText.gridx=1;                                       //第二列
		conLableText.gridy=1;										//第一行
		conLableText.insets=new Insets(0,0,0,20);
		topPane.add(textFile,conLableText);
		buttonStart=new JButton("启动");
		buttonStop=new JButton("停止");
		//按键状态初始化设置
		if(!isOpen){
			buttonStart.setEnabled(true);
			buttonStop.setEnabled(false);
		}
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//FIXME 启动按钮监听
				//旧的port值
				int oldPort=iport;
				port=textPort.getText();
				file=textFile.getText();
				iport=Integer.parseInt(port);
				if(server==null||iport!=oldPort){
					server=new HttpServer(iport,file);
				}
				thread=new Thread(new Runnable(){
					@Override
					public void run() {
						server.await();

					}

				});
				thread.start();
				isOpen=true;
				//设置按钮显示状态
				if(isOpen){
					buttonStart.setEnabled(false);
					start.setEnabled(false);
					buttonStop.setEnabled(true);
					stop.setEnabled(true);
				}
				System.out.println("启动成功！");
			}
		});

		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("关闭！");
				isOpen=false;
				thread.stop();
				if(!isOpen){
					buttonStart.setEnabled(true);
					start.setEnabled(true);
					buttonStop.setEnabled(false);
					stop.setEnabled(false);
				}
			}
		});

		bottomPane.add(buttonStart);
		bottomPane.add(buttonStop);
		pane.add(topPane,BorderLayout.CENTER);
		pane.add(bottomPane,BorderLayout.PAGE_END);

	}
	/*
	 * 创建界面并显示
	 */
	public static void createView() {

		frame.addWindowListener(new WindowAdapter() { // 窗口关闭事件
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			};

			public void windowIconified(WindowEvent e) { // 窗口最小化事件

				frame.setVisible(false);
				Window.miniTray();

			}

		});

		frame.setLocation(500, 500);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置内容面板
		addComponentdToPane(frame.getContentPane());
		//显示窗口
		frame.pack();
		frame.setVisible(true);

	}



	private static void miniTray() {  //窗口最小化到任务栏托盘

		ImageIcon trayImg = new ImageIcon("D://mouse.png");//托盘图标

		PopupMenu pop = new PopupMenu();  //增加托盘右击菜单
		MenuItem show = new MenuItem("显示主界面");
		MenuItem exit = new MenuItem("退出");
		

		show.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { // 按下还原键

				tray.remove(trayIcon);
				frame.setVisible(true);
				frame.setExtendedState(JFrame.NORMAL);
				frame.toFront();
			}

		});

		exit.addActionListener(new ActionListener() { // 按下退出键

			public void actionPerformed(ActionEvent e) {

				tray.remove(trayIcon);
				System.exit(0);

			}

		});
		
		start.addActionListener(new ActionListener() { // 按下启动键

			public void actionPerformed(ActionEvent e) {

				    //FIXME 启动按钮监听
					//旧的port值
					int oldPort=iport;
					port=textPort.getText();
					file=textFile.getText();
					iport=Integer.parseInt(port);
					if(server==null||iport!=oldPort){
						server=new HttpServer(iport,file);
					}
					thread=new Thread(new Runnable(){
						@Override
						public void run() {
							server.await();

						}

					});
					thread.start();
					isOpen=true;
					//设置按钮显示状态
					if(isOpen){
						buttonStart.setEnabled(false);
						start.setEnabled(false);
						buttonStop.setEnabled(true);
						stop.setEnabled(true);
					}
					System.out.println("启动成功！");

			}

		});

		stop.addActionListener(new ActionListener() { // 按下停止键

			public void actionPerformed(ActionEvent e) {

				System.out.println("关闭！");
				isOpen=false;
				thread.stop();
				if(!isOpen){
					buttonStart.setEnabled(true);
					start.setEnabled(true);
					buttonStop.setEnabled(false);
					stop.setEnabled(false);
				}
			}

		});

		pop.add(show);
		pop.add(exit);
		pop.add(start);
		pop.add(stop);

		trayIcon = new TrayIcon(trayImg.getImage(), "web服务器", pop);
		trayIcon.setImageAutoSize(true);

		trayIcon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) { // 鼠标器双击事件

				if (e.getClickCount() == 2) {

					tray.remove(trayIcon); // 移去托盘图标
					frame.setVisible(true);
					frame.setExtendedState(JFrame.NORMAL); // 还原窗口
					frame.toFront();
				}

			}

		});

		try {

			tray.add(trayIcon);

		} catch (AWTException e1) {
			
			e1.printStackTrace();
		}

	}
	public static void main(String[] args) {

		Window.createView();

	}
	public class MyRun implements Runnable {
		public void run() {
			server.await();
		}
	}

}


