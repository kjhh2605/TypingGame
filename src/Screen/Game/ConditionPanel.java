package Screen.Game;

import Service.Condition;
import Service.GameEffects;
import javax.swing.*;
import java.awt.*;

public class ConditionPanel extends JPanel {
    public JLabel face;//상태 이미지
    public JLabel conditionText;//상태 텍스트

    public ConditionPanel(){
        setLayout(null);
        new Condition();

        face = Condition.getFaceLabel(1);
        face.setBounds(50,100,200,200);
        add(face);
        conditionText=Condition.getConditionText(1);
        conditionText.setBounds(50, 20, 200, 50);
        add(conditionText);
    }

}
