package com.test.haley01;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;

// Collections, HashMap, Iterator, Map,Set 클래스/인터페이스 사용

public class MyChattingServerBackClass {

	// 서버 소켓
	private ServerSocket mServerSocket;

	// 받아올 소켓 저장
	private Socket mClientSocket;

	// 서버 화면 객체
	private MyServerClass mServerGUI;

	// 메세지 보관 변수
	private String mMsg;

	// 사용자들의 정보를 저장하는 맵
	// String : 키(key)의 자료형 -> 닉네임의 타입 -> 동일한 닉네임은 저장 x

	private Map<String, DataOutputStream> mClientMap = new HashMap<String, DataOutputStream>();

	// 화면 객체 저장 함수
	public void setGui(MyServerClass gui) {
		this.mServerGUI = gui;
	}

	// 서버가 생성될 떄 셋팅해주는 함수
	// 1. 메모리에 서버 소켓 객체 생성
	// 2. accept() 함수 실행 -> 클라이언트 연결 기다림
	public void setting() {

		try {

			// clientMap을 네트워크 처리 (동기화)
			Collections.synchronizedMap(mClientMap);

			mServerSocket = new ServerSocket(7777);

			while (true) {

				// 방문자를 계속 받아서, 쓰레드 리시버를 계속 생성
				System.out.println("클라이언트들을 기다리는 중...");

				mClientSocket = mServerSocket.accept();

				System.out.println(mClientSocket.getInetAddress() + "에서 접속했습니다.");

				// 여기서 새로운 사용자 스레드 클래스를 생성해서 소켓 정보 넣기
				// 클라이언트 소켓 정보를 보관하는 스레드 객체 생성
				// Receiver : 스레드 클래스
				Receiver receiver = new Receiver(mClientSocket);

				receiver.start();

				// mClientSocket : 서버와 연결된 클라이언트 정보 보관 변수 , 데이터 입출력시 사용할 변수

			}

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} // end of setting()

	// 맵의 내용 저장과 삭제

	// 리시버 클래스(스레드 클래스)
	class Receiver extends Thread {

		private DataInputStream in;

		private DataOutputStream out;

		// 닉네임 보관 변수
		private String nick;

		// 클라이언트 소켓 객체를 받는 생성자 함수

		public Receiver(Socket socket) {

			// IOException 대비
			try {

				// 소켓ㅇ르로 부터 데이터 출력에 사용할 객체 주소 가져오기
				out = new DataOutputStream(socket.getOutputStream());

				// 소켓으로부터 데이터 입력에 사용할 객체 주소 가져오기
				in = new DataInputStream(socket.getInputStream());

				// readUTF() : 문자열 값을 읽어오는 함수 -> 사용자가 입력한 닉네임을 읽어와서 저장
				nick = in.readUTF();

				// hashMap 자료 구조에 새로운 클라이언트 정보 저장
				addClient(nick, out);

			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} // end of Receiver 생성자

		// 모든 스레드들이 함께 실행하는 fun 함수
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

	} // end of Receiver 클래스

	// 현재 서버에 접속 중인 모든 클라이언트 들에게 새로운 클라이언트 접속을 알리는
	// 메세지를 출력하고 HashMap 자료 구조에 클라이언트를 저장하는 함수
	public void addClient(String nick, DataOutputStream out) throws IOException {

		// 화면에 출력할 메세지 변수 보관
		String message = nick + "님이 입장하셨습니다.\n";

		// 서버에 접속 중인 모든 클라이언트에게 메세지 전송
		sendMessage(message);

		// 서버 화면에도 메세지 추가
		mServerGUI.appendMsg(message);

		// HashMap 자료 구조에 새로운 클라이언트 저장 (put() 함수)
		// nick : 키(key)
		// out : 값(Value)
		mClientMap.put(nick, out);

	} // end of addClient()

	// 사용자 퇴장 함수 처리
	public void removeClient(String nick) {

		String message = nick + "님이 퇴장하셨습니다.\n";

		// 서버에 접속 중인 모든 사용자에게 메세지 전송
		sendMessage(message);

		// 서버 화면에도 메세지 출력
		mServerGUI.appendMsg(message);

		// remove() 퇴장한 사용자 정보를 HashMap에서 없애기
		mClientMap.remove(nick);
	} // end of removeClient()

	// 메세지 내용을 현재 서버에 접속한 모든 클라이언트들에세 전송하는 함수
	public void sendMessage(String msg) {

		// synchronizedMap() 함수를 실행해서 모든 사용자 정보가 저장된 HashMap 자료구조 동기화
		Map<String, DataOutputStream> map = Collections.synchronizedMap(mClientMap);

		// keySet() : HashMap 자료 구조에 보관된 모든 키들을 읽어옴
		Set<String> set = map.keySet();

		// map 변수를 하나의 스레드에서만 사용할 수 있도록 동기화 블럭 만들기
		synchronized (map) {

			// 이 안의 명령문들은 하나의 스레드만 실행함!!

			// 반복자 : 자동으로 메모리에 저장된 사용자 정보들을 가져오는 변수
			Iterator<String> iterator = set.iterator();

			// 키 저장 임시 변수
			String key = "";

			// while() 반복문과 반복자 변수가 갖고 있는 hasNext() 함수를 사용해서
			// HashMap 자료 구조에 보관되어 있는 모든 사용자의 키와 값을 읽어와서 처리
			// hasNext() : HashMap 자료 구조에서 읽어올 사용자 정보가 있는 경우에만 true 반환
			while (iterator.hasNext() == true) {

				// 닉네임 가져오기
				key = iterator.next();

				try {

					// 사용자 키에 맞는 출력 스트림 변수 가져오기
					DataOutputStream dos = mClientMap.get(key);

					// 화면에 메세지 출력
					dos.writeUTF(msg);

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} // end of while(iterator.hasNext() == true)

		} // end of synchronized (map)

	} // end of public void sendMessage(String msg)

} // end of MyChattingServerBackClass
