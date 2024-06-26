package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EnterGui extends JFrame {
    private JPanel enterPane; //전체 틀
    private JTextField textField; //닉네임 읽어오는 텍스트 필드
    private JButton enterButton; //입력 버튼
    private String name; //닉네임

    //전체 창 구성
    public EnterGui() {
        enterPanel(); //전체틀 생성
        nickNameLabel(); //닉네임 입력 라벨, 텍스트 필드 생성
        enterButtonEvent(); //입장 버튼 생성 및 처리
        setVisible(true); //GUI 켜기
    }

    /*
    JFrame을 설정하고, JPanel 객체인 enterPane을 초기화하고 배경색 설정한다. enterPane을 프레임의 content pane으로 설정하고
    레이아웃을 null로 설정한다. 이렇게 하면 컴포넌트의 위치와 크기를 개발자가 직접 설정 가능
     */
    public void enterPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창 종료시 프로세스 종료
        setBounds(100, 100, 450, 300); //패널 사이즈 코드
        enterPane = new JPanel();
        enterPane.setBackground(new Color(237, 207, 255));
        setContentPane(enterPane);
        enterPane.setLayout(null);
    }

    /*
    JLabel과 JTextField를 사용하여 닉네임 입력. 라벨과 텍스트 필드의 위치는 setBounds 메서드 사용하여 직접 설정
     */
    public void nickNameLabel() {
        JLabel nickName = new JLabel("닉네임 입력");
        nickName.setBounds(180, 60, 80, 15);
        enterPane.add(nickName);

        textField = new JTextField();
        textField.setBounds(155, 75, 115, 20);
        enterPane.add(textField);
        textField.setColumns(10); //글자수 제한
    }

    /*
    입장 버튼을 생성하고, 버튼 클릭 이벤트와 엔터키 입력 이벤트 처리. 이벤트 발생시 sendToChatGui 메서드가 호출되며, 이 메서드는 닉네임을
    읽어와 ChatGui를 실행하고 EnterGui 종료
     */
    public void enterButtonEvent() {
        enterButton = new JButton("입장");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendToChatGui();
            }
        });
        enterButton.setBounds(165, 134, 97, 23);
        enterPane.add(enterButton);

        //엔터 입력시 입장 처리
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //엔터키 입력시 이벤트 처리
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendToChatGui();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    /*
    사용자의 닉네임을 받아 채팅 GUI 시작. 사용자가 닉네임을 입력하고 입장 버튼을 누르거나 엔터키를 누르면 이 함수는
    닉네임을 받아 새로운 채팅 GUI를 생성하고, 입력창을 숨긴다.
     */
    public void sendToChatGui() {
        name = textField.getText(); //닉네임 name 읽어오기
        setVisible(false); //EnterGui 창 끄기
        new ChatGui(name); //ChatGui에 닉네임 name값 전달
    }



}
