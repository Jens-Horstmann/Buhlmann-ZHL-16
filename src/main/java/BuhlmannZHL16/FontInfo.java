package BuhlmannZHL16;

/**
 * Created by Jens on 06.03.2016.
 */
public class FontInfo {

    private final char startChar;
    private final char endChar;
    private final int spaceWidth;
    public final int [][] descriptors;
    public final byte [] bitmaps;


    public FontInfo(char startChar, char endChar, int spaceWidth, int[][] descriptors, byte[] bitmaps) {
        this.startChar = startChar;
        this.endChar = endChar;
        this.spaceWidth = spaceWidth;
        this.descriptors = descriptors;
        this.bitmaps = bitmaps;
    }

    public char getStartChar() {
        return startChar;
    }

    public char getEndChar() {
        return endChar;
    }

    public int getSpaceWidth() {
        return spaceWidth;
    }
}
