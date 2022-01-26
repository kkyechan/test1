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
		// �޴� �� - ����(�޴� �г� ���� ���)
//		MenuBar bar = new MenuBar();
//		Menu mn1 = new Menu("�޴�");
//		mi1 = new MenuItem("ó������");
//		mi1.addActionListener(this);
//		mn1.add(mi1);
//		mi2 = new MenuItem("����");
//		mi2.addActionListener(this);
//		mn1.add(mi2);
//		bar.add(mn1);
//		setMenuBar(bar);
		
		// ù ��° ������
		page1 = new Panel(new GridLayout(2, 1));
		// ù ��° ������ ����
		Panel upperPanel = new Panel(new BorderLayout());
		Label titleLabel = new Label("ä�� ���α׷�", Label.CENTER);
		upperPanel.add(titleLabel, BorderLayout.CENTER);
		page1.add(upperPanel);
		// ù ��° ������ �̸� �Է�
		Panel lowerPanel = new Panel(new BorderLayout());
		Panel loginPanel = new Panel();
		// �̸� �Է� â
		nameTextField = new TextField("  �̸��� �Է��ϼ���", 15);
		nameTextField.setEditable(false);
		// �̸� �Է�â ���� �� �Է�â Ȱ��ȭ �� �ȳ����� ����
		nameTextField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				nameTextField.setEditable(true);
				nameTextField.setText("");
			}
		});
		// �̸��� �Է¹����� �Է� ��ư Ȱ��ȭ, �Էµ� �̸��� ��, ��, ���� ������ ����� name ������ ���
		nameTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				enterButton.setEnabled(true);
			}
		});
		loginPanel.add(nameTextField);
		// ���� ��ư
		enterButton = new Button("����");
		enterButton.setEnabled(false);
		enterButton.addActionListener(this);
		loginPanel.add(enterButton);
		lowerPanel.add(loginPanel, BorderLayout.NORTH);
		page1.add(lowerPanel);
		// �� ��° ������
		page2 = new Panel(new GridLayout(1, 2));
		// �� ��° ������ ���� ��
//		page2Game = new Panel(new BorderLayout());
//		page2GameSub = new Panel();
//		page2Game.add(page2GameSub, BorderLayout.NORTH);
//		page2GameMain = new JPanel(new ImageIcon("thile.png"));
//		page2GameMain.set
//		page2Game.add(page2GameMain, BorderLayout.CENTER);
//		page2.add(page2Game);
		
		// �� ��° ������ ä�� ��
		page2Chat = new Panel(new BorderLayout());
		// �� ��° ������  ä�� �� ����ȭ��
		page2ChatCenter = new Panel(new BorderLayout());
		// �� ��° ������ ä��ȭ��
		veiwTextArea = new TextArea();
		veiwTextArea.setEditable(false);
		// �� ��° ������ ä�� �Է� �κ�
		Panel inputPanel = new Panel(new BorderLayout());
		// ���� ��ư
		inputButton = new Button(" �� �� ");
		inputButton.setEnabled(false);
		inputButton.addActionListener(this);
		inputPanel.add(inputButton, BorderLayout.EAST);
		// �޼��� �Է�â
		mainTextArea = new TextArea("", 3, 10);
		// �Է��� ������ ���ų� ���鹮���� ��� ���� ��ư ��Ȱ��ȭ ���� ����
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
				// shift enter �Է� �� mainTextArea ������ ����
				if(input1 == '\n' && input2 == 1){
				// enter �Է� �� �޼��� ����
				}else if(input1 == '\n'){
					// �޼��� ���� �� ���� �ð� ���
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					extraMsg = mainTextArea.getText();
					inputMsg = extraMsg.substring(0, extraMsg.lastIndexOf('\n') - 1);
					// ���ڰ� �������� ������ �޼��� ���� �ȵ�.
					if((inputMsg.trim()).equals("")){
						mainTextArea.setText("");
						mainTextArea.append(inputMsg);
					}else{
						try {
							//�޼��� ����
							bw.write("\n[" + name + "] (" + time + ")\n" + inputMsg.trim());
							bw.newLine();
							bw.flush();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						// �޼��� ���� �� ä��â �ʱ�ȭ
						mainTextArea.setText("");
					}
				}
				
			}
		});
		inputPanel.add(mainTextArea, BorderLayout.CENTER);
		page2ChatCenter.add(veiwTextArea, BorderLayout.CENTER);
		page2ChatCenter.add(inputPanel, BorderLayout.SOUTH);
		// �� ��° ������ ����ȭ��
		page2ChatRight = new Panel(new BorderLayout());
		// �� ��° ������ ��� �г�
		Panel page2NorthPanel = new Panel(new BorderLayout());
		// �� ��° ������ ��� �г��� ���� �ο� ��Ȳ ǥ��â
		personListLabel = new Label("���� �ο� (0��)");
		page2NorthPanel.add(personListLabel, BorderLayout.NORTH);
		personList = new List(5, false);
		page2NorthPanel.add(personList, BorderLayout.CENTER);
		page2ChatRight.add(page2NorthPanel, BorderLayout.NORTH);
		
		page2Chat.add(page2ChatCenter, BorderLayout.CENTER);
		page2Chat.add(page2ChatRight, BorderLayout.EAST);
		
		page2.add(page2Chat);
		// �˸�â
		dl = new Dialog(this, "�˸�", true);
		Panel dlp = new Panel(new GridLayout(2, 1));
		Panel dlp1 = new Panel(new BorderLayout());
		dlplb = new Label("", Label.CENTER);
		dlp1.add(dlplb, BorderLayout.SOUTH);
		dlp.add(dlp1);
		Panel dlp2 = new Panel(new BorderLayout());
		Panel dlsp = new Panel();
		Button dlpbtn = new Button("Ȯ��");
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
					bw.write("\n" + name + "���� �����Ͽ����ϴ�. (" + time + ")");
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
		if(obj == enterButton){ // ���� ��ư Ŭ�� ��
			name = ((nameTextField.getText()).trim()).replace(" ", "");
			if(name.equals("")){
				// �̸� ���Է� �� �����ϴ� �˸�â
				dlplb.setText("�̸��� �Է����ּ���");
				dl.setVisible(true);
			}else if(list.contains(name)){
				dlplb.setText("�̹� ������� �̸��Դϴ�");
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
					veiwTextArea.setText("ä���� ���۵Ǿ����ϴ�\n");
					card.show(this, "p2");
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					bw.write("\n" + name + "���� �����Ͽ����ϴ�. (" + time + ")");
					bw.newLine();
					bw.flush();
					mainTextArea.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
//		}else if(obj == mi1){
//			try {
//				bw.write("\n" + name + "���� �����Ͽ����ϴ�.");
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
				// ���ڰ� �������� ������ ä��â�� ���µǰ� �޼��� ���۵� �ȵ�.
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
				}else if(msg.startsWith("*")){ // ������ �޼��� �� �Ϲ� �޼����� ���� �� *�� Ȯ��
					msg = msg.substring(1);
					veiwTextArea.append(msg + "\n");
				}else if(msg.startsWith("!")){ // ���� �ο� �ֽ�ȭ �޼����� !�� Ȯ��
					personList.removeAll();
					msg = msg.substring(1).trim();
					String[] arr = msg.split(" ");
					list = new ArrayList<>();
					for(int i = 0; i < arr.length; i++){
						personList.add(arr[i]);
						list.add(arr[i]);
					}
				}else if(msg.startsWith("#")){ // ���� �ο� �� �ֽ�ȭ �޼����� #�� Ȯ��
					msg = msg.substring(1);
					personListLabel.setText("���� �ο� (" + msg + "��)");
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
