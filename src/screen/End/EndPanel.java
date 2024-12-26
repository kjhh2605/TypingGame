package screen.End;

import screen.Game.GamePanel;
import screen.MainFrame;
import service.GameEffects;
import setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel {
    private final Image successBackground = new ImageIcon(getClass().getResource("/images/성공.png")).getImage();//성공 시 배경화면
    private final Image failedBackground = new ImageIcon(getClass().getResource("/images/실패.png")).getImage();//실패 시 배경화면

    private String text;
    private final String successText = "칸예웨스트가 마이크를 잡고 등장했습니다!!";//성공 시 텍스트
    private final String failedText = "칸예웨스트가 집으로 떠납니다...";//실패 시 텍스트

    private final boolean isSuccess;//true면 성공화면 false면 실패화면

    public EndPanel(MainFrame mainFrame, GamePanel gamePanel, Setting setting, boolean isSuccess) {
        setLayout(null);
        this.isSuccess = isSuccess;

        if (isSuccess)
            text = successText;
        else if (!isSuccess)
            text = failedText;

        JLabel msg = new JLabel(text);
        msg.setBounds(0, 0, 800, 50);
        msg.setFont(new Font("SansSerif", Font.BOLD, 30));
        add(msg);

        JButton startMenuBtn = setting.makeBtn("처음으로", "StartMenuPanel");
        startMenuBtn.setBounds(250, 320, 500, 70);
        startMenuBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameEffects.stopBgm();
            }
        });
        add(startMenuBtn);

        JButton replayBtn = setting.makeBtn("다시하기", "GamePanel");
        replayBtn.setBounds(250, 420, 500, 70);
        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameEffects.stopBgm();//종료화면 bgm 종료
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
        if (isSuccess)
            g.drawImage(successBackground, 0, 0, getWidth(), getHeight(), this);
        else
            g.drawImage(failedBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
