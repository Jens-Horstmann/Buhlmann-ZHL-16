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

    public DiveDataPoint(){this(0,0);}

    public DiveDataPoint(double time, double depthInBar){
        this(time, depthInBar, 5940, 0, 0, 0);
    }

    public DiveDataPoint(double time, double depthInBar, double ndl, double stopDepth, double stopTime, double tts) {
        this.time = time;
        this.depthInBar = depthInBar;
        this.ndl = ndl;
        this.stopDepth = stopDepth;
        this.stopTime = stopTime;
        this.tts = tts;
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
}
