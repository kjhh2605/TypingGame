package Screen.StartMenu;

import Screen.MainFrame;
import Setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class RankPanel extends JPanel {
    private Image rankBackground = new ImageIcon(getClass().getResource("/images/랭킹배경.png")).getImage();
    private Vector<JLabel> rankLabelList = new Vector<JLabel>(10);
    private static List<String> rankList = new ArrayList<String>();

    //RankLabel클래스 -> 등수 / 이름 / 난이도 / 시간
    public RankPanel(MainFrame mainFrame, Setting setting){
        setLayout(null);

        setRank();//랭킹 세팅
        drawRankLabel();//화면에 출력

        JButton backBtn = setting.makeBtn("메인화면","StartMenuPanel");
        backBtn.setBounds(450,660,100,40);
        add(backBtn);
    }

    public List<String> loadingTextFile(String path) {//텍스트 파일을 라인단위로 읽어 리스트로 반환
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            return new ArrayList<>();//실패시 빈 리스트 반환
        }
    }
    class rankText{
        private String name;
        private String time;
        private String difficulty;
        public String getText(){
            return name +" "+time+" "+difficulty;
        }
    }

    // 시간(HH:mm:ss)에 따라 오름차순 정렬
    public static void sortByTime(Vector<String> users) {
        Collections.sort(users, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                // 시간 추출 및 LocalTime으로 변환
                LocalTime time1 = LocalTime.parse(line1.split(",")[1]);
                LocalTime time2 = LocalTime.parse(line2.split(",")[1]);
                // 시간 비교
                return time1.compareTo(time2);
            }
        });
    }

    void setRank(){
        rankList=loadingTextFile("src/Service/rank.txt");//
        //난이도에 따라 데이터 분류
        Vector<String>usersHard = new Vector<>();
        Vector<String>usersNormal = new Vector<>();
        Vector<String>usersEasy = new Vector<>();
        for(String line : rankList){
            //line은 "이름,시간,난이도"
            switch(line.split(",")[2]){
                case("HARD"):
                    usersHard.add(line);
                    break;
                case("NORMAL"):
                    usersNormal.add(line);
                    break;
                case("EASY"):
                    usersEasy.add(line);
                    break;
            }
        }

        // 난이도별로 시간에 따라 정렬
        sortByTime(usersHard);
        sortByTime(usersNormal);
        sortByTime(usersEasy);




    }

    void drawRankLabel(){

        rankLabelList.add(new JLabel("1등 "+rankList.get(0)));
        rankLabelList.add(new JLabel("2등"));
        rankLabelList.add(new JLabel("3등"));
        rankLabelList.add(new JLabel("4등"));
        rankLabelList.add(new JLabel("5등"));
        rankLabelList.add(new JLabel("6등"+rankList.get(0)));

        int rightFlag=0;
        int x = 200;
        int y = 300;
        for (JLabel rankLabel : rankLabelList) {
            if(rightFlag==5) {//6등 부터는 오른쪽에 출력되도록
                x += 300;
                y = 300;
            }
            this.add(rankLabel);
            rankLabel.setBounds(x,y,400,100);
            rankLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            y+=50;
            rightFlag++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rankBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
