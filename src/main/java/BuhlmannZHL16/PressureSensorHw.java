package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.03.2016.
 */
public class PressureSensorHw {
    private JPanel panel;

    int initial =(int) (1.013*13108/14+0x666);
    JSlider pressureSlider = new JSlider(JSlider.VERTICAL, 0x666, 0x399A, initial);


    public PressureSensorHw(JPanel panel){
        this.panel = panel;
        pressureSlider.setInverted(true);
        panel.add(pressureSlider);
    }

    public int getPressure(){
        return pressureSlider.getValue(); //+ (int)((Math.random()*50)-25);
    }

}
