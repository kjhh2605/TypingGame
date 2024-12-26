package screen.Game;

import screen.MainFrame;
import screen.StartMenu.RankPanel;
import service.GameEffects;
import service.MyLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class InputPanel extends JPanel {
    private int fullComboNum;
    private int fullWarningCount;
    private int curCombo;
    private int curComboStack;
    private int curWarningCount;

    private final JTextField text = new JTextField(15);//최대 10글자
    private GamePanel gamePanel;
    private GameGroundPanel groundPanel;
    private ConditionPanel conditionPanel;
    private ComboPanel comboPanel;
    private TimerPanel timerPanel;
    private RankPanel rankPanel;

    private boolean isPaused = false;
    private JDialog pauseDialog = null;

    //단어를 입력 받고 그에 따른 효과를 적용하는 패널
    public InputPanel(MainFrame mainFrame) {
        this.setBackground(Color.LIGHT_GRAY);
        add(text);
        pauseBtn();

        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField tf = (JTextField) e.getSource();
                String t = tf.getText();//입력한 텍스트
                boolean isExisted = false;
                Component[] components = groundPanel.getComponents();//패널에 있는 컴포넌트
                for (Component comp : components) {
                    if (comp instanceof JLabel label) {//패널에 있는 JLabel에 대해
                        if (t.equals(label.getText())) {//JLabel의 텍스트가 입력한 텍스트와 같으면
                            isExisted = true;
                            tf.setText("");//텍스트 필드 지우기
                            groundPanel.remove(label);//레이블 제거
                            checkMyLabel(mainFrame, t);//제거한 레이블에 따른 효과 적용
                            groundPanel.deleteFallingLabelThread(label);//스레드 삭제
                            groundPanel.repaint();//화면 다시 그리기
                        } else {
                            tf.setText("");
                        }//enter 입력 시 항상 tf 비움
                    }
                }
                if (!isExisted) {//잘못 입력하면
                    if (curComboStack > 0) {
                        curComboStack--;
                        GameEffects.changeComboStack(comboPanel, curComboStack);
                    }
                    curWarningCount++;
                    GameEffects.increaseWarningBar(conditionPanel, curWarningCount, 1);
                    if (curWarningCount >= fullWarningCount - 1) {//게임 종료(실패)
                        fail(mainFrame);
                    }
                }
                if (t.length() == 0) {
                }
            }
        });
    }

    public void pauseBtn() { // 설정 메뉴바
        JButton pauseBtn = new JButton("Stop/Start");
        pauseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPaused) {
                    groundPanel.continueGame();
                    isPaused = false;

                    // 일시정지 알림창 닫기
                    if (pauseDialog != null) {
                        pauseDialog.dispose();
                        pauseDialog = null;
                    }
                } else {
                    groundPanel.pauseGame();
                    isPaused = true;

                    // 일시정지 알림창 표시
                    pauseDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(groundPanel), "알림", false);
                    JLabel pauseLabel = new JLabel("공연이 잠시 멈췄습니다..", SwingConstants.CENTER);
                    pauseLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                    pauseDialog.getContentPane().add(pauseLabel);
                    pauseDialog.setSize(300, 150);
                    pauseDialog.setLocationRelativeTo(groundPanel);
                    pauseDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // 창 닫기 방지
                    pauseDialog.setVisible(true);
                }
            }
        });
        this.add(pauseBtn);
    }

    public void init(GamePanel gamePanel, GameGroundPanel groundPanel, ConditionPanel conditionPanel, ComboPanel comboPanel, TimerPanel timerPanel) {
        this.gamePanel = gamePanel;
        this.groundPanel = groundPanel;
        this.conditionPanel = conditionPanel;
        this.comboPanel = comboPanel;
        this.timerPanel = timerPanel;
    }

    //입력한 단어를 확인 후 게임 효과 적용
    private void checkMyLabel(MainFrame mainFrame, String t) {
        synchronized (groundPanel.labelsOnPanel) {//iterator가 벡터를 순회하는 동안 수정하지 못하도록 synchronized
            Iterator<MyLabel> iterator = groundPanel.labelsOnPanel.iterator();
            while (iterator.hasNext()) {
                MyLabel ml = iterator.next();
                if (ml.getLabel().getText().equals(t)) { // 입력한 단어에 해당하는 MyLabel을 찾고
                    applyEffectsByType(ml.getLabelType(), conditionPanel, comboPanel, groundPanel);//효과 적용
                    iterator.remove();
                    break;
                }
            }
        }
        if (curCombo == fullComboNum - 1) {//게임 종료(성공)
            success(mainFrame);
        }
        if (curWarningCount >= fullWarningCount - 1) {//게임 종료(실패)
            fail(mainFrame);
        }
    }

    private void success(MainFrame mainFrame) {
        //게임 스레드 종료 %
        groundPanel.endGame();
        timerPanel.stopTimer();//타이머 스레드 종료
        addUserRank();//랭킹 목록에 추가
        new javax.swing.Timer(2000, e -> {
            MainFrame.showPanel("SuccessPanel");
            GameEffects.playSound("/audio/runaway.wav");//성공
            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
        }).start();
    }

    private void fail(MainFrame mainFrame) {
        groundPanel.endGame();
        timerPanel.stopTimer();
        new javax.swing.Timer(2000, e -> {
            MainFrame.showPanel("FailedPanel");
            GameEffects.playSound("/audio/24.wav");//성공
            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
        }).start();
    }

    private void addUserRank() {
        String name = gamePanel.getPlayerInformation().split(",")[0];
        String time = timerPanel.getTimerLabelText();
        String difficulty = gamePanel.getPlayerInformation().split(",")[1].toUpperCase();


        String line = String.format("%s,%s,%s", name, time, difficulty);

        try (FileOutputStream fout = new FileOutputStream("src/Service/rank.txt", true)) { // append 모드
            fout.write(line.getBytes());
            fout.write("\n".getBytes()); // 줄바꿈 추가
        } catch (IOException e) {
        }
    }

    private void applyEffectsByType(String labelType, ConditionPanel conditionPanel, ComboPanel comboPanel, GameGroundPanel ground) {
        switch (labelType) {
            case "아디다스"://입력 후 잠시동안 다른레이블 처리 안됨
                curWarningCount++;
                GameEffects.increaseWarningBar(conditionPanel, curWarningCount, 3); // 경고 스택 증가
                curWarningCount += 2;
                curComboStack = 0;// 콤보 초기화
                GameEffects.changeComboStack(comboPanel, curComboStack);
                GameEffects.blur(ground); // 전체 블러 처리
                GameEffects.changeFace(conditionPanel, 0); // 표정 변화
                GameEffects.changeConditionText(conditionPanel, 0); // 상태 메시지 변화
                break;
            case "맥도날드":
                curComboStack += 2;
                if (curComboStack >= 10) {
                    curCombo++;
                    GameEffects.addCombo(comboPanel, curCombo, fullComboNum);
                    curComboStack -= 10;
                    if (curComboStack == 11) {
                        GameEffects.changeComboStack(comboPanel, 1);
                    } else {
                        GameEffects.changeComboStack(comboPanel, curComboStack); // 콤보 += 2
                    }
                    GameEffects.changeFace(conditionPanel, 3);
                    GameEffects.changeConditionText(conditionPanel, 3);
                } else {
                    GameEffects.changeComboStack(comboPanel, curComboStack); // 콤보 += 2
                    GameEffects.changeFace(conditionPanel, 2); // 표정 변화
                    GameEffects.changeConditionText(conditionPanel, 2);
                }
                break;

            default:
                curComboStack++;
                if (curComboStack >= 10) {
                    curCombo++;
                    GameEffects.addCombo(comboPanel, curCombo, fullComboNum);
                    curComboStack -= 10; // 스택 초기화
                    GameEffects.changeFace(conditionPanel, 3);
                    GameEffects.changeConditionText(conditionPanel, 3);
                }
                GameEffects.changeComboStack(comboPanel, curComboStack); // 콤보 ++
        }
        System.out.println(curComboStack);
    }

    //값 설정을 위한 setter
    public void setFullComboNum(int n) {
        this.fullComboNum = n;
    }

    public void setCurCombo(int n) {
        this.curCombo = n;
    }

    public void setCurComboStack(int n) {
        this.curComboStack = n;
    }

    //값을 넘겨주기 위한 getter
    public int getCurWarningCount() {
        return curWarningCount;
    }

    public void setCurWarningCount(int n) {
        this.curWarningCount = n;
    }

    public int getFullWarningCount() {
        return fullWarningCount;
    }

    public void setFullWarningCount(int n) {
        this.fullWarningCount = n;
    }

}


