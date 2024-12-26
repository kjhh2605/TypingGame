package screen.Game;

import service.Combo;

import javax.swing.*;
import java.awt.*;

public class ComboPanel extends JPanel {
    public JLabel comboStackLabel;
    private JLabel[] comboBar;
    private int fullComboNum;
    private int comboUnitX;
    private final Combo c;

    public ComboPanel() {
        setLayout(null);
        this.setBackground(Color.LIGHT_GRAY);
        c = new Combo();
    }

    public void initComboStackLabel() {
        comboStackLabel = Combo.getComboStackLabel(0);
        comboStackLabel.setLocation(80, 115);
        this.add(comboStackLabel);
    }

    public void initComboBar() {
        comboBar = c.setComboBar(fullComboNum);
        comboUnitX = c.getUnitWidth(fullComboNum);
        c.drawComboBar(this, comboBar, comboUnitX, 15, 50);
    }

    public void draw() {
        c.drawComboBar(this, comboBar, comboUnitX, 15, 50);
    }

    public void setFullComboNum(int n) {
        this.fullComboNum = n;
    }

    public void paintComboBar(int n, Color color) {//배경색 변경 -> 콤보 증가
        comboBar[n].setBackground(color);
    }

    public void resetComboStackLabel() {
        if (comboStackLabel != null) {
            this.remove(comboStackLabel);
        }
        comboStackLabel = null;
        this.repaint();
    }

    public void resetComboBar() {
        if (comboBar != null) {
            for (JLabel label : comboBar) {
                this.remove(label); // warningBar의 각 요소를 패널에서 제거
            }
            comboBar = null; // 배열 초기화
            this.repaint();
        }
    }

}
