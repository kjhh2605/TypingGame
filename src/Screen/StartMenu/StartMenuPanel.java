package Screen.StartMenu;

import Screen.MainFrame;
import Setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;


public class StartMenuPanel extends JPanel {
    private Image rankBackground = new ImageIcon(getClass().getResource("/images/시작화면.png")).getImage();

    private Vector<JButton>buttonSet = new Vector<JButton>();

    public StartMenuPanel(MainFrame mainFrame,Setting setting){
        setLayout(null);
        setBackground(Color.lightGray);
        // 버튼 추가
        JButton howToPlayBtn = setting.makeBtn("게임 설명", "HowToPlayPanel");
        buttonSet.add(howToPlayBtn);
        JButton loginBtn = setting.makeBtn("로그인", "LoginPanel");
        buttonSet.add(loginBtn);
        JButton rankBtn = setting.makeBtn("랭킹", "RankPanel");
        buttonSet.add(rankBtn);
        JButton registerBtn = setting.makeBtn("회원가입", "RegisterPanel");
        buttonSet.add(registerBtn);

        //버튼 출력 모양, 위치
        int y = 320;
        for (JButton btn : buttonSet) {
            btn.setBounds(250, y, 500, 70);
            y += 100;
            this.add(btn);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rankBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
