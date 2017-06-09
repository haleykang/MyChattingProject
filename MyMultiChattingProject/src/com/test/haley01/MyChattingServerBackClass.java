package com.test.haley01;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;

// Collections, HashMap, Iterator, Map,Set Ŭ����/�������̽� ���

public class MyChattingServerBackClass {

	// ���� ����
	private ServerSocket mServerSocket;

	// �޾ƿ� ���� ����
	private Socket mClientSocket;

	// ���� ȭ�� ��ü
	private MyServerClass mServerGUI;

	// �޼��� ���� ����
	private String mMsg;

	// ����ڵ��� ������ �����ϴ� ��
	// String : Ű(key)�� �ڷ��� -> �г����� Ÿ�� -> ������ �г����� ���� x

	private Map<String, DataOutputStream> mClientMap = new HashMap<String, DataOutputStream>();

	// ȭ�� ��ü ���� �Լ�
	public void setGui(MyServerClass gui) {
		this.mServerGUI = gui;
	}

	// ������ ������ �� �������ִ� �Լ�
	// 1. �޸𸮿� ���� ���� ��ü ����
	// 2. accept() �Լ� ���� -> Ŭ���̾�Ʈ ���� ��ٸ�
	public void setting() {

		try {

			// clientMap�� ��Ʈ��ũ ó�� (����ȭ)
			Collections.synchronizedMap(mClientMap);

			mServerSocket = new ServerSocket(7777);

			while (true) {

				// �湮�ڸ� ��� �޾Ƽ�, ������ ���ù��� ��� ����
				System.out.println("Ŭ���̾�Ʈ���� ��ٸ��� ��...");

				mClientSocket = mServerSocket.accept();

				System.out.println(mClientSocket.getInetAddress() + "���� �����߽��ϴ�.");

				// ���⼭ ���ο� ����� ������ Ŭ������ �����ؼ� ���� ���� �ֱ�
				// Ŭ���̾�Ʈ ���� ������ �����ϴ� ������ ��ü ����
				// Receiver : ������ Ŭ����
				Receiver receiver = new Receiver(mClientSocket);

				receiver.start();

				// mClientSocket : ������ ����� Ŭ���̾�Ʈ ���� ���� ���� , ������ ����½� ����� ����

			}

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} // end of setting()

	// ���� ���� ����� ����

	// ���ù� Ŭ����(������ Ŭ����)
	class Receiver extends Thread {

		private DataInputStream in;

		private DataOutputStream out;

		// �г��� ���� ����
		private String nick;

		// Ŭ���̾�Ʈ ���� ��ü�� �޴� ������ �Լ�

		public Receiver(Socket socket) {

			// IOException ���
			try {

				// ���Ϥ����� ���� ������ ��¿� ����� ��ü �ּ� ��������
				out = new DataOutputStream(socket.getOutputStream());

				// �������κ��� ������ �Է¿� ����� ��ü �ּ� ��������
				in = new DataInputStream(socket.getInputStream());

				// readUTF() : ���ڿ� ���� �о���� �Լ� -> ����ڰ� �Է��� �г����� �о�ͼ� ����
				nick = in.readUTF();

				// hashMap �ڷ� ������ ���ο� Ŭ���̾�Ʈ ���� ����
				addClient(nick, out);

			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} // end of Receiver ������

		// ��� ��������� �Բ� �����ϴ� fun �Լ�
		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {

				while (in != null) {

					if (in.available() > 0) {

						mMsg = in.readUTF();
						sendMessage(mMsg);
						mServerGUI.appendMsg(mMsg);

						if (mMsg.equals("/q") == true) {
							System.out.println("if(mMsg.equals(\"/q\")==true)");
							removeClient(nick);

						}

					}

				} // end of while

			} catch (Exception e) {
				// TODO: handle exception

				e.printStackTrace();
			}

		} // end of run()

	} // end of Receiver Ŭ����

	// ���� ������ ���� ���� ��� Ŭ���̾�Ʈ �鿡�� ���ο� Ŭ���̾�Ʈ ������ �˸���
	// �޼����� ����ϰ� HashMap �ڷ� ������ Ŭ���̾�Ʈ�� �����ϴ� �Լ�
	public void addClient(String nick, DataOutputStream out) throws IOException {

		// ȭ�鿡 ����� �޼��� ���� ����
		String message = nick + "���� �����ϼ̽��ϴ�.\n";

		// ������ ���� ���� ��� Ŭ���̾�Ʈ���� �޼��� ����
		sendMessage(message);

		// ���� ȭ�鿡�� �޼��� �߰�
		mServerGUI.appendMsg(message);

		// HashMap �ڷ� ������ ���ο� Ŭ���̾�Ʈ ���� (put() �Լ�)
		// nick : Ű(key)
		// out : ��(Value)
		mClientMap.put(nick, out);

	} // end of addClient()

	// ����� ���� �Լ� ó��
	public void removeClient(String nick) {

		String message = nick + "���� �����ϼ̽��ϴ�.\n";

		// ������ ���� ���� ��� ����ڿ��� �޼��� ����
		sendMessage(message);

		// ���� ȭ�鿡�� �޼��� ���
		mServerGUI.appendMsg(message);

		// remove() ������ ����� ������ HashMap���� ���ֱ�
		mClientMap.remove(nick);
	} // end of removeClient()

	// �޼��� ������ ���� ������ ������ ��� Ŭ���̾�Ʈ�鿡�� �����ϴ� �Լ�
	public void sendMessage(String msg) {

		// synchronizedMap() �Լ��� �����ؼ� ��� ����� ������ ����� HashMap �ڷᱸ�� ����ȭ
		Map<String, DataOutputStream> map = Collections.synchronizedMap(mClientMap);

		// keySet() : HashMap �ڷ� ������ ������ ��� Ű���� �о��
		Set<String> set = map.keySet();

		// map ������ �ϳ��� �����忡���� ����� �� �ֵ��� ����ȭ �� �����
		synchronized (map) {

			// �� ���� ��ɹ����� �ϳ��� �����常 ������!!

			// �ݺ��� : �ڵ����� �޸𸮿� ����� ����� �������� �������� ����
			Iterator<String> iterator = set.iterator();

			// Ű ���� �ӽ� ����
			String key = "";

			// while() �ݺ����� �ݺ��� ������ ���� �ִ� hasNext() �Լ��� ����ؼ�
			// HashMap �ڷ� ������ �����Ǿ� �ִ� ��� ������� Ű�� ���� �о�ͼ� ó��
			// hasNext() : HashMap �ڷ� �������� �о�� ����� ������ �ִ� ��쿡�� true ��ȯ
			while (iterator.hasNext() == true) {

				// �г��� ��������
				key = iterator.next();

				try {

					// ����� Ű�� �´� ��� ��Ʈ�� ���� ��������
					DataOutputStream dos = mClientMap.get(key);

					// ȭ�鿡 �޼��� ���
					dos.writeUTF(msg);

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} // end of while(iterator.hasNext() == true)

		} // end of synchronized (map)

	} // end of public void sendMessage(String msg)

} // end of MyChattingServerBackClass
