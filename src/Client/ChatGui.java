package Client;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChatGui extends JFrame {
    private JPanel chatPanel;
    private JTextField textMsg;
    private TextArea chatLog;
    private BufferedReader reader;
    public PrintWriter writer;

    //ChatGui 구현
    public ChatGui(String id) {
        //EnterGui에서 보내는 닉네임 값을 매개변수로 받음
        String msg = "login/" + id;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 500);
        chatPanel = new JPanel();
        chatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(chatPanel);
        chatPanel.setBackground(new Color(237, 207, 255));
        chatPanel.setLayout(null);

        //chatLabel과 chatLog
        JLabel chatLabel = new JLabel("채팅방"); //chatLabel 생성
        chatLabel.setBounds(240, 10, 95, 15);
        chatPanel.add(chatLabel);

        chatLog = new TextArea(); //chatLog 생성
        chatLog.setEditable(false);
        chatLog.setText("채팅 로그입니다.");
        chatLog.setBounds(18, 25, 500, 400);
        chatPanel.add(chatLog);

        textMsg = new JTextField();
        textMsg.setText("메세지를 입력하세요");
        textMsg.setBounds(18, 430, 500, 20);
        chatPanel.add(textMsg);
        textMsg.setColumns(10);

        //메세지 전송 엔터키 이벤트 처리
        textMsg.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //엔터키 눌렀을 때 실행
                    String text = textMsg.getText();
                    writer.println(text); //텍스트를 서버로 전송
                    textMsg.setText(""); //텍스트 필드값 지움 (초기화)
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        setVisible(true);


        //소켓통신 : 서버로 메세지 전송
        try {
            //서버 정보
            String serverIP = "localhost";
            int serverPort = 8888; //서버 포트 번호

            //서버에 연결
            Socket socket = new Socket(serverIP, serverPort); //socket 객체를 이용해 서버의 IP주소와 포트를 사용하여 서버에 연결
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //BufferedReader 사용해 서버로부터 메세지 읽기

            //서버로부터 메세지 읽는 스레드 시작
            Thread readThread = new Thread(new ServerMessageReader()); //별도의 스레드에서 서버로부터 메시지를 비동기적으로 읽기
            readThread.start();

            //서버로 메세지 전송
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
            writer.println(msg); //outputstream과 printwriter를 사용하여 서버에 메세지 전송

        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //서버로부터 메세지 받기
    private class ServerMessageReader implements Runnable { //Runnable 인터페이스 구현하여 별도의 스레드에서 실행될 수 있도록 함

        @Override
        public void run() { //run 메서드에서는 BufferedReader를 사용해 서버로부터 메세지 읽어옴
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("서버로부터 메세지 : " + message); //읽어온 메세지는 콘솔에 출력된다.
                    uploadText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    //받은 메세지 채팅창에 업로드
    public void uploadText(String message) { //서버로부터 받은 메세지를 채팅창에 업데이트한다.
        chatLog.append(message + "\n");
    }
}
