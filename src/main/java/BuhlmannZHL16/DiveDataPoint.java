package BuhlmannZHL16;

/**
 * Created by Jens on 17.01.2016.
 */
public class DiveDataPoint {

    private double time;
    private double depthInBar;
    private double ndl;
    private double stopDepth;
    private double stopTime;
    private double tts;
    private int leadingTissue;
    private double GF;
    private double maxDepth;
    private double temperature;

    public DiveDataPoint(){this(0,0);}

    public DiveDataPoint(double time, double depthInBar){
        this(time, depthInBar, 5940, -1, -1, -1, -1, -1, -1, -1);
    }

    public DiveDataPoint(double time, double depthInBar, double ndl, double stopDepth, double stopTime, double tts, int leadingTissue, double GF, double maxDepth, double temperature) {
        this.time = time;
        this.depthInBar = depthInBar;
        this.ndl = ndl;
        this.stopDepth = stopDepth;
        this.stopTime = stopTime;
        this.tts = tts;
        this.leadingTissue = leadingTissue;
        this.GF = GF;
        this.maxDepth = maxDepth;
        this.temperature = temperature;
    }


    public double getTime() {
        return time;
    }

    public double getDepthInBar() {
        return depthInBar;
    }

    public double getNdl() {
        return ndl;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDepthInBar(double depthInBar) {
        this.depthInBar = depthInBar;
    }

    public void setNdl(double ndl) {
        this.ndl = ndl;
    }

    public double getStopDepth() {
        return stopDepth;
    }

    public double getStopTime() {
        return stopTime;
    }

    public double getTts() {
        return tts;
    }

    public int getLeadingTissue() {
        return leadingTissue;
    }

    public void setLeadingTissue(int leadingTissue) {
        this.leadingTissue = leadingTissue;
    }

    public double getGF() {
        return GF;
    }

    public void setGF(double GF) {
        this.GF = GF;
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
    }
}
