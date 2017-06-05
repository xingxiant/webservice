package com.xxt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window {
	static String port="";
	static String file="";
	/*
	 * 添加组件的方法：
	 */
	public static void addComponentdToPane(Container pane) {
		JButton buttonStart,buttonStop;	//创建启动，结束按钮
		JLabel lable1,lable2;           //创建标签对象
		JTextField textFile;            //创建文本框对象
		JTextField textPort;            //创建文本框对象
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
		conLablePort.weighty=0.5;									//水平方向权重
		conLablePort.gridx=1;                                       //第二列
		conLablePort.gridy=1;										//第二行
		conLablePort.insets=new Insets(0,0,0,20);
		topPane.add(textPort,conLablePort);
		
		//file文本框
		GridBagConstraints conLableText=new GridBagConstraints();  //创建约束对象
		conLableText.fill=GridBagConstraints.HORIZONTAL;				  //组件水平扩展到整个单元格
		textFile=new JTextField();
		conLableText.weightx=0.8;									//水平方向权重
		conLableText.weighty=0.5;									//水平方向权重
		conLableText.gridx=1;                                       //第二列
		conLableText.gridy=0;										//第一行
		conLableText.insets=new Insets(10,0,10,20);
		topPane.add(textFile,conLableText);
		
		buttonStart=new JButton("启动");
		
		buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	port=textPort.getText();
            	file=textFile.getText();
            	new Thread(new   Runnable(){

            		@Override
            		public void run() {
            			int iport=Integer.parseInt(port);
            			int ifile=Integer.parseInt(file);
            			HttpServer server = new HttpServer(iport,ifile);
            			//等待连接请求
            			server.await();
            			
            		}
            		
            	}).start();
            }
        });
		buttonStop=new JButton("停止");
		bottomPane.add(buttonStart);
		bottomPane.add(buttonStop);
		
		pane.add(topPane,BorderLayout.CENTER);
		pane.add(bottomPane,BorderLayout.PAGE_END);

	}
	/*
	 * 创建界面并显示
	 */
	public static void createView() {
		//创建并设置窗口
		JFrame frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置内容面板
		addComponentdToPane(frame.getContentPane());
		//显示窗口
		frame.pack();
		frame.setVisible(true);
		
		

	}
	
	public static void main(String[] args) {
		
		Window.createView();

	}
	

}

