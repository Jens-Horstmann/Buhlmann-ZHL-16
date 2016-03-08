package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.03.2016.
 */
public class DiveDisplay {
    private OledHw display;
    private final int DISPLAYWIDTH = 128;
    private final int DISPLAYHEIGT = 128;
    public Font fontTahoma16pB = new Font( new Tahoma22pB_Numbers().fontInfo);
    public Font fontArialNarrow12pB = new Font( new ArialNarrow12pB().fontInfo);
    private PixelBuffer pixelBuffer = new PixelBuffer(DISPLAYWIDTH, DISPLAYHEIGT);
    private PixelBuffer screenBuffer = new PixelBuffer(DISPLAYWIDTH, DISPLAYHEIGT);


    public DiveDisplay(JPanel panel){
        this.display = new OledHw(panel);
    }

    public int rgbToInt(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }

    public byte rgbToByte(int r, int g, int b){
        return (byte) (((r/32)<<5) | ((g/32)<<2) | (b/64));
    }


    public int rgbByteToInt(byte rgbByte){
        int r = ((rgbByte & (0b11100000))>>5)*32;
        int g = ((rgbByte & (0b00011100))>>2)*32;
        int b = (rgbByte & 0b00000011)*64;
        return (r << 16) | (g << 8) | b;
    }


    public void setPixel(int x, int y, byte color){
        pixelBuffer.setPixel(x,y,color);
    }

    public int drawChar(int posX, int posY, char c, byte color, Font font){
        int courser = 0;
        if (c !=' ') {
            for (int y = 0; y < font.getCharHeight(c); y++) {
                for (int x = 0; x < font.getCharWidth(c); x++) {
                    if (font.getCharPixel(c, x, y)) {
                        setPixel(posX + x, posY + y-font.getCharHeight(c), color);
                    }
                }
            }
            courser= font.getCharWidth(c);
        }
        else{
            courser = font.fontInfo.getSpaceWidth();
        }
        return courser;
    }

    private int getStringLength(String s, Font font){
        int length=0;
        for (int i=0; i<s.length();i++){
            length = length + font.getCharWidth(s.charAt(i)) + 2;
        }
        return length;
    }

    public int drawString(int posX, int posY, String s, byte color, Font font, boolean leftAligned){
        int courser = 0;
        int alignment = 0;
        if (!leftAligned) {
            alignment = -1 * getStringLength(s,font);
        }
        for (int i=0; i<s.length();i++){
            courser = courser + drawChar(posX+courser+alignment, posY,s.charAt(i),color, font) + 2;
        }
        return courser;
    }

    public void clearBuffer(){
        for (int y=0;y<DISPLAYHEIGT;y++) {
            for (int x = 0; x < DISPLAYWIDTH; x++) {
                pixelBuffer.setPixel(x,y,(byte)0);
            }
        }
    }

    public void updateDisplay(){
        for (int y=0;y<DISPLAYHEIGT;y++){
            for (int x=0;x<DISPLAYWIDTH;x++){
                if(screenBuffer.getPixel(x,y) != pixelBuffer.getPixel(x,y)) {
                    screenBuffer.setPixel(x, y, pixelBuffer.getPixel(x, y));
                    display.drawPixel(x, y, rgbByteToInt(pixelBuffer.getPixel(x, y)));
                }
            }
        }
    }

    public void clearPixelBuffer(){
        for (int y=0;y<DISPLAYHEIGT;y++){
            for (int x=0;x<DISPLAYWIDTH;x++){
                pixelBuffer.setPixel(x,y,(byte)0);
            }
        }

    }

    public void drawHorizontalLine(int line, int thisness, byte color){
        for (int y=0;y<thisness;y++) {
            for (int x = 0; x < DISPLAYWIDTH; x++) {
                pixelBuffer.setPixel(x,line+y,color);
            }
        }
    }


}
