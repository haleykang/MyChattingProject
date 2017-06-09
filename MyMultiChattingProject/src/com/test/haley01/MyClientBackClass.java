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

	// 클라이언트 창의 시작 주소를 설정해주는 함수
	public void setGui(MyClientServerClass gui) {
		this.mClientGUI = gui;
	}

	// 서버와 연결 시도하는 함수
	public void connect() {

		try {
			// 서버와 연결 시도
			mSocket = new Socket("127.0.0.1", 7777);

			// 서버와 연결 되면 자동으로 실행되는 명령문
			System.out.println("서버에 연결됨");

			// 소켓 연결 됐을 경우 참을 반환
			boolean connectedServer = mSocket.isConnected();

			System.out.println("서버 연결 확인 : " + connectedServer);

			mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
			mDataInputStream = new DataInputStream(mSocket.getInputStream());

			// 서버로 닉네임 전송
			mDataOutputStream.writeUTF(mNickName);

			System.out.println("클라이언트 : 닉네임 전송 완료");

			while (mDataInputStream != null) {

				// available() : 입력 스트림에 서버로부터 받은 메세지가 있으면 0보다 큰 정수 반환
				if (mDataInputStream.available() > 0) {

					mMsg = mDataInputStream.readUTF();

					// ta에 메세지 출력
					mClientGUI.appendMsg(mMsg);

				}

			} // end of while()
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} // end of connect();

	// 서버로 메세지 전송

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
