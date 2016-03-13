package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.03.2016.
 */

public class PressureSensor {

    private PressureSensorHw pressureSensorHw;
    private static final double range = 14.0; //Range in Bar
    private static final int zeroOutput = 0x666;
    private static final int maxPressureOutput = 0x399A;

    public PressureSensor(JPanel panel){
        this.pressureSensorHw = new PressureSensorHw(panel);
    }

    public double getPressure(){
        //returns pressure in Bar
        double pressure = 0;
        pressure = (pressureSensorHw.getPressure()-zeroOutput)*range/(maxPressureOutput-zeroOutput);
        return pressure;
    }

}
