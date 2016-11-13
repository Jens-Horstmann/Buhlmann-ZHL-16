package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.11.2016.
 */
public class RotarySwitchHw {

    private JPanel panel;

    private boolean pushButtonPressed;
    private boolean scrollUpPressed;
    private boolean scrollDownPressed;

    JButton pushButton = new JButton("Push");
    JButton scrollUp = new JButton("Up");
    JButton scrollDown = new JButton("Down");

    public RotarySwitchHw(JPanel panel){
        this.panel = panel;

        panel.add(scrollDown);
        panel.add(pushButton);
        panel.add(scrollUp);
    }

    public boolean isPushButtonPressed() {
        return pushButtonPressed;
    }

    public void setPushButtonPressed(boolean pushButtonPressed) {
        this.pushButtonPressed = pushButtonPressed;
    }

    public boolean isScrollUpPressed() {
        return scrollUpPressed;
    }

    public void setScrollUpPressed(boolean scrollUpPressed) {
        this.scrollUpPressed = scrollUpPressed;
    }

    public boolean isScrollDownPressed() {
        return scrollDownPressed;
    }

    public void setScrollDownPressed(boolean scrollDownPressed) {
        this.scrollDownPressed = scrollDownPressed;
    }
}
