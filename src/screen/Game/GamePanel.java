package screen.Game;

import screen.MainFrame;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    public ComboPanel comboPanel;
    public ConditionPanel conditionPanel;
    public GameGroundPanel groundPanel;
    private final TimerPanel timerPanel;
    private final InputPanel inputPanel;

    private String playerId;
    private String playerDifficulty;

    public GamePanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        comboPanel = new ComboPanel();
        conditionPanel = new ConditionPanel();
        groundPanel = new GameGroundPanel();
        inputPanel = new InputPanel(mainFrame);
        timerPanel = new TimerPanel(groundPanel);

        groundPanel.init(inputPanel, conditionPanel, mainFrame, timerPanel);
        inputPanel.init(this, groundPanel, conditionPanel, comboPanel, timerPanel);

        makeSplit();
    }

    public void setPlayerInformation(String id, String difficulty) {
        this.playerId = id;
        this.playerDifficulty = difficulty;
    }

    public String getPlayerInformation() {
        return playerId + "," + playerDifficulty;
    }

    public void setDifficulty(String difficulty) {//난이도 설정
        switch (difficulty) {
            case ("Hard"):
                inputPanel.setFullComboNum(10);
                inputPanel.setFullWarningCount(5);
                comboPanel.setFullComboNum(10);
                conditionPanel.setFullWarningNum(5);
                groundPanel.setDifficulty("Hard");
                break;
            case ("Normal"):
                inputPanel.setFullComboNum(5);
                inputPanel.setFullWarningCount(10);
                comboPanel.setFullComboNum(5);
                conditionPanel.setFullWarningNum(10);
                groundPanel.setDifficulty("Normal");
                break;
            case ("Easy"):
                inputPanel.setFullComboNum(3);
                inputPanel.setFullWarningCount(15);
                comboPanel.setFullComboNum(3);
                conditionPanel.setFullWarningNum(15);
                groundPanel.setDifficulty("Easy");
                break;
        }
    }

    public void reset() {//설정 값 초기화
        inputPanel.setCurWarningCount(-1);
        inputPanel.setCurComboStack(0);
        inputPanel.setCurCombo(-1);
    }

    private void makeSplit() {
        JSplitPane mPane = new JSplitPane();//게임 진행 & 게임 정보
        mPane.setDividerLocation(700);
        mPane.setEnabled(false);//마우스로 크기 조절 x
        mPane.setDividerSize(1);
        add(mPane, BorderLayout.CENTER);

        JSplitPane gPane = new JSplitPane();//게임 그라운드 & 단어 입력창
        gPane.setOrientation(JSplitPane.VERTICAL_SPLIT);//수직 방향 분할
        gPane.setDividerLocation(720);
        gPane.setEnabled(false);
        gPane.setDividerSize(1);
        gPane.setTopComponent(groundPanel);
        gPane.setBottomComponent(inputPanel);

        JSplitPane cPane = new JSplitPane();//콤보 & 타이머
        cPane.setOrientation(JSplitPane.VERTICAL_SPLIT);//수직 방향으로 분할
        cPane.setDividerLocation(200);
        cPane.setEnabled(false);
        cPane.setDividerSize(1);
        cPane.setTopComponent(comboPanel);//콤보 패널 추가
        cPane.setBottomComponent(timerPanel);//타이머 패널 추가

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
