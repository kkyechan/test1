package com.yc.test;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Thread {
	static HashMap<String, Socket> map = new HashMap<>();
	static ArrayList<String> list = new ArrayList<>();
	Socket sock = null;
	
	// Map에 들어있는 모든 Socket에 메세지 전송
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
				
				// ArrayList의 목록들을 문자열에 담기
				String msg1 = "";
				for(int i = 0; i < list.size(); i++){
					msg1 += list.get(i) + " ";
				}
				
				// 일반 메세지는 *로 전송
				bw.write("*" + msg);
				bw.newLine();
				
				// ArrayList 문자열은 !로 전송
				bw.write("!" + msg1);
				bw.newLine();
				
				// Map에 들어있는 Socket의 수는 #으로 전송
				bw.write("#" + Integer.toString(map.size()));
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 특정한 한 Socket에만 메세지 전송
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
	
	// ArrayList 목록을 요청한 소켓에게만 보내기
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
			
			// ArrayList 목록은 %로 전송
			bw.write("%" + msg1);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 요청한 시점의 사용자 명단 및 ArrayList 목록을 전송
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
				
				// ArrayList의 목록들을 문자열에 담기
				String msg1 = "";
				for(int i = 0; i < list.size(); i++){
					msg1 += list.get(i) + " ";
				}
				
				// ArrayList 문자열은 !로 전송
				bw.write("!" + msg1);
				bw.newLine();
				
				// Map에 들어있는 Socket의 수는 #으로 전송
				bw.write("#" + Integer.toString(map.size()));
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 스레드 생성 후 start() 호출 시 실행문
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
				// 입장 메세지를 $로 수신 후 Map에 이름과 Socket을 추가
				if(msg.startsWith("$")){
					name = msg.substring(1);
					map.put(name, sock);
					list.add(name);
					
				// 퇴장 메세지를 @로 수신 후 Map에서 이름과 Socket을 삭제
				}else if(msg.startsWith("@")){
					name = msg.substring(1);
					map.remove(name);
					list.remove(name);
					name = "";
					
				// ArrayList 목록 요청 메세지를 %로 수신 후 ArrayList 전송 메소드를 실행
				}else if(msg.startsWith("%")){
					sendNameList(sock);
					
				// 귓속말 메세지를 ^로 수신 후 대상 소켓에게만 메세지를 전송하는 메소드 실행
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
				// 일반 메세지 수신 후 모두에게 메세지를 전송하는 메소드 실행
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
			// Client 측에서 예상치 못한 종료 발생시 해당 Client를 Map과 ArrayList에서 삭제하고 관련 메세지와 목록을 나머지 Client들에게 전송
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String time = sdf.format(date);
			
			map.remove(name);
			list.remove(name);
			if(!name.equals("")){
				sendAll(name + "님이 퇴장하였습니다. (" + time + ")");
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
