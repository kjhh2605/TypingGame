package Screen.Game;

import Screen.MainFrame;
import Service.GameEffects;
import Service.MakeLabel;
import Service.MyLabel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;


public class GameGroundPanel extends JPanel {
    private final Image gameBackground = new ImageIcon(getClass().getResource("/images/게임배경.png")).getImage();
    public Vector<MyLabel>labelsOnPanel = new Vector<MyLabel>();
    public ArrayList<FallingLabelThread> labelThreadList=new ArrayList<FallingLabelThread>();
    private int width;
    private int height;

    public StartGameThread startGameThread;

    private ConditionPanel conditionPanel;
    private InputPanel inputPanel;

    public GameGroundPanel(InputPanel inputPanel, ConditionPanel conditionPanel) {
        setLayout(null);
        this.conditionPanel=conditionPanel;
        this.inputPanel=inputPanel;
        new MakeLabel(this);
    }

    public void setInputPanel(InputPanel inputPanel){
        this.inputPanel=inputPanel;
    }

    public void deleteFallingLabelThread(JLabel label){
        Iterator<FallingLabelThread> iterator = labelThreadList.iterator();
        while (iterator.hasNext()) {
            FallingLabelThread th = iterator.next();
            if (th.getText().equals(label.getText())) {
                th.interrupt(); // 스레드 종료
                iterator.remove(); // 리스트에서 제거
                break;
            }
        }
    }
    class StartGameThread extends Thread{
        private GameGroundPanel gameGroundPanel;

        StartGameThread(GameGroundPanel gameGroundPanel){
            this.gameGroundPanel=gameGroundPanel;
        }

        @Override
        public void run(){
            while(true) {
                JLabel wordLabel = MakeLabel.makeLabel(); // JLabel & MyLabel 생성
                wordLabel.setSize(100, 30);
                add(wordLabel); // 레이블을 패널에 추가
                FallingLabelThread th = new FallingLabelThread(gameGroundPanel,wordLabel); // 새 스레드 생성
                gameGroundPanel.labelThreadList.add(th);//스레드 리스트에 추가
                th.start();//레이블이 랜덤으로 움직이며 떨어짐

                try {
                    sleep(1000);//3초마다 생성
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    class FallingLabelThread extends Thread{
        private JLabel l = null;
        private  GameGroundPanel groundPanel;
        FallingLabelThread(GameGroundPanel groundPanel, JLabel l){
            this.l=l;
            this.groundPanel=groundPanel;
        }
        public String getText(){
            return l.getText();
        }
        @Override
        public void run(){

            int x = (int) (Math.random() * 600);
            int y = 0;
            while(true) {
                l.setLocation(x, y);

                int t =(int)(Math.random()*2);
                if(t==0) {
                    x += (int) (Math.random() * 5)*6;
                    if(x+l.getWidth()>=width)//패널 벗어나면
                        x-=(int) (Math.random() * 5)*6;//반대로 이동
                }
                else {
                    x -= (int) (Math.random() * 5)*6;
                    if(x<0)//패널 벗어나면
                        x+=(int) (Math.random() * 5)*6;//반대로 이동
                }

                if(y>height){//화면 벗어나면
                    remove(l);//패널의 레이블 삭제
                    groundPanel.deleteFallingLabelThread(l);//스레드 삭제
                    int fullWarningCount = inputPanel.getFullWarningCount();
                    int curWarningCount = inputPanel.getCurWarningCount();

                    synchronized (labelsOnPanel) {//iterator가 벡터를 순회하는 동안 수정하지 못하도록 synchronized
                        Iterator<MyLabel> iterator = labelsOnPanel.iterator();

                        while (iterator.hasNext()) {
                            MyLabel ml = iterator.next();
                            // 벗어난 레이블에 해당하는 MyLabel(같은 단어)을 찾고
                            if (ml.getLabel().getText().equals(l.getText())) {
                                iterator.remove();//삭제

                                if (!ml.getLabelType().equals("아디다스")) {
                                    curWarningCount++;//아디다스 레이블은 떨어져도 warning 쌓이지 x
                                    inputPanel.setCurWarningCount(curWarningCount);
                                    GameEffects.increaseWarningBar(conditionPanel, curWarningCount, 1);//경고 스택 증가
                                }

                                break;
                            }


                        }
                        }
                    if(curWarningCount>=fullWarningCount) {//WarningCount 다 차면 종료
                        groundPanel.startGameThread.interrupt();

                        //SwingUtilities.invokeLater(new Runnable)
                        new javax.swing.Timer(2000, e -> {
                            // mainFrame.showPanel("FailedPanel");
                            GameEffects.playSound("/Audio/24.wav");//성공
                            ((javax.swing.Timer) e.getSource()).stop(); // Timer 종료
                        }).start();
                    }
                    break;//스레드 종료
                }
                else
                    y+=20;

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    return;
                }

            }
        }
    }

    public void startGame(){
        startGameThread= new StartGameThread(this);
        startGameThread.start();
    }

    @Override//배경
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), this);
        width = getWidth();
        height = getHeight();
    }
}
