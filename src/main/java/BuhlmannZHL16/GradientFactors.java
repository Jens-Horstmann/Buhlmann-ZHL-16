package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class GradientFactors {

    private double low;
    private double high;

    private double highDepth;
    private double lowDepth;


    public void setLow(double low) {
        this.low = low;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getGF(double depthInBar){
        return high - ((high-low)/(lowDepth-highDepth)*(depthInBar-highDepth));
    }

}
