package Screen.End;

import Screen.Game.GamePanel;
import Screen.MainFrame;
import Setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel {
    private Image successBackground = new ImageIcon(getClass().getResource("/images/성공.png")).getImage();//성공 시 배경화면
    private Image failedBackground = new ImageIcon(getClass().getResource("/images/실패.png")).getImage();//실패 시 배경화면

    private String text;
    private String successText = "칸예웨스트가 드디어 마이크를 잡았습니다!!";//성공 시 텍스트
    private String failedText = "칸예웨스트가 집으로 떠납니다...";//실패 시 텍스트

    private boolean isSuccess;//true면 성공화면 false면 실패화면

    public EndPanel(MainFrame mainFrame, GamePanel gamePanel, Setting setting, boolean isSuccess){
        setLayout(null);
        this.isSuccess=isSuccess;

        if(isSuccess)
            text = successText;
        else if(!isSuccess)
            text = failedText;

        JLabel msg = new JLabel(text);
        msg.setBounds(0,0,800,50);
        msg.setFont(new Font("SansSerif", Font.BOLD, 30));
        add(msg);

        JButton startMenuBtn = setting.makeBtn("처음으로", "StartMenuPanel");
        startMenuBtn.setBounds(250,320,500,70);
        add(startMenuBtn);

        JButton replayBtn = setting.makeBtn("다시하기", "GamePanel");
        replayBtn.setBounds(250,420,500,70);
        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //이전 게임패널 초기화
                gamePanel.reset();//설정 값 초기화
                gamePanel.conditionPanel.resetWarningBar();//이전 warningBar삭제
                gamePanel.comboPanel.resetComboStackLabel();
                gamePanel.comboPanel.resetComboBar();//이전 comboBar삭제
                gamePanel.comboPanel.initComboBar();//난이도에 따라 콤보바 생성
                gamePanel.comboPanel.initComboStackLabel();
                gamePanel.conditionPanel.initWarningBar();

                gamePanel.groundPanel.startGame();//게임 스레드 실행
            }
        });
        add(replayBtn);

    }

    @Override//배경
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isSuccess)
            g.drawImage(successBackground, 0, 0, getWidth(), getHeight(), this);
        else
            g.drawImage(failedBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
