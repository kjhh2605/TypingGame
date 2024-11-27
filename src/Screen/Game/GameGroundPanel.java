package Screen.Game;

import Service.MakeLabel;
import Service.MyLabel;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Vector;


public class GameGroundPanel extends JPanel {
    private final Image gameBackground = new ImageIcon(getClass().getResource("/images/게임배경.png")).getImage();
    public Vector<MyLabel>labelsOnPanel = new Vector<MyLabel>();

    private int width;
    private int height;

    public StartGameThread startGameThread;

    public GameGroundPanel() {
        setLayout(null);
        new MakeLabel(this);
        //startGame();
    }

    class StartGameThread extends Thread{
        @Override
        public void run(){
            while(true) {
                JLabel wordLabel = MakeLabel.makeLabel(); // JLabel & MyLabel 생성
                wordLabel.setSize(100, 30);
                add(wordLabel); // 레이블을 패널에 추가
                FallingLabelThread th = new FallingLabelThread(wordLabel); // 새 스레드 생성
                th.start();//레이블이 랜덤으로 움직이며 떨어짐

                try {
                    sleep(500);//3초마다 생성
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    class FallingLabelThread extends Thread{
        private JLabel l = null;
        FallingLabelThread(JLabel l){
            this.l=l;
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
                    Iterator<MyLabel> iterator = labelsOnPanel.iterator();
                    int index = -1;
                    while (iterator.hasNext()) {
                        MyLabel ml = iterator.next();

                        // 벗어난 레이블에 해당하는 MyLabel을 찾고
                        if(ml.getLabelType().equals(l.getText())) {
                            index = labelsOnPanel.indexOf(ml);
                        }
//                            iterator.remove();//삭제
                    }
                    if (index != -1) {
                        labelsOnPanel.remove(index);
                    }
                }
                else
                    y+=10;

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    return;
                }

            }
        }
    }

    public void startGame(){
        startGameThread= new StartGameThread();
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
