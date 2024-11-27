package Service;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Combo {
    private String[] comboStackList = {"0 / 10", "1 / 10", "2 / 10", "3 / 10", "4 / 10", "5 / 10", "6 / 10", "7 / 10", "8 / 10", "9 / 10", "10 / 10"};
    private static Vector<JLabel> comboStackLabelList = new Vector<JLabel>();

    public Combo(){
        setComboStackLabelList();
    }

    public int getUnitWidth(int n){//콤보 한칸의 너비
        int comboBarWidth = 250;//콤보바 전체 너비
        return comboBarWidth/n;
    }
    public JLabel[] setComboBar(int n){//설정 개수에 따라 콤보 바 생성
        JLabel[] comboBar = new JLabel[n];
        int comboBarHeight = 50;//콤보바 높이

        int unitWidth = getUnitWidth(n);
        int unitHeight = comboBarHeight;

        for(int i=0;i<n;i++){
            JLabel unit = new JLabel();
            unit.setSize(unitWidth,unitHeight);
            unit.setBackground(Color.WHITE);
            unit.setOpaque(true);//배경색 보이도록
            comboBar[i]=unit;
        }
        return comboBar;
    }
    private void setComboStackLabelList(){//콤보 스택에 사용할 JLabel 생성
        for(int i=0;i<comboStackList.length;i++){
            JLabel label = new JLabel(comboStackList[i]);
            label.setSize(200,50);
            label.setFont(new Font("SansSerif", Font.BOLD, 50));
            comboStackLabelList.add(label);
        }
    }
    public static JLabel getComboStackLabel(int n){
        return comboStackLabelList.get(n);
    }
}
