package BuhlmannZHL16;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Jens on 06.03.2016.
 */
public class FrameBufferPanel extends JPanel {

    private final int width;
    private final int height;

    private int realWidth;
    private int realHeight;

    private final BufferedImage image;
    private final int[] pixels;

    public FrameBufferPanel(int width, int height, double widthmm, double heightmm) {
        this.width = width;
        this.height = height;
        this.realWidth = (int) (widthmm/0.18);
        this.realHeight = (int) (heightmm/0.18);
        setPreferredSize(new Dimension(realWidth, realHeight));
        image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        pixels = new int[this.width * this.height];
//        randomPixels();
    }

    @Override
    public void paint(Graphics graphics) {
        final Image scaledInstance = image.getScaledInstance(realWidth, realHeight, Image.SCALE_FAST);


        graphics.drawImage(scaledInstance, 0, 0, realWidth, realHeight, null);
    }

    public int rgbToInt(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }

    private void randomPixels() {
        int[] pixels = lock();
        for (int i = 0; i < width * height; i++) {
            int r = (int) (Math.random() * 256);
            int g = (int) (Math.random() * 256);
            int b = (int) (Math.random() * 256);
            int p = (r << 16) | (g << 8) | b;
            pixels[i] = p;
        }
        update(pixels);
    }

    public int[] lock() {
        return pixels;
    }


    public void updatePixel(int x, int y, int color){

    }

    public void update(int[] pixels) {
        if (pixels != this.pixels) {
            throw new IllegalArgumentException("Should update with locked pixel array");
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
        this.repaint();
    }
}
