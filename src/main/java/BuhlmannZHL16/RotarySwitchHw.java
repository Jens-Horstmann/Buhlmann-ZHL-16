package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.11.2016.
 */
public class RotarySwitchHw {

    private JPanel panel;

    JButton pushButton = new JButton("Push");
    JButton scrollUp = new JButton("Up");
    JButton scrollDown = new JButton("Down");

    public RotarySwitchHw(JPanel panel){
        this.panel = panel;

        panel.add(scrollDown);
        panel.add(pushButton);
        panel.add(scrollUp);
    }



}
