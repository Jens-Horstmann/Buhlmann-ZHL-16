package BuhlmannZHL16;

/**
 * Created by Jens on 03.02.2016.
 */
public class SafetyStop {

    private double stopDepth;
    private int stopTime;
    private boolean isActive;

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

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }
}
