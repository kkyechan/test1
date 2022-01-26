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
	static String receivePersonName = "��� ";
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
		
		// ù ��° ������
		page1 = new Panel(new GridLayout(2, 1));
		
		// ù ��° ������ ����
		Panel upperPanel = new Panel(new BorderLayout());
		
		Label titleLabel = new Label("ä�� ���α׷�", Label.CENTER);
		upperPanel.add(titleLabel, BorderLayout.CENTER);
		
		// ù ��° ������ ȯ�� ����(���� ���� ���� Ȯ�� �޼���)
		page1MainLabel = new Label("ȯ���մϴ�!", Label.CENTER);
		page1MainLabel.setForeground(Color.BLUE);
		upperPanel.add(page1MainLabel, BorderLayout.SOUTH);
		page1.add(upperPanel);
		
		// ù ��° ������ �̸� �Է� �г�
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
				warninglb.setText("");
			}
		});
		// �̸��� �Է¹����� �Է� ��ư Ȱ��ȭ, �Էµ� �̸��� ��, ��, ���� ������ ����� name ������ ���
		nameTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				enterButton.setEnabled(true);
			}
		});
		nameTextField.addActionListener(this);
		loginPanel.add(nameTextField);
		
		// ���� ��ư
		enterButton = new Button("����");
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
		
		// �ȳ� ����
		Panel warningPanel = new Panel(new BorderLayout());
		warninglb = new Label("", Label.CENTER);
		warninglb.setForeground(Color.RED);
		warningPanel.add(warninglb, BorderLayout.NORTH);
		lowerPanel.add(warningPanel, BorderLayout.CENTER);
		page1.add(lowerPanel);
		
		// �� ��° ������
		page2 = new Panel(new BorderLayout());
		
		// �� ��° ������ ����ȭ��
		page2CenterPanel = new Panel(new BorderLayout());
		
		// �� ��° ������ ä��ȭ��
		viewTextArea = new TextArea();
		viewTextArea.setEditable(false);
		
		// �� ��° ������ ä�� �Է� �κ�
		Panel inputPanel = new Panel(new BorderLayout());
		
		// ���� ��ư
		inputButton = new JButton("�� ��");
		inputButton.setBackground(Color.WHITE);
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
					inputMsg = extraMsg.substring(0, extraMsg.lastIndexOf('\n') - 1).trim();
					String[] arr1 = inputMsg.split("\n");
					// ���ڰ� �������� ������ �޼��� ���� �ȵ�.
					if((inputMsg.trim()).equals("")){
						mainTextArea.setText("");
						mainTextArea.append(inputMsg);
					}else{
						try {
							// ��ο��� ������ �޼���// �� ������Ը� ������ �ӼӸ�
							if(receivePersonName.equals("��� ")){
								bw.write("&[" + name + " - ��ο���] (" + time + ")");
								bw.newLine();
								for(int i = 0; i < arr1.length; i++){
									bw.write("&{" + arr1[i]);
								}
								bw.newLine();
								bw.flush();
							// �� ������Ը� ������ �ӼӸ�
							}else{
								bw.write("^" + receivePersonName);
								bw.newLine();
								bw.write("^[" + name + " - " + receivePersonName + "���� �ӼӸ�] (" + time + ")");
								bw.newLine();
								for(int i = 0; i < arr1.length; i++){
									bw.write("^{" + arr1[i]);
								}
								bw.newLine();
								bw.flush();
								viewTextArea.append("\n[" + name + " - " + receivePersonName + "���� �ӼӸ�] (" + time + ")\n" + inputMsg.trim() + "\n");
							}
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
		
		// �޼��� �Է� ����â
		Panel mainTextAreaStatusPanel = new Panel(new BorderLayout());
		
		Panel extraPanel = new Panel();
		
		mainTextAreaStatusLabel1 = new Label("", Label.CENTER);
		extraPanel.add(mainTextAreaStatusLabel1);
		
		// ������ ����â
		receivePersonChoice = new Choice();
		receivePersonChoice.addItem("��� ");
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
		
		// �� ��° ������ �����г�
		page2NorthPanel = new Panel(new BorderLayout());
		
		// �� ��° ������ �����г��� ��� �г�
		Panel page2ListPanel = new Panel(new BorderLayout());
		
		// �� ��° ������ ��� �г��� �޴� �г�
		Panel MenuPanel = new Panel(new BorderLayout());
		
		MenuButton = new JButton("�޴�");
		MenuButton.setBackground(Color.WHITE);
		MenuButton.addActionListener(this);
		MenuPanel.add(MenuButton, BorderLayout.EAST);
		
		page2ListPanel.add(MenuPanel, BorderLayout.NORTH);
		
		// MenuButton Ŭ�� �� �����ϴ� �˾��޴�
		popupMenu1 = new PopupMenu();
		
		// �˾��޴��� ù ��° �޴� ������
		popupMenu1Item1 = new MenuItem("ó������");
		popupMenu1Item1.addActionListener(this);
		popupMenu1.add(popupMenu1Item1);
		
		// �˾��޴��� �� ��° �޴� ������
		popupMenu1Item2 = new MenuItem("����");
		popupMenu1Item2.addActionListener(this);
		popupMenu1.add(popupMenu1Item2);
		
		// �� ��° ������ ���� �г��� ��� �г��� ��¥ ǥ�� ��
		MenuTimeLabel1 = new JLabel("0000�� 00�� 00��", JLabel.CENTER);
		MenuTimeLabel1.setOpaque(true);
		MenuTimeLabel1.setBackground(Color.LIGHT_GRAY);
		MenuPanel.add(MenuTimeLabel1, BorderLayout.WEST);
		
		// �� ��° ������ ���� �г��� ��� �г��� �ð� ǥ�� ��
		MenuTimeLabel2 = new JLabel("00:00", JLabel.CENTER);
		MenuTimeLabel2.setOpaque(true);
		MenuTimeLabel2.setBackground(Color.LIGHT_GRAY);
		MenuPanel.add(MenuTimeLabel2, BorderLayout.CENTER);
		
		// �� ��° ������ ��� �г��� ���� �ο� ��Ȳ ǥ��â
		Panel personListPanel = new Panel(new GridLayout(2, 1));
		
		JLabel personListLabel1 = new JLabel(" ���� �ο� ", JLabel.CENTER);
		personListPanel.add(personListLabel1);
		
		// ���� �ο� ���� �����ִ� ��
		personListLabel2 = new JLabel("(0��)", JLabel.CENTER);
		personListPanel.add(personListLabel2);
		page2ListPanel.add(personListPanel, BorderLayout.WEST);
		
		// ���� �ο� ����� �����ִ� ����Ʈ
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
		
		// �� ��° ������ ����� ����� �˾��޴�
		popupMenu2 = new PopupMenu();
		
		// �� ��° ������ ����� ����� �˾��޴��� ù ��° �޴� ������
		popupMenu2Item1 = new MenuItem("�ӼӸ�");
		popupMenu2Item1.addActionListener(this);
		popupMenu2.add(popupMenu2Item1);
		
		page2ListPanel.add(personList, BorderLayout.CENTER);
		page2NorthPanel.add(page2ListPanel, BorderLayout.NORTH);
		
		page2.add(page2CenterPanel, BorderLayout.CENTER);
		page2.add(page2NorthPanel, BorderLayout.NORTH);
		
		// �˸�â
//		dl = new Dialog(this, "�˸�", true);
//		Panel dlp = new Panel(new GridLayout(2, 1));
//		Panel dlp1 = new Panel(new BorderLayout());
//		dlplb = new Label("", Label.CENTER);
//		dlp1.add(dlplb, BorderLayout.SOUTH);
//		dlp.add(dlp1);
//		Panel dlp2 = new Panel(new BorderLayout());
//		Panel dlsp = new Panel();
//		Button dlpbtn = new Button("Ȯ��");
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
						bw.write(name + "���� �����Ͽ����ϴ�. (" + time + ")");
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
		
		// ī�� ���̾ƿ�
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
		
		// ���� ��ư Ŭ�� ��
		if(obj == enterButton){
			name = ((nameTextField.getText()).trim()).replace(" ", "");
			if(name.equals("")){
				// �̸� ���Է� �� �����ϴ� �ȳ�����
				warninglb.setText("�̸��� �Է����ּ���");
			}else if(list.contains(name)){
				warninglb.setText("�̹� ������� �̸��Դϴ�");
			}else if(name.contains("~") || name.contains("`") || name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&") || name.contains("*") || name.contains("(") || name.contains(")") || name.contains("-") || name.contains("_") || name.contains("=") || name.contains("+") || name.contains(",") || name.contains(".") || name.contains("/") || name.contains("<") || name.contains(">") || name.contains("?") || name.contains(";") || name.contains(":") || name.contains("'") || name.contains("\"") || name.contains("[") || name.contains("]") || name.contains("{") || name.contains("}") || name.contains("\\") || name.contains("|")){
				warninglb.setText("Ư�����ڴ� ����� �� �����ϴ�");
			}else{
				try {
					bw.write("$" + name);
					bw.newLine();
					
					viewTextArea.setText("ä���� ���۵Ǿ����ϴ�.\n");
					mainTextArea.setText("");
					
					mainTextAreaStatusLabel1.setText(name + "��(��)");
					mainTextAreaStatusLabel2.setText("���� ����");
					
					card.show(this, "p2");
					
					date = new Date();
					sdf = new SimpleDateFormat("HH:mm:ss");
					time = sdf.format(date);
					
					bw.write(name + "���� �����Ͽ����ϴ�. (" + time + ")");
					bw.newLine();
					bw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			nameTextField.setText("");
			receivePersonName = "��� ";
			
		// ó������ ���� �޴� Ŭ�� ��
		}else if(obj == popupMenu1Item1){
			try {
				date = new Date();
				sdf = new SimpleDateFormat("HH:mm:ss");
				time = sdf.format(date);
				
				if(!name.equals("")){
					bw.write("@" + name);
					bw.newLine();
					bw.write(name + "���� �����Ͽ����ϴ�. (" + time + ")");
					bw.newLine();
					bw.write("%");
					bw.newLine();
					bw.flush();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			name = "";
			page1MainLabel.setText("ȯ���մϴ�!");
			nameTextField.setText("");
			
			card.show(this, "p1");
			
		// ���� �޴� Ŭ�� ��
		}else if(obj == popupMenu1Item2){
			dispose();
			try {
				date = new Date();
				sdf = new SimpleDateFormat("HH:mm:ss");
				time = sdf.format(date);
				
				if(!name.equals("")){
					bw.write("@" + name);
					bw.newLine();
					bw.write(name + "���� �����Ͽ����ϴ�. (" + time + ")");
					bw.newLine();
					bw.write("%");
					bw.newLine();
					bw.flush();
				}
				
				endMsg = true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		// ���� ��ư Ŭ�� ��
		}else if(obj == inputButton){
			System.out.println(receivePersonName);
			date = new Date();
			sdf = new SimpleDateFormat("HH:mm:ss");
			time = sdf.format(date);
			
			inputMsg = mainTextArea.getText().trim();
			String[] arr1 = inputMsg.split("\n");
			if((inputMsg.trim()).equals("")){
				// ���ڰ� �������� ������ ä��â�� ���µǰ� �޼��� ���۵� �ȵ�.
				mainTextArea.setText("");
			}else{
				try {
					// ��ο��� ������ �޼���
					if(receivePersonName.equals("��� ")){
						bw.write("&[" + name + " - ��ο���] (" + time + ")");
						bw.newLine();
						for(int i = 0; i < arr1.length; i++){
							bw.write("&{" + arr1[i]);
						}
						bw.newLine();
						bw.flush();
						
					// �� ������Ը� ������ �ӼӸ�
					}else{
						bw.write("^" + receivePersonName);
						bw.newLine();
						bw.write("^[" + name + " - " + receivePersonName + "���� �ӼӸ�] (" + time + ")");
						bw.newLine();
						for(int i = 0; i < arr1.length; i++){
							bw.write("^{" + arr1[i]);
						}
						bw.newLine();
						bw.flush();
						viewTextArea.append("\n[" + name + " - " + receivePersonName + "���� �ӼӸ�] (" + time + ")\n" + inputMsg.trim() + "\n");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			mainTextArea.setText("");
			}
		
		// �޴� ��ư Ŭ�� ��
		}else if(obj == MenuButton){
			popupMenu1.show(page2, 219, 28);
			
		// �̸� �Է�â���� Ű���带 ������ ��
		}else if(obj == nameTextField){
			warninglb.setText("���콺�� ���� ��ư�� Ŭ�����ּ���");
			
		// ����� ����� �˾��޴��� �ӼӸ� �޴��� Ŭ�� ��
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
		
		// ���� �ð� ǥ�� �г��� ������
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
				
				// ���α׷� ���� �� endMsg�� true�� �Ǹ鼭 �ݺ��� ����
				if(endMsg){
					break;
				
				// ������ �޼��� �� �Ϲ� �޼����� ���� �� *�� Ȯ��
				}else if(msg.startsWith("*")){
					msg = msg.substring(1);
					if(msg.startsWith("?")){
						msg = msg.substring(1);
						viewTextArea.append(msg + "\n");
					}else{
						viewTextArea.append("\n" + msg + "\n");
					}
					
				// ���� �ο� ��� �ֽ�ȭ �޼����� !�� Ȯ��
				}else if(msg.startsWith("!")){
					personList.removeAll();
					receivePersonChoice.removeAll();
					receivePersonChoice.add("��� ");
					
					msg = msg.substring(1).trim();
					String[] arr = msg.split(" ");
					for(int i = 0; i < arr.length; i++){
						personList.add(arr[i]);
						if(!arr[i].equals(name)){
							receivePersonChoice.addItem(arr[i]);
						}
					}
					receivePersonChoice.select(receivePersonName);
					
				// ���� �ο� �� �ֽ�ȭ �޼����� #�� Ȯ��
				}else if(msg.startsWith("#")){
					msg = msg.substring(1);
					personListLabel2.setText("(" + msg + "��)");
					
				// ������ ����� ArrayList�� ��� �޼����� %�� ����
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
			// ������ ����Ǿ��� ��
			card.show(me, "p1");
			page1MainLabel.setText("������ �������� �ʽ��ϴ�");
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
