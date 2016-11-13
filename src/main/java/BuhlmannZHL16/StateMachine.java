package BuhlmannZHL16;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private int state = STATE_INITIALISE;

    long timer[] = new long[5];

    Clock clock = new Clock();

    final DiveSettings settings = new DiveSettings();

    final ZHL16 zhl16 = new ZHL16(settings, 0);

    int tick = 1;



    Menu menu = new Menu();


    private JFrame diveComputerSimulator = new JFrame("Dive Computer Simulator");


    JPanel pressureSensorPanel = new JPanel();
    JPanel displayPanel = new JPanel();
    JPanel rotarySwitchPanel = new JPanel();
    JPanel mainPanel = new JPanel(new BorderLayout());

    final PressureSensor pressureSensor = new PressureSensor(pressureSensorPanel);
    final RotarySwitchHw rotarySwitchHw = new RotarySwitchHw(rotarySwitchPanel);
    final DiveDisplay diveDisplay = new DiveDisplay(displayPanel);




    public StateMachine(){

        diveComputerSimulator.setSize(new Dimension(400, 350));
        diveComputerSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diveComputerSimulator.setLocationRelativeTo(null);
//        displayPanel.setPreferredSize(new Dimension(186, 150));
        mainPanel.add(pressureSensorPanel, BorderLayout.EAST);
        mainPanel.add(rotarySwitchPanel, BorderLayout.SOUTH);
        mainPanel.add(displayPanel, BorderLayout.CENTER);
        diveComputerSimulator.add(mainPanel);
        diveComputerSimulator.setVisible(true);

        clock.setCurrentDateTime(2016, 11, 12, 23, 59, 45);

        timer[0] = clock.startTimer(timer[0]);

        rotarySwitchHw.pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotarySwitchHw.setPushButtonPressed(true);
            }
        });

        rotarySwitchHw.scrollDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(state==STATE_MAINMENU) {
                    rotarySwitchHw.setScrollDownPressed(true);
                }
            }
        });

        rotarySwitchHw.scrollUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(state==STATE_MAINMENU) {
                    rotarySwitchHw.setScrollUpPressed(true);
                }
            }
        });

        checkStates();
    }

    public void checkStates(){
        while(true) {
            switch (state) {
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
    }

    private void initialise(){
        state = STATE_SURFACEMODE;
    }

    private void surfacemode(){


        diveDisplay.drawString(64, 27, clock.getTimeString(0), diveDisplay.colors.LIGHTBLUE, diveDisplay.fontTahoma22pB, 3);
        diveDisplay.drawString(64, 45, clock.getDateString(0), diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB, 3);
        diveDisplay.drawHorizontalLine(46, 1, diveDisplay.colors.LIGHTBLUE);

        diveDisplay.drawString(64, 65, "SI: " + "00:27" + " - " + "32m " + "27'", diveDisplay.colors.BLUE, diveDisplay.fontArialNarrow12pB, diveDisplay.alignCenter());
        diveDisplay.drawHorizontalLine(66, 1, diveDisplay.colors.BLUE);

        diveDisplay.drawString(5, 83, "NDL at 10m: ", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, 1);
        diveDisplay.drawString(126, 83, (int) zhl16.getNDL(zhl16.meterToBar(10, settings))/60 + "'", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, diveDisplay.alignRight());
        diveDisplay.drawString(5, 83+15, "NDL at 20m: ", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, 1);
        diveDisplay.drawString(126, 83+15, (int) zhl16.getNDL(zhl16.meterToBar(20, settings))/60 + "'", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, diveDisplay.alignRight());
        diveDisplay.drawString(5, 83+30, "NDL at 30m: ", diveDisplay.colors.ORANGEYELLOW, diveDisplay.fontArialNarrow12pB, 1);
        diveDisplay.drawString(126, 83+30, (int) zhl16.getNDL(zhl16.meterToBar(30, settings))/60 + "'", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, diveDisplay.alignRight());
        diveDisplay.drawString(5, 83+45, "NDL at 40m: ", diveDisplay.colors.ORANGERED, diveDisplay.fontArialNarrow12pB, 1);
        diveDisplay.drawString(126, 83+45, (int) zhl16.getNDL(zhl16.meterToBar(40, settings))/60 + "'", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, diveDisplay.alignRight());

            diveDisplay.antiAliasing();
        diveDisplay.updateDisplay();
        diveDisplay.clearBuffer();


        double pressure = pressureSensor.getPressure();

        //Change state
        boolean sleep = true;
        tick = 5;

        while(sleep) { //active wait (sleep mode in AVR)

            try {
                Thread.sleep(10);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            if (pressure > (settings.getSurfacePressure() + 0.1)) {
                state = STATE_DIVEMODE;
                timer[1] = clock.startTimer(timer[1]);
                sleep = false;
            }

            if (rotarySwitchHw.isPushButtonPressed()) {
                state = STATE_MAINMENU;
                rotarySwitchHw.setPushButtonPressed(false);
                sleep = false;
            }
            if(clock.getTimer(timer[0]) >= tick){
                zhl16.dive(pressure, clock.getTimer(timer[0]));
                timer[0] = clock.startTimer(timer[0]);
                sleep = false;
            }
        }
    }

    private void divemode(){


        DiveDataPoint divePoint = zhl16.dive(pressureSensor.getPressure(), tick);

        int courser = 0;
        diveDisplay.drawHorizontalLine(22, 1, diveDisplay.colors.LIGHTBLUE);
        diveDisplay.drawHorizontalLine(23, 1, diveDisplay.colors.LIGHTBLUE+4);
        diveDisplay.drawHorizontalLine(106, 1, diveDisplay.colors.YELLOWGREEN);
        diveDisplay.drawHorizontalLine(107, 1, diveDisplay.colors.YELLOWGREEN+4);

        courser = 5;
        courser += diveDisplay.drawString(courser,20, settings.getGas().getOxygenAmount()*100 + "% O", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB, 1);
        courser += diveDisplay.drawString(courser, 28, "² ", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB,1);
        courser += diveDisplay.drawString(courser,20,"max:" + Math.round(zhl16.barToMeter(zhl16.getMaxAllowedDepth(),settings)-0.5) + "m", diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB,1);


        diveDisplay.drawString(50, 126, (int)(divePoint.getTime()/60) + "min", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, 0);
        diveDisplay.drawString(65, 126, "" + Math.round(zhl16.barToMeter(divePoint.getMaxDepth(),settings)*10)/10 + "m", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB,1);
        diveDisplay.drawString(diveDisplay.getDISPLAYWIDTH(), 126, 0 + "°C", diveDisplay.colors.YELLOWGREEN, diveDisplay.fontArialNarrow12pB, 0);


        diveDisplay.drawString(2, 42, "Depth:", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, 1);

        courser = diveDisplay.getDISPLAYWIDTH();
        courser -= diveDisplay.drawString(courser, 53, "m", diveDisplay.colors.RED, diveDisplay.fontArialNarrow12pB, 0);
        courser -= diveDisplay.drawString(courser, 50, Math.round(zhl16.barToMeter(divePoint.getDepthInBar(), settings)*10)/10d + "", diveDisplay.colors.RED, diveDisplay.fontTahoma22pB, 0);


        if (divePoint.getNdl()>0){

            diveDisplay.drawString(2, 74, "NDL:", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, 1);

            if (divePoint.getNdl()<5940) {
                courser = diveDisplay.getDISPLAYWIDTH();
                courser -= diveDisplay.drawString(courser, 85, "min", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, 0);
                courser -= diveDisplay.drawString(courser, 82, Math.round(divePoint.getNdl() / 60) + "", diveDisplay.colors.GREEN, diveDisplay.fontTahoma22pB, 0);
            }else{
                diveDisplay.drawString(diveDisplay.getDISPLAYWIDTH()-20, 82, "- -", diveDisplay.colors.GREEN, diveDisplay.fontTahoma22pB, 0);
            }

        } else{

            diveDisplay.drawString(2, 74, "Stop:", diveDisplay.colors.PURPLE, diveDisplay.fontArialNarrow12pB, 1);
            diveDisplay.drawString(2, 101, "TTS:" + Math.round(divePoint.getTts()/60+0.5), diveDisplay.colors.PURPLE, diveDisplay.fontArialNarrow12pB, 1);

            courser = diveDisplay.getDISPLAYWIDTH();
            courser -= diveDisplay.drawString(courser, 85, "m", diveDisplay.colors.PURPLE, diveDisplay.fontArialNarrow12pB, 0);
            courser -= diveDisplay.drawString(courser, 82, Math.round(zhl16.barToMeter(divePoint.getStopDepth(), settings)) + "", diveDisplay.colors.PURPLE, diveDisplay.fontTahoma22pB, 0);
            courser -= diveDisplay.drawString(courser, 82, (int)(divePoint.getStopTime()/60) + "-", diveDisplay.colors.PURPLE, diveDisplay.fontTahoma22pB, 0);

        }

//        diveDisplay.antiAliasing();
        diveDisplay.updateDisplay();
        diveDisplay.clearBuffer();

        //Change state
        boolean sleep = true;
        tick = 1;

        while(sleep) { //active wait (sleep mode in AVR)

            try {
                Thread.sleep(10);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            if (divePoint.getDepthInBar() > (settings.getSurfacePressure() + settings.getSurfacePressureThreshold())) {
                timer[1] = clock.startTimer(timer[1]);
            }

            if (clock.getTimer(timer[1]) >= 15){
                state = STATE_SURFACEMODE;
                sleep = false;
            }

            if(clock.getTimer(timer[1]) >= 15){
                state = STATE_SURFACEMODE;
                sleep = false;
            }

            if(clock.getTimer(timer[0]) >= tick){
                zhl16.dive(divePoint.getDepthInBar(), clock.getTimer(timer[0]));
                timer[0] = clock.startTimer(timer[0]);
                sleep = false;
            }
        }

    }

    private void mainmenu(){

        int courser = 40;
        menu.item[0].setValue((int)(settings.getGas().getOxygenAmount()*100));
        menu.item[1].setValue((int)(settings.getMaxPP02()*10));
        menu.item[2].setValue((int)(settings.getGf().getLow()*100));
        menu.item[3].setValue((int)(settings.getGf().getHigh()*100));

        for(int i=0;i<menu.getNumItems(); i++){
            diveDisplay.drawString(2, courser, menu.item[i].getName(), diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB, 1);
            if(menu.item[i].isShowValue()) diveDisplay.drawString(125, courser, menu.item[i].getValueString(), diveDisplay.colors.LIGHTBLUE, diveDisplay.fontArialNarrow12pB, 0);
            courser += 17;
        }

        diveDisplay.drawString(45, 17, "Menu", diveDisplay.colors.GREEN, diveDisplay.fontArialNarrow12pB, 1);
        diveDisplay.drawHorizontalLine(18, 1, diveDisplay.colors.GREEN);
        diveDisplay.drawHorizontalLine(19, 1, diveDisplay.colors.GREEN + 4);

//        diveDisplay.antiAliasing();
        diveDisplay.updateDisplay();
        diveDisplay.clearBuffer();


    }

    private void sleep(){

    }



}
