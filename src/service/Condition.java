package service;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Condition {//입력 단어에 따라 표정 변화
    private static final int CONDITION_NUMBER = 4;
    private static final Vector<ImageIcon> faceImgList = new Vector<ImageIcon>();
    private static final Vector<JLabel> faceLabelList = new Vector<JLabel>();
    //0:안좋음 1:무표정 2:좋음 3:매우좋음
    private static final Vector<JLabel> conditinoTextList = new Vector<>();//상태 텍스트 리스트


    public Condition() {
        setImageIcon();
        setFaceLabel();
        setConditinoTextList();
    }

    public static JLabel getFaceLabel(int n) {
        return faceLabelList.get(n);
    }

    public static JLabel getConditionText(int n) {
        return conditinoTextList.get(n);
    }

    private void setImageIcon() {

        for (int i = 0; i < CONDITION_NUMBER; i++) {
            String curImgPath = "/images/face/" + i + ".png";//아이콘 이미지 경로
            ImageIcon ic = new ImageIcon(getClass().getResource(curImgPath));//이미지 아이콘 객체 생성
            Image scaledIc = ic.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon setIc = new ImageIcon(scaledIc);
            faceImgList.add(setIc);//리스트에 추가
        }
    }

    private void setFaceLabel() {
        for (int i = 0; i < CONDITION_NUMBER; i++) {
            Image scaledImage = faceImgList.get(i).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel faceLabel = new JLabel(new ImageIcon(scaledImage));
            faceLabelList.add(faceLabel);
        }
    }

    private void setConditinoTextList() {
        JLabel[] textList = new JLabel[CONDITION_NUMBER];
        textList[0] = new JLabel("NO...");
        textList[1] = new JLabel("Hmm...");
        textList[2] = new JLabel("GOOD");
        textList[3] = new JLabel("EXCELLENT!");
        for (JLabel label : textList) {
            label.setSize(250, 50);
            label.setFont(new Font("SansSerif", Font.BOLD, 40));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            conditinoTextList.add(label);
        }


    }

}
