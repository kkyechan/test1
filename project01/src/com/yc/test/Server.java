package com.yc.test;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Thread {
	static HashMap<String, Socket> map = new HashMap<>();
	static ArrayList<String> list = new ArrayList<>();
	Socket sock = null;
	
	// Map�� ����ִ� ��� Socket�� �޼��� ����
	public void sendAll(String msg){
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			Iterator<String> keys = map.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				Socket sock = map.get(key);
				
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				
				// ArrayList�� ��ϵ��� ���ڿ��� ���
				String msg1 = "";
				for(int i = 0; i < list.size(); i++){
					msg1 += list.get(i) + " ";
				}
				
				// �Ϲ� �޼����� *�� ����
				bw.write("*" + msg);
				bw.newLine();
				
				// ArrayList ���ڿ��� !�� ����
				bw.write("!" + msg1);
				bw.newLine();
				
				// Map�� ����ִ� Socket�� ���� #���� ����
				bw.write("#" + Integer.toString(map.size()));
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Ư���� �� Socket���� �޼��� ����
	public void sendOnlyOne(String name, String msg){
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			Socket sock = map.get(name);
			os = sock.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			bw.write("*" + msg);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ArrayList ����� ��û�� ���Ͽ��Ը� ������
	public void sendNameList(Socket sock){
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			os = sock.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			String msg1 = "";
			for(int i = 0; i < list.size(); i++){
				msg1 += list.get(i) + " ";
			}
			
			// ArrayList ����� %�� ����
			bw.write("%" + msg1);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ��û�� ������ ����� ��� �� ArrayList ����� ����
	public void sendNameList(){
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			Iterator<String> keys = map.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				Socket sock = map.get(key);
				
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				
				// ArrayList�� ��ϵ��� ���ڿ��� ���
				String msg1 = "";
				for(int i = 0; i < list.size(); i++){
					msg1 += list.get(i) + " ";
				}
				
				// ArrayList ���ڿ��� !�� ����
				bw.write("!" + msg1);
				bw.newLine();
				
				// Map�� ����ִ� Socket�� ���� #���� ����
				bw.write("#" + Integer.toString(map.size()));
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ������ ���� �� start() ȣ�� �� ���๮
	@Override
	public void run() {
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		String name = "";
		
		try {
			is = sock.getInputStream();
			os = sock.getOutputStream();
			isr = new InputStreamReader(is);
			osw = new OutputStreamWriter(os);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(osw);
			
			while(true){
				String msg = br.readLine();
				// ���� �޼����� $�� ���� �� Map�� �̸��� Socket�� �߰�
				if(msg.startsWith("$")){
					name = msg.substring(1);
					map.put(name, sock);
					list.add(name);
					
				// ���� �޼����� @�� ���� �� Map���� �̸��� Socket�� ����
				}else if(msg.startsWith("@")){
					name = msg.substring(1);
					map.remove(name);
					list.remove(name);
					name = "";
					
				// ArrayList ��� ��û �޼����� %�� ���� �� ArrayList ���� �޼ҵ带 ����
				}else if(msg.startsWith("%")){
					sendNameList(sock);
					
				// �ӼӸ� �޼����� ^�� ���� �� ��� ���Ͽ��Ը� �޼����� �����ϴ� �޼ҵ� ����
				}else if(msg.startsWith("^")){
					if(msg.startsWith("^[")){
						msg = msg.substring(1);
						sendOnlyOne(name, msg);
					}else if(msg.startsWith("^{")){
						msg = "?" + msg.substring(2);
						sendOnlyOne(name, msg);
					}else{
						name = msg.substring(1);
					}
				// �Ϲ� �޼��� ���� �� ��ο��� �޼����� �����ϴ� �޼ҵ� ����
				}else if(msg.startsWith("&")){
					if(msg.startsWith("&[")){
						msg = msg.substring(1);
						sendAll(msg);
					}else if(msg.startsWith("&{")){
						msg = "?" + msg.substring(2);
						sendAll(msg);
					}
				}else{
					sendAll(msg);
				}
			}
			
		} catch (IOException e) {
			// Client ������ ����ġ ���� ���� �߻��� �ش� Client�� Map�� ArrayList���� �����ϰ� ���� �޼����� ����� ������ Client�鿡�� ����
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String time = sdf.format(date);
			
			map.remove(name);
			list.remove(name);
			if(!name.equals("")){
				sendAll(name + "���� �����Ͽ����ϴ�. (" + time + ")");
			}
			sendNameList();
			e.printStackTrace();
		}
		finally {
			try {
				bw.close();
				br.close();
				osw.close();
				isr.close();
				os.close();
				is.close();
				sock.close();
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
				Server me = new Server();
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
