package com.test.haley01;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// 서버 화면 구성 클래스 : 화면에 윈도우를 출력(JFrame 클래스 상속)
// 사용자가 메세지를 입력후 엔터 누를 때 발생하는 이벤트 -> ActionListener
public class MyServerClass extends JFrame implements ActionListener {

	// 1. 전역 변수

	// 서버에 접속한 모든 클라이언트가 보낸 메세지를 보여주는 창
	private JTextArea mAllMessagesTa = new JTextArea(40, 25);
	private JPanel panel = new JPanel();
	private JButton bt = new JButton("전송");

	// 메세지 입력 창
	private JTextField mMessageTf = new JTextField(25);

	// 서버 스레드 객체 생성
	private MyChattingServerBackClass mServer = new MyChattingServerBackClass();

	// 1-2 생성자 함수
	public MyServerClass() throws IOException {
		
		
		// 가운데 위치에 TA 넣기
		add(mAllMessagesTa, BorderLayout.CENTER);
		add(new JScrollPane(mAllMessagesTa));
		add(panel, BorderLayout.SOUTH );
		 
		panel.setLayout(new FlowLayout());
		panel.add(mMessageTf);
		panel.add(bt);

		// 아래에 TF 넣기
		//add(mMessageTf, BorderLayout.SOUTH);
		
		//mJScrollPane.setViewportView(mAllMessagesTa);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("사용자가 X 버튼 클릭");
				
		
				
			}
			
		});

		// 엔터 이벤트
		mMessageTf.addActionListener(this);
		// send 버튼 클릭시
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String msg = "[관리자] " + mMessageTf.getText() + "\n";
				System.out.print(msg);
				mServer.sendMessage(msg);
				appendMsg(msg);
				mMessageTf.setText("");
				
			}
		});
		

		// 화면에 출력할 윈도우 창의 위치와 타이틀
		setBounds(200, 100, 400, 600);

		// 화면에 출력할 타이틍
		setTitle("채팅 서버 창");

		// 화면에 출력되는 창에서 사용자가 윈도우 종료 버튼을 눌렀을 때
		// 현재 화면을 화면에서 없애고 프로그램 전체 종료
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 화면 출력
		setVisible(true);

		// 현재 화면 객체의 주소를 서버 객체 전역 변수에 저장
		mServer.setGui(this);

		// 서버 소켓 객체를 메모리에 생성하고 클라이언트 연결을 기다림
		mServer.setting();

	} // end of MyChattingServerGUIClass()

	// 4. 메인 함수
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MyServerClass server;

		try {

			// 위에서 만든 생성자 함수 실행
			server = new MyServerClass();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

		}

	} // end of main()

	// 2. 재정의 함수
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		// 사용자가 입력한 메세지 가져오기
		// 텍스트 아레아에 사용자가 입력한 메세지 출력후 개행
		String msg = "[관리자] " + mMessageTf.getText() + "\n";
		
		// 콘솔 화면에 메세지 출력
		System.out.print(msg);

		// 현재 서버에 접속한 모든 클라이언트들에게도 전송
		mServer.sendMessage(msg);

		// 현재 창에 있는 ta에 메세지 추가
		appendMsg(msg);

		// 사용자가 입력했던 메세지 지우기
		mMessageTf.setText("");

	} // end of actionPerformed()
	
	

	// 3. 사용자 정의 함수

	// 사용자가 입력한 메세지를 화면에 보여주는 함수
	public void appendMsg(String msg) {

		if (msg != null && msg.trim().length() > 0) {
			
			mAllMessagesTa.append(msg);

		}

	} // end of public void appendMsg(String msg)

} // end of class
