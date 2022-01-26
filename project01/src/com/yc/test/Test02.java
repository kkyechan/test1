package com.yc.test;

import java.net.*;
import java.io.*;
import java.util.*;

public class Test02 extends Thread {
//	static ArrayList<Socket> list = new ArrayList<>();
	static HashMap<Object, Socket> map = new HashMap<>();
	static ArrayList<String> list = new ArrayList<>();
	Socket sock = null;
	
	public void sendAll(String msg){
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			Iterator<Object> keys = map.keySet().iterator();
			while(keys.hasNext()){
				Object key = keys.next();
				Socket sock = map.get(key);
				System.out.println(key + "\n" + sock);
				System.out.println(map.size());
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				String msg1 = "";
				for(int i = 0; i < list.size(); i++){
					msg1 += list.get(i) + " ";
				}
				bw.write("*" + msg);
				bw.newLine();
				bw.write("!" + msg1);
				bw.newLine();
				bw.write("#" + Integer.toString(map.size()));
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			is = sock.getInputStream();
			os = sock.getOutputStream();
			isr = new InputStreamReader(is);
			osw = new OutputStreamWriter(os);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(osw);
			
			String name = br.readLine();
			map.put(name, sock);
			list.add(name);
			
			while(true){
				String msg = br.readLine();
				if(msg.startsWith("@")){
					msg = msg.substring(1);
					map.remove(msg);
					list.remove(msg);
				}else{
					sendAll(msg);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ServerSocket serv = null;
		
		try {
			serv = new ServerSocket(5001);
			while(true){
				Test02 me = new Test02();
				me.sock = serv.accept();
				me.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serv.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
