package Screen.StartMenu;

import Screen.MainFrame;
import Setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RankPanel extends JPanel {
    private Image rankBackground = new ImageIcon(getClass().getResource("/images/랭킹배경.png")).getImage();
    private Vector<JLabel>RankedList = new Vector<JLabel>(5);
    //RankLabel클래스 -> 등수 / 이름 / 난이도 / 시간
    public RankPanel(MainFrame mainFrame, Setting setting){
        setLayout(null);
        setRank();

        int y = 300;
        for (JLabel rankLabel : RankedList) {
            this.add(rankLabel);
            rankLabel.setBounds(300,y,400,100);
            rankLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            y+=50;
        }

        JButton backBtn = setting.makeBtn("메인화면","StartMenuPanel");
        backBtn.setBounds(450,660,100,40);
        add(backBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rankBackground, 0, 0, getWidth(), getHeight(), this);
    }
    void setRank(){
        RankedList.add(new JLabel("1등"));
        RankedList.add(new JLabel("2등"));
        RankedList.add(new JLabel("3등"));
        RankedList.add(new JLabel("4등"));
        RankedList.add(new JLabel("5등"));
    }
}
