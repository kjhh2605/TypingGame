package Screen.Game;

import Screen.MainFrame;
import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {
    public GameGroundPanel groundPanel = new GameGroundPanel();
    private ComboPanel comboPanel    = new ComboPanel();
    private TimerPanel timer = new TimerPanel();
    private ConditionPanel conditionPanel = new ConditionPanel();
    private InputPanel inputPanel;

    public GamePanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        inputPanel = new InputPanel(mainFrame,groundPanel,conditionPanel,comboPanel);

        setDifficulty("normal");//난이도 설정
        comboPanel.drawComboBar();//난이도에 따른 콤보바 생성

        makeSplit();
    }
    private void setDifficulty(String difficulty){//난이도 설정
        switch(difficulty){
            case("hard"):
                inputPanel.setFullComboNum(10);
                inputPanel.setFullHateCount(1);
                comboPanel.setFullComboNum(10);
            case("normal"):
                inputPanel.setFullComboNum(5);
                inputPanel.setFullHateCount(3);
                comboPanel.setFullComboNum(5);
            case("easy"):
                inputPanel.setFullComboNum(3);
                inputPanel.setFullHateCount(5);
                comboPanel.setFullComboNum(3);
        }
    }
    private void makeSplit(){
        JSplitPane mPane = new JSplitPane();//게임 진행 & 게임 정보
        mPane.setDividerLocation(700);
        mPane.setEnabled(false);//마우스로 크기 조절 x
        mPane.setDividerSize(1);
        add(mPane,BorderLayout.CENTER);

        JSplitPane gPane = new JSplitPane();//게임 그라운드 & 단어 입력창
        gPane.setOrientation(JSplitPane.VERTICAL_SPLIT);//수직 방향 분할
        gPane.setDividerLocation(700);
        gPane.setEnabled(false);
        gPane.setDividerSize(1);
        gPane.setTopComponent(groundPanel);
        gPane.setBottomComponent(inputPanel);

        JSplitPane cPane = new JSplitPane();//콤보 & 타이머
        cPane.setOrientation(JSplitPane.VERTICAL_SPLIT);//수직 방향으로 분할
        cPane.setDividerLocation(200);
        cPane.setEnabled(false);
        cPane.setDividerSize(1);
        cPane.setTopComponent(comboPanel);//점수 패널 추가
        cPane.setBottomComponent(timer);//입력 패널 추가

        JSplitPane rPane = new JSplitPane();// (콤보,타이머) & 얼굴
        rPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rPane.setDividerLocation(350);
        rPane.setEnabled(false);
        rPane.setDividerSize(1);
        rPane.setTopComponent(cPane);
        rPane.setBottomComponent(conditionPanel);

        mPane.setRightComponent(rPane);
        mPane.setLeftComponent(gPane);
    }
}
