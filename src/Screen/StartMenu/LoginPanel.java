package Screen.StartMenu;

import Screen.Game.ConditionPanel;
import Screen.Game.GamePanel;
import Screen.MainFrame;
import Setting.Setting;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private Image backgroundImage = new ImageIcon(getClass().getResource("/images/로그인.png")).getImage();

    private JComboBox<String> difficultyBox;
    private String[] difficultyList = {"Easy","Normal","Hard"};

    private JTextField inputId = new JTextField(10); //id 입력

    private JLabel msg = new JLabel("공연이 곧 시작합니다...");

    public LoginPanel(MainFrame mainFrame, Setting setting,GamePanel gamePanel){
        setLayout(null);


        //난이도 설정
        difficultyBox = new JComboBox<>(difficultyList);
        difficultyBox.setBounds(440,490,120,30);
        difficultyBox.setBackground(Color.LIGHT_GRAY);
        difficultyBox.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(difficultyBox);

        //아이디 입력창
        inputId.setBounds(450,530,100,20);
        add(inputId);

        //안내 문구
        msg.setBounds(380,580,1000,50);
        msg.setFont(new Font("SansSerif", Font.BOLD, 25));
        add(msg);

        //메인화면 버튼
        JButton backBtn = setting.makeBtn("메인화면","StartMenuPanel");
        backBtn.setBounds(390,660,100,40);
        add(backBtn);

        //시작 버튼(누르면 게임 스레드 실행)
        JButton startBtn = setting.makeBtn("시작","GamePanel");
        startBtn.setBounds(510,660,100,40);
        startBtn.addActionListener(new ActionListener() {//버튼 누르면 게임스레드 실행
            @Override
            public void actionPerformed(ActionEvent e) {
                //이전 게임패널 초기화
                gamePanel.reset();//설정 값 초기화
                gamePanel.conditionPanel.resetWarningBar();//이전 warningBar삭제
                gamePanel.comboPanel.resetComboStackLabel();
                gamePanel.comboPanel.resetComboBar();//이전 comboBar삭제

                String difficulty = (String) difficultyBox.getSelectedItem();//사용자가 선택한 난이도
                gamePanel.setDifficulty(difficulty);//난이도 설정하고
                gamePanel.comboPanel.initComboStackLabel();
                gamePanel.comboPanel.initComboBar();//난이도에 따라 콤보바 생성
                gamePanel.conditionPanel.initWarningBar();
                gamePanel.groundPanel.startGame();//게임 스레드 실행

                
            }
        });
        add(startBtn);
    }

    //배경이미지
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

}
