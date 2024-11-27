package Screen.StartMenu;

import Screen.Game.GamePanel;
import Screen.MainFrame;
import Setting.Setting;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private Image backgroundImage = new ImageIcon(getClass().getResource("/images/로그인.png")).getImage();
    private JTextField inputId = new JTextField(10);
    private JLabel msg = new JLabel("공연이 곧 시작합니다...");
    private String[] difficultyList = {"Easy","Normal","Hard"};

    public LoginPanel(MainFrame mainFrame, Setting setting,GamePanel gamePanel){
        setLayout(null);

        JComboBox<String> difficultyBox = new JComboBox<>(difficultyList);
        difficultyBox.setBounds(450,480,100,20);
        add(difficultyBox);

        inputId.setBounds(450,530,100,20);
        add(inputId);

        msg.setBounds(380,580,1000,50);
        msg.setFont(new Font("SansSerif", Font.BOLD, 25));
        add(msg);

        JButton backBtn = setting.makeBtn("메인화면","StartMenuPanel");
        backBtn.setBounds(390,660,100,40);
        add(backBtn);

        //이벤트 리스너 추가 -> 게임 시작(?)
        JButton startBtn = setting.makeBtn("시작","GamePanel");
        startBtn.setBounds(510,660,100,40);
        startBtn.addActionListener(new ActionListener() {//버튼 누르면 게임스레드 실행
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.groundPanel.startGame();
            }
        });
        add(startBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

}
