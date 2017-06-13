package com.xxt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

public class HttpServer {
	int iport;
	static int oldPort=0;
	String ifile;
	ServerSocket serverSocket=null;
	/**
	 * WEB_ROOT是HTML和其它文件存放的目录. 这里的WEB_ROOT为工作目录下的webroot目录
	 */
	//public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
	public static  String WEB_ROOT = "";

	// 关闭服务命令
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

	public HttpServer(int iport, String ifile) {
		this.iport=iport;
		System.out.println(ifile);
		WEB_ROOT=ifile.replaceAll("\\\\","\\\\\\\\");
		if(!ifile.endsWith("\\")){
			WEB_ROOT+="\\\\";
		}
		System.out.println(WEB_ROOT);
	}

	

	public  void await() {
		
		System.out.println(iport);
		try {
			//服务器套接字对象
			if(serverSocket==null){
				if(oldPort!=iport){
					serverSocket = new ServerSocket(iport, 1, InetAddress.getByName("127.0.0.1"));
					oldPort=iport;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
		// 循环等待一个请求
		while (true) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				//等待连接，连接成功后，返回一个Socket对象
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// 创建Request对象并解析
				Request request = new Request(input);
				request.parse();
				
				// 检查是否是关闭服务命令
				if (request.getUri().equals(SHUTDOWN_COMMAND)) {
					break;
				}

				// 创建 Response 对象
				Response response = new Response(output);
				response.setRequest(request);
				response.sendStaticResource();

				// 关闭 socket 对象
				socket.close();

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
}
