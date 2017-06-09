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

// ���� ȭ�� ���� Ŭ���� : ȭ�鿡 �����츦 ���(JFrame Ŭ���� ���)
// ����ڰ� �޼����� �Է��� ���� ���� �� �߻��ϴ� �̺�Ʈ -> ActionListener
public class MyServerClass extends JFrame implements ActionListener {

	// 1. ���� ����

	// ������ ������ ��� Ŭ���̾�Ʈ�� ���� �޼����� �����ִ� â
	private JTextArea mAllMessagesTa = new JTextArea(40, 25);
	private JPanel panel = new JPanel();
	private JButton bt = new JButton("����");

	// �޼��� �Է� â
	private JTextField mMessageTf = new JTextField(25);

	// ���� ������ ��ü ����
	private MyChattingServerBackClass mServer = new MyChattingServerBackClass();

	// 1-2 ������ �Լ�
	public MyServerClass() throws IOException {
		
		
		// ��� ��ġ�� TA �ֱ�
		add(mAllMessagesTa, BorderLayout.CENTER);
		add(new JScrollPane(mAllMessagesTa));
		add(panel, BorderLayout.SOUTH );
		 
		panel.setLayout(new FlowLayout());
		panel.add(mMessageTf);
		panel.add(bt);

		// �Ʒ��� TF �ֱ�
		//add(mMessageTf, BorderLayout.SOUTH);
		
		//mJScrollPane.setViewportView(mAllMessagesTa);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("����ڰ� X ��ư Ŭ��");
				
		
				
			}
			
		});

		// ���� �̺�Ʈ
		mMessageTf.addActionListener(this);
		// send ��ư Ŭ����
		bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String msg = "[������] " + mMessageTf.getText() + "\n";
				System.out.print(msg);
				mServer.sendMessage(msg);
				appendMsg(msg);
				mMessageTf.setText("");
				
			}
		});
		

		// ȭ�鿡 ����� ������ â�� ��ġ�� Ÿ��Ʋ
		setBounds(200, 100, 400, 600);

		// ȭ�鿡 ����� Ÿ�̺v
		setTitle("ä�� ���� â");

		// ȭ�鿡 ��µǴ� â���� ����ڰ� ������ ���� ��ư�� ������ ��
		// ���� ȭ���� ȭ�鿡�� ���ְ� ���α׷� ��ü ����
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// ȭ�� ���
		setVisible(true);

		// ���� ȭ�� ��ü�� �ּҸ� ���� ��ü ���� ������ ����
		mServer.setGui(this);

		// ���� ���� ��ü�� �޸𸮿� �����ϰ� Ŭ���̾�Ʈ ������ ��ٸ�
		mServer.setting();

	} // end of MyChattingServerGUIClass()

	// 4. ���� �Լ�
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MyServerClass server;

		try {

			// ������ ���� ������ �Լ� ����
			server = new MyServerClass();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

		}

	} // end of main()

	// 2. ������ �Լ�
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		// ����ڰ� �Է��� �޼��� ��������
		// �ؽ�Ʈ �Ʒ��ƿ� ����ڰ� �Է��� �޼��� ����� ����
		String msg = "[������] " + mMessageTf.getText() + "\n";
		
		// �ܼ� ȭ�鿡 �޼��� ���
		System.out.print(msg);

		// ���� ������ ������ ��� Ŭ���̾�Ʈ�鿡�Ե� ����
		mServer.sendMessage(msg);

		// ���� â�� �ִ� ta�� �޼��� �߰�
		appendMsg(msg);

		// ����ڰ� �Է��ߴ� �޼��� �����
		mMessageTf.setText("");

	} // end of actionPerformed()
	
	

	// 3. ����� ���� �Լ�

	// ����ڰ� �Է��� �޼����� ȭ�鿡 �����ִ� �Լ�
	public void appendMsg(String msg) {

		if (msg != null && msg.trim().length() > 0) {
			
			mAllMessagesTa.append(msg);

		}

	} // end of public void appendMsg(String msg)

} // end of class
