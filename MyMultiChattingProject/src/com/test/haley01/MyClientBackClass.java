package com.test.haley01;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyClientBackClass {

	private Socket mSocket;
	private DataInputStream mDataInputStream;
	private DataOutputStream mDataOutputStream;
	private MyClientServerClass mClientGUI;
	private String mMsg;
	private String mNickName;

	// private MyChattingServerBackClass myChattingServerBackClass;

	// Ŭ���̾�Ʈ â�� ���� �ּҸ� �������ִ� �Լ�
	public void setGui(MyClientServerClass gui) {
		this.mClientGUI = gui;
	}

	// ������ ���� �õ��ϴ� �Լ�
	public void connect() {

		try {
			// ������ ���� �õ�
			mSocket = new Socket("127.0.0.1", 7777);

			// ������ ���� �Ǹ� �ڵ����� ����Ǵ� ��ɹ�
			System.out.println("������ �����");

			// ���� ���� ���� ��� ���� ��ȯ
			boolean connectedServer = mSocket.isConnected();

			System.out.println("���� ���� Ȯ�� : " + connectedServer);

			mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
			mDataInputStream = new DataInputStream(mSocket.getInputStream());

			// ������ �г��� ����
			mDataOutputStream.writeUTF(mNickName);

			System.out.println("Ŭ���̾�Ʈ : �г��� ���� �Ϸ�");

			while (mDataInputStream != null) {

				// available() : �Է� ��Ʈ���� �����κ��� ���� �޼����� ������ 0���� ū ���� ��ȯ
				if (mDataInputStream.available() > 0) {

					mMsg = mDataInputStream.readUTF();

					// ta�� �޼��� ���
					mClientGUI.appendMsg(mMsg);

				}

			} // end of while()
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} // end of connect();

	// ������ �޼��� ����

	public void sendMessage(String msg) {
		try {

			mDataOutputStream.writeUTF(msg);

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} // end of sendMessage()

	public void setNickname(String nickName) {
		this.mNickName = nickName;
	}

}
