package BuhlmannZHL16;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Jens on 16.01.2016.
 */


public class Main {


    public static void main(String[] args) {

        final DiveSettings settings = new DiveSettings();

        final ZHL16 zhl16 = new ZHL16(settings, 0);
/*
        zhl16.dive(4.6, 10);
        zhl16.dive(4.6, 1250);
        zhl16.dive(2.5, 1);
        zhl16.dive(2.5, 34);
        zhl16.dive(2.2, 1);
        zhl16.dive(2.2, 83);
        zhl16.dive(2.2, 2);
        zhl16.dive(1.9, 1);
        zhl16.dive(1.9, 106);
        zhl16.dive(1.9, 4);
        zhl16.dive(1.6, 1);
        zhl16.dive(1.6, 217);
        zhl16.dive(1.3, 1);
        zhl16.dive(1.3, 458);
        zhl16.dive(1, 1);
*/


        JFrame diveComputerSimulator = new JFrame("Dive Computer Simulator");

        JPanel pressureSensorPanel = new JPanel();
        JPanel displayPanel = new JPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());

        final PressureSensor pressureSensor = new PressureSensor(pressureSensorPanel);
        final DiveDisplay diveDisplay = new DiveDisplay(displayPanel);

        diveComputerSimulator.setSize(new Dimension(1024, 768));
        diveComputerSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diveComputerSimulator.setLocationRelativeTo(null);
//        displayPanel.setPreferredSize(new Dimension(186, 150));
        mainPanel.add(pressureSensorPanel, BorderLayout.EAST);
        mainPanel.add(displayPanel, BorderLayout.CENTER);
        diveComputerSimulator.add(mainPanel);
        diveComputerSimulator.setVisible(true);


        java.util.Timer seconds = new java.util.Timer();
/*
        int color=0;
        for (int y=0;y<16;y++){
            for (int x=0;x<16;x++){
                diveDisplay.setPixel(x,y, color);
                color++;
            }
        }
        diveDisplay.updateDisplay();
*/

        TimerTask timerTick = new TimerTask() {
            @Override
            public void run() {
                DiveDataPoint divePoint = zhl16.dive(pressureSensor.getPressure(),60);

                int courser = 0;
                diveDisplay.drawHorizontalLine(22, 2, diveDisplay.colors.LIGHTBLUE);
                diveDisplay.drawHorizontalLine(106, 2, diveDisplay.colors.YELLOWGREEN);

                courser = 5;
                courser += diveDisplay.drawString(courser,20, settings.getGas().getOxygenAmount()*100 + "% O", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB, true);
                courser += diveDisplay.drawString(courser, 28, "² ", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB,true);
                courser += diveDisplay.drawString(courser,20,"max:" + Math.round(zhl16.barToMeter(zhl16.getMaxAllowedDepth(),settings)-0.5) + "m", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB,true);

                diveDisplay.drawString(2, 42, "Depth:", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, true);

                courser = 160;
                courser -= diveDisplay.drawString(courser, 53, "m", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, false);
                courser -= diveDisplay.drawString(courser, 50, Math.round(zhl16.barToMeter(divePoint.getDepthInBar(), settings)*10)/10d + "", diveDisplay.colors.RED, diveDisplay.fontTahoma22pB, false);


                if (divePoint.getNdl()>0){

                    diveDisplay.drawString(2, 74, "NDL:", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, true);

                    courser = 160;
                    courser -= diveDisplay.drawString(courser, 85, "min", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, false);
                    courser -= diveDisplay.drawString(courser, 82, Math.round(divePoint.getNdl()/60) + "", diveDisplay.colors.GREEN, diveDisplay.fontTahoma22pB, false);

                } else{

                    diveDisplay.drawString(2, 74, "Stop:", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, true);
                    diveDisplay.drawString(2, 101, "TTS:" + Math.round(divePoint.getTts()/60+0.5), diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, true);

                    courser = 160;
                    courser -= diveDisplay.drawString(courser, 85, "m", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, false);
                    courser -= diveDisplay.drawString(courser, 82, Math.round(zhl16.barToMeter(divePoint.getStopDepth(), settings)) + "", diveDisplay.colors.RED, diveDisplay.fontTahoma22pB, false);
                    courser -= diveDisplay.drawString(courser, 82, Math.round((divePoint.getStopTime()/60)+0.5) + "-", diveDisplay.colors.RED, diveDisplay.fontTahoma22pB, false);

                }

                diveDisplay.antiAliasing();
                diveDisplay.updateDisplay();
                diveDisplay.clearBuffer();
            }
        };


        seconds.schedule(timerTick, 0, 1000);

    }


}

