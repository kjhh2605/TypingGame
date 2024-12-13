package Screen;

import Screen.End.EndPanel;
import Screen.Game.GamePanel;
import Screen.Intro.IntroPanel;
import Screen.StartMenu.*;
import Setting.Setting;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel mainPanel = new JPanel(cardLayout);
    private static Screen.MainFrame MainFrame;
    private static final Setting setting = new Setting(MainFrame );

    public MainFrame(){
        setTitle("타이핑 게임");
        setSize(1000,800);
        setLocationRelativeTo(null); // 화면 중앙r
        setResizable(false); // 화면 크기 조절x
        setDefaultCloseOperation(EXIT_ON_CLOSE); // x창 누르면 실행 종료
        settingMenu();

        //인트로 패널
        //IntroPanel introPanel = new IntroPanel(this);
        //mainPanel.add(introPanel,"IntroPanel");
        //시작화면 패널
        StartMenuPanel startMenuPanel = new StartMenuPanel(this,setting);
        mainPanel.add(startMenuPanel, "StartMenuPanel");
        //게임 설명 패널
        HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this,setting);
        mainPanel.add(howToPlayPanel,"HowToPlayPanel");
        //회원가입 패널
        RegisterPanel registerPanel = new RegisterPanel(this,setting);
        mainPanel.add(registerPanel,"RegisterPanel");
        //랭킹 패널
        RankPanel rankPanel = new RankPanel(this,setting);
        mainPanel.add(rankPanel, "RankPanel");
        //게임 패널
        GamePanel gamePanel = new GamePanel(this);
        mainPanel.add(gamePanel,"GamePanel");
        //로그인 패널
        LoginPanel loginPanel = new LoginPanel(this,setting,gamePanel);
        mainPanel.add(loginPanel,"LoginPanel");
        //결과 패널(성공)
        EndPanel successPanel = new EndPanel(this,gamePanel,setting,true);
        mainPanel.add(successPanel,"SuccessPanel");
        //결과 패널(실패)
        EndPanel failedPanel = new EndPanel(this,gamePanel,setting,false);
        mainPanel.add(failedPanel,"FailedPanel");


        add(mainPanel);

        setVisible(true);
    }

    public static void showPanel(String name) {//name 패널로 전환
        cardLayout.show(mainPanel, name);
    }

    public void settingMenu() {//설정 메뉴바
        JMenuBar mb = new JMenuBar();

        JMenu setting = new JMenu("설정");
        mb.add(setting);

        JMenuItem soundControl = new JMenuItem("소리 끄기/켜기");
        setting.add(soundControl);

        JMenuItem addWord = new JMenuItem("단어 추가");
        setting.add(addWord);

        setJMenuBar(mb);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
