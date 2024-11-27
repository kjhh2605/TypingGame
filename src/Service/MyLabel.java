package Service;

import javax.swing.*;

public class MyLabel {
    private String labelType;
    private JLabel label;

    public MyLabel(String labelType, JLabel label){
        this.labelType = labelType;
        this.label = label;
    }
    public void setLabelType(String labelType){
        this.labelType=labelType;
    }
    public void setJLabel(JLabel label){
        this.label = label;
    }
    public String getLabelType(){
        return labelType;
    }
    public JLabel getLabel(){
        return label;
    }
}
