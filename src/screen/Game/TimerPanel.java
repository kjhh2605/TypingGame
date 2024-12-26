package screen.Game;

import service.MyTimer;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class TimerPanel extends JPanel {
    private final JLabel timerLabel = new JLabel("00 : 00 : 00");
    private final MyTimer t;
    private final GameGroundPanel groundPanel;
    private TimerThread timerThread;
    private boolean isPaused = false;
    private Instant pauseTime;
    private Duration pausedDuration = Duration.ZERO;

    public TimerPanel(GameGroundPanel groundPanel) {
        this.groundPanel = groundPanel; //시작 시간 받아오기 위함
        t = new MyTimer();
        setLayout(new BorderLayout());
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 40));//폰트 설정
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);//수평 가운데 정렬
        add(timerLabel, BorderLayout.CENTER);
    }

    public String getTimerLabelText() {
        return timerLabel.getText();
    }

    public void startTimer() {
        if (timerThread == null || !timerThread.isAlive()) {
            timerThread = new TimerThread();
            timerThread.start();
        }
    }

    public void stopTimer() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();//스레드 종료
            pausedDuration = Duration.ZERO;//정지 시간 초기화
        }
    }

    public void pauseTimer() {
        isPaused = true;
        pauseTime = Instant.now();
    }

    public void continueTimer() {
        if (isPaused) {
            isPaused = false;
            //정지 시간
            pausedDuration = pausedDuration.plus(Duration.between(pauseTime, Instant.now()));
        }
    }

    class TimerThread extends Thread {
        @Override
        public void run() {
            Instant startTime = groundPanel.getStartTime();
            while (true) {
                if (!isPaused) {
                    timerLabel.setText(t.setTimerText(startTime, pausedDuration));
                    repaint();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

}
