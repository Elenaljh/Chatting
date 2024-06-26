package Server;

import java.awt.Color;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/*
클라이언트의 상태를 시각적으로 확인하기 위해 구현됨.
클라이언트들의 대화 내용과 참여 중인 유저 목록을 보여준다.
 */
public class ServerGui {
    private JFrame serverFrame;
    private TextArea chatTextArea;
    protected TextField chatTextField;
    private JList list;
    private DefaultListModel model;

    public ServerGui() {
        serverFrame = new JFrame();
        serverFrame.setDefaultCloseOperation(serverFrame.EXIT_ON_CLOSE);
        serverFrame.setBounds(100, 100, 825, 475);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        serverFrame.setContentPane(contentPane);
        contentPane.setLayout(null);

        chatTextArea = new TextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setBounds(18, 12, 567, 384);
        chatTextArea.setBackground(Color.WHITE);
        contentPane.add(chatTextArea);

        chatTextField = new TextField();
        chatTextField.setColumns(30);
        chatTextField.setBounds(17, 403, 572, 22);
        contentPane.add(chatTextField);

        Label userListLabel = new Label("CHAT USER ");
        userListLabel.setBounds(667, 16, 100, 16);
        contentPane.add(userListLabel);

        model = new DefaultListModel();

        list = new JList(model);
        list.setBounds(602, 41, 199, 373);
        list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        contentPane.add(list);

        serverFrame.setResizable(false);
        serverFrame.setTitle("채팅프로그램 서버");
        serverFrame.setVisible(true);
    }

    public void setFrameVisible() {
        serverFrame.setVisible(true);
    }

    public void setTextFieldBlank() {
        chatTextField.setText(null);
    }

    public void appendMessage(String message) { //텍스트 값을 받아 GUI에 출력
        chatTextArea.append(message + "\n");
    }

    public void appendUserList(String user) { //유저 아이디를 받아 유저 리스트에 추가
        model.addElement(user);
    }

    public void removeUserList(String user) {
        model.removeElement(user);
    }

    public String getChatMessage() {
        return chatTextField.getText();
    }
}
