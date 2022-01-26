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

public class Client extends Frame implements ActionListener {
	static ArrayList<String> list = new ArrayList<>();
	static String name = "";
	static String personListItem = "";
	static String receivePersonName = "모두 ";
	static BufferedWriter bw = null;
	static BufferedReader br = null;
	static TextField nameTextField;
	static TextArea viewTextArea, mainTextArea;
	static Label page1MainLabel, dlplb, warninglb, mainTextAreaStatusLabel1, mainTextAreaStatusLabel2;
	static JLabel personListLabel2, MenuTimeLabel1, MenuTimeLabel2;
	static List personList;
	static Choice receivePersonChoice;
	static boolean endMsg = false;
	PopupMenu popupMenu1, popupMenu2;
	MenuItem popupMenu1Item1, popupMenu1Item2, popupMenu2Item1;
	static CardLayout card;
	Panel page1, page2, page2CenterPanel, page2NorthPanel;
	Button enterButton;
	JButton inputButton, MenuButton;
	Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	String inputMsg = "";
	Date date;
	SimpleDateFormat sdf;
	String time;
	Dialog dl;
	
	
	public Client(){
		
		// 첫 번째 페이지
		page1 = new Panel(new GridLayout(2, 1));
		
		// 첫 번째 페이지 제목
		Panel upperPanel = new Panel(new BorderLayout());
		
		Label titleLabel = new Label("채팅 프로그램", Label.CENTER);
		upperPanel.add(titleLabel, BorderLayout.CENTER);
		
		// 첫 번째 페이지 환영 문구(서버 연결 여부 확인 메세지)
		page1MainLabel = new Label("환영합니다!", Label.CENTER);
		page1MainLabel.setForeground(Color.BLUE);
		upperPanel.add(page1MainLabel, BorderLayout.SOUTH);
		page1.add(upperPanel);
		
		// 첫 번째 페이지 이름 입력 패널
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
				warninglb.setText("");
			}
		});
		// 이름을 입력받으면 입력 버튼 활성화, 입력된 이름의 앞, 뒤, 사이 공백을 지우고 name 변수에 담기
		nameTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				enterButton.setEnabled(true);
			}
		});
		nameTextField.addActionListener(this);
		loginPanel.add(nameTextField);
		
		// 입장 버튼
		enterButton = new Button("입장");
		enterButton.setEnabled(false);
		enterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				try {
					bw.write("%");
					bw.newLine();
					bw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		enterButton.addActionListener(this);
		loginPanel.add(enterButton);
		lowerPanel.add(loginPanel, BorderLayout.NORTH);
		
		// 안내 문구
		Panel warningPanel = new Panel(new BorderLayout());
		warninglb = new Label("", Label.CENTER);
		warninglb.setForeground(Color.RED);
		warningPanel.add(warninglb, BorderLayout.NORTH);
		lowerPanel.add(warningPanel, BorderLayout.CENTER);
		page1.add(lowerPanel);
		
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
					inputMsg = extraMsg.substring(0, extraMsg.lastIndexOf('\n') - 1).trim();
					String[] arr1 = inputMsg.split("\n");
					// 문자가 존재하지 않으면 메세지 전송 안됨.
					if((inputMsg.trim()).equals("")){
						mainTextArea.setText("");
						mainTextArea.append(inputMsg);
					}else{
						try {
							// 모두에게 보내는 메세지// 한 사람에게만 보내는 귓속말
							if(receivePersonName.equals("모두 ")){
								bw.write("&[" + name + " - 모두에게] (" + time + ")");
								bw.newLine();
								for(int i = 0; i < arr1.length; i++){
									bw.write("&{" + arr1[i]);
								}
								bw.newLine();
								bw.flush();
							// 한 사람에게만 보내는 귓속말
							}else{
								bw.write("^" + receivePersonName);
								bw.newLine();
								bw.write("^[" + name + " - " + receivePersonName + "에게 귓속말] (" + time + ")");
								bw.newLine();
								for(int i = 0; i < arr1.length; i++){
									bw.write("^{" + arr1[i]);
								}
								bw.newLine();
								bw.flush();
								viewTextArea.append("\n[" + name + " - " + receivePersonName + "에게 귓속말] (" + time + ")\n" + inputMsg.trim() + "\n");
							}
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
		
		// 메세지 입력 상태창
		Panel mainTextAreaStatusPanel = new Panel(new BorderLayout());
		
		Panel extraPanel = new Panel();
		
		mainTextAreaStatusLabel1 = new Label("", Label.CENTER);
		extraPanel.add(mainTextAreaStatusLabel1);
		
		// 수신자 선택창
		receivePersonChoice = new Choice();
		receivePersonChoice.addItem("모두 ");
		receivePersonChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				receivePersonName = receivePersonChoice.getSelectedItem();
				personListItem = receivePersonChoice.getSelectedItem();
				System.out.println("--" + receivePersonName + "--");
			}
		});
		extraPanel.add(receivePersonChoice);
		
		mainTextAreaStatusLabel2 = new Label("");
		extraPanel.add(mainTextAreaStatusLabel2);
		
		mainTextAreaStatusPanel.add(extraPanel, BorderLayout.WEST);
		inputPanel.add(mainTextAreaStatusPanel, BorderLayout.NORTH);
		
		page2CenterPanel.add(viewTextArea, BorderLayout.CENTER);
		page2CenterPanel.add(inputPanel, BorderLayout.SOUTH);
		
		// 두 번째 페이지 서브패널
		page2NorthPanel = new Panel(new BorderLayout());
		
		// 두 번째 페이지 서브패널의 상단 패널
		Panel page2ListPanel = new Panel(new BorderLayout());
		
		// 두 번째 페이지 상단 패널의 메뉴 패널
		Panel MenuPanel = new Panel(new BorderLayout());
		
		MenuButton = new JButton("메뉴");
		MenuButton.setBackground(Color.WHITE);
		MenuButton.addActionListener(this);
		MenuPanel.add(MenuButton, BorderLayout.EAST);
		
		page2ListPanel.add(MenuPanel, BorderLayout.NORTH);
		
		// MenuButton 클릭 시 등장하는 팝업메뉴
		popupMenu1 = new PopupMenu();
		
		// 팝업메뉴의 첫 번째 메뉴 아이템
		popupMenu1Item1 = new MenuItem("처음으로");
		popupMenu1Item1.addActionListener(this);
		popupMenu1.add(popupMenu1Item1);
		
		// 팝업메뉴의 두 번째 메뉴 아이템
		popupMenu1Item2 = new MenuItem("종료");
		popupMenu1Item2.addActionListener(this);
		popupMenu1.add(popupMenu1Item2);
		
		// 두 번째 페이지 서브 패널의 상단 패널의 날짜 표시 라벨
		MenuTimeLabel1 = new JLabel("0000년 00월 00일", JLabel.CENTER);
		MenuTimeLabel1.setOpaque(true);
		MenuTimeLabel1.setBackground(Color.LIGHT_GRAY);
		MenuPanel.add(MenuTimeLabel1, BorderLayout.WEST);
		
		// 두 번째 페이지 서브 패널의 상단 패널의 시간 표시 라벨
		MenuTimeLabel2 = new JLabel("00:00", JLabel.CENTER);
		MenuTimeLabel2.setOpaque(true);
		MenuTimeLabel2.setBackground(Color.LIGHT_GRAY);
		MenuPanel.add(MenuTimeLabel2, BorderLayout.CENTER);
		
		// 두 번째 페이지 상단 패널의 접속 인원 현황 표시창
		Panel personListPanel = new Panel(new GridLayout(2, 1));
		
		JLabel personListLabel1 = new JLabel(" 접속 인원 ", JLabel.CENTER);
		personListPanel.add(personListLabel1);
		
		// 접속 인원 수를 보여주는 라벨
		personListLabel2 = new JLabel("(0명)", JLabel.CENTER);
		personListPanel.add(personListLabel2);
		page2ListPanel.add(personListPanel, BorderLayout.WEST);
		
		// 접속 인원 명단을 보여주는 리스트
		personList = new List(5, false);
		personList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				for(int i = 0; i < personList.countItems(); i++){
					personList.deselect(i);
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(personList.getSelectedItem() != null && e.getModifiers() == 4 && !personList.getSelectedItem().equals(name)){
					personListItem = personList.getSelectedItem();
					popupMenu2.show(personList, e.getX(), e.getY());
				}
			}
		});
		
		// 두 번째 페이지 사용자 명단의 팝업메뉴
		popupMenu2 = new PopupMenu();
		
		// 두 번째 페이지 사용자 명단의 팝업메뉴의 첫 번째 메뉴 아이템
		popupMenu2Item1 = new MenuItem("귓속말");
		popupMenu2Item1.addActionListener(this);
		popupMenu2.add(popupMenu2Item1);
		
		page2ListPanel.add(personList, BorderLayout.CENTER);
		page2NorthPanel.add(page2ListPanel, BorderLayout.NORTH);
		
		page2.add(page2CenterPanel, BorderLayout.CENTER);
		page2.add(page2NorthPanel, BorderLayout.NORTH);
		
		// 알림창
//		dl = new Dialog(this, "알림", true);
//		Panel dlp = new Panel(new GridLayout(2, 1));
//		Panel dlp1 = new Panel(new BorderLayout());
//		dlplb = new Label("", Label.CENTER);
//		dlp1.add(dlplb, BorderLayout.SOUTH);
//		dlp.add(dlp1);
//		Panel dlp2 = new Panel(new BorderLayout());
//		Panel dlsp = new Panel();
//		Button dlpbtn = new Button("확인");
//		dlpbtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				dl.dispose();
//			}
//		});
//		dlsp.add(dlpbtn);
//		dlp2.add(dlsp, BorderLayout.NORTH);
//		dlp.add(dlp2);
//		dl.add(dlp);
//		dl.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				dl.dispose();
//			}
//		});
//		dl.setBounds((int)dm.getWidth() / 2 - 100, (int)dm.getHeight() / 2 - 50, 200, 100);
//		dl.setVisible(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				try {
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					
					if(!name.equals("")){
						bw.write("@" + name);
						bw.newLine();
						bw.write(name + "님이 퇴장하였습니다. (" + time + ")");
						bw.newLine();
						bw.write("%");
						bw.newLine();
						bw.flush();
					}
					
					endMsg = true;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// 카드 레이아웃
		card = new CardLayout(0, 0);
		
		setLayout(card);
		add(popupMenu1);
		add(popupMenu2);
		add("p1", page1);
		add("p2", page2);
		setTitle("Client");
		setBounds(((int)dm.getWidth() / 2) - (316 / 2), ((int)dm.getHeight() / 2) - (489 / 2), 316, 489);
//		System.out.println(this.getWidth() + "," + this.getHeight());
		setVisible(true);
		setResizable(false);
		revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		// 입장 버튼 클릭 시
		if(obj == enterButton){
			name = ((nameTextField.getText()).trim()).replace(" ", "");
			if(name.equals("")){
				// 이름 미입력 시 등장하는 안내문구
				warninglb.setText("이름을 입력해주세요");
			}else if(list.contains(name)){
				warninglb.setText("이미 사용중인 이름입니다");
			}else if(name.contains("~") || name.contains("`") || name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&") || name.contains("*") || name.contains("(") || name.contains(")") || name.contains("-") || name.contains("_") || name.contains("=") || name.contains("+") || name.contains(",") || name.contains(".") || name.contains("/") || name.contains("<") || name.contains(">") || name.contains("?") || name.contains(";") || name.contains(":") || name.contains("'") || name.contains("\"") || name.contains("[") || name.contains("]") || name.contains("{") || name.contains("}") || name.contains("\\") || name.contains("|")){
				warninglb.setText("특수문자는 사용할 수 없습니다");
			}else{
				try {
					bw.write("$" + name);
					bw.newLine();
					
					viewTextArea.setText("채팅이 시작되었습니다.\n");
					mainTextArea.setText("");
					
					mainTextAreaStatusLabel1.setText(name + "이(가)");
					mainTextAreaStatusLabel2.setText("에게 전송");
					
					card.show(this, "p2");
					
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					
					bw.write(name + "님이 입장하였습니다. (" + time + ")");
					bw.newLine();
					bw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			nameTextField.setText("");
			receivePersonName = "모두 ";
			
		// 처음으로 가기 메뉴 클릭 시
		}else if(obj == popupMenu1Item1){
			try {
				date = new Date();
				sdf = new SimpleDateFormat("HH:mm:ss");
				time = sdf.format(date);
				
				if(!name.equals("")){
					bw.write("@" + name);
					bw.newLine();
					bw.write(name + "님이 퇴장하였습니다. (" + time + ")");
					bw.newLine();
					bw.write("%");
					bw.newLine();
					bw.flush();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			name = "";
			page1MainLabel.setText("환영합니다!");
			nameTextField.setText("");
			
			card.show(this, "p1");
			
		// 종료 메뉴 클릭 시
		}else if(obj == popupMenu1Item2){
			dispose();
			try {
				date = new Date();
				sdf = new SimpleDateFormat("HH:mm:ss");
				time = sdf.format(date);
				
				if(!name.equals("")){
					bw.write("@" + name);
					bw.newLine();
					bw.write(name + "님이 퇴장하였습니다. (" + time + ")");
					bw.newLine();
					bw.write("%");
					bw.newLine();
					bw.flush();
				}
				
				endMsg = true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		// 전송 버튼 클릭 시
		}else if(obj == inputButton){
			System.out.println(receivePersonName);
			date = new Date();
			sdf = new SimpleDateFormat("HH:mm:ss");
			time = sdf.format(date);
			
			inputMsg = mainTextArea.getText().trim();
			String[] arr1 = inputMsg.split("\n");
			if((inputMsg.trim()).equals("")){
				// 문자가 존재하지 않으면 채팅창이 리셋되고 메세지 전송도 안됨.
				mainTextArea.setText("");
			}else{
				try {
					// 모두에게 보내는 메세지
					if(receivePersonName.equals("모두 ")){
						bw.write("&[" + name + " - 모두에게] (" + time + ")");
						bw.newLine();
						for(int i = 0; i < arr1.length; i++){
							bw.write("&{" + arr1[i]);
						}
						bw.newLine();
						bw.flush();
						
					// 한 사람에게만 보내는 귓속말
					}else{
						bw.write("^" + receivePersonName);
						bw.newLine();
						bw.write("^[" + name + " - " + receivePersonName + "에게 귓속말] (" + time + ")");
						bw.newLine();
						for(int i = 0; i < arr1.length; i++){
							bw.write("^{" + arr1[i]);
						}
						bw.newLine();
						bw.flush();
						viewTextArea.append("\n[" + name + " - " + receivePersonName + "에게 귓속말] (" + time + ")\n" + inputMsg.trim() + "\n");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			mainTextArea.setText("");
			}
		
		// 메뉴 버튼 클릭 시
		}else if(obj == MenuButton){
			popupMenu1.show(page2, 219, 28);
			
		// 이름 입력창에서 키보드를 눌렀을 때
		}else if(obj == nameTextField){
			warninglb.setText("마우스로 입장 버튼을 클릭해주세요");
			
		// 사용자 명단의 팝업메뉴의 귓속말 메뉴를 클릭 시
		}else if(obj == popupMenu2Item1){
			receivePersonChoice.select(personListItem);
			receivePersonName = receivePersonChoice.getSelectedItem();
		}
	}

	public static void main(String[] args) {
		Client me = new Client();
		Socket sock = null;
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		
		// 현재 시간 표시 패널의 스레드
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
		
		try {
			sock = new Socket("127.0.0.1", 5001);
			is = sock.getInputStream();
			os = sock.getOutputStream();
			isr = new InputStreamReader(is);
			osw = new OutputStreamWriter(os);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(osw);
			
			while(true){
				String msg = br.readLine();
				
				// 프로그램 종료 시 endMsg가 true가 되면서 반복문 종료
				if(endMsg){
					break;
				
				// 입퇴장 메세지 및 일반 메세지는 수신 시 *로 확인
				}else if(msg.startsWith("*")){
					msg = msg.substring(1);
					if(msg.startsWith("?")){
						msg = msg.substring(1);
						viewTextArea.append(msg + "\n");
					}else{
						viewTextArea.append("\n" + msg + "\n");
					}
					
				// 접속 인원 명단 최신화 메세지는 !로 확인
				}else if(msg.startsWith("!")){
					personList.removeAll();
					receivePersonChoice.removeAll();
					receivePersonChoice.add("모두 ");
					
					msg = msg.substring(1).trim();
					String[] arr = msg.split(" ");
					for(int i = 0; i < arr.length; i++){
						personList.add(arr[i]);
						if(!arr[i].equals(name)){
							receivePersonChoice.addItem(arr[i]);
						}
					}
					receivePersonChoice.select(receivePersonName);
					
				// 접속 인원 수 최신화 메세지는 #로 확인
				}else if(msg.startsWith("#")){
					msg = msg.substring(1);
					personListLabel2.setText("(" + msg + "명)");
					
				// 접속자 명단을 ArrayList에 담는 메세지는 %로 수신
				}else if(msg.startsWith("%")){
					list.clear();
					msg = msg.substring(1).trim();
					String[] arr = msg.split(" ");
					for(int i = 0; i < arr.length; i++){
						list.add(arr[i]);
					}
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// 서버가 종료되었을 때
			card.show(me, "p1");
			page1MainLabel.setText("서버가 존재하지 않습니다");
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
