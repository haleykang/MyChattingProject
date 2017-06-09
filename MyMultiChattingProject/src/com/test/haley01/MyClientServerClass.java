package com.test.haley01;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// 클라이언트 화면 구성

public class MyClientServerClass extends JFrame implements ActionListener {

	private JTextArea mTa = new JTextArea(40, 25);
	private JTextField mTf = new JTextField(25);
	private JButton mBt = new JButton("전송");

	private JPanel panel = new JPanel();
	// 클라이언트 스레드 객체
	private MyClientBackClass mClient = new MyClientBackClass();

	// 닉네임 보관 변수
	private static String mNickName;

	// 생성자 함수
	public MyClientServerClass() {

		add(mTa, BorderLayout.CENTER);
		add(new JScrollPane(mTa));

		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout());
		panel.add(mTf);
		panel.add(mBt);

		// 사용자가 tf에 입력 후 엔터 누를때 이벤트 처리
		mTf.addActionListener(this);
		
		mBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String msg = "[" + mNickName + "] " + mTf.getText() + "\n";

				mClient.sendMessage(msg);
				// appendMsg(msg);

				mTf.setText("");
				
			}
		});
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
			//	super.windowClosing(e);
				System.out.println("종료 버튼 클릭");
				
				
				String message = mNickName + "님이 퇴장하셨습니다.\n";
				
				// 서버에 접속 중인 모든 사용자에게 메세지 전송
				mClient.sendMessage(message);
				/*
				// 서버 화면에도 메세지 출력
				mServerGUI.appendMsg(message);
				
				// remove() 퇴장한 사용자 정보를 HashMap에서 없애기
				mClientMap.remove(nick);*/
				
				
			}
		});
		
		

		// 윈도우 종료(x) 클릭 시 창 꺼짐 & 프로그램 종료
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		

		setBounds(800, 100, 400, 600);
		setTitle("사용자 채팅 창");
		setVisible(true);

		mClient.setGui(this);
		mClient.setNickname(mNickName);
		mClient.connect();

	} // end of public MyClientClass() -> 생성자 함수

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);

		System.out.print("사용하실 닉네임을 입력하세요 : ");

		mNickName = scanner.nextLine();

		scanner.close();

		// 생성자 함수 실행 -> 화면에 클라이언트 창 출력
		new MyClientServerClass();

	}

	// 사용자가 메세지 입력 후 엔터 입력하는 경우
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		String msg = "[" + mNickName + "] " + mTf.getText() + "\n";

		mClient.sendMessage(msg);
		// appendMsg(msg);

		mTf.setText("");

	} // end of public void actionPerformed(ActionEvent e)

	// 사용자가 입력한 메세지를 화면에 출력
	public void appendMsg(String msg) {
		mTa.append(msg);
	}

}
