package screen.StartMenu;

import screen.MainFrame;
import setting.Setting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordAddPanel extends JPanel {
    private final JTextField wordText = new JTextField(10);
    private final Image addBackground = new ImageIcon(getClass().getResource("/images/단어추가.jpg")).getImage();

    public WordAddPanel(MainFrame mainFrame, Setting setting) {
        setLayout(null);
        wordText.setBounds(530, 20, 150, 25);
        wordText.setOpaque(false);
        wordText.setForeground(Color.WHITE);
        add(wordText);

        JButton addBtn = new JButton();
        addBtn.setBounds(740, 20, 45, 25);
        addBtn.setOpaque(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setBorderPainted(false);
        addBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = wordText.getText().trim();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "단어를 입력하세요!", "알림", JOptionPane.WARNING_MESSAGE);
                    return; // 입력이 없으면 메서드 종료
                }
                try (FileOutputStream fout = new FileOutputStream("src/WordText/words.txt", true)) { // append 모드
                    fout.write(text.getBytes());
                    fout.write("\n".getBytes()); // 줄바꿈 추가
                    JOptionPane.showMessageDialog(null, "단어가 추가되었습니다!", "알림", JOptionPane.INFORMATION_MESSAGE);
                    wordText.setText("");
                } catch (IOException ex) {
                }
            }
        });
        add(addBtn);


        JButton backBtn = setting.makeBtn("메인화면", "StartMenuPanel");
        backBtn.setBounds(450, 660, 100, 40);
        add(backBtn);
    }

    @Override//배경
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(addBackground, 0, 0, getWidth(), getHeight(), this);
    }

}
