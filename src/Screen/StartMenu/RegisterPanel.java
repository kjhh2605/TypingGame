package Screen.StartMenu;

import Screen.MainFrame;
import Setting.Setting;
import javax.swing.*;
import java.net.IDN;

public class RegisterPanel extends JPanel {
    private JTextField inputId = new JTextField(10);
    private JTextField inputPw = new JTextField(10);
    private JTextField inputName = new JTextField(10);

    public RegisterPanel(MainFrame mainFrame, Setting setting){
        setLayout(null);
        JLabel idText = new JLabel("ID : ");
        idText.setBounds(380,100,40,25);
        add(idText);
        inputId.setBounds(425,100,150,25);
        add(inputId);
        inputPw.setBounds(425,150,150,25);
        add(inputPw);
        inputName.setBounds(425,200,150,25);
        add(inputName);

        JButton backBtn = setting.makeBtn("메인화면","StartMenuPanel");
        backBtn.setBounds(450,660,100,40);
        add(backBtn);
    }

}
