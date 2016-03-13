package BuhlmannZHL16;

import javax.swing.*;

/**
 * Created by Jens on 06.03.2016.
 */
public class DiveDisplay {
    private OledHw display;
    private final int DISPLAYWIDTH = display.pixelsizex;
    private final int DISPLAYHEIGT = display.pixelsizey;
    public Font fontTahoma22pB = new Font( new Tahoma22pB_Numbers().fontInfo);
    public Font fontArialNarrow12pB = new Font( new ArialNarrow12pB().fontInfo);
    private PixelBuffer pixelBuffer = new PixelBuffer(DISPLAYWIDTH, DISPLAYHEIGT);
    private PixelBuffer screenBuffer = new PixelBuffer(DISPLAYWIDTH, DISPLAYHEIGT);

    private int [] color = new int[256];

    public ColorPalette colors = new ColorPalette();


    public DiveDisplay(JPanel panel){

        this.display = new OledHw(panel);

        //create color palette
        int i=0;
        for (int f=0; f<100;f+=7){
            color[i] = hsvToRgbInt(0, 0, f);
            i++;
        }
        color[i]= hsvToRgbInt(0,0,100);
        i++;
        for (int h=0; h<360; h+=24){
            for (int s=100; s>=25; s-=25) {
                for (int v=100; v >=25; v -= 25) {
                    color[i]=hsvToRgbInt(h,s,v);
                    i++;
                }
            }
        }
    }

    public int rgbToInt(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }

    public int hsvToRgbInt(float h, float s, float v){
        float rHsv;
        float gHsv;
        float bHsv;
        int color;

        s=s/100;
        v=v/100;

        float c = s*v;
        float x = c * (1-Math.abs((h/60)%2-1));
        float m = v-c;

        if(h>=0 && h<=60){
            rHsv=c;
            gHsv=x;
            bHsv=0;
        }else if (h>60 && h<=120){
            rHsv=x;
            gHsv=c;
            bHsv=0;

        }else if (h>120 && h<=180){
            rHsv=0;
            gHsv=c;
            bHsv=x;

        }else if (h>180 && h<=240){
            rHsv=0;
            gHsv=x;
            bHsv=c;

        }else if (h>240 && h<=300){
            rHsv=x;
            gHsv=0;
            bHsv=c;

        }else if (h>300 && h<=360){
            rHsv=c;
            gHsv=0;
            bHsv=x;

        }else{
            System.out.println("H value out of bounds" + h);
            rHsv=0;
            gHsv=0;
            bHsv=0;
        }

        int r = (int) ((rHsv+m)*255);
        int g = (int) ((gHsv+m)*255);
        int b = (int) ((bHsv+m)*255);
        color = rgbToInt(r,g,b);
        return color;

    }

    public void setPixel(int x, int y, int color){
        pixelBuffer.setPixel(x,y,color);
    }

    public int drawChar(int posX, int posY, char c, int color, Font font){
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

    public int drawString(int posX, int posY, String s, int color, Font font, boolean leftAligned){
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
                pixelBuffer.setPixel(x,y,0);
            }
        }
    }

    public void antiAliasing(){
        for (int y=1;y<DISPLAYHEIGT-1;y++) {
            for (int x = 1; x < DISPLAYWIDTH-1; x++) {
                int pixel = pixelBuffer.getPixel(x,y);


                if (pixel != 0) {

                    int topLeft = pixelBuffer.getPixel(x-1,y-1);
                    int top = pixelBuffer.getPixel(x,y-1);
                    int topRight = pixelBuffer.getPixel(x+1,y-1);
                    int right = pixelBuffer.getPixel(x+1,y);
                    int botRight = pixelBuffer.getPixel(x+1,y+1);
                    int bottom = pixelBuffer.getPixel(x,y+1);
                    int botLeft = pixelBuffer.getPixel(x-1,y+1);
                    int left = pixelBuffer.getPixel(x-1,y);

                    if (pixel == left && pixel == top && pixel != topLeft) {
                        pixelBuffer.setPixel(x - 1, y - 1, pixel+2);
                    }

                    if (pixel == right && pixel == top && pixel != topRight) {
                        pixelBuffer.setPixel(x + 1, y - 1, pixel+2);
                    }

                    if (pixel == left && pixel == bottom && pixel != botLeft) {
                        pixelBuffer.setPixel(x - 1, y + 1, pixel+2);
                    }

                    if (pixel == right && pixel == bottom && pixel != botRight) {
                        pixelBuffer.setPixel(x + 1, y + 1, pixel+2);
                    }

                }
            }
        }
    }

    public void updateDisplay(){
        for (int y=0;y<DISPLAYHEIGT;y++){
            for (int x=0;x<DISPLAYWIDTH;x++){
                if(screenBuffer.getPixel(x,y) != pixelBuffer.getPixel(x,y)) {
                    screenBuffer.setPixel(x, y, pixelBuffer.getPixel(x, y));
                    display.drawPixel(x, y, color[pixelBuffer.getPixel(x,y)]);
                }
            }
        }
    }

    public void drawHorizontalLine(int line, int thickness, int color){
        for (int y=0;y<thickness;y++) {
            for (int x = 0; x < DISPLAYWIDTH; x++) {
                pixelBuffer.setPixel(x,line+y,color);
            }
        }
    }


}
