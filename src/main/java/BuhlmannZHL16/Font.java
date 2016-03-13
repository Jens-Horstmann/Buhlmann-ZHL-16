package BuhlmannZHL16;

/**
 * Created by Jens on 06.03.2016.
 */
public class Font {

    public final FontInfo fontInfo;

    public Font(FontInfo fontInfo){
        this.fontInfo = fontInfo;
    }

    public int getCharWidth(char c){
        if(c != ' ') {
            return fontInfo.descriptors[c - fontInfo.getStartChar()][0];
        }else
            return fontInfo.getSpaceWidth();
    }

    public int getCharHeight(char c){
        return fontInfo.descriptors[c-fontInfo.getStartChar()][1];
    }

    public int getCharWidthBytes(char c){
            return (fontInfo.descriptors[c - fontInfo.getStartChar()][0] + 7) / 8;
    }

    private byte getCharByte(char c, int byteNumber){
        return fontInfo.bitmaps[fontInfo.descriptors[c-fontInfo.getStartChar()][2]+byteNumber];
    }


    private boolean getBitInByte(byte b, int bitNumber){
        boolean value=false;
        if ((b & (1<<bitNumber)) != 0){
            value = true;
        }
        return value;
    }

    private int getByteNumber(char c, int x, int y){
        return y*getCharWidthBytes(c)+x/8;
    }

    public boolean getCharPixel(char c, int x, int y){
        boolean value=false;
        if (getBitInByte(getCharByte(c, getByteNumber(c,x,y)), x%8)){
            value=true;
        }

        return value;
    }


}
