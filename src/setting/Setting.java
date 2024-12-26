package setting;

import screen.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Setting {
    private final MainFrame mainFrame;

    // 생성자에서 MainFrame 객체를 받음
    public Setting(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // 버튼 생성 메서드
    public JButton makeBtn(String btnName, String destinationPanel) {
        JButton btn = new JButton(btnName);
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.showPanel(destinationPanel);
            }
        });
        return btn;
    }


}
