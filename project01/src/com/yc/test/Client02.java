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

public class Client02 extends Frame implements ActionListener {
	static ArrayList<String> list = new ArrayList<>();
	static String Id = null;
	static String Pw = null;
	static BufferedWriter bw = null;
	static BufferedReader br = null;
	static TextArea viewTextArea, mainTextArea;
	static Label dlplb;
	static JLabel personListLabel2, MenuTimeLabel1, MenuTimeLabel2, warningLabel1, warningLabel2;
	TextField idTextField, pwTextField, signUpIdInputTextField, signUpPwInputTextField1, signUpPwInputTextField2;
	PopupMenu popupMenu;
	MenuItem mi1, mi2;
	CardLayout card;
	Panel page2, page2CenterPanel, page2NorthPanel, loginPage, signUpPage;
	JButton inputButton, MenuButton, enterButton, signUpButton1, signUpButton2;
	Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	String inputMsg = null;
	Date date;
	SimpleDateFormat sdf;
	String time;
	static boolean endMsg = false;
	Dialog dl;
	static List personList;
	
	public Client02(){
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
		
		// 초기화면
		loginPage = new Panel(new GridLayout(4, 1));
		Panel loginFirstPanel = new Panel(new BorderLayout());
		JLabel titleLabel1 = new JLabel("채팅 프로그램", JLabel.CENTER);
		loginFirstPanel.add(titleLabel1, BorderLayout.SOUTH);
		loginPage.add(loginFirstPanel);
		Panel loginSecondPanel = new Panel(new BorderLayout());
		Panel loginputPanel = new Panel(new GridLayout(3, 1));
		warningLabel1 = new JLabel("안내문구1", JLabel.CENTER);
		loginputPanel.add(warningLabel1);
		Panel idPanel = new Panel();
		JLabel idLabel = new JLabel("  아이디  ");
		idPanel.add(idLabel);
		idTextField = new TextField("   아이디를 입력하세요", 18);
		idTextField.setEditable(false);
		idTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				idTextField.setEditable(true);
				idTextField.setText("");
				pwTextField.setEditable(true);
				pwTextField.setText("");
				enterButton.setEnabled(true);
				warningLabel1.setText("");
			}
		});
		idTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				
			}
		});
		idPanel.add(idTextField);
		loginputPanel.add(idPanel);
		Panel pwPanel = new Panel();
		JLabel pwLabel = new JLabel("비밀번호");
		pwPanel.add(pwLabel);
		pwTextField = new TextField("  비밀번호를 입력하세요", 18);
		pwTextField.setEditable(false);
		pwTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				warningLabel1.setText("");
			}
		});
		pwTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				
			}
		});
		pwPanel.add(pwTextField);
		loginputPanel.add(pwPanel);
		loginSecondPanel.add(loginputPanel, BorderLayout.SOUTH);
		loginPage.add(loginSecondPanel);
		Panel loginThirdPanel = new Panel(new BorderLayout());
		Panel loginPageButtonPanel = new Panel();
		enterButton = new JButton("로그인");
		enterButton.setBackground(Color.WHITE);
		enterButton.setEnabled(false);
		loginPageButtonPanel.add(enterButton);
		signUpButton1 = new JButton("회원가입");
		signUpButton1.setBackground(Color.WHITE);
		signUpButton1.addActionListener(this);
		loginPageButtonPanel.add(signUpButton1);
		loginThirdPanel.add(loginPageButtonPanel, BorderLayout.NORTH);
		loginPage.add(loginThirdPanel);
		Panel loginFourthPanel = new Panel(new BorderLayout());
		loginPage.add(loginFourthPanel);
		
		// 회원가입 페이지
		signUpPage = new Panel(new GridLayout(3, 1));
		
		Panel signUpTitlePanel = new Panel(new BorderLayout());
		JLabel signUpTitle = new JLabel("회원가입 페이지", JLabel.CENTER);
		signUpTitlePanel.add(signUpTitle, BorderLayout.CENTER);
		signUpPage.add(signUpTitlePanel);
		
		Panel signUpMainPanel = new Panel(new GridLayout(4, 1));
		
		Panel signUpIdInputPanel = new Panel();
		JLabel signUpIdInputLabel = new JLabel("      아이디       ");
		signUpIdInputPanel.add(signUpIdInputLabel);
		signUpIdInputTextField = new TextField("", 10);
		signUpIdInputPanel.add(signUpIdInputTextField);
		signUpMainPanel.add(signUpIdInputPanel);
		
		Panel signUpPwInputPanel1 = new Panel();
		JLabel signUpPwInputLabel1 = new JLabel("    비밀번호     ");
		signUpPwInputPanel1.add(signUpPwInputLabel1);
		signUpPwInputTextField1 = new TextField("", 10);
		signUpPwInputPanel1.add(signUpPwInputTextField1);
		signUpMainPanel.add(signUpPwInputPanel1);
		
		Panel signUpPwInputPanel2 = new Panel();
		JLabel signUpPwInputLabel2 = new JLabel("비밀번호 확인");
		signUpPwInputPanel2.add(signUpPwInputLabel2);
		signUpPwInputTextField2 = new TextField("", 10);
		signUpPwInputPanel2.add(signUpPwInputTextField2);
		signUpMainPanel.add(signUpPwInputPanel2);
		
		warningLabel2 = new JLabel("안내문구2", JLabel.CENTER);
		signUpMainPanel.add(warningLabel2);
		
		signUpPage.add(signUpMainPanel);
		
		Panel signUpButtonPanel = new Panel();
		signUpButton2 = new JButton("회원가입");
		signUpButton2.setBackground(Color.WHITE);
		signUpButton2.addActionListener(this);
		signUpButtonPanel.add(signUpButton2);
		signUpPage.add(signUpButtonPanel);
		
		
		// 두 번째 페이지
		page2 = new Panel(new BorderLayout());
		// 두 번째 페이지 메인화면
		page2CenterPanel = new Panel(new BorderLayout());
		// 두 번째 페이지 채팅화면
		viewTextArea = new TextArea();
		viewTextArea.setEditable(false);
		// 두 번째 페이지 채팅 입력 부분
		Panel inputPanel = new Panel(new BorderLayout());
		// 전송 버튼
		inputButton = new JButton("전 송");
		inputButton.setBackground(Color.WHITE);
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
							bw.write("\n[" + Id + "] (" + time + ")\n" + inputMsg.trim());
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
		page2CenterPanel.add(viewTextArea, BorderLayout.CENTER);
		page2CenterPanel.add(inputPanel, BorderLayout.SOUTH);
		// 두 번째 페이지 서브화면
		page2NorthPanel = new Panel(new BorderLayout());
		// 두 번째 페이지 상단 패널
		Panel page2ListPanel = new Panel(new BorderLayout());
		// 두 번째 페이지 상단 패널의 메뉴 패널
		Panel MenuPanel = new Panel(new BorderLayout());
		MenuButton = new JButton("메뉴");
		MenuButton.setBackground(Color.WHITE);
		MenuButton.addActionListener(this);
		MenuPanel.add(MenuButton, BorderLayout.EAST);
		page2ListPanel.add(MenuPanel, BorderLayout.NORTH);
		// MenuButton 클릭 시 등장하는 팝업메뉴
		popupMenu = new PopupMenu();
		mi1 = new MenuItem("처음으로");
		mi1.addActionListener(this);
		popupMenu.add(mi1);
		mi2 = new MenuItem("종료");
		mi2.addActionListener(this);
		popupMenu.add(mi2);
		MenuTimeLabel1 = new JLabel("0000년 00월 00일", JLabel.CENTER);
		MenuTimeLabel1.setOpaque(true);
		MenuTimeLabel1.setBackground(Color.LIGHT_GRAY);
		MenuPanel.add(MenuTimeLabel1, BorderLayout.WEST);
		MenuTimeLabel2 = new JLabel("00:00", JLabel.CENTER);
		MenuTimeLabel2.setOpaque(true);
		MenuTimeLabel2.setBackground(Color.LIGHT_GRAY);
		MenuPanel.add(MenuTimeLabel2, BorderLayout.CENTER);
		// 두 번째 페이지 상단 패널의 접속 인원 현황 표시창
		Panel personListPanel = new Panel(new GridLayout(2, 1));
		JLabel personListLabel1 = new JLabel(" 접속 인원 ", JLabel.CENTER);
		personListPanel.add(personListLabel1);
		personListLabel2 = new JLabel("(0명)", JLabel.CENTER);
		personListPanel.add(personListLabel2);
		page2ListPanel.add(personListPanel, BorderLayout.WEST);
		personList = new List(5, true);
		page2ListPanel.add(personList, BorderLayout.CENTER);
		page2NorthPanel.add(page2ListPanel, BorderLayout.NORTH);
		page2.add(page2CenterPanel, BorderLayout.CENTER);
		page2.add(page2NorthPanel, BorderLayout.NORTH);
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
				try {
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					if(!(Id == null)){
						bw.write("@" + Id);
						bw.newLine();
						bw.write("\n" + Id + "님이 퇴장하였습니다. (" + time + ")");
						bw.newLine();
						bw.flush();
					}
					endMsg = true;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
		});
		card = new CardLayout(0, 0);
		setLayout(card);
		add(popupMenu);
		add("lp", loginPage);
		add("sp", signUpPage);
		add("p2", page2);
		setTitle("채팅 프로그램");
		setBounds(((int)dm.getWidth() / 2) - (316 / 2), ((int)dm.getHeight() / 2) - (489 / 2), 316, 489);
		System.out.println(this.getWidth() + "," + this.getHeight());
		setVisible(true);
		setResizable(false);
		revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == enterButton){ // 입장 버튼 클릭 시
			Id = ((idTextField.getText()).trim()).replace(" ", "");
			if(Id.equals("")){
				// 이름 미입력 시 등장하는 안내문구
				warningLabel1.setText("이름을 입력해주세요");
//				dlplb.setText("이름을 입력해주세요");
//				dl.setVisible(true);
			}else if(list.contains(Id)){
				warningLabel1.setText("이미 사용중인 이름입니다");
//				dlplb.setText("이미 사용중인 이름입니다");
//				dl.setVisible(true);
			}else if(Id.contains("~") || Id.contains("`") || Id.contains("!") || Id.contains("@") || Id.contains("#") || Id.contains("$") || Id.contains("%") || Id.contains("^") || Id.contains("&") || Id.contains("*") || Id.contains("(") || Id.contains(")") || Id.contains("-") || Id.contains("_") || Id.contains("=") || Id.contains("+") || Id.contains(",") || Id.contains(".") || Id.contains("/") || Id.contains("<") || Id.contains(">") || Id.contains("?") || Id.contains(";") || Id.contains(":") || Id.contains("'") || Id.contains("\"") || Id.contains("[") || Id.contains("]") || Id.contains("{") || Id.contains("}") || Id.contains("\\") || Id.contains("|")){
				warningLabel1.setText("특수문자는 사용할 수 없습니다");
//				dlplb.setText("특수문자는 사용할 수 없습니다");
//				dl.setVisible(true);
			}else{
				try {
					bw.write("$" + Id);
					bw.newLine();
					viewTextArea.setText("채팅이 시작되었습니다.\n");
					card.show(this, "p2");
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					bw.write("\n" + Id + "님이 입장하였습니다. (" + time + ")");
					bw.newLine();
					bw.flush();
					mainTextArea.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}else if(obj == mi1){
			try {
				date = new Date();
				sdf = new SimpleDateFormat("HH:mm:ss");
				time = sdf.format(date);
				if(!(Id == null)){
					bw.write("@" + Id);
					bw.newLine();
					bw.write("\n" + Id + "님이 퇴장하였습니다. (" + time + ")");
					bw.newLine();
					bw.flush();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Id = null;
			idTextField.setText("");
			dispose();
			Client02 me = new Client02();
//			card.show(this, "p1");
		}else if(obj == mi2){
			dispose();
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
				bw.write("\n[" + Id + "] (" + time + ")\n" + inputMsg.trim());
				bw.newLine();
				bw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			mainTextArea.setText("");
			}
		}else if(obj == MenuButton){
			popupMenu.show(page2, 219, 28);
		}else if(obj == signUpButton1){
			card.show(this, "sp");
		}else if(obj == signUpButton2){
			card.show(this, "lp");
		}
	}

	public static void main(String[] args) {
		Client02 me = new Client02();
		Thread thr = new Thread(){
			@Override
			public void run() {
				while(true){
				Date cal = new Date();
				DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
				MenuTimeLabel1.setText("  " + df.format(cal));
				
				SimpleDateFormat sdf = new SimpleDateFormat("a HH:mm:ss");
				MenuTimeLabel2.setText(sdf.format(cal));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
			}
		};
		thr.start();
		
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
			
			
//			if(list.isEmpty()){
//				System.out.println("시작-list 비어있음");
//			}else{
//				for(int i = 0; i < list.size(); i++){
//					System.out.print("-" + list.get(i));
//				}
//				System.out.println();
//			}
			
//			bw.write("%");
//			bw.newLine();
//			bw.flush();
//			String msg1 = br.readLine();
//			msg1 = msg1.substring(1);
//			String[] arr1 = msg1.split(" ");
//			list = new ArrayList<>();
//			for(int i = 0; i < arr1.length; i++){
//				list.add(arr1[i]);
//			}
			
			while(true){
				String msg = br.readLine();
				if(endMsg){
					break;
				}else if(msg.startsWith("*")){ // 입퇴장 메세지 및 일반 메세지는 수신 시 *로 확인
					msg = msg.substring(1);
					viewTextArea.append(msg + "\n");
				}else if(msg.startsWith("!")){ // 접속 인원 최신화 메세지는 !로 확인
					personList.removeAll();
					msg = msg.substring(1).trim();
					String[] arr = msg.split(" ");
					for(int i = 0; i < arr.length; i++){
						personList.add(arr[i]);
					}
				}else if(msg.startsWith("#")){ // 접속 인원 수 최신화 메세지는 #로 확인
					msg = msg.substring(1);
					personListLabel2.setText("(" + msg + "명)");
//				}else if(msg.startsWith("%")){
//					msg = msg.substring(1).trim();
//					String[] arr = msg.split(" ");
//					for(int i = 0; i < arr.length; i++){
//						list.add(arr[i]);
//					}
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
