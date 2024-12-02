package Screen.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


import Screen.MainFrame;
import Service.GameEffects;
import Service.MyLabel;

//copyOnWriteArrayList

public class InputPanel extends JPanel {
    private int fullComboNum;
    private int fullWarningCount;

    private int curCombo = -1;
    private int curComboStack = 0;
    private int curWarningCount=0;

    private JTextField text = new JTextField(10);//최대 10글자

    public InputPanel(MainFrame mainFrame,GameGroundPanel ground,ConditionPanel conditionPanel,ComboPanel comboPanel) {
        this.setBackground(Color.LIGHT_GRAY);
        add(text);

        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField tf = (JTextField)e.getSource();
                String t = tf.getText();//입력한 텍스트

                Component[] components = ground.getComponents();//패널에 있는 컴포넌트
                for (Component comp : components) {
                    if (comp instanceof JLabel) {//패널에 있는 JLabel에 대해
                        JLabel label = (JLabel) comp;
                        if(t.equals(label.getText())) {//JLabel의 텍스트가 입력한 텍스트와 같으면
                            tf.setText("");//텍스트 필드 지우기
                            ground.remove(label);//레이블 제거
                            ground.deleteFallingLabelThread(label);//스레드 삭제

                            checkMyLabel(mainFrame, ground,t,conditionPanel,comboPanel);//제거한 레이블에 따른 효과 적용
                            ground.repaint();//화면 다시 그리기
                        }
                        else tf.setText("");//enter 입력 시 항상 tf 비움
                    }
                }
                if(t.length()==0)
                    return;
            }
        });
    }
    //난이도에 따라 변경하기 위한 setter
    public void setFullComboNum(int n){
        this.fullComboNum = n;
    }
    public void setFullWarningCount(int n){
        this.fullWarningCount =n;
    }
    public void reset(){//초기화
        this.curWarningCount =-1;
        this.curCombo=-1;
        this.curComboStack=0;
    }

    public int getCurWarningCount(){
        return curWarningCount;
    }
    public void setCurWarningCount(int n){
        this.curWarningCount = n;
    }
    public int getFullWarningCount(){
        return fullWarningCount;
    }

    //입력한 단어를 확인 후 게임 효과 적용
    private void checkMyLabel(MainFrame mainFrame,GameGroundPanel ground, String t, ConditionPanel conditionPanel,ComboPanel comboPanel) {
        synchronized (ground.labelsOnPanel) {//iterator가 벡터를 순회하는 동안 수정하지 못하도록 synchronized
            Iterator<MyLabel> iterator = ground.labelsOnPanel.iterator();
            while (iterator.hasNext()) {
                MyLabel ml = iterator.next();
                if (ml.getLabel().getText().equals(t)) { // 입력한 단어에 해당하는 MyLabel을 찾고
                    switch (ml.getLabelType()) { // 해당 객체의 타입에 따라 효과 적용
                        case "아디다스":
                            //curWarningCount+=3;
                            GameEffects.increaseWarningBar(conditionPanel,curWarningCount+=3,3);//경고 스택 증가
                            GameEffects.changeComboStack(comboPanel, curComboStack);// 콤보 초기화
                            GameEffects.blur(ground); // 전체 블러 처리
                            GameEffects.changeFace(conditionPanel, 0); // 표정변화
                            GameEffects.changeConditionText(conditionPanel, 0);// 상태 메세지 변화
                            break;
                        case "맥도날드":
                            curComboStack += 2;
                            if (curComboStack >= 10) {
                                curCombo++;
                                GameEffects.addCombo(comboPanel, curCombo);
                                curComboStack -= 10;
                                if (curComboStack == 11)
                                    GameEffects.changeComboStack(comboPanel, 1);
                                else
                                    GameEffects.changeComboStack(comboPanel, curComboStack);// 콤보 += 2
                                //excellent와 good조건 중복시 excellent 우선
                                GameEffects.changeFace(conditionPanel, 3);
                                GameEffects.changeConditionText(conditionPanel, 3);
                            } else {
                                GameEffects.changeComboStack(comboPanel, curComboStack);// 콤보 += 2
                                GameEffects.changeFace(conditionPanel, 2);// 표정 변화
                                GameEffects.changeConditionText(conditionPanel, 2);
                            }
                            break;
                        default:
                            curComboStack++;
                            if (curComboStack >= 10) {
                                curCombo++;
                                GameEffects.addCombo(comboPanel, curCombo);
                                curComboStack -= 10;//스택 초기화
                                GameEffects.changeFace(conditionPanel, 3);
                                GameEffects.changeConditionText(conditionPanel, 3);
                            }
                            GameEffects.changeComboStack(comboPanel, curComboStack);// 콤보 ++
                    }
                    iterator.remove();

                    if (curCombo == fullComboNum - 1) {//게임 종료(성공)
                        ground.startGameThread.interrupt();
                        new javax.swing.Timer(2000, e -> {
                            mainFrame.showPanel("SuccessPanel");
                            GameEffects.playSound("/Audio/runaway.wav");//성공
                            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
                        }).start();

                    }

                    if (curWarningCount == fullWarningCount-1) {//게임 종료(실패)
                        ground.startGameThread.interrupt();
                        new javax.swing.Timer(2000, e -> {
                            mainFrame.showPanel("FailedPanel");
                            GameEffects.playSound("/Audio/24.wav");//성공
                            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
                        }).start();
                    }
                }
            }
        }
    }



}
