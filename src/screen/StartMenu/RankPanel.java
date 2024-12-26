package screen.StartMenu;

import screen.MainFrame;
import service.Rank;
import setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RankPanel extends JPanel {
    private final Image rankBackground = new ImageIcon(getClass().getResource("/images/랭킹배경.png")).getImage();
    private final Vector<JLabel> rankLabelList = new Vector<>();
    private final Rank rankService = new Rank(); // RankService 인스턴스 생성
    private Vector<String> setRankResult;

    //RankLabel클래스 -> 등수 / 이름 / 난이도 / 시간
    public RankPanel(MainFrame mainFrame, Setting setting) {
        setLayout(null);
        JButton backBtn = setting.makeBtn("메인화면", "StartMenuPanel");
        backBtn.setBounds(450, 660, 100, 40);
        add(backBtn);
    }

    void updateRankPanel() {
        setRankResult = rankService.setRankResult("src/Service/rank.txt");
        rankService.reWriteRankFile("src/Service/rank.txt");
        for (Component component : this.getComponents()) {
            if (component instanceof JLabel) {
                this.remove(component); // JLabel 제거
            }
        }
        drawRankLabel();
        rankLabelList.clear();
    }

    void drawRankLabel() {
        for (int i = 0; i < setRankResult.size(); i++) {
            String name = setRankResult.get(i).split(",")[0];
            String time = setRankResult.get(i).split(",")[1];
            String difficulty = setRankResult.get(i).split(",")[2];
            String text = "";
            switch (difficulty) {
                case ("HARD"):
                    text = String.format(
                            "<html>%s등 %s " +
                                    "<span style='color:#585858;'>%s</span> " +
                                    "<span style='color:red;'>%s</span></html>",
                            i + 1, name, time, difficulty);
                    break;
                case ("NORMAL"):
                    text = String.format(
                            "<html>%s등 %s " +
                                    "<span style='color:#585858;'>%s</span> " +
                                    "<span style='color:blue;'>%s</span></html>",
                            i + 1, name, time, difficulty);
                    break;
                case ("EASY"):
                    text = String.format(
                            "<html>%s등 %s " +
                                    "<span style='color:#585858;'>%s</span> " +
                                    "<span style='color:green;'>%s</span></html>",
                            i + 1, name, time, difficulty);
                    break;
            }

            rankLabelList.add(new JLabel(text));
        }

        int rightFlag = 0;
        int x = 200;
        int y = 300;
        for (JLabel rankLabel : rankLabelList) {
            if (rightFlag == 5) {//6등 부터는 오른쪽에 출력되도록
                x += 300;
                y = 300;
            }
            this.add(rankLabel);
            rankLabel.setBounds(x, y, 400, 100);
            rankLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            y += 50;
            rightFlag++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rankBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
