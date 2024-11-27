package Service;

import Screen.Game.GameGroundPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;



public class MakeLabel {//랜덤한 단어 레이블 생성
    private final static int ICON_TOTAL_NUMBER=12;//아이콘 이미지 종류
    private static Vector<ImageIcon> iconList = new Vector<ImageIcon>(12);//아이콘 이미지
    private static List<String> textList = new ArrayList<String>();//단어장 단어
    public static GameGroundPanel ground = null;

    public MakeLabel(GameGroundPanel ground){
        textList = loadingTextFile("src/WordText/words.txt");
        setImageIcon();
        this.ground = ground;
    }

    public List<String> loadingTextFile(String path) {//텍스트 파일을 라인단위로 읽어 리스트로 반환
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            return new ArrayList<>();//실패시 빈 리스트 반환
        }
    }
    private void setImageIcon(){//이미지를 불러와 iconList에 저장
        int curIconNum=1;//아이콘 종류
        for(int i=0;i<ICON_TOTAL_NUMBER;i++){
            String curIconPath = "/images/icons/"+curIconNum+".png";//아이콘 이미지 경로
            //이미지 폴더에서 1~10은 앨범, 11은 맥도날드, 12는 아디다스
            ImageIcon ic = new ImageIcon(getClass().getResource(curIconPath));//이미지 아이콘 객체 생성
            Image scaledIc = ic.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
            ImageIcon setIc = new ImageIcon(scaledIc);
            iconList.add(setIc);//리스트에 추가
            curIconNum++;
        }
    }

    public static JLabel makeLabel(){//랜덤 확률로 떨어지는 레이블 생성
        int n = (int)(Math.random()*10);//확률 설정을 위한 난수

        if(n==0){//10% 확률
            JLabel label = setLabel(11,ground);//아디다스 = iconList[11]
            MyLabel ml = new MyLabel("아디다스", label);
            ground.labelsOnPanel.add(ml);
            return label;

        }
        else if(n==1||n==2){//20% 확률
            JLabel label = setLabel(10,ground);//맥도날드 = iconList[10]
            MyLabel ml = new MyLabel("맥도날드", label);
            ground.labelsOnPanel.add(ml);
            return label;

        }
        else{
            int albumNum=(int)(Math.random()*10);//앨범 = iconList[0~9]
            JLabel label = setLabel(albumNum,ground);//맥도날드 = iconList[10]
            MyLabel ml = new MyLabel("앨범", label);
            ground.labelsOnPanel.add(ml);
            return label;
        }
    }
    private static JLabel setLabel(int iconNum,GameGroundPanel ground){//레이블에 아이콘과 단어 추가
        ImageIcon ic = iconList.get(iconNum);
        String word;

        boolean isDuplicated;//중복 체크 플래그
        do {
            word = textList.get((int) (Math.random() * textList.size()));
            isDuplicated = false;

            Component[] components = ground.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getText().equals(word)) {//이미 패널에 존재하는 단어면
                        isDuplicated = true;
                        break;
                    }
                }
            }
        }while(isDuplicated);//중복인 경우 다시 생성

        JLabel label = new JLabel(word, ic, JLabel.LEFT);
        // 텍스트 위치 설정 (이미지 오른쪽에 텍스트 배치)
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setVerticalTextPosition(SwingConstants.CENTER);

        label.setForeground(Color.WHITE);//글자 색
        label.setBackground(Color.BLACK);//배경 색
        label.setOpaque(true);//불투명
        return label;
    }
}
