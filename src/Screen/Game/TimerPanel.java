package Screen.Game;

import javax.swing.*;
import java.awt.*;

public class TimerPanel extends JPanel{
    public TimerPanel(){
        JLabel timerLabel = new JLabel("0");
        timerLabel.setFont(new Font("SanSerif",Font.BOLD,80));
        add(timerLabel);
        Thread th = new TimerThread(timerLabel,1000);//스레드 생성(Thread 클래스)
       // th.start();//스레드 시작(JVM에게, 스케줄링 해도 됨)
    }
    class TimerThread extends Thread{
        private JLabel label = null;
        private int delay = 0;

        public TimerThread(JLabel label,int delay) {
            this.label = label;
            this.delay = delay;
        }

        @Override
        public void run() {//이 주소에서 실행을 시작하도록 TCB에 기록 -> 스레드 코드
            int n =1;
            while(true){
                label.setText(Integer.toString(n));
                n++;
                try {
                    Thread.sleep(delay);//1000ms 는 1초
                } catch (InterruptedException e) {
                    Container c = label.getParent();//업캐스팅
                    c.remove(label);//컴포넌트를 컨테이너에서 제거
                    c.repaint();
                    return;//[run]의 리턴 -> 스레드 종료
                }
            }
        }
    }
}
