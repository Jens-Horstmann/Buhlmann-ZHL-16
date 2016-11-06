package BuhlmannZHL16;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jens on 06.11.2016.
 */
public class StateMachine {


    private final int STATE_INITIALISE  = 0;
    private final int STATE_SURFACEMODE = 1;
    private final int STATE_DIVEMODE    = 2;
    private final int STATE_MAINMENU    = 3;
    private final int STATE_SLEEP       = 4;
    private int state = STATE_SURFACEMODE;

    final DiveSettings settings = new DiveSettings();

    final ZHL16 zhl16 = new ZHL16(settings, 0);

    final int tick = 800;

    java.util.Timer timer0 = new java.util.Timer();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> result = executor.scheduleAtFixedRate(timer0Overflow(), 0, 800,TimeUnit.MILLISECONDS);

    private boolean timer0OverflowFlag = false;


    private JFrame diveComputerSimulator = new JFrame("Dive Computer Simulator");


    JPanel pressureSensorPanel = new JPanel();
    JPanel displayPanel = new JPanel();
    JPanel rotarySwitchPanel = new JPanel();
    JPanel mainPanel = new JPanel(new BorderLayout());

    final PressureSensor pressureSensor = new PressureSensor(pressureSensorPanel);
    final RotarySwitchHw rotarySwitchHw = new RotarySwitchHw(rotarySwitchPanel);
    final DiveDisplay diveDisplay = new DiveDisplay(displayPanel);




    public StateMachine(){

        diveComputerSimulator.setSize(new Dimension(400, 280));
        diveComputerSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diveComputerSimulator.setLocationRelativeTo(null);
//        displayPanel.setPreferredSize(new Dimension(186, 150));
        mainPanel.add(pressureSensorPanel, BorderLayout.EAST);
        mainPanel.add(rotarySwitchPanel, BorderLayout.SOUTH);
        mainPanel.add(displayPanel, BorderLayout.CENTER);
        diveComputerSimulator.add(mainPanel);
        diveComputerSimulator.setVisible(true);


        checkStates();
    }

    public void checkStates(){
        switch (state){
            case STATE_INITIALISE:
                initialise();
                break;
            case STATE_SURFACEMODE:
                surfacemode();
                break;
            case STATE_DIVEMODE:
                divemode();
                break;
            case STATE_MAINMENU:
                mainmenu();
                break;
            case STATE_SLEEP:
                sleep();
                break;
        }
    }

    private void initialise(){

    }

    private void surfacemode(){

        if (timer0OverflowFlag== false) {
            diveDisplay.drawString(30, 27, "13:24", diveDisplay.colors.WHITE, diveDisplay.fontTahoma22pB, true);
            diveDisplay.drawHorizontalLine(32, 1, diveDisplay.colors.WHITE);
            diveDisplay.drawHorizontalLine(33, 1, diveDisplay.colors.WHITE - 4);

            diveDisplay.antiAliasing();
            diveDisplay.updateDisplay();
            diveDisplay.clearBuffer();

  //          timer0.schedule(timer0Overflow, 0, 5000);
        }

        double pressure = pressureSensor.getPressure();
        zhl16.dive(pressure, 5);

        if (pressure>(settings.getSurfacePressure()+0.1)){

            timer0OverflowFlag = false;
 //           timer0Overflow.cancel();
            state = STATE_DIVEMODE;
            checkStates();
        }
    }

    private void divemode(){
//        java.util.Timer seconds = new java.util.Timer();

//        TimerTask timerTick = new TimerTask() {
//            @Override
//            public void run() {

        if(timer0OverflowFlag == false) {
 //           timer0.schedule(timer0Overflow, 0, tick);
        }

        DiveDataPoint divePoint = zhl16.dive(pressureSensor.getPressure(), tick/1000);

        int courser = 0;
        diveDisplay.drawHorizontalLine(22, 1, diveDisplay.colors.LIGHTBLUE);
        diveDisplay.drawHorizontalLine(23, 1, diveDisplay.colors.LIGHTBLUE+4);
        diveDisplay.drawHorizontalLine(106, 1, diveDisplay.colors.YELLOWGREEN);
        diveDisplay.drawHorizontalLine(107, 1, diveDisplay.colors.YELLOWGREEN+4);

        courser = 5;
        courser += diveDisplay.drawString(courser,20, settings.getGas().getOxygenAmount()*100 + "% O", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB, true);
        courser += diveDisplay.drawString(courser, 28, "² ", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB,true);
        courser += diveDisplay.drawString(courser,20,"max:" + Math.round(zhl16.barToMeter(zhl16.getMaxAllowedDepth(),settings)-0.5) + "m", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB,true);


        diveDisplay.drawString(50, 126, (int)(divePoint.getTime()/60) + "min", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, false);
        diveDisplay.drawString(65, 126, "" + Math.round(zhl16.barToMeter(divePoint.getMaxDepth(),settings)*10)/10 + "m", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB,true);
        diveDisplay.drawString(diveDisplay.getDISPLAYWIDTH(), 126, 0 + "°C", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, false);


        diveDisplay.drawString(2, 42, "Depth:", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, true);

        courser = diveDisplay.getDISPLAYWIDTH();
        courser -= diveDisplay.drawString(courser, 53, "m", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, false);
        courser -= diveDisplay.drawString(courser, 50, Math.round(zhl16.barToMeter(divePoint.getDepthInBar(), settings)*10)/10d + "", diveDisplay.colors.RED, diveDisplay.fontTahoma22pB, false);


        if (divePoint.getNdl()>0){

            diveDisplay.drawString(2, 74, "NDL:", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, true);

            if (divePoint.getNdl()<5940) {
                courser = diveDisplay.getDISPLAYWIDTH();
                courser -= diveDisplay.drawString(courser, 85, "min", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, false);
                courser -= diveDisplay.drawString(courser, 82, Math.round(divePoint.getNdl() / 60) + "", diveDisplay.colors.GREEN, diveDisplay.fontTahoma22pB, false);
            }else{
                diveDisplay.drawString(diveDisplay.getDISPLAYWIDTH()-20, 82, "- -", diveDisplay.colors.GREEN, diveDisplay.fontTahoma22pB, false);
            }

        } else{

            diveDisplay.drawString(2, 74, "Stop:", diveDisplay.colors.PURPLE, diveDisplay.fontArialNarrow12pB, true);
            diveDisplay.drawString(2, 101, "TTS:" + Math.round(divePoint.getTts()/60+0.5), diveDisplay.colors.PURPLE, diveDisplay.fontArialNarrow12pB, true);

            courser = diveDisplay.getDISPLAYWIDTH();
            courser -= diveDisplay.drawString(courser, 85, "m", diveDisplay.colors.PURPLE, diveDisplay.fontArialNarrow12pB, false);
            courser -= diveDisplay.drawString(courser, 82, Math.round(zhl16.barToMeter(divePoint.getStopDepth(), settings)) + "", diveDisplay.colors.PURPLE, diveDisplay.fontTahoma22pB, false);
            courser -= diveDisplay.drawString(courser, 82, (int)(divePoint.getStopTime()/60) + "-", diveDisplay.colors.PURPLE, diveDisplay.fontTahoma22pB, false);

        }

        diveDisplay.antiAliasing();
        diveDisplay.updateDisplay();
        diveDisplay.clearBuffer();
 //           }
 //       };
//        seconds.schedule(timerTick, 0, tick);
    }

    private void mainmenu(){

    }

    private void sleep(){

    }


    runnable timer0Overflow(){
        timer0OverflowFlag = true;
        checkStates();
    }

}
