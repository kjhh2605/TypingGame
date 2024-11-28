package Screen.Game;

import Service.Combo;

import javax.swing.*;
import java.awt.*;

public class ComboPanel extends JPanel {
    public JLabel comboStackLabel;
    private JLabel[]comboBar;
    private int fullComboNum;
    private int comboUnitX;
    private Combo c;

    public ComboPanel(){
        setLayout(null);
        this.setBackground(Color.LIGHT_GRAY);

        c = new Combo();

        //콤보 스택 레이블
        comboStackLabel = c.getComboStackLabel(0);
        comboStackLabel.setLocation(80,115);
        this.add(comboStackLabel);

        //drawComboBar();
    }
    public void drawComboBar(){//콤보 바 그리기
        comboBar = c.setComboBar(fullComboNum);
        comboUnitX = c.getUnitWidth(fullComboNum);
        int startX=15;//패널에서 그릴 위치
        for(JLabel unit : comboBar){
            unit.setLocation(startX,50);
            startX+=comboUnitX;//콤보 한칸 씩 이어서 그리도록
            this.add(unit);
        }
    }

    public void setFullComboNum(int n){
        this.fullComboNum=n;
    }
    public void paintComboBar(int n,Color color){//배경색 변경 -> 콤보 증가
        comboBar[n].setBackground(color);
    }


}
