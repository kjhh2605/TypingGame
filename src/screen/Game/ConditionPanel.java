package screen.Game;

import service.Combo;
import service.Condition;

import javax.swing.*;
import java.awt.*;

public class ConditionPanel extends JPanel {
    public JLabel conditionText;//상태 텍스트
    public JLabel face;//상태 이미지
    private JLabel[] warningBar;
    private int fullWarningNum;
    private int warningUnitX;
    private final Combo c;


    public ConditionPanel() {
        setLayout(null);
        new Condition();
        c = new Combo();
        //상태 메시지
        conditionText = Condition.getConditionText(1);
        conditionText.setBounds(50, 20, 200, 50);
        //add(conditionText);
        //상태 이미지
        face = Condition.getFaceLabel(1);
        face.setBounds(50, 100, 200, 200);
        add(face);

    }

    public void initWarningBar() {
        warningBar = c.setComboBar(fullWarningNum);
        warningUnitX = c.getUnitWidth(fullWarningNum);
        c.drawComboBar(this, warningBar, warningUnitX, 20, 320);
    }

    public void draw() {
        c.drawComboBar(this, warningBar, warningUnitX, 20, 320);
    }

    public void setFullWarningNum(int n) {
        this.fullWarningNum = n;
    }

    public void paintWarningBar(int n, Color color) {//배경색 변경 -> 콤보 증가
        warningBar[n].setBackground(color);
        this.repaint();
    }

    public void resetWarningBar() {
        if (warningBar != null) {
            for (JLabel label : warningBar) {
                this.remove(label); // warningBar의 각 요소를 패널에서 제거
            }
            warningBar = null; // 배열 초기화
            this.repaint();
        }
    }


}
