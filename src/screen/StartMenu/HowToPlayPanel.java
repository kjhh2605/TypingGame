package screen.StartMenu;

import screen.MainFrame;
import setting.Setting;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    private final ImageIcon img1 = new ImageIcon(getClass().getResource("/images/howToPlay/1.png"));
    private final ImageIcon img2 = new ImageIcon(getClass().getResource("/images/howToPlay/2.png"));
    private final JLabel labelImg1;
    private final JLabel labelImg2;

    public HowToPlayPanel(MainFrame mainFrame, Setting setting) {
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        String text = "<html>세계적인 래퍼 칸예웨스트가 14년만에 한국에 왔습니다!" +
                "<br>리스닝 파티로 예정되어있는 본 행사에서 라이브를 할 수 도 있다는 소문이...?" +
                "<br><br><br><br>공연이 진행되는 동안 칸예의 기분이 상하지 않게 주의하세요!" +
                "<br><br><br><br>힌트 : " +
                "<br>칸예는 아이다스를 싫어합니다" +
                "<br>칸예는 맥도날드를 좋아합니다</html>";
        JLabel textLabel = new JLabel(text);
        textLabel.setBounds(20, -50, 1000, 600);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        add(textLabel);

        Image scaledImage1 = img1.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        labelImg1 = new JLabel(new ImageIcon(scaledImage1));
        labelImg1.setBounds(450, 350, 300, 300);
        add(labelImg1);

        Image scaledImage2 = img2.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        labelImg2 = new JLabel(new ImageIcon(scaledImage2));
        labelImg2.setBounds(700, 350, 300, 300);
        add(labelImg2);


        JButton backBtn = setting.makeBtn("메인화면", "StartMenuPanel");
        backBtn.setBounds(450, 660, 100, 40);
        add(backBtn);
    }
}
