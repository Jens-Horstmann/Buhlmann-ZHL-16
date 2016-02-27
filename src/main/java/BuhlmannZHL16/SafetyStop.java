package BuhlmannZHL16;

/**
 * Created by Jens on 03.02.2016.
 */
public class SafetyStop {

    private double stopDepth;
    private double stopTime;
    private boolean isActive;


    public SafetyStop(double stopDepth) {
        this.stopDepth = stopDepth;
        stopTime = 0;
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getStopDepth() {
        return stopDepth;
    }

    public void setStopDepth(double stopDepth) {
        this.stopDepth = stopDepth;
    }

    public double getStopTime() {
        return stopTime;
    }

    public void setStopTime(double stopTime) {
        this.stopTime = stopTime;
    }


}
