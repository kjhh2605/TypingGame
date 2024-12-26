package screen.Game;

import screen.MainFrame;
import service.GameEffects;
import service.MakeLabel;
import service.MyLabel;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


public class GameGroundPanel extends JPanel {
    private final Image gameBackground = new ImageIcon(getClass().getResource("/images/게임배경.png")).getImage();
    public Vector<MyLabel> labelsOnPanel = new Vector<MyLabel>();
    public ArrayList<FallingLabelThread> labelThreadList = new ArrayList<FallingLabelThread>();
    public StartGameThread startGameThread;
    public boolean isBlurred = false;
    private int width, height;
    private Instant start, end;
    private ConditionPanel conditionPanel;
    private InputPanel inputPanel;
    private MainFrame mainFrame;
    private TimerPanel timerPanel;
    private boolean isPaused = false;//일시정지 기능을 위한 플래그
    private String difficulty;

    public GameGroundPanel() {
        setLayout(null);
        this.conditionPanel = conditionPanel;
        this.inputPanel = inputPanel;
        this.mainFrame = mainFrame;
        new MakeLabel(this);
    }

    public void init(InputPanel inputPanel, ConditionPanel conditionPanel, MainFrame mainFrame, TimerPanel timerPanel) {
        this.inputPanel = inputPanel;
        this.conditionPanel = conditionPanel;
        this.mainFrame = mainFrame;
        this.timerPanel = timerPanel;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void deleteFallingLabelThread(JLabel label) {
        Iterator<FallingLabelThread> iterator = labelThreadList.iterator();
        while (iterator.hasNext()) {
            FallingLabelThread th = iterator.next();
            if (th.getText().equals(label.getText())) {
                th.interrupt(); // 스레드 종료
                iterator.remove(); // 리스트에서 제거
                break;
            }
        }
    }

    public void pauseGame() {
        isPaused = true;
        timerPanel.pauseTimer();
        if (isBlurred)
            GameEffects.pauseBlur();
    }

    public synchronized void continueGame() {
        isPaused = false;
        notifyAll(); // 모든 스레드 재시작
        timerPanel.continueTimer();
        GameEffects.continueBlur();
    }

    public void endGame() {//게임 종료 시 groundPanel에서 실행될 메소드
        startGameThread.interrupt();//게임스레드 종료
        for (FallingLabelThread thread : labelThreadList) {//fallingLabel 스레드 전체 종료
            thread.interrupt();
        }
        labelThreadList.clear();//레이블 스레드 리스트 초기화
        labelsOnPanel.clear();//화면 단어 관리 벡터 초기화
        this.removeAll();//패널의 레이블 전체 삭제
        this.repaint();


    }

    public void startGame() {
        startGameThread = new StartGameThread(this, inputPanel);
        startGameThread.start();
        start = Instant.now();//시작 시간
        timerPanel.startTimer();//타이머 스레드 시작
    }

    public Instant getStartTime() {
        return start;
    }

    @Override//배경
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), this);
        width = getWidth();
        height = getHeight();
    }

    class StartGameThread extends Thread {
        private final GameGroundPanel gameGroundPanel;
        private final InputPanel inputPanel;

        StartGameThread(GameGroundPanel gameGroundPanel, InputPanel inputPanel) {
            this.gameGroundPanel = gameGroundPanel;
            this.inputPanel = inputPanel;

        }

        @Override
        public void run() {
            while (true) {
                synchronized (GameGroundPanel.this) {
                    while (isPaused) {
                        try {
                            GameGroundPanel.this.wait(); // 일시 정지 상태
                        } catch (InterruptedException e) {
                            return; // 스레드 종료
                        }
                    }
                }
                JLabel wordLabel = MakeLabel.makeLabel(); // JLabel & MyLabel 생성
                wordLabel.setSize(130, 30);
                add(wordLabel); // 레이블을 패널에 추가
                FallingLabelThread th = new FallingLabelThread(gameGroundPanel, wordLabel, inputPanel, mainFrame); // 새 스레드 생성
                gameGroundPanel.labelThreadList.add(th);//스레드 리스트에 추가
                th.start();//레이블이 랜덤으로 움직이며 떨어짐

                try {
                    sleep(2000);//2초마다 생성
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    class FallingLabelThread extends Thread {
        private JLabel l = null;
        private final GameGroundPanel groundPanel;
        private final InputPanel inputPanel;
        private final MainFrame mainFrame;

        FallingLabelThread(GameGroundPanel groundPanel, JLabel l, InputPanel inputPanel, MainFrame mainFrame) {
            this.l = l;
            this.groundPanel = groundPanel;
            this.inputPanel = inputPanel;
            this.mainFrame = mainFrame;
        }

        public String getText() {
            return l.getText();
        }

        private int updateX(int x) {
            int t = (int) (Math.random() * 2);
            if (t == 0) {
                x += (int) (Math.random() * 5) * 6;
                if (x + l.getWidth() >= width - 5)//패널 벗어나면
                    x -= (int) (Math.random() * 5) * 6;//반대로 이동
            } else {
                x -= (int) (Math.random() * 5) * 6;
                if (x < 5)//패널 벗어나면
                    x += (int) (Math.random() * 5) * 6;//반대로 이동
            }
            return x;
        }

        private int updateY() {
            int moveDistance = 0;
            switch (difficulty) {
                case ("Hard"):
                    moveDistance = (int) (Math.random() * 5) * 10 + 5;
                    System.out.println("hard" + moveDistance);
                    break;
                case ("Normal"):
                    moveDistance = (int) (Math.random() * 3) * 10 + 5;
                    System.out.println("normal" + moveDistance);
                    break;
                case ("Easy"):
                    moveDistance = (int) (Math.random() * 2) * 10 + 5;
                    System.out.println("easy" + moveDistance);
                    break;

            }
            return moveDistance;
        }

        private void fail() {
            endGame();
            //타이머 종료
            timerPanel.stopTimer();
            //SwingUtilities.invokeLater(new Runnable)
            new javax.swing.Timer(2000, e -> {
                MainFrame.showPanel("FailedPanel");
                GameEffects.playSound("/audio/24.wav");//성공
                ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
            }).start();
        }

        private void labelOutOfRange() {
            int fullWarningCount = inputPanel.getFullWarningCount();
            int curWarningCount = inputPanel.getCurWarningCount();

            synchronized (labelsOnPanel) {//iterator가 벡터를 순회하는 동안 수정하지 못하도록 synchronized
                Iterator<MyLabel> iterator = labelsOnPanel.iterator();
                while (iterator.hasNext()) {
                    MyLabel ml = iterator.next();

                    if (ml.getLabel().getText().equals(l.getText())) {// 벗어난 레이블에 해당하는 MyLabel(같은 단어)을 찾고
                        iterator.remove();//삭제

                        if (!ml.getLabelType().equals("아디다스")) {//아디다스 레이블은 떨어져도 warning 쌓이지 x
                            curWarningCount++;
                            inputPanel.setCurWarningCount(curWarningCount);
                            GameEffects.increaseWarningBar(conditionPanel, curWarningCount, 1);//경고 스택 증
                        }

                        if (curWarningCount >= fullWarningCount - 1) {//WarningCount 다 차면 종료
                            fail();
                        }
                        break;//스레드 종료
                    }
                }
            }
        }

        @Override
        public void run() {
            int x = (int) (Math.random() * 600);
            int y = 0;
            int moveDistanceY = updateY();
            while (true) {
                synchronized (GameGroundPanel.this) {
                    while (isPaused) {
                        try {
                            GameGroundPanel.this.wait(); // 일시 정지 상태
                        } catch (InterruptedException e) {
                            return; // 스레드 종료
                        }
                    }
                }

                l.setLocation(x, y);
                x = updateX(x);
                if (y > height) {//화면 벗어나면
                    remove(l);//패널의 레이블 삭제
                    groundPanel.deleteFallingLabelThread(l);//스레드 삭제
                    labelOutOfRange();
                } else
                    y += moveDistanceY;

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
