package com.yc.test;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;

import javax.swing.*;

public class Test01 extends Frame implements ActionListener {
	static ArrayList<String> list = new ArrayList<>();
	static String name = null;
	static BufferedWriter bw = null;
	static BufferedReader br = null;
	static TextField nameTextField;
	static TextArea veiwTextArea, mainTextArea;
	static Label personListLabel, dlplb;
	MenuItem mi1, mi2;
	CardLayout card;
	Panel page1, page2, page2Game, page2GameSub, page2Chat, page2ChatCenter, page2ChatRight;
	JPanel page2GameMain;
	Button enterButton, inputButton;
	Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	String inputMsg = null;
	Date date;
	SimpleDateFormat sdf;
	String time;
	static boolean endMsg = false;
	Dialog dl;
	static List personList;
	
	public Test01(){
		// 메뉴 바 - 보류(메뉴 패널 생성 고려)
//		MenuBar bar = new MenuBar();
//		Menu mn1 = new Menu("메뉴");
//		mi1 = new MenuItem("처음으로");
//		mi1.addActionListener(this);
//		mn1.add(mi1);
//		mi2 = new MenuItem("종료");
//		mi2.addActionListener(this);
//		mn1.add(mi2);
//		bar.add(mn1);
//		setMenuBar(bar);
		
		// 첫 번째 페이지
		page1 = new Panel(new GridLayout(2, 1));
		// 첫 번째 페이지 제목
		Panel upperPanel = new Panel(new BorderLayout());
		Label titleLabel = new Label("채팅 프로그램", Label.CENTER);
		upperPanel.add(titleLabel, BorderLayout.CENTER);
		page1.add(upperPanel);
		// 첫 번째 페이지 이름 입력
		Panel lowerPanel = new Panel(new BorderLayout());
		Panel loginPanel = new Panel();
		// 이름 입력 창
		nameTextField = new TextField("  이름을 입력하세요", 15);
		nameTextField.setEditable(false);
		// 이름 입력창 선택 시 입력창 활성화 및 안내문구 삭제
		nameTextField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				nameTextField.setEditable(true);
				nameTextField.setText("");
			}
		});
		// 이름을 입력받으면 입력 버튼 활성화, 입력된 이름의 앞, 뒤, 사이 공백을 지우고 name 변수에 담기
		nameTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				enterButton.setEnabled(true);
			}
		});
		loginPanel.add(nameTextField);
		// 입장 버튼
		enterButton = new Button("입장");
		enterButton.setEnabled(false);
		enterButton.addActionListener(this);
		loginPanel.add(enterButton);
		lowerPanel.add(loginPanel, BorderLayout.NORTH);
		page1.add(lowerPanel);
		// 두 번째 페이지
		page2 = new Panel(new GridLayout(1, 2));
		// 두 번째 페이지 게임 존
//		page2Game = new Panel(new BorderLayout());
//		page2GameSub = new Panel();
//		page2Game.add(page2GameSub, BorderLayout.NORTH);
//		page2GameMain = new JPanel(new ImageIcon("thile.png"));
//		page2GameMain.set
//		page2Game.add(page2GameMain, BorderLayout.CENTER);
//		page2.add(page2Game);
		
		// 두 번째 페이지 채팅 존
		page2Chat = new Panel(new BorderLayout());
		// 두 번째 페이지  채팅 존 메인화면
		page2ChatCenter = new Panel(new BorderLayout());
		// 두 번째 페이지 채팅화면
		veiwTextArea = new TextArea();
		veiwTextArea.setEditable(false);
		// 두 번째 페이지 채팅 입력 부분
		Panel inputPanel = new Panel(new BorderLayout());
		// 전송 버튼
		inputButton = new Button(" 전 송 ");
		inputButton.setEnabled(false);
		inputButton.addActionListener(this);
		inputPanel.add(inputButton, BorderLayout.EAST);
		// 메세지 입력창
		mainTextArea = new TextArea("", 3, 10);
		// 입력한 내용이 없거나 공백문자일 경우 전송 버튼 비활성화 상태 유지
		mainTextArea.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				if(!mainTextArea.getText().trim().isEmpty()){
					inputButton.setEnabled(true);
				}else{
					inputButton.setEnabled(false);
				}
			}
		});
		mainTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String extraMsg = null;
				char input1 = e.getKeyChar();
				int input2 = e.getModifiers();
				// shift enter 입력 시 mainTextArea 내에서 개행
				if(input1 == '\n' && input2 == 1){
				// enter 입력 시 메세지 전송
				}else if(input1 == '\n'){
					// 메세지 전송 시 전송 시간 출력
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					extraMsg = mainTextArea.getText();
					inputMsg = extraMsg.substring(0, extraMsg.lastIndexOf('\n') - 1);
					// 문자가 존재하지 않으면 메세지 전송 안됨.
					if((inputMsg.trim()).equals("")){
						mainTextArea.setText("");
						mainTextArea.append(inputMsg);
					}else{
						try {
							//메세지 전송
							bw.write("\n[" + name + "] (" + time + ")\n" + inputMsg.trim());
							bw.newLine();
							bw.flush();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						// 메세지 전송 후 채팅창 초기화
						mainTextArea.setText("");
					}
				}
				
			}
		});
		inputPanel.add(mainTextArea, BorderLayout.CENTER);
		page2ChatCenter.add(veiwTextArea, BorderLayout.CENTER);
		page2ChatCenter.add(inputPanel, BorderLayout.SOUTH);
		// 두 번째 페이지 서브화면
		page2ChatRight = new Panel(new BorderLayout());
		// 두 번째 페이지 상단 패널
		Panel page2NorthPanel = new Panel(new BorderLayout());
		// 두 번째 페이지 상단 패널의 접속 인원 현황 표시창
		personListLabel = new Label("접속 인원 (0명)");
		page2NorthPanel.add(personListLabel, BorderLayout.NORTH);
		personList = new List(5, false);
		page2NorthPanel.add(personList, BorderLayout.CENTER);
		page2ChatRight.add(page2NorthPanel, BorderLayout.NORTH);
		
		page2Chat.add(page2ChatCenter, BorderLayout.CENTER);
		page2Chat.add(page2ChatRight, BorderLayout.EAST);
		
		page2.add(page2Chat);
		// 알림창
		dl = new Dialog(this, "알림", true);
		Panel dlp = new Panel(new GridLayout(2, 1));
		Panel dlp1 = new Panel(new BorderLayout());
		dlplb = new Label("", Label.CENTER);
		dlp1.add(dlplb, BorderLayout.SOUTH);
		dlp.add(dlp1);
		Panel dlp2 = new Panel(new BorderLayout());
		Panel dlsp = new Panel();
		Button dlpbtn = new Button("확인");
		dlpbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dl.dispose();
			}
		});
		dlsp.add(dlpbtn);
		dlp2.add(dlsp, BorderLayout.NORTH);
		dlp.add(dlp2);
		dl.add(dlp);
		dl.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dl.dispose();
			}
		});
		dl.setBounds((int)dm.getWidth() / 2 - 100, (int)dm.getHeight() / 2 - 50, 200, 100);
		dl.setVisible(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					bw.write("@" + name);
					bw.newLine();
					bw.write("\n" + name + "님이 퇴장하였습니다. (" + time + ")");
					bw.newLine();
					bw.flush();
					endMsg = true;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		card = new CardLayout(0, 0);
		setLayout(card);
		add("p1", page1);
		add("p2", page2);
		setTitle("Chat With Me");
		setBounds(((int)dm.getWidth() / 2) - (966 / 2), ((int)dm.getHeight() / 2) - (489 / 2), 966, 489);
		setVisible(true);
//		setResizable(false);
		revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == enterButton){ // 입장 버튼 클릭 시
			name = ((nameTextField.getText()).trim()).replace(" ", "");
			if(name.equals("")){
				// 이름 미입력 시 등장하는 알림창
				dlplb.setText("이름을 입력해주세요");
				dl.setVisible(true);
			}else if(list.contains(name)){
				dlplb.setText("이미 사용중인 이름입니다");
				dl.setVisible(true);
			}else{
				try {
//					System.out.println(list.isEmpty());
//					for(int i = 0; i < list.size(); i++){
//						System.out.println(list.get(i));
//					}
					System.out.println("-" + name + "-");
					bw.write(name);
					bw.newLine();
					veiwTextArea.setText("채팅이 시작되었습니다\n");
					card.show(this, "p2");
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					bw.write("\n" + name + "님이 입장하였습니다. (" + time + ")");
					bw.newLine();
					bw.flush();
					mainTextArea.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
//		}else if(obj == mi1){
//			try {
//				bw.write("\n" + name + "님이 퇴장하였습니다.");
//				bw.newLine();
//				bw.flush();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			nameTextField.setText("");
//			card.show(this, "p1");
//		}else if(obj == mi2){
//			dispose();
		}else if(obj == inputButton){
			date = new Date();
			sdf = new SimpleDateFormat("HH:mm:ss");
			time = sdf.format(date);
			inputMsg = mainTextArea.getText();
			if((inputMsg.trim()).equals("")){
				// 문자가 존재하지 않으면 채팅창이 리셋되고 메세지 전송도 안됨.
				mainTextArea.setText("");
			}else{
			try {
				bw.write("\n[" + name + "] (" + time + ")\n" + inputMsg.trim());
				bw.newLine();
				bw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			mainTextArea.setText("");
			}
		}
	}

	public static void main(String[] args) {
		Test01 me = new Test01();
		Socket sock = null;
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		try {
			sock = new Socket("127.0.0.1", 5001);
			is = sock.getInputStream();
			os = sock.getOutputStream();
			isr = new InputStreamReader(is);
			osw = new OutputStreamWriter(os);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(osw);
			while(true){
				for(int i = 0; i < list.size(); i++){
					System.out.println(list.get(i));
				}
				String msg = br.readLine();
				if(endMsg){
					break;
				}else if(msg.startsWith("*")){ // 입퇴장 메세지 및 일반 메세지는 수신 시 *로 확인
					msg = msg.substring(1);
					veiwTextArea.append(msg + "\n");
				}else if(msg.startsWith("!")){ // 접속 인원 최신화 메세지는 !로 확인
					personList.removeAll();
					msg = msg.substring(1).trim();
					String[] arr = msg.split(" ");
					list = new ArrayList<>();
					for(int i = 0; i < arr.length; i++){
						personList.add(arr[i]);
						list.add(arr[i]);
					}
				}else if(msg.startsWith("#")){ // 접속 인원 수 최신화 메세지는 #로 확인
					msg = msg.substring(1);
					personListLabel.setText("접속 인원 (" + msg + "명)");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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


}
