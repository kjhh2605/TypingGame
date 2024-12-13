package Service;

import Screen.Game.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class GameEffects {

    public static void changeLabelTextColor(GameGroundPanel ground, Color color) {
        Component[] components = ground.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setForeground(color);
            }
        }
    }
    public static void blur(GameGroundPanel ground) {
        changeLabelTextColor(ground, Color.BLACK);//검정색으로 변경(블러처리)
        new javax.swing.Timer(3000, e -> {//3초 후
            changeLabelTextColor(ground, Color.WHITE);//흰색으로 변경(원래대로)
            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
        }).start();
    }

    private static void updateFace(ConditionPanel conditionPanel, int n) {
        conditionPanel.remove(conditionPanel.face); // 기존 face 제거
        conditionPanel.face = Condition.getFaceLabel(n); // 새로운 face로 교체
        conditionPanel.face.setBounds(50, 100, 200, 200);
        conditionPanel.add(conditionPanel.face); // 패널에 추가
        conditionPanel.repaint(); // 패널 갱신
    }
    public static void changeFace(ConditionPanel conditionPanel, int n) {
        updateFace(conditionPanel, n);//face 변경
        new javax.swing.Timer(3000, e -> {//3초 후
            updateFace(conditionPanel, 1); // 무표정으로 변경(원래대로)
            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
        }).start();
    }

    private static void updateConditionText(ConditionPanel conditionPanel, int n) {
        conditionPanel.remove(conditionPanel.conditionText);
        conditionPanel.conditionText = Condition.getConditionText(n);
        conditionPanel.conditionText.setLocation(25, 20);
        conditionPanel.add(conditionPanel.conditionText);
        conditionPanel.repaint();
    }
    public static void changeConditionText(ConditionPanel conditionPanel, int n){
        updateConditionText(conditionPanel,n);
        // 3초 후 텍스트 제거
        new javax.swing.Timer(3000, e -> {
            updateConditionText(conditionPanel,1);
            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
        }).start();
    }

    public static void changeComboStack(ComboPanel comboPanel,int n){
        comboPanel.remove((comboPanel.comboStackLabel));
        comboPanel.comboStackLabel = Combo.getComboStackLabel(n);
        comboPanel.comboStackLabel.setLocation(80,115);
        comboPanel.add(comboPanel.comboStackLabel);
        comboPanel.repaint();
        System.out.print("스택증가 ");
    }
    public static void addCombo(ComboPanel comboPanel,int curCombo){
        comboPanel.paintComboBar(curCombo,Color.GRAY);//한칸 칠하고
        comboPanel.draw();//다시 그림

        int n = curCombo+1;
        String audioPath = "/Audio/"+n+".wav";
        playSound(audioPath);//효과음
    }
    public static void increaseWarningBar(ConditionPanel conditionPanel, int curWarningCount,int n){

        for(int i=curWarningCount;i<(curWarningCount+n);i++) {
            conditionPanel.paintWarningBar(i, Color.black);
        }
        conditionPanel.draw();
    }

    public static void playSound(String path){
        try {
            Clip clip = AudioSystem.getClip();
            URL resource = GameEffects.class.getResource(path);
            File audioFile = new File(resource.getPath());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioStream);
            clip.start();

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
