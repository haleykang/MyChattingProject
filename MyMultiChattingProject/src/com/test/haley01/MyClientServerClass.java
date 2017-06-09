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

// Ŭ���̾�Ʈ ȭ�� ����

public class MyClientServerClass extends JFrame implements ActionListener {

	private JTextArea mTa = new JTextArea(40, 25);
	private JTextField mTf = new JTextField(25);
	private JButton mBt = new JButton("����");

	private JPanel panel = new JPanel();
	// Ŭ���̾�Ʈ ������ ��ü
	private MyClientBackClass mClient = new MyClientBackClass();

	// �г��� ���� ����
	private static String mNickName;

	// ������ �Լ�
	public MyClientServerClass() {

		add(mTa, BorderLayout.CENTER);
		add(new JScrollPane(mTa));

		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout());
		panel.add(mTf);
		panel.add(mBt);

		// ����ڰ� tf�� �Է� �� ���� ������ �̺�Ʈ ó��
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
				System.out.println("���� ��ư Ŭ��");
				
				
				String message = mNickName + "���� �����ϼ̽��ϴ�.\n";
				
				// ������ ���� ���� ��� ����ڿ��� �޼��� ����
				mClient.sendMessage(message);
				/*
				// ���� ȭ�鿡�� �޼��� ���
				mServerGUI.appendMsg(message);
				
				// remove() ������ ����� ������ HashMap���� ���ֱ�
				mClientMap.remove(nick);*/
				
				
			}
		});
		
		

		// ������ ����(x) Ŭ�� �� â ���� & ���α׷� ����
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		

		setBounds(800, 100, 400, 600);
		setTitle("����� ä�� â");
		setVisible(true);

		mClient.setGui(this);
		mClient.setNickname(mNickName);
		mClient.connect();

	} // end of public MyClientClass() -> ������ �Լ�

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);

		System.out.print("����Ͻ� �г����� �Է��ϼ��� : ");

		mNickName = scanner.nextLine();

		scanner.close();

		// ������ �Լ� ���� -> ȭ�鿡 Ŭ���̾�Ʈ â ���
		new MyClientServerClass();

	}

	// ����ڰ� �޼��� �Է� �� ���� �Է��ϴ� ���
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		String msg = "[" + mNickName + "] " + mTf.getText() + "\n";

		mClient.sendMessage(msg);
		// appendMsg(msg);

		mTf.setText("");

	} // end of public void actionPerformed(ActionEvent e)

	// ����ڰ� �Է��� �޼����� ȭ�鿡 ���
	public void appendMsg(String msg) {
		mTa.append(msg);
	}

}
