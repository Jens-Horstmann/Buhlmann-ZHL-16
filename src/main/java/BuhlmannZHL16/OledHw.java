package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.03.2016.
 */
public class OledHw {
    public static int pixelsizex = 128;
    public static int pixelsizey = 128;
    private JPanel panel = new JPanel();

    int[] pixels = new int[pixelsizex*pixelsizey];

    FrameBufferPanel screen = new FrameBufferPanel(pixelsizex, pixelsizey, pixelsizex*0.36, pixelsizey*0.36);
//    JFrame oled = new JFrame(windowName);

    public OledHw(JPanel panel){
        this.panel = panel;
        initialize();
    }

    private void initialize(){
        panel.add(screen);
    }

    public void drawPixel(int x, int y, int color){
        pixels = screen.lock();
        pixels[y * pixelsizex + x] = color;
//        screen.update(pixels);

    }

    public void updateDisplay(){
        screen.update(pixels);
    }

}
