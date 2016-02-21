package BuhlmannZHL16;

/**
 * Created by Jens on 16.01.2016.
 */
public class GradientFactors {

    private double low;
    private double high;

    private double highDepth;
    private double lowDepth = 2.2;


    public GradientFactors(double gfHighDepth){
        this(0.3, 0.8, gfHighDepth);
    }

    public GradientFactors(double gfLow, double gfHigh, double gfHighDepth){
        this.low = gfLow;
        this.high = gfHigh;
        this.highDepth = gfHighDepth;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }

    public double getGF(double depthInBar){
        return high - ((high-low)/(lowDepth-highDepth)*(depthInBar-highDepth));
    }

    public double gfCorrectA(double depthInBar, double a){
        return getGF(depthInBar)*a;
    }

    public double gfCorrectB(double depthInBar, double b){
        return 1/((getGF(depthInBar)/b)-getGF(depthInBar)+1);
    }

    public void setHighDepth(double highDepth) {
        this.highDepth = highDepth;
    }

    public void setLowDepth(double lowDepth) {
        this.lowDepth = lowDepth;
    }
}
