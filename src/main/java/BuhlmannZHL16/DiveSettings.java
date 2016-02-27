package BuhlmannZHL16;

/**
 * Created by Jens on 17.01.2016.
 */

public class DiveSettings {

    private double surfacePressure;
    private double depthPerBar;
    private GradientFactors gf;
    private Gas gas = new Gas();
    private double maxPP02;
    private double minStopTime;


    private double maxAscentRate;

    private final static double flightPressure = 0.58;
    private final static double Pw = 0.0627;            //water vapor pressure in Bar

    public int [] stops = {  0,  3,  6, 9, 12,
                            15, 21, 24, 27,
                            30, 33, 36, 39,
                            42, 45, 48, 51,
                            54, 57, 60, 63,
                            66, 69, 72, 75,
                            78, 81, 84, 87,
                            90, 93, 96, 99,
                            102, 105, 108, 111,
                            114, 117, 120};


    private double logThreshold;

    public DiveSettings(){
        this(1.0, 10, new GradientFactors(1), new Gas(), 1.4);
    }

    public DiveSettings(double surfacePressure, double depthPerBar, GradientFactors gf, Gas gas, double maxPP02) {
        this.surfacePressure = surfacePressure;
        this.depthPerBar = depthPerBar;
        this.gf = new GradientFactors(gf.getLow(), gf.getHigh(), this.surfacePressure);
        this.gas = gas;
        this.maxPP02 = maxPP02;
        logThreshold = 0.1;
        maxAscentRate = 0.3;
        minStopTime = 60;
    }



    public void setSurfacePressure(double surfacePressure) {
        this.surfacePressure = surfacePressure;
    }

    public void setDepthPerBar(double depthPerBar) {
        this.depthPerBar = depthPerBar;
    }

    public void setGf(GradientFactors gf) {
        if (gf.getHigh()>1){
            gf.setHigh(1);
        }
        if (gf.getHigh()<0.01){
            gf.setHigh(0.01);
        }

        if (gf.getLow()>1){
            gf.setLow(1);
        }

        if (gf.getLow()<0.01){
            gf.setLow(0.01);
        }

        this.gf = gf;
    }

    public void setGas(Gas gas) {
        this.gas = gas;
    }

    public void setMaxPP02(double maxPP02) {
        this.maxPP02 = maxPP02;
    }

    public static double getFlightPressure() {
        return flightPressure;
    }

    public static double getPw() {
        return Pw;
    }

    public double getDepthPerBar() {
        return depthPerBar;
    }

    public GradientFactors getGf() {
        return gf;
    }

    public Gas getGas() {
        return gas;
    }

    public double getMaxPP02() {
        return maxPP02;
    }

    public double getSurfacePressure() {
        return surfacePressure;
    }

    public double getLogThreshold() {
        return logThreshold;
    }

    public void setLogThreshold(double threshold) {
        logThreshold = threshold;
    }

    public double getMaxAscentRate() { return maxAscentRate; }

    public void setMaxAscentRate(double maxAscentRate) { this.maxAscentRate = maxAscentRate; }

    public double getMinStopTime() {
        return minStopTime;
    }

    public void setMinStopTime(double minStopTime) {
        this.minStopTime = minStopTime;
    }
}
