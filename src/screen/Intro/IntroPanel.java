package screen.Intro;

import screen.MainFrame;

import javax.swing.*;
import java.awt.*;

public class IntroPanel extends JPanel {
    private final ImageIcon kanyeImg = new ImageIcon(getClass().getResource("/images/intro/yzy.png"));
    private final JLabel kanye;
    private final Image backgroundImage = new ImageIcon(getClass().getResource("/images/intro/인트로배경.png")).getImage();

    public IntroPanel(MainFrame mainFrame) {
        setBackground(Color.lightGray);
        setLayout(null);

        JLabel introText = new JLabel("칸예가 드디어 한국에 도착했습니다..");
        introText.setBounds(5, -30, 600, 100); // x, y, width, height
        introText.setForeground(Color.BLACK); // 글자 색 변경 (배경에 맞게)
        introText.setFont(new Font("맑은 고딕", Font.BOLD, 24)); // 글꼴, 스타일, 크기 설정
        add(introText);

        Image scaledImage = kanyeImg.getImage().getScaledInstance(200, 400, Image.SCALE_SMOOTH);
        kanye = new JLabel(new ImageIcon(scaledImage));
        kanye.setBounds(0, 300, 200, 400);
        add(kanye);

        new EnterThread().start();

        //5초후 자동으로 넘어감
        Timer timer = new Timer(5000, e -> {
            MainFrame.showPanel("StartMenuPanel");
        });
        timer.setRepeats(false); // 한 번만 실행되도록 설정
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    class EnterThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    kanye.setLocation(kanye.getX() + 50, kanye.getY());
                    sleep(250);
                } catch (InterruptedException e) {
                    return;//스레드 종료
                }
            }
        }
    }
}
