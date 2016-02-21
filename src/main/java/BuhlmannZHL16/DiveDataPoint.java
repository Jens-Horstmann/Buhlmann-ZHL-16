package BuhlmannZHL16;

/**
 * Created by Jens on 17.01.2016.
 */
public class DiveDataPoint {

    private long time;
    private double depthInBar;
    private double ndl;
    private double ascentCeiling;
    private int leadingCeiling;
    private int leadingNdl;


    public DiveDataPoint(long time, double depthInBar){
        this(time, depthInBar, 5940, 0, 0, 0);
    }

    public DiveDataPoint(long time, double depthInBar, double ndl, double ascentCeiling, int leadingCeiling, int leadingNdl) {
        this.time = time;
        this.depthInBar = depthInBar;
        this.ndl = ndl;
        this.ascentCeiling = ascentCeiling;
        this.leadingCeiling = leadingCeiling;
        this.leadingNdl = leadingNdl;
    }


    public long getTime() {
        return time;
    }

    public double getDepthInBar() {
        return depthInBar;
    }

    public double getNdl() {
        return ndl;
    }

    public double getAscentCeiling() {
        return ascentCeiling;
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

    public void setAscentCeiling(double ascentCeiling) {
        this.ascentCeiling = ascentCeiling;
    }

    public int getLeadingCeiling() {
        return leadingCeiling;
    }

    public void setLeadingCeiling(int leadingCeiling) {
        this.leadingCeiling = leadingCeiling;
    }

    public int getLeadingNdl() {
        return leadingNdl;
    }

    public void setLeadingNdl(int leadingNdl) {
        this.leadingNdl = leadingNdl;
    }
}
