package Screen.Game;

import Service.MyTimer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class TimerPanel extends JPanel{
    private final JLabel timerLabel = new JLabel("00 : 00 : 00");
    private MyTimer t;
    private GameGroundPanel groundPanel;
    private TimerThread timerThread;

    public TimerPanel(GameGroundPanel groundPanel){
        this.groundPanel = groundPanel; //시작 시간 받아오기 위함
        t = new MyTimer();
        setLayout(new BorderLayout());
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 40));//폰트 설정
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);//수평 가운데 정렬
        add(timerLabel,BorderLayout.CENTER);
    }

    public void resetTimerLabel() {
        timerLabel.setText("00 : 00 : 00");
        repaint();
    }

    public void startTimer() {
        if (timerThread == null || !timerThread.isAlive()) {
            timerThread = new TimerThread();
            timerThread.start();
        }
    }

    public void stopTimer() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }

    class TimerThread extends Thread{
        @Override
        public void run() {
            Instant startTime = groundPanel.getStartTime();
            while(true){
                timerLabel.setText(t.setTimerText(startTime));
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

}
