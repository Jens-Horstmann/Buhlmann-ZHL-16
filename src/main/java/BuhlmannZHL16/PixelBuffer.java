package BuhlmannZHL16;

/**
 * Created by Jens on 06.03.2016.
 */
public class PixelBuffer {

    private int width;
    private int height;
    private byte[] pixelBuffer;


    public PixelBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        pixelBuffer = new byte[this.width* this.height];
    }


    private int getByteNumber(int x, int y){
        return y*width+x;
    }

    private boolean isOutOfBounds(int x, int y){
        boolean value = false;
        if (x>= width)  value = true;
        if (x<0)        value = true;
        if (y>=height)  value = true;
        if (y<0)        value = true;
        return value;
    }

    public void setPixel(int x, int y, byte color){
        if (!isOutOfBounds(x,y)){
            pixelBuffer[getByteNumber(x,y)] = color;
        }
    }

    public byte getPixel(int x, int y){
        return pixelBuffer[getByteNumber(x,y)];
    }

}
