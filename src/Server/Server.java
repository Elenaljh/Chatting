package Server;

import Model.User;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ServerGui serverGui;
    ArrayList<ChatThread> chatlist = new ArrayList<ChatThread>();

    //메인 실행 함수
    public static void main(String[] args) {
        new Server();
    }

    //Server 생성자
    public Server() {
        this.start();
    }

    //start 함수
    public void start() {
        try {
            //GUI 켜기
            serverGui = new ServerGui();

            //GUI에 이벤트 추가
            serverGui.chatTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String s = serverGui.getChatMessage().trim();
                        if (!s.isEmpty()) {
                            serverGui.appendMessage("SERVER: " + s);
                            sendToAll("server : " + s);
                        }
                        serverGui.setTextFieldBlank();
                    }
                }
            });

            //서버 소켓 생성
            serverSocket = new ServerSocket(8888);
            serverGui.appendMessage("서버가 시작되었습니다");

            /*
            ArrayList는 비동기화된 컬렉션 클래스이기 때문에 충돌 가능성이 높으나 성능면에서 우수함
            메소드를 통해 추후에 동기화를 할 수 있어 일반적으로 스레드를 모아놓기 위해 많이 사용되는 컬렉션
             */
            //여러 클라이언트 연결 대기
            while (true) {
                clientSocket = serverSocket.accept();
                ChatThread chatthread = new ChatThread(); //스레드 생성
                chatlist.add(chatthread); //리스트에 스레드 추가
                serverGui.appendMessage("클라이언트가 연결됐습니다." + "(IP : " + clientSocket.getInetAddress().toString().substring(1) + ")");
                chatthread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void sendToAll(String s) {
        for (ChatThread thread : chatlist) {
            thread.writerToClient.println(s);
        }
    }


    class ChatThread extends Thread {
        String msg;
        String[] rmsg;
        private BufferedReader reader = null;
        private PrintWriter writerToClient = null;
        private User user;

        public void run() { //스레드 실행 코드는 run() 메소드를 오버라이딩하여 구현된다.
            boolean loginBool = true;
            user = new User(); //user 객체 생성 (스레드 접속 클라이언트)
            user.setThreadName(this.getName()); //user 스레드 이름 입력

            try {
                /**클라이언트 채팅 수신처리**/
                //클라이언트와 입출력 스트림 생성
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writerToClient = new PrintWriter(clientSocket.getOutputStream(), true);
                //클라이언트에 연결 사실 전송
                writerToClient.println("서버에 연결 됐습니다(serverIP : " + serverSocket.getInetAddress().toString().substring(1) + ")");

                //클라이언트로부터 메시지를 반복해서 읽어옴
                while (loginBool) { //클라이언트 로그인, 로그아웃 처리
                    msg = reader.readLine();
                    rmsg = msg.split("/"); //구분자를 기준으로 나누기(login/id)

                    if (new BlacklistedWord().containBlacklistedWord(msg)) {  //욕설 처리
                        sendToAll("server : 부적절한 단어입니다. 바른말을 써주세요");
                        continue;
                    }

                    serverGui.appendMessage(user.getId() + " : " + msg);

                    if (rmsg[0].equals("login")) { //로그인 처리
                        user.setId(rmsg[1]);
                        sendToAll("server : " + user.getId() + "님이 입장했습니다.");
                        serverGui.appendUserList(user.getId());
                    } else if (msg.equals(".quit")) { //로그아웃 처리
                        sendToAll("server : " + user.getId() + "님이 나갔습니다.");
                        serverGui.appendMessage(user.getId() + "님이 나갔습니다.");
                        loginBool = false; //클라이언트가 .quit을 치면 나감
                    } else {
                        sendToAll(user.getId() + " : " + msg); //모든 클라이언트에게 클라이언트의 메세지 전송
                    }
                }

                //클라이언트 챗스레드 종료
                chatlist.remove(this);
                serverGui.removeUserList(user.getId());
                serverGui.appendMessage(this.getName() + " stopped!");
                this.interrupt();

            } catch (IOException e) {
                //클라이언트와 연결이 끊어짐
                serverGui.appendMessage("클라이언트 " + user.getId() + "의 연결이 끊어졌습니다");
                serverGui.appendMessage(this.getName() + " stopped!");
                serverGui.removeUserList(user.getId());
                chatlist.remove(this);
            }
        }
    } //chatThread 클래스
} //클래스
