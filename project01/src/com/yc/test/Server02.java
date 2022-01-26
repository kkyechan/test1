package com.yc.test;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server02 extends Thread {
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
	
	public void sendNameList(){
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			Iterator<Object> keys = map.keySet().iterator();
			while(keys.hasNext()){
				Object key = keys.next();
				Socket sock = map.get(key);
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				String msg1 = "";
				for(int i = 0; i < list.size(); i++){
					msg1 += list.get(i) + " ";
				}
				bw.write(msg1);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void sendNameList(){
//		OutputStream os = null;
//		OutputStreamWriter osw = null;
//		BufferedWriter bw = null;
//		try {
//			os = sock.getOutputStream();
//			osw = new OutputStreamWriter(os);
//			bw = new BufferedWriter(osw);
//			String msg1 = "";
//			for(int i = 0; i < list.size(); i++){
//				msg1 += list.get(i) + " ";
//			}
//			bw.write("%" + msg1);
//			bw.newLine();
//			bw.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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
			
			while(true){
				String msg = br.readLine();
				if(msg.startsWith("$")){
					msg = msg.substring(1);
					map.put(msg, sock);
					list.add(msg);
				}else if(msg.startsWith("@")){
					msg = msg.substring(1);
					map.remove(msg);
					list.remove(msg);
				}else if(msg.startsWith("null")){
					
//				}else if(msg.startsWith("%")){
//					sendNameList();
				}else{
					sendAll(msg);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File file = new File("data.text");
		InputStream fis = null;
		InputStreamReader fisr = null;
		BufferedReader fbr = null;
		OutputStream fos = null;
		OutputStreamWriter fosw= null;
		BufferedWriter fbw = null;
		
		ServerSocket serv = null;
		
		try {
			fis = new FileInputStream("data.text");
			fisr = new InputStreamReader(fis);
			fbr = new BufferedReader(fisr);
			fos = new FileOutputStream("data.text", true);
			fosw = new OutputStreamWriter(fos);
			fbw = new BufferedWriter(fosw);
			
			if(!file.exists()){
				file.createNewFile();
			}
			
			
			serv = new ServerSocket(5001);
			while(true){
				Server02 me = new Server02();
				me.sock = serv.accept();
				me.start();
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fbw.close();
				fosw.close();
				fos.close();
				fbr.close();
				fisr.close();
				fis.close();
				serv.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
