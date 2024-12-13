package Screen.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import Screen.MainFrame;
import Service.GameEffects;
import Service.MyLabel;

public class InputPanel extends JPanel {
    private int fullComboNum;
    private int fullWarningCount;
    private int curCombo;
    private int curComboStack;
    private int curWarningCount;

    private JTextField text = new JTextField(10);//최대 10글자

    private GameGroundPanel groundPanel;
    private ConditionPanel conditionPanel;
    private ComboPanel comboPanel;
    private TimerPanel timerPanel;

    //단어를 입력 받고 그에 따른 효과를 적용하는 패널
    public InputPanel(MainFrame mainFrame) {
        this.setBackground(Color.LIGHT_GRAY);
        add(text);

        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField tf = (JTextField) e.getSource();
                String t = tf.getText();//입력한 텍스트

                Component[] components = groundPanel.getComponents();//패널에 있는 컴포넌트
                for (Component comp : components) {
                    if (comp instanceof JLabel) {//패널에 있는 JLabel에 대해
                        JLabel label = (JLabel) comp;
                        if (t.equals(label.getText())) {//JLabel의 텍스트가 입력한 텍스트와 같으면
                            tf.setText("");//텍스트 필드 지우기
                            groundPanel.remove(label);//레이블 제거
                            checkMyLabel(mainFrame, groundPanel, t, conditionPanel, comboPanel);//제거한 레이블에 따른 효과 적용
                            groundPanel.deleteFallingLabelThread(label);//스레드 삭제
                            groundPanel.repaint();//화면 다시 그리기
                        } else tf.setText("");//enter 입력 시 항상 tf 비움
                    }
                }
                if (t.length() == 0)
                    return;
            }
        });
    }

    public void init(GameGroundPanel groundPanel, ConditionPanel conditionPanel, ComboPanel comboPanel,TimerPanel timerPanel) {
        this.groundPanel = groundPanel;
        this.conditionPanel = conditionPanel;
        this.comboPanel = comboPanel;
        this.timerPanel = timerPanel;
    }
    //입력한 단어를 확인 후 게임 효과 적용
    private void checkMyLabel(MainFrame mainFrame, GameGroundPanel groundPanel, String t, ConditionPanel conditionPanel, ComboPanel comboPanel) {
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
            //게임 스레드 종료 %
            groundPanel.endGame();
            timerPanel.stopTimer();//타이머 스레드 종료

            new javax.swing.Timer(2000, e -> {
                mainFrame.showPanel("SuccessPanel");
                GameEffects.playSound("/Audio/runaway.wav");//성공
                ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
            }).start();
        }
        if (curWarningCount >= fullWarningCount - 1) {//게임 종료(실패)
            groundPanel.endGame();
            timerPanel.stopTimer();
            new javax.swing.Timer(2000, e -> {
                mainFrame.showPanel("FailedPanel");
                GameEffects.playSound("/Audio/24.wav");//성공
                ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
            }).start();
        }
    }
    private void applyEffectsByType(String labelType, ConditionPanel conditionPanel, ComboPanel comboPanel, GameGroundPanel ground) {
        switch (labelType) {
            case "아디다스"://입력 후 잠시동안 다른레이블 처리 안됨
                curWarningCount++;
                GameEffects.increaseWarningBar(conditionPanel, curWarningCount, 1); // 경고 스택 증가
                //curWarningCount+=2;
                curComboStack=0;// 콤보 초기화
                GameEffects.changeComboStack(comboPanel, curComboStack);
                GameEffects.blur(ground); // 전체 블러 처리
                GameEffects.changeFace(conditionPanel, 0); // 표정 변화
                GameEffects.changeConditionText(conditionPanel, 0); // 상태 메시지 변화
                break;
            case "맥도날드":
                curComboStack += 2;
                if (curComboStack >= 10) {
                    curCombo++;
                    GameEffects.addCombo(comboPanel, curCombo);
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
                    GameEffects.addCombo(comboPanel, curCombo);
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
    public void setFullWarningCount(int n) {
        this.fullWarningCount = n;
    }
    public void setCurCombo(int n) {
        this.curCombo = n;
    }
    public void setCurComboStack(int n) {
        this.curComboStack = n;
    }
    public void setCurWarningCount(int n) {
        this.curWarningCount = n;
    }
    //값을 넘겨주기 위한 getter
    public int getCurWarningCount() {
        return curWarningCount;
    }
    public int getFullWarningCount() {
        return fullWarningCount;
    }

}


